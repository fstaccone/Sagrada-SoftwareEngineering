package it.polimi.ingsw.model.gameobjects;

import java.util.*;

public  class PrivateObjectiveCardDeck extends Deck{

    Random randomGenerator;

    public PrivateObjectiveCardDeck(int numOfPlayers) {//VOLENDO SENZA UTILIZZARE I GENERIC POSSO IMPLEMENTARE COME GLI ALTRI MAZZI
        super();
        for (Colors c: Colors.values()) {
            if (c != Colors.NONE) {
                deck.add(c.toString());
            }
        }

        for(int i=0;i<numOfPlayers;i++){
                randomGenerator = new Random();
                int privateIndex = randomGenerator.nextInt(deck.size());
                String privateName = deck.get(privateIndex);

                switch (privateName) {

                    case "RED":
                        PrivateObjectiveCard card1 = new PrivateObjectiveCard(Colors.RED);
                        this.pickedCards.add(card1);
                        this.deck.remove("RED");
                        break;
                    case "BLUE":
                        PrivateObjectiveCard card2 = new PrivateObjectiveCard(Colors.BLUE);
                        this.pickedCards.add(card2);
                        this.deck.remove("BLUE");
                        break;
                    case "GREEN":
                        PrivateObjectiveCard card3 = new PrivateObjectiveCard(Colors.GREEN);
                        this.pickedCards.add(card3);
                        this.deck.remove("GREEN");
                        break;
                    case "VIOLET":
                        PrivateObjectiveCard card4 = new PrivateObjectiveCard(Colors.VIOLET);
                        this.pickedCards.add(card4);
                        this.deck.remove("VIOLET");
                        break;
                    case "YELLOW":
                        PrivateObjectiveCard card5 = new PrivateObjectiveCard(Colors.YELLOW);
                        this.pickedCards.add(card5);
                        this.deck.remove("YELLOW");
                        break;
                }
        }
    }
}

