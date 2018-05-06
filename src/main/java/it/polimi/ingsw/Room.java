package it.polimi.ingsw;

import it.polimi.ingsw.Server;
import it.polimi.ingsw.model.gameobjects.Match;
import it.polimi.ingsw.model.gameobjects.MatchMultiplayer;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.model.gameobjects.PlayerMultiplayer;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/*  Room is the container of players who decided to play a match
    It has to be created with a fixed number of players (between 1 and 4)
    ...
    Once the room is full, a match begins and other players cannot access the room.
 */


public class Room {

    private int roomId;
    private Match match;
    private List<Player> players;
    private boolean started;
    private boolean full;
    private int numPlayers;

    // Da capire come la room comunica con il server
    private Server server;

    public Room(int roomId, int numPlayers, Server server) {
        this.roomId = roomId;
        this.numPlayers = numPlayers;
        this.full = false;
        this.started = false;
        this.server = server;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    // if we have to return the players' set, do we need to protect the reference?
    public List<Player> getPlayers() { return players; }

    // have these cases to be managed with exceptions?
    public synchronized void addPlayer(PlayerMultiplayer player) {
        if (isFull()){
            System.out.println("This match is already full!");
            return;
        }
        // maybe it makes no sense because a player can decide to quit and then to continue the match after the reconnection
        if (isStarted()){
            System.out.println("This match is already begun!");
            return;
        }
        this.players.add(player);

        // if the room is full the game can begin
        // potrebbe nascere un problema per il timer, ma comunque io credo (Paolo) che sia un bene far iniziare la partita appena la stanza è piena anche se il timer non è scaduto
        if (this.isFull()){
            //server.createNewRoom();
            this.setStarted();
            this.startMatch();
        }
    }

    public synchronized void removePlayer(PlayerMultiplayer player) { this.players.remove(player); }

    // Can be useful for the server to have this method? If not it makes no sense
    public boolean isStarted() { return started; }

    private void setStarted() { this.started = true; }

    private boolean isFull() {
        if(players.size() == numPlayers) {
            this.full = true;
        }
        return full;
    }

    // Crea match e fa partire il gioco
    // Potrebbe creare la finestra di gioco nella finestra in cui girava la room
    private void startMatch(){
        // solo per far funzionare temporaneamente, non serve
        this.match = new MatchMultiplayer(players.stream().map(p -> p.toString()).collect(Collectors.toList()));
        match.gameInit();
    }

}
