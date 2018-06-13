package it.polimi.ingsw;

import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
import it.polimi.ingsw.model.gamelogic.MatchSingleplayer;
import it.polimi.ingsw.model.gamelogic.MatchStarter;
import it.polimi.ingsw.model.gameobjects.PlayerMultiplayer;
import it.polimi.ingsw.socket.responses.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Lobby {

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

    public synchronized void createSingleplayerMatch(String name, int difficulty, ObjectOutputStream socketOut) {
        singleplayerMatches.put(name, new MatchSingleplayer(matchCounter, name, difficulty, turnTime, this, socketOut));
        matchCounter++;

        // debug
        System.out.println("By lobby: Match number: " + matchCounter + " type: singleplayer");
    }

    private synchronized void createMultiplayerMatch(List<String> clients, Map<String, ObjectOutputStream> socketsOut) {

        MatchMultiplayer match = new MatchMultiplayer(matchCounter, clients, turnTime, socketsOut, this);
        for (String s : clients) {
            multiplayerMatches.put(s, match);
        }

        System.out.println("By lobby: Match number: " + matchCounter + " type: multiplayer");

        clients.forEach(c -> System.out.print(c + "\t"));
        System.out.println("\n");
    }

    private void notifyToAllRmiWaiting() {
        for (String observerName : remoteObservers.keySet()) {
            try {
                remoteObservers.get(observerName).onWaitingPlayers(waitingPlayers);
            } catch (RemoteException e) {
                remoteObservers.remove(observerName);
                removeFromWaitingPlayers(observerName);
            }
        }
    }

    public void removeFromWaitingPlayers(String name) {
        boolean unique = false;
        synchronized (waitingPlayers) {
            try {
                if (waitingPlayers.size() == 2) {
                    timer.cancel();
                    waitingPlayers.remove(name);
                    removeUsername(name);
                    unique = true;

                    // to update waiting players on the exiting players
                    notifyToAllRmiWaiting();
                    WaitingPlayersResponse response1 = new WaitingPlayersResponse(waitingPlayers, name, unique);
                    notifyToAllSocket(response1);

                    // to keep all players updated on the player's exit
                    for (String observerName : remoteObservers.keySet()) {
                        try {
                            remoteObservers.get(observerName).onLastPlayer(name);
                        } catch (RemoteException e) {
                            remoteObservers.remove(observerName);
                            removeFromWaitingPlayers(observerName);
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

                    WaitingPlayersResponse response3 = new WaitingPlayersResponse(waitingPlayers, name, unique);
                    notifyToAllSocket(response3);


                    // to update waiting players on the names of players not anymore in the room
                    for (String observerName : remoteObservers.keySet()) {
                        try {
                            remoteObservers.get(observerName).onPlayerExit(name);
                        } catch (RemoteException e) {
                            remoteObservers.remove(observerName);
                            removeFromWaitingPlayers(observerName);
                        }
                    }

                    PlayerExitRoomResponse response4 = new PlayerExitRoomResponse(name);
                    notifyToAllSocket(response4);


                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("From lobby: Something wants to delete a name that doesn't exist!");
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
                        e.printStackTrace();
                    }
                }
            }

            assert p != null;
            if (p.isMyTurn()) {
                p.setMyTurn(false);
                match.goThrough();
            }

            // to check if the game must be closed
            if (match.checkConnection() < 2) {
                for (PlayerMultiplayer player : match.getPlayers()) {
                    if (!player.getName().equals(name) && player.getStatus().equals(ConnectionStatus.CONNECTED)) {
                        match.setStillPlaying(false);
                        // notifies to the player he is the only still in game
                        if (match.getRemoteObservers().get(player) != null) {
                            try {
                                if(!match.getTurnManagerMultiplayer().isTimerExpired()){
                                    match.getTurnManagerMultiplayer().setTimerExpiredTrue();
                                }
                                match.getRemoteObservers().get(player).onGameClosing();
                            } catch (RemoteException e) {
                                System.out.println("Partita terminata!");
                            }
                            match.getRemoteObservers().remove(player);
                        } else {
                            match.getSocketObservers().get(player).writeObject(new ClosingGameResponse());
                            match.getSocketObservers().get(player).reset();
                            match.getSocketObservers().remove(player);

                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("From Lobby: problem in disconnecting player " + name + "!");
        }
    }

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
                System.out.println("Player " + p.getName() + " disconnected!");
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
                    System.out.println("Player " + p.getName() + " disconnected!");
                }
            }
        }
    }

    public void removeMatchSingleplayer(String name) {
        try {
            singleplayerMatches.remove(name);
            removeUsername(name);
        } catch (Exception e) {
            e.printStackTrace();
            // debug
            System.out.println("From lobby: this SinglePlayer Match doesn't exist.");
        }
    }

    public void transferAllData(String name) {
        multiplayerMatches.get(name).afterReconnection(name);
    }

    void addToWaitingPlayers(String name) {
        synchronized (waitingPlayers) {

            waitingPlayers.add(name);
            System.out.println("Lobby: waitingplayers size: " + waitingPlayers.size() + "");
            System.out.println("Lobby: lobby rmi observers size: " + remoteObservers.size() + "");
            System.out.println("Lobby: lobby socket observers size: " + socketObservers.size() + "");

            // ANY TIME A WAITING PLAYER IS ADDED, THE NOTIFICATION IS SENT TO THE WAITINGSCREENHANDLER BOTH FOR RMI AND SOCKETS
            //RMI
            notifyToAllRmiWaiting();

            //SOCKET
            WaitingPlayersResponse response = new WaitingPlayersResponse(waitingPlayers, null, false);

            notifyToAllSocket(response);

            //DEBUG SERVER SIDE
            if (waitingPlayers.size() == 1) System.out.println("Lobby: There is 1 player waiting for a match." + "\n");
            else
                System.out.println("Lobby: There are " + waitingPlayers.size() + " players waiting for a match." + "\n");


            // IF THERE ARE 2 PLAYERS WAITING FOR THE MATCH BEGINNING, THE TIMER IS SET
            if (waitingPlayers.size() == 2) {
                MatchStarter task;

                System.out.println("Lobby :Timer started: " + waitingTime / 1000 + " seconds from now!");

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
                }
            }
        }
    }

    public void startMatch() {

        synchronized (waitingPlayers) {

            //NOTIFIES TO ALL THE LOBBY "OBSERVERS" THE CREATION OF THE MATCH, SO FROM THEN THE CLIENTS CAN START THE GUI/CLI AND "OBSERVE" THE MATCH

            //SOCKETS
            MatchStartedResponse response = new MatchStartedResponse();
            notifyToAllSocket(response);

            createMultiplayerMatch(waitingPlayers, new HashMap<>(socketObservers));

            //RMI
            for (String name : remoteObservers.keySet()) {
                try {
                    remoteObservers.get(name).onMatchStarted();
                } catch (RemoteException e) {
                    remoteObservers.remove(name);
                    removeFromWaitingPlayers(name);
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

    public void observeLobbyRemote(String name, LobbyObserver lobbyObserver) {
        remoteObservers.put(name, lobbyObserver);
    }


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

    // check if there is a still alive match in which the player must be put todo: potrebbe non servire più
    public void addPlayer(String name) {
        /*if (multiplayerMatches.get(name) != null) {
            for (PlayerMultiplayer p : multiplayerMatches.get(name).getPlayers()) {
                if (p.getName().equals(name)) {
                    p.setStatus(ConnectionStatus.CONNECTED);
                }
            }
        } else {*/
        addToWaitingPlayers(name);
        // }
    }

    public ConnectionStatus checkName(String name) {
        if (!takenUsernames.keySet().contains(name)) {
            return ConnectionStatus.ABSENT;
        } else {
            return takenUsernames.get(name);
        }
    }


}