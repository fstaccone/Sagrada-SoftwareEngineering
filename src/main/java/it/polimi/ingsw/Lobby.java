package it.polimi.ingsw;

import it.polimi.ingsw.model.gameobjects.MatchMultiplayer;
import it.polimi.ingsw.model.gameobjects.MatchSinglePlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lobby {

    private List<String> takenUsernames;
    private int matchCounter;
    private List<String> waitingPlayers;
    // map that contains the link between a client(only multiplayer clients) and the matchId useful for reconnection
    private Map<String,Integer> mapClientsToRoom;
    private List<MatchMultiplayer> multiplayerMatches;

    public Lobby() {
        this.takenUsernames = new ArrayList<>();
        this.matchCounter = 0;
        this.waitingPlayers = new ArrayList<>();
        this.mapClientsToRoom = new HashMap<>();
        this.multiplayerMatches = new ArrayList<>();
    }

    public List<String> getTakenUsernames() {
        // this must return a copy of the instance (only for reading)
        return takenUsernames;
    }

    // to add a new username to the list
    public synchronized void addUsername(String name){
        this.takenUsernames.add(name);
    }

    // to remove usernames at the end of a match
    public synchronized void removeUsername(String name){
        this.takenUsernames.remove(name);
    }

    public synchronized void createSingleplayerMatch( String name){
        new MatchSinglePlayer(matchCounter, name);
        matchCounter++;
        for (int i = 0; i < takenUsernames.size(); i++) {
            System.out.println("Usernames: " + takenUsernames.toArray()[i].toString());
        }
    }

    public synchronized void createMultiplayerMatch(List<String> clients){
        multiplayerMatches.add( new MatchMultiplayer(clients));
        System.out.println("Match multiplayer creato: " + (matchCounter - 1));
    }

    public synchronized void addToWaitingPlayers(String name){

        if(waitingPlayers.size() == 0){
            // vuol dire che è una nuova partita dunque fa partire il timer
        }
        this.waitingPlayers.add(name);
        this.waitingPlayers.stream().forEach(p -> System.out.println(p));
        checkForStarting();
    }

    // deve fare anche il controllo sul timer
    public void checkForStarting(){
        if (waitingPlayers.size() == 4){
            for (String name: waitingPlayers) {
                mapClientsToRoom.put(name,matchCounter);
            }
            createMultiplayerMatch(waitingPlayers);
            matchCounter++;
            waitingPlayers = new ArrayList<>();
        }
    }

}
