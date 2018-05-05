package it.polimi.ingsw.model.gameobjects;

import java.util.ArrayList;
import java.util.List;

public class Lobby {

    private List<String> takenUsernames;
    private int matchCounter;

    public Lobby() {
        this.takenUsernames = new ArrayList<>();
        this.matchCounter = 0;
    }

    public List<String> getTakenUsernames() {
        // this must return a copy of the instance (only for reading)
        return takenUsernames;
    }

    // to add a new username to the list
    public void addUsername(){

    }

    // to remove usernames at the end of a match
    public void removeUsername(){

    }

    public void createSingleplayerMatch(String name){
        new MatchSinglePlayer(matchCounter, name);

    }

    public void createMultiplayerMatch(){

    }


}
