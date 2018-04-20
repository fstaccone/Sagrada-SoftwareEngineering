package it.polimi.ingsw.model.gameobjects;


import java.util.HashSet;
import java.util.Set;

public  class PrivateObjectiveCardDeck extends Deck<PrivateObjectiveCard>{

    public PrivateObjectiveCardDeck() {
        Set cards = new HashSet<>();
        for (Colors c: Colors.values()) {
            if(c!=Colors.NONE) {
                cards.add(new PrivateObjectiveCard(c));
            }
        }
    }

    @Override
    public Card pickOneCard() {
        return null;
    }

    @Override
    public Set<Card> pickNCards(int num) {
        return null;
    }
}
