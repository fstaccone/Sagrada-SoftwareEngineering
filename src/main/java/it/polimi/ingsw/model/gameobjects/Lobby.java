package it.polimi.ingsw.model.gameobjects;

import java.util.ArrayList;
import java.util.List;

public class Lobby {

    private List<String> takenUsernames;

    public Lobby() {
        takenUsernames = new ArrayList<>();
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

}
