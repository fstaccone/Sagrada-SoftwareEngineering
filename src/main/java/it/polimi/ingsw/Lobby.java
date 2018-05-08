package it.polimi.ingsw;

import it.polimi.ingsw.model.gameobjects.MatchMultiplayer;
import it.polimi.ingsw.model.gameobjects.MatchSinglePlayer;

import java.util.*;

public class Lobby {

    private List<String> takenUsernames;
    private int matchCounter;
    private List<String> waitingPlayers;
    private int waitingTime;
    // map that contains the link between a client(only multiplayer clients) and the matchId useful for reconnection
    private Map<String, Integer> mapClientsToRoom;
    private List<MatchMultiplayer> multiplayerMatches;
    Timer timer;
    MatchStarter task;

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
    public synchronized void addUsername(String name) {
        this.takenUsernames.add(name);
    }

    // to remove usernames at the end of a match
    public synchronized void removeUsername(String name) {
        this.takenUsernames.remove(name);
    }

    public synchronized void createSingleplayerMatch(String name) {
        new MatchSinglePlayer(matchCounter, name);
        matchCounter++;
        for (int i = 0; i < takenUsernames.size(); i++) {
            System.out.println("Usernames: " + takenUsernames.toArray()[i].toString());
        }
    }

    public synchronized void createMultiplayerMatch(List<String> clients) {
        multiplayerMatches.add(new MatchMultiplayer(clients));
        System.out.println("Match multiplayer creato: " + (matchCounter));
        System.out.println("Giocatori:");
        clients.forEach(c -> System.out.println(c));
    }

    public void addToWaitingPlayers(String name) {
        synchronized (waitingPlayers) {
            //debug
            System.out.println("Players already waiting: " + waitingPlayers.size());

            this.waitingPlayers.add(name);

            if (waitingPlayers.size() == 2) {
                // L'inizializzazione di timer e task va messa qui perchè altrimenti dopo la prima volta che
                // viene chiamato il metodo timer.cancel() il timer non è più utilizzabile
                this.timer = new Timer();
                task = new MatchStarter(this);
                timer.schedule(task, waitingTime);
            }

            if (waitingPlayers.size() == 4) {
                timer.cancel();
                startMatch();
                // just for debug
                this.waitingPlayers.forEach(p -> System.out.println(p));
            }
        }
    }

        // potrebbe controllare che tutti i giocatori aggiunti siano connessi prima di creare la partita
        // in caso di disconnessione gli fa passare il turno
        // in questo modo possiamo gestire la rinuncia alla partita semplicemente con un metodo removePlayer se il client
        // chiude prima che la partita sia iniziata

        public void startMatch () {
            synchronized (waitingPlayers) {
                // links between client and match are registered into the map
                for (String name : waitingPlayers) {
                    mapClientsToRoom.put(name, matchCounter);
                }
                createMultiplayerMatch(waitingPlayers);
                matchCounter++;
                refreshWaitingList();
            }
        }

        public List<String> getWaitingPlayers () {
            return waitingPlayers;
        }

        public List<MatchMultiplayer> getMultiplayerMatches () {
            return multiplayerMatches;
        }

        private void refreshWaitingList () {
            this.waitingPlayers = new ArrayList<>();
        }
    }