package it.polimi.ingsw.model.gameobjects;


public class PrivateObjectiveCardDeck extends Deck<PrivateObjectiveCard>{

    public PrivateObjectiveCardDeck() {
        for (Colors c: Colors.values()) {
            cards.add(new PrivateObjectiveCard(c));
        }
    }

    public static void main (String[] args){
        PrivateObjectiveCardDeck deck = new PrivateObjectiveCardDeck();
        System.out.println(deck);
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
