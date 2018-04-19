package it.polimi.ingsw.model.gameobjects;


import java.util.HashSet;
import java.util.Set;

public class PrivateObjectiveCardDeck extends Deck<PrivateObjectiveCard>{

    public PrivateObjectiveCardDeck() {
        Set cards = new HashSet<>();
        for (Colors c: Colors.values()) {
            if(c!=Colors.NONE) {
                cards.add(new PrivateObjectiveCard(c));
            }
        }
    }

    /*public static void main (String[] args){
        PrivateObjectiveCardDeck deck = new PrivateObjectiveCardDeck();
        System.out.println(deck.cards.size());
    }*/

    @Override
    public Card pickOneCard() {
        return null;
    }

    @Override
    public Card[] pickNCards(int num) {
        return new Card[0];
    }
}
