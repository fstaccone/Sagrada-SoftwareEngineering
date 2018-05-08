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
    private int waitingTime;
    // map that contains the link between a client(only multiplayer clients) and the matchId useful for reconnection
    private Map<String,Integer> mapClientsToRoom;
    private List<MatchMultiplayer> multiplayerMatches;

    public Lobby(int waitingTime) {
        this.takenUsernames = new ArrayList<>();
        this.matchCounter = 0;
        this.waitingPlayers = new ArrayList<>();
        this.mapClientsToRoom = new HashMap<>();
        this.multiplayerMatches = new ArrayList<>();
        this.waitingTime = waitingTime;
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
        System.out.println("Match multiplayer creato: " + (matchCounter));
        System.out.println("Giocatori:");
        clients.forEach(c -> System.out.println(c));
    }

    public void addToWaitingPlayers(String name) {

        if(waitingPlayers.size() == 1){
            this.waitingPlayers.add(name);
            checkForStarting(this.waitingTime);
        }
       else{
            this.waitingPlayers.add(name);
        }
        // just for debug
        this.waitingPlayers.forEach(p -> System.out.println(p));
    }

    // deve fare anche il controllo sul timer
    public void checkForStarting(int waitingTime) {

        if(waitingPlayers.size() > 1) {
            int timer = 0;
            // just for debug
            System.out.println("Timer started!");
            while (waitingPlayers.size() < 4 && timer < (waitingTime / 1000)) {
                try {
                    Thread.sleep(1000);
                    timer++;
                    System.out.println(timer + "s of " + waitingTime/1000 );
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Problem in sleeping time while updating waiting time in Lobby");
                }
            }
            startMatch();
        }
    }

    // potrebbe controllare che tutti i giocatori aggiunti siano connessi prima di creare la partita
    // in caso di disconnessione gli fa passare il turno
    // in questo modo possiamo gestire la rinuncia alla partita semplicemente con un metodo removePlayer se il client
    // chiude prima che la partita sia iniziata
    private synchronized void startMatch(){
        // links between client and match are registered into the map
        for (String name: waitingPlayers) {
            mapClientsToRoom.put(name,matchCounter);
        }
        createMultiplayerMatch(waitingPlayers);
        matchCounter++;
        waitingPlayers = new ArrayList<>();
    }

}
