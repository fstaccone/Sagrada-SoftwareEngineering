package it.polimi.ingsw.model.gameobjects;

import java.util.Set;

public class Match {
    // Match doesn't contain numPlayers because that information is in Room (that's the entity that manage connections to the match)

    int matchId;
    Set<Player> players;
    DecksContainer decksContainer;
    Bag bag;
    Board board;

    public Match(Set<Player> players) {
        this.players = players;
        this.decksContainer = new DecksContainer();
        this.bag = new Bag(18);
        this.board = new Board(this, players);
    }

    public int getMatchId() {
        return matchId;
    }

    public DecksContainer getDecksContainer() {
        return decksContainer;
    }

    public Bag getBag() {
        return bag;
    }

    public Board getBoard() {
        return board;
    }
    // TODO: understand how to manage match's flow
    public void gameInit() { }

    public void proposeWindowPatternCards(){}

    public void assignPrivateObjectiveCardToPlayer(Player player){
    }

}

