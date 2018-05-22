package it.polimi.ingsw;

import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
import it.polimi.ingsw.model.gamelogic.MatchSingleplayer;
import it.polimi.ingsw.model.gamelogic.MatchStarter;
import it.polimi.ingsw.model.gameobjects.PlayerMultiplayer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.util.*;

public class Lobby {

    private int matchCounter;
    private int waitingTime;
    private int turnTime;

    // to ensure that a username is unique
    private List<String> takenUsernames;
    // waiting list before the  beginning of a new multiplayer match
    private final List<String> waitingPlayers;

    // to store relation between client and match
    private Map<String, MatchSingleplayer> singleplayerMatches;
    private Map<String, MatchMultiplayer> multiplayerMatches;

    // to store observers
    private Map<String, LobbyObserver> remoteObservers;
    private Map<String,ObjectOutputStream> socketObservers;



    // to simulate the timer before creating a match
    private Timer timer;
    private MatchStarter task;


    public Lobby(int waitingTime, int turnTime) {

        this.matchCounter = 0;

        this.takenUsernames = new ArrayList<>();

        this.waitingPlayers = new ArrayList<>();

        this.multiplayerMatches = new HashMap<>();
        this.singleplayerMatches = new HashMap<>();

        this.remoteObservers = new HashMap<>();

        this.waitingTime = waitingTime;
        this.turnTime = turnTime;
        this.socketObservers=new HashMap<>();
    }

    public List<String> getTakenUsernames() {
        return new ArrayList<>(takenUsernames);
    }

    public Map<String, MatchMultiplayer> getMultiplayerMatches() {
        return multiplayerMatches;
    }

    public Map<String, MatchSingleplayer> getSingleplayerMatches() { return singleplayerMatches; }

    // to add a new username to the list
    public synchronized void addUsername(String name) {
        this.takenUsernames.add(name);
    }

    // to remove usernames at the end of a match or when a player leave a match before its creation
    private synchronized void removeUsername(String name) {
        this.takenUsernames.remove(name);
    }

    public synchronized void createSingleplayerMatch(String name) {

        singleplayerMatches.put(name, new MatchSingleplayer(matchCounter, name));
        matchCounter++;

        // debug
        System.out.println("By lobby: Match number: " + matchCounter + " type: singleplayer");
        System.out.println("By lobby: Player: " + name);
    }

    private synchronized void createMultiplayerMatch(List<String> clients,  Map<String,ObjectOutputStream> socketsOut) {

        MatchMultiplayer match = new MatchMultiplayer(matchCounter, clients, turnTime, socketsOut);
        for (String s : clients) {
            multiplayerMatches.put(s, match);
        }

        System.out.println("By lobby: Match number: " + matchCounter + " type: multiplayer");
        System.out.println("By lobby: Players: ");

        clients.forEach(c -> System.out.print(c + "\t"));
        System.out.println("\n");
    }

    public void removeFromWaitingPlayers(String name) {
        boolean unique=false;
        synchronized (waitingPlayers) {
            try {
                if (waitingPlayers.size() == 2) {
                    timer.cancel();
                    waitingPlayers.remove(name);
                    removeUsername(name);
                    unique=true;

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

            WaitingPlayersResponse response = new WaitingPlayersResponse(waitingPlayers,name,unique);
            socketObservers.keySet().forEach(playerName -> {
                try {
                    socketObservers.get(playerName).writeObject(response);
                    socketObservers.get(playerName).reset();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    // da usare in caso di disconnessione. A fine partita?
    public void removeObserver(String name) {
        remoteObservers.remove(name);
    }

    private PlayerMultiplayer getPlayer(String name){

        for(PlayerMultiplayer p : multiplayerMatches.get(name).getPlayers()){
            if(p.getName().equals(name)) {
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
            removeUsername(p.getName());
            multiplayerMatches.get(name).getRemoteObservers().remove(p);

            // to check if the game must be closed
            if (multiplayerMatches.get(name).checkConnection() < 2) {

                // to check if the match must be closed and eventually
                // delete observers and usernames of other players
                for (PlayerMultiplayer player : multiplayerMatches.get(name).getPlayers()) {
                    if (!player.getName().equals(p.getName())) {
                        removeUsername(player.getName());
                        multiplayerMatches.get(name).getRemoteObservers().remove(player);
                    }
                }
                multiplayerMatches.get(name).terminateMatch();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("From Lobby: problem in disconnecting player " + name + "!");
        }
    }

    public void removeFromMatchSingleplayer(String name) {
        try {
            singleplayerMatches.get(name).terminateMatch();
            removeUsername(name);
        } catch (Exception e) {
            e.printStackTrace();
            // debug
            System.out.println("From lobby: this SinglePlayer Match doesn't exist.");
        }
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
            WaitingPlayersResponse response = new WaitingPlayersResponse(waitingPlayers,null,false);
            socketObservers.keySet().forEach(playerName -> {
                try {
                    socketObservers.get(playerName).writeObject(response);
                    socketObservers.get(playerName).reset();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            //DEBUG SERVER SIDE
            if (waitingPlayers.size() == 1) System.out.println("Lobby: There is 1 player waiting for a match." + "\n");
            else
                System.out.println("Lobby: There are " + waitingPlayers.size() + " players waiting for a match." + "\n");


            // IF THERE ARE 2 PLAYERS WAITING FOR THE MATCH BEGINNING, THE TIMER IS SET
            if (waitingPlayers.size() == 2) {
                System.out.println("Lobby :Timer started: 30 seconds from now!");
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
            for (ObjectOutputStream out: socketObservers.values()) {
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
        for (MatchMultiplayer match : multiplayerMatches.values()) {
            if (match == multiplayerMatches.get(username)) {
                match.observeMatchRemote(observer, username);
                break;
            }
        }
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
                    p.setStatus(ConnectionStatus.READY);
                }
            }
        } else {
            addToWaitingPlayers(name);
        }
    }

    public boolean checkName(String name){
        return takenUsernames.contains(name);
    }

}