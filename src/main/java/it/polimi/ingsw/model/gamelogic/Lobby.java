package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.socket.responses.*;
import it.polimi.ingsw.view.LobbyObserver;
import it.polimi.ingsw.view.MatchObserver;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Lobby {

    private static final Logger LOGGER = Logger.getLogger(Lobby.class.getName());
    private int matchCounter;
    private int waitingTime;
    private int turnTime;

    /**
     * Map that contains the link between a client and its state.
     * It's used to keep trace of players in game to ensure the uniqueness of usernames.
     * Furthermore it's used to check the presence of a player in a match in case of reconnection.
     */
    private Map<String, ConnectionStatus> takenUsernames;

    /**
     * A list that contains usernames of players waiting for the creation of a multiplayer match.
     * It's cleared when the timer expires or (before timer expiring) if the size is equal to 4.
     */
    private final List<String> waitingPlayers;

    private Map<String, MatchSingleplayer> singleplayerMatches;
    private Map<String, MatchMultiplayer> multiplayerMatches;

    // to store observers
    private Map<String, LobbyObserver> remoteObservers;
    private Map<String, ObjectOutputStream> socketObservers;


    // to simulate the timer before creating a match
    private Timer timer;

    /**
     * Constructor for Lobby.
     *
     * @param waitingTime is the waiting time after two players join the waiting room.
     * @param turnTime    is the time limit for each player's turn.
     */
    public Lobby(int waitingTime, int turnTime) {
        this.matchCounter = 0;
        this.takenUsernames = new HashMap<>();
        this.waitingPlayers = new ArrayList<>();
        this.multiplayerMatches = new HashMap<>();
        this.singleplayerMatches = new HashMap<>();
        this.remoteObservers = new ConcurrentHashMap<>();
        this.waitingTime = waitingTime;
        this.turnTime = turnTime;
        this.socketObservers = new ConcurrentHashMap<>();
    }

    public Map<String, MatchMultiplayer> getMultiplayerMatches() {
        return multiplayerMatches;
    }

    public Map<String, MatchSingleplayer> getSingleplayerMatches() {
        return singleplayerMatches;
    }

    /**
     * It puts a username into the map with the default status CONNECTED.
     *
     * @param name is the username of a client.
     */
    public synchronized void addUsername(String name) {
        this.takenUsernames.put(name, ConnectionStatus.CONNECTED);
    }

    /**
     * to remove a username at the end of a match or when a player leaves a match before its creation
     *
     * @param name is the username to be deleted
     */
    private synchronized void removeUsername(String name) {
        this.takenUsernames.remove(name);
    }

    /**
     * Creates a single player match.
     *
     * @param name is the player's username.
     * @param difficulty is the player's chosen difficulty for the current match.
     * @param socketOut represents the object output stream used to communicate with that player's view
     */
    public synchronized void createSingleplayerMatch(String name, int difficulty, ObjectOutputStream socketOut) {
        singleplayerMatches.put(name, new MatchSingleplayer(matchCounter, name, difficulty, turnTime, this, socketOut));
        matchCounter++;

        LOGGER.log(Level.INFO, "By lobby: Match number: " + matchCounter + " type: singleplayer");
    }

    private synchronized void createMultiplayerMatch(List<String> clients, Map<String, ObjectOutputStream> socketsOut) {

        MatchMultiplayer match = new MatchMultiplayer(matchCounter, clients, turnTime, socketsOut, this);
        for (String s : clients) {
            multiplayerMatches.put(s, match);
        }

        LOGGER.log(Level.INFO, "By lobby: Match number: " + matchCounter + " type: multiplayer \tnumber of players: " + clients.size());
    }

    private void notifyToAllRmiWaiting() {
        for (String observerName : remoteObservers.keySet()) {
            try {
                remoteObservers.get(observerName).onWaitingPlayers(waitingPlayers);
            } catch (RemoteException e) {
                remoteObservers.remove(observerName);
                removeFromWaitingPlayers(observerName);
                LOGGER.log(Level.INFO, "Player " + observerName + " removed from waiting players");
            }
        }
    }

    /**
     * Removes a player from the list of players waiting for a match.
     *
     * @param name is the username of the player to be removed.
     */
    public void removeFromWaitingPlayers(String name) {
        synchronized (waitingPlayers) {
            try {
                if (waitingPlayers.size() == 2) {
                    timer.cancel();
                    waitingPlayers.remove(name);
                    removeUsername(name);

                    // to update waiting players on the exiting players
                    notifyToAllRmiWaiting();
                    WaitingPlayersResponse response1 = new WaitingPlayersResponse(waitingPlayers, name, true);
                    notifyToAllSocket(response1);

                    // to keep all players updated on the player's exit
                    for (String observerName : remoteObservers.keySet()) {
                        try {
                            remoteObservers.get(observerName).onLastPlayer(name);
                        } catch (RemoteException e) {
                            remoteObservers.remove(observerName);
                            removeFromWaitingPlayers(observerName);
                            LOGGER.log(Level.INFO, "Player " + observerName + " removed from waiting players");
                        }
                    }

                    LastPlayerRoomResponse response2 = new LastPlayerRoomResponse(name);
                    notifyToAllSocket(response2);

                } else {
                    waitingPlayers.remove(name);
                    remoteObservers.remove(name);
                    removeUsername(name);

                    // to update waiting players on the exiting players
                    notifyToAllRmiWaiting();

                    WaitingPlayersResponse response3 = new WaitingPlayersResponse(waitingPlayers, name, false);
                    notifyToAllSocket(response3);


                    // to update waiting players on the names of players not anymore in the room
                    for (String observerName : remoteObservers.keySet()) {
                        try {
                            remoteObservers.get(observerName).onPlayerExit(name);
                        } catch (RemoteException e) {
                            remoteObservers.remove(observerName);
                            removeFromWaitingPlayers(observerName);
                            LOGGER.log(Level.INFO, "Player " + observerName + " removed from waiting players");
                        }
                    }

                    PlayerExitRoomResponse response4 = new PlayerExitRoomResponse(name);
                    notifyToAllSocket(response4);


                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "From lobby: Something wants to delete a name that doesn't exist!", e);
            }


        }
    }

    private PlayerMultiplayer getPlayer(String name) {

        for (PlayerMultiplayer p : multiplayerMatches.get(name).getPlayers()) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Sets the connection status of the chosen player to DISCONNECTED.
     *
     * @param name is the username of the player to disconnect.
     */
    public void disconnect(String name) {
        try {
            PlayerMultiplayer p;
            MatchMultiplayer match;

            p = getPlayer(name);
            match = multiplayerMatches.get(name);

            if (p != null) {
                p.setStatus(ConnectionStatus.DISCONNECTED);
            }
            takenUsernames.put(name, ConnectionStatus.DISCONNECTED);
            match.getRemoteObservers().remove(p);
            match.getSocketObservers().remove(p);

            for (MatchObserver mo : match.getRemoteObservers().values()) {
                mo.onPlayerExit(name);
            }

            Response response = new PlayerExitGameResponse(name);
            for (PlayerMultiplayer player : match.getPlayers()) {
                if (player.getStatus().equals(ConnectionStatus.CONNECTED)) {
                    try {
                        if (match.getSocketObservers().get(player) != null) {
                            match.getSocketObservers().get(player).writeObject(response);
                            match.getSocketObservers().get(player).reset();
                        }
                    } catch (IOException e) {
                        LOGGER.log(Level.SEVERE, "exception in notification of the exit of a player");
                    }
                }
            }

            assert p != null;
            if (p.isMyTurn()) {
                p.setMyTurn(false);
                match.goThrough();
            }

            // check if the game must be closed
            if (match.checkConnection() < 2) {
                notifyAndRemoveObservers(name);
                removeDisconnectedPlayers(name);
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "From Lobby: problem in disconnecting player " + name, e);
        }
    }

    /**
     * Remove the names of disconnected players from the map multiplayerMatches and from takenUsernames
     *
     * @param name is the name of one player of the match considered in this action
     */
    private void removeDisconnectedPlayers(String name) {
        for (PlayerMultiplayer p : multiplayerMatches.get(name).getPlayers()) {
            if (p.getStatus().equals(ConnectionStatus.DISCONNECTED)) {
                removeFromMatchMulti(p.getName());
            }
        }
    }

    /**
     * Removes the name of the player from the map multiPlayerMatches and from takenUsernames.
     *
     * @param name is the username of the player to remove.
     */
    public void removeFromMatchMulti(String name) {
        multiplayerMatches.remove(name);
        removeUsername(name);
    }

    private void notifyAndRemoveObservers(String name) throws IOException {
        MatchMultiplayer match;
        match = multiplayerMatches.get(name);
        for (PlayerMultiplayer player : match.getPlayers()) {
            if (!player.getName().equals(name) && player.getStatus().equals(ConnectionStatus.CONNECTED)) {
                match.setStillPlayingFalse();
                if (!match.getTurnManagerMultiplayer().isTimerExpired()) {
                    match.getTurnManagerMultiplayer().setTimerExpiredTrue();
                }
                // notifies to the player he is the only still in game
                if (match.getRemoteObservers().get(player) != null) {
                    match.getRemoteObservers().get(player).onGameClosing();
                    match.getRemoteObservers().remove(player);
                } else if ((match.getSocketObservers().get(player) != null)) {
                    match.getSocketObservers().get(player).writeObject(new ClosingGameResponse());
                    match.getSocketObservers().remove(player);
                }
            }
        }
    }

    /**
     * Puts a previously disconnected player's connection status to CONNECTED and notifies the reconnection
     * to all the other players.
     *
     * @param name is the username of the player to reconnect.
     */
    public void reconnect(String name) {
        MatchMultiplayer match;

        match = multiplayerMatches.get(name);

        takenUsernames.put(name, ConnectionStatus.CONNECTED);
        match.getPlayer(name).setStatus(ConnectionStatus.CONNECTED);

        // if it's socket, make it observer of the match
        if (socketObservers.get(name) != null) {
            match.getSocketObservers().put(match.getPlayer(name), socketObservers.remove(name));
            match.afterReconnection(name);
        }

        // notify to all other players that the player is now in game
        for (PlayerMultiplayer p : match.getRemoteObservers().keySet()) {
            try {
                match.getRemoteObservers().get(p).onPlayerReconnection(name);
            } catch (RemoteException e) {
                disconnect(p.getName());
                LOGGER.log(Level.INFO, "Player " + p.getName() + " disconnected!");
            }
        }

        Response response = new PlayerReconnectionResponse(name);
        for (PlayerMultiplayer p : match.getPlayers()) {
            if (!p.getName().equals(name)) {
                try {
                    if (match.getSocketObservers().get(p) != null) {
                        match.getSocketObservers().get(p).writeObject(response);
                        match.getSocketObservers().get(p).reset();
                    }
                } catch (IOException e) {
                    disconnect(p.getName());
                    LOGGER.log(Level.INFO, "Player " + p.getName() + " disconnected!");
                }
            }
        }
    }

    /**
     * Removes a player's username from the key values of the singlePlayerMatches map and from the list takenUsernames.
     *
     * @param name is the username to remove.
     */
    void removeMatchSingleplayer(String name) {
        try {
            singleplayerMatches.remove(name);
            removeUsername(name);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "From lobby: this SinglePlayer Match doesn't exist.", e);
        }
    }

    /**
     * Used after a player's reconnection. Transfers all the current game data.
     *
     * @param name is the username of the reconnected player.
     */
    public void transferAllData(String name) {
        multiplayerMatches.get(name).afterReconnection(name);
    }

    /**
     * Adds a player's username to the list of waiting players and notifies it to all the other waiting players.
     *
     * @param name is the username of the player to add.
     */
    public void addToWaitingPlayers(String name) {
        synchronized (waitingPlayers) {

            waitingPlayers.add(name);
            LOGGER.log(Level.INFO, "Lobby: waitingplayers size: " + waitingPlayers.size() + "\n" +
                    "Lobby: lobby rmi observers size: " + remoteObservers.size() + "\n" +
                    "Lobby: lobby socket observers size: " + socketObservers.size());

            // any time a waiting player is added, the notification is sent to the waitingScreenHandler
            notifyToAllRmiWaiting();

            WaitingPlayersResponse response = new WaitingPlayersResponse(waitingPlayers, null, false);
            notifyToAllSocket(response);

            // info server side
            if (waitingPlayers.size() == 1) {
                LOGGER.log(Level.INFO, "Lobby: There is 1 player waiting for a match.");
            } else {
                LOGGER.log(Level.INFO, "Lobby: There are " + waitingPlayers.size() + " players waiting for a match.");
            }

            // IF THERE ARE 2 PLAYERS WAITING FOR THE MATCH BEGINNING, THE TIMER IS SET
            if (waitingPlayers.size() == 2) {
                MatchStarter task;

                LOGGER.log(Level.INFO, "Lobby :Timer started: " + waitingTime / 1000 + " seconds from now!");

                this.timer = new Timer();
                task = new MatchStarter(this);
                timer.schedule(task, waitingTime);
            }

            if (waitingPlayers.size() == 4) {
                timer.cancel();
                startMatch();
            }
        }
    }

    private void notifyToAllSocket(Response response) {
        for (String name : socketObservers.keySet()) {
            if (socketObservers.get(name) != null) {
                try {
                    socketObservers.get(name).writeObject(response);
                    socketObservers.get(name).reset();
                } catch (IOException e) {
                    socketObservers.remove(name);
                    removeFromWaitingPlayers(name);
                    LOGGER.log(Level.INFO, "Player " + name + " removed from waiting players");
                }
            }
        }
    }

    /**
     * Creates a new multi player match with all the players in the waitingPlayers list.
     */
    public void startMatch() {

        synchronized (waitingPlayers) {

            //notifies to all the lobby observers the creation of the match, so from then the clients can start the GUI/cli and observe the match
            //socket
            MatchStartedResponse response = new MatchStartedResponse();
            notifyToAllSocket(response);
            createMultiplayerMatch(waitingPlayers, new HashMap<>(socketObservers));

            //rmi
            for (String name : remoteObservers.keySet()) {
                try {
                    remoteObservers.get(name).onMatchStarted();
                } catch (RemoteException e) {
                    remoteObservers.remove(name);
                    removeFromWaitingPlayers(name);
                    LOGGER.log(Level.INFO, "Player " + name + " removed from waiting players");
                }
            }

            matchCounter++;
            socketObservers.clear();
            remoteObservers.clear();
            waitingPlayers.clear();
        }
    }

    public List<String> getWaitingPlayers() {
        return waitingPlayers;
    }

    /**
     * Adds a player username in the key values of the remoteObservers map.
     *
     * @param name is the player's username.
     * @param lobbyObserver receives notifications from Lobby.
     */
    public void observeLobbyRemote(String name, LobbyObserver lobbyObserver) {
        remoteObservers.put(name, lobbyObserver);
    }

    /**
     * Adds the current player's view to the match observers.
     *
     * @param username is the player's username.
     * @param observer receives notifies from Match.
     * @param single is true if the player's in a single player match, false otherwise.
     */
    public void observeMatchRemote(String username, MatchObserver observer, boolean single) {
        if (single) {
            singleplayerMatches.get(username).observeMatchRemote(observer);
        } else {
            multiplayerMatches.get(username).observeMatchRemote(observer, username);
        }
    }

    public Map<String, ObjectOutputStream> getSocketObservers() {
        return socketObservers;
    }

    public Map<String, LobbyObserver> getRemoteObservers() {
        return remoteObservers;
    }

    public ConnectionStatus checkName(String name) {
        if (!takenUsernames.keySet().contains(name)) {
            return ConnectionStatus.ABSENT;
        } else {
            return takenUsernames.get(name);
        }
    }

    /**
     * Removes the names of all disconnected players from the map multiPlayerMatches and from takenUsernames.
     * @param players is the list of all players in the match.
     */
    void deleteDisconnectedClients(List<String> players) {
        for (String player : players) {
            if (takenUsernames.get(player).equals(ConnectionStatus.DISCONNECTED)) {
                removeFromMatchMulti(player);
            }
        }
    }
}