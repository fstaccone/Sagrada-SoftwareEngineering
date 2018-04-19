package it.polimi.ingsw.model.gameobjects;

import java.lang.reflect.Array;

public class PrivateObjectiveCardDeck extends Deck<PrivateObjectiveCard>{

    public PrivateObjectiveCardDeck() {
        Array colors = Colors.
    }

    @Override
    public Card pickOneCard() {
        return null;
    }

    @Override
    public Card[] pickNCards(int num) {
        return new Card[0];
    }
}
