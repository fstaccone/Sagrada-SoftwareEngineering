package it.polimi.ingsw.model.gameobjects;

import java.util.Set;

public class Board {
    private Set<PublicObjectiveCard> publicObjectiveCards;
    private Set <Player> players;
    private Reserve reserve;

    public Board(Match match, Set<Player> players) {
        this.publicObjectiveCards= match.getDecksContainer().getPublicObjectiveCardDeck().pickNCards(3);
        this.players = players;
        this.reserve = new Reserve();
    }
}

