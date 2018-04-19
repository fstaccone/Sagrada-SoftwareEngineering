package it.polimi.ingsw.model.gameobjects;

import java.util.Set;

public class Room {
    private final int roomId;
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
    }

    public synchronized void removePlayer(Player player) { this.players.remove(player); }

    public boolean isStarted() { return started; }

    public void setStarted(boolean started) { this.started = started; }

    public boolean isFull() {
        if(players.size() == numPlayers) {
            setFull(true);
        }
        return full;
    }

    public void setFull(boolean full) { this.full = full; }

    public void startMatch(){
        this.match = new Match(players);
    }
}
