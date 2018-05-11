package it.polimi.ingsw;

import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
import it.polimi.ingsw.model.gamelogic.MatchSingleplayer;

import java.rmi.RemoteException;
import java.util.*;

public class Lobby {

    private List<LobbyObserver> observers;
    private List<String> takenUsernames;
    private int matchCounter;
    private final List<String> waitingPlayers;
    private int waitingTime;
    private int turnTime;
    // map that contains the link between a client(only multiplayer clients) and the matchId useful for reconnection
    private Map<String, Integer> mapClientsToRoom;
    private List<MatchMultiplayer> multiplayerMatches;
    // to simulate the timer before creating a match
    private Timer timer;
    private MatchStarter task;

    public Lobby(int waitingTime, int turnTime) {
        this.observers=new LinkedList<>();
        this.takenUsernames = new ArrayList<>();
        this.matchCounter = 0;
        this.waitingPlayers = new ArrayList<>();
        this.mapClientsToRoom = new HashMap<>();
        this.multiplayerMatches = new ArrayList<>();
        this.waitingTime = waitingTime;
        this.turnTime = turnTime;
    }

    public List<String> getTakenUsernames() {
        return new ArrayList<>(takenUsernames);
    }

    // to add a new username to the list
    public synchronized void addUsername(String name) {
        this.takenUsernames.add(name);
    }

    // to remove usernames at the end of a match or when a player leave a match before its creation
    public synchronized void removeUsername(String name) {
        this.takenUsernames.remove(name);
    }

    public synchronized void createSingleplayerMatch(String name) {
        new MatchSingleplayer(matchCounter, name);
        matchCounter++;

        // debug
        System.out.println("By lobby: Match number: " + matchCounter + " type: singleplayer");
        System.out.println("By lobby: Player: " + name);
    }

    private synchronized void createMultiplayerMatch(List<String> clients) {
        multiplayerMatches.add(new MatchMultiplayer(matchCounter, clients, turnTime));
        System.out.println("By lobby: Match number: " + matchCounter + " type: multiplayer");
        System.out.println("By lobby: Players: ");
        clients.forEach(c -> System.out.print(c + "\t"));
        System.out.println("\n");
        // da rivedere la chiamata
        multiplayerMatches.get(multiplayerMatches.size()-1).run();
    }

    public void removeFromWaitingPlayers(String name) {
        synchronized (waitingPlayers) {

            if(waitingPlayers.size() == 2) {
                waitingPlayers.remove(name);
                timer.cancel();
                System.out.println("Player " + name + " has left the room!");
                System.out.println("Timer has been reset, only one waiting player left!");
            }
            else{
                waitingPlayers.remove(name);
                // debug
                System.out.println("Player " + name + " has left the room!");
            }

        }
    }


    public void addToWaitingPlayers(String name) {
        synchronized (waitingPlayers) {

            waitingPlayers.add(name);

            for (LobbyObserver observer : observers) {
                try {
                    observer.onWaitingPlayers(waitingPlayers);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            //debug
            if (waitingPlayers.size()==1) System.out.println("There is 1 player waiting for a match.");
            else System.out.println("There are " + waitingPlayers.size() + " players waiting for a match.");


            // if there are two players waiting for the match beginning, the timer is set
            if (waitingPlayers.size() == 2) {
                System.out.println("Timer started: 30 seconds from now!");
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
            // links between client and match are registered into the map
            for (String name : waitingPlayers) {
                mapClientsToRoom.put(name, matchCounter);
            }
            createMultiplayerMatch(waitingPlayers);
            matchCounter++;
            waitingPlayers.clear();
        }
    }

    public List<String> getWaitingPlayers() {
        return waitingPlayers;
    }

    public List<MatchMultiplayer> getMultiplayerMatches() {
        return multiplayerMatches;
    }

    public void observeLobby(LobbyObserver lobbyObserver){
        observers.add(lobbyObserver);
    }
}