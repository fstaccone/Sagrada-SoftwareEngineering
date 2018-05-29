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
        this.remoteObservers = new HashMap<>();
        this.waitingTime = waitingTime;
        this.turnTime = turnTime;
        this.socketObservers = new HashMap<>();
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

    // to remove a username at the end of a match or when a player leaves a match before its creation
    private synchronized void removeUsername(String name) {
        this.takenUsernames.remove(name);
    }

    public synchronized void createSingleplayerMatch(String name) {

        singleplayerMatches.put(name, new MatchSingleplayer(matchCounter, name));
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

    public void removeFromWaitingPlayers(String name) {
        boolean unique = false;
        synchronized (waitingPlayers) {
            try {
                if (waitingPlayers.size() == 2) {
                    timer.cancel();
                    waitingPlayers.remove(name);
                    removeUsername(name);
                    unique = true;

                    // TODO: l'ho messo qui temporaneamente per testare la seconda text area
                    // to update waiting players on the exiting players
                    for (LobbyObserver observer : remoteObservers.values()) {
                        try {
                            observer.onWaitingPlayers(waitingPlayers);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }

                    // to keep all players updated on the player's exit
                    for (LobbyObserver observer : remoteObservers.values()) {
                        try {
                            observer.onLastPlayer(name);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    waitingPlayers.remove(name);
                    remoteObservers.remove(name);
                    removeUsername(name);

                    // to update waiting players on the exiting players
                    for (LobbyObserver observer : remoteObservers.values()) {
                        try {
                            observer.onWaitingPlayers(waitingPlayers);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }

                    // to update waiting players on the names of players not anymore in the room
                    for (LobbyObserver observer : remoteObservers.values()) {
                        try {
                            observer.onPlayerExit(name);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("From lobby: Something wants to delete a name that doesn't exist!");
            }

            WaitingPlayersResponse response = new WaitingPlayersResponse(waitingPlayers, name, unique);
            socketResponseToAll(response);
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
            p = getPlayer(name);

            p.setStatus(ConnectionStatus.DISCONNECTED);
            takenUsernames.put(name, ConnectionStatus.DISCONNECTED);
            multiplayerMatches.get(name).getRemoteObservers().remove(p);
            multiplayerMatches.get(name).getSocketObservers().remove(p);

            for (MatchObserver mo : multiplayerMatches.get(name).getRemoteObservers().values()) {
                mo.onPlayerExit(name);
            }

            Response response = new PlayerExitResponse(name);
            for (PlayerMultiplayer player : multiplayerMatches.get(name).getPlayers()) {
                if (player.getStatus().equals(ConnectionStatus.CONNECTED)) {
                    try {
                        if (multiplayerMatches.get(player.getName()).getSocketObservers().get(player) != null) {
                            multiplayerMatches.get(player.getName()).getSocketObservers().get(player).writeObject(response);
                            multiplayerMatches.get(player.getName()).getSocketObservers().get(player).reset();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (p.isMyTurn()) {
                p.setMyTurn(false);
                multiplayerMatches.get(name).goThrough();
            }

            // to check if the game must be closed
            if (multiplayerMatches.get(name).checkConnection() < 2) {
                for (PlayerMultiplayer player : multiplayerMatches.get(name).getPlayers()) {
                    if (!player.getName().equals(p.getName()) && player.getStatus().equals(ConnectionStatus.CONNECTED)) {
                        // notifica ai giocatori che la partita è finita e poi li rimuove
                        multiplayerMatches.get(name).getRemoteObservers().get(player).onGameClosing();
                        multiplayerMatches.get(name).getRemoteObservers().remove(player);

                        //multiplayerMatches.get(name).getSocketObservers().get(player).writeObject(new ClosingGameResponse());
                        multiplayerMatches.get(name).getSocketObservers().remove(player);
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("From Lobby: problem in disconnecting player " + name + "!");
        }
    }

    public void reconnect(String name) {
        takenUsernames.put(name, ConnectionStatus.CONNECTED);
        multiplayerMatches.get(name).getPlayer(name).setStatus(ConnectionStatus.CONNECTED);

        // if it's socket, make it observer of the match
        if (socketObservers.get(name) != null) {
            multiplayerMatches.get(name).getSocketObservers().put(multiplayerMatches.get(name).getPlayer(name), socketObservers.remove(name));
            multiplayerMatches.get(name).afterReconnection(name);
        }

        // notify to all other players that the player is now in game
        for (MatchObserver mo : multiplayerMatches.get(name).getRemoteObservers().values()) {
            try {
                mo.onPlayerReconnection(name);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        Response response = new PlayerReconnectionResponse(name);
        for (PlayerMultiplayer p : multiplayerMatches.get(name).getPlayers()) {
            if (!p.getName().equals(name)) {
                try {
                    if (multiplayerMatches.get(p.getName()).getSocketObservers().get(p) != null) {
                        multiplayerMatches.get(p.getName()).getSocketObservers().get(p).writeObject(response);
                        multiplayerMatches.get(p.getName()).getSocketObservers().get(p).reset();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
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

    private void socketResponseToAll(Response response) {
        socketObservers.keySet().forEach(playerName -> {
            try {
                socketObservers.get(playerName).writeObject(response);
                socketObservers.get(playerName).reset();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void transferAllData(String name) {
        multiplayerMatches.get(name).afterReconnection(name);
    }

    public void addToWaitingPlayers(String name) {
        synchronized (waitingPlayers) {

            waitingPlayers.add(name);
            System.out.println("Lobby: waitingplayers size: " + waitingPlayers.size() + "");
            System.out.println("Lobby: lobby rmi observers size: " + remoteObservers.size() + "");
            System.out.println("Lobby: lobby socket observers size: " + socketObservers.size() + "");

            // ANY TIME A WAITING PLAYER IS ADDED, THE NOTIFICATION IS SENT TO THE WAITINGSCREENHANDLER BOTH FOR RMI AND SOCKETS
            //RMI
            for (LobbyObserver observer : remoteObservers.values()) {
                try {
                    observer.onWaitingPlayers(waitingPlayers);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            //SOCKET
            WaitingPlayersResponse response = new WaitingPlayersResponse(waitingPlayers, null, false);

            socketResponseToAll(response);

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

    public void startMatch() {

        synchronized (waitingPlayers) {

            //NOTIFIES TO ALL THE LOBBY "OBSERVERS" THE CREATION OF THE MATCH, SO FROM THEN THE CLIENTS CAN START THE GUI/CLI AND "OBSERVE" THE MATCH

            //SOCKETS
            MatchStartedResponse response = new MatchStartedResponse();
            for (ObjectOutputStream out : socketObservers.values()) {
                try {
                    out.writeObject(response);
                    out.reset();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            createMultiplayerMatch(waitingPlayers, new HashMap<>(socketObservers));

            //RMI
            for (LobbyObserver observer : remoteObservers.values()) {
                try {
                    observer.onMatchStarted();
                } catch (RemoteException e) {
                    e.printStackTrace();
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


    public void observeMatchRemote(String username, MatchObserver observer) {
        multiplayerMatches.get(username).observeMatchRemote(observer, username);
    }

    public Map<String, ObjectOutputStream> getSocketObservers() {
        return socketObservers;
    }

    public Map<String, LobbyObserver> getRemoteObservers() {
        return remoteObservers;
    }

    // check if there is a still alive match in which the player must be put
    public void addPlayer(String name) {
        if (multiplayerMatches.get(name) != null) {
            for (PlayerMultiplayer p : multiplayerMatches.get(name).getPlayers()) {
                if (p.getName().equals(name)) {
                    p.setStatus(ConnectionStatus.CONNECTED);
                }
            }
        } else {
            addToWaitingPlayers(name);
        }
    }

    public ConnectionStatus checkName(String name) {
        if (!takenUsernames.keySet().contains(name)) {
            return ConnectionStatus.ABSENT;
        } else {
            return takenUsernames.get(name);
        }
    }

}