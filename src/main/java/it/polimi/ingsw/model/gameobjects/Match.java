package it.polimi.ingsw.model.gameobjects;

import java.util.List;

public class Match {
    List<Player> players;
    DecksContainer decksContainer;
    Bag bag;
    Board board;

    public Match(List<Player> players) {
        this.players=players;//or not? to understand what's the relationship with Room
        this.decksContainer = new DecksContainer();
        this.bag = new Bag(18);
        this.board=new Board(this,players);
    }

    public DecksContainer getDecksContainer() {
        return decksContainer;
    }
}

