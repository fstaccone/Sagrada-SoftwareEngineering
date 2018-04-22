package it.polimi.ingsw.model.gameobjects;

import java.rmi.RemoteException;
import java.util.Set;

/*  Room is the container of players who decided to play a match
    It has to be created with a fixed number of players (between 1 and 4)
    ...
    Once the room is full, a match begins and other players cannot access the room.
 */


public class Room {

    private int roomId;
    private Match match;
    private Set<Player> players;
    private boolean started;
    private boolean full;
    private int numPlayers;

    public Room(int roomId, int numPlayers) {
        this.roomId = roomId;
        this.numPlayers = numPlayers;
        this.full = false;
        this.started = false;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    // if we have to return the players' set, do we need to protect the reference?
    public Set<Player> getPlayers() { return players; }

    // have these cases to be managed with exceptions?
    public synchronized void addPlayer(Player player) {
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
            this.setStarted();
            this.startMatch();
        }
    }

    public synchronized void removePlayer(Player player) { this.players.remove(player); }

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
        this.match = new Match(players);
        match.gameInit();
    }

    public int getRoomId() {
        return roomId;
    }

    /*  il controllo va fatto sul server, prima di accedere alla room, in questo modo puù essere usato come identificatore

    //added to try connectivity from view to model(MVC)
    public synchronized Player login(String username) throws RemoteException {
        if (loggedPlayers.stream().map(Player::getName).anyMatch(u -> u.equals(username))) {
            throw new RemoteException("Another player is already using the username you choose: " + username);
        }
        Player player = new Player(username,this);
        loggedPlayers.add(player);
        return player; //ritorna player al controller
    }

    */
}
