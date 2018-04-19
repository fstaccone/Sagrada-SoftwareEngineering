package it.polimi.ingsw.model.gameobjects;

import java.util.HashSet;
import java.util.Set;

public class PublicObjectiveCardDeck extends Deck{

    public PublicObjectiveCardDeck() {
        cards = new HashSet(); // TODO: incomplete
    }

    public Set<PublicObjectiveCard> getPublicObjectiveCardDeck() {
        return cards;
    }
}
