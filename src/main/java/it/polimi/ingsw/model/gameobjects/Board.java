package it.polimi.ingsw.model.gameobjects;

import java.util.List;

public class Board {
    private List <PublicObjectiveCard> publicObjectiveCards;
    private List <Player> players;
    private Reserve reserve;

    public Board(Match match, List<Player> players) {
        this.publicObjectiveCards= match.getDecksContainer().getPublicObjectiveCardDeck().pickNCards(3);
        this.players = players;
        this.reserve = new Reserve();
    }
}

