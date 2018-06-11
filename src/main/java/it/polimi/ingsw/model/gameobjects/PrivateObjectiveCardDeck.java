package it.polimi.ingsw.model.gameobjects;

import java.util.Random;

public class PrivateObjectiveCardDeck extends Deck<PrivateObjectiveCard> {

    private Random randomGenerator;

    /**
     * Initializes the deck with the names of all possible private objective cards
     *
     * @param numOfPlayers is the match number of players
     */
    public PrivateObjectiveCardDeck(int numOfPlayers) {
        super();
        for (Colors c : Colors.values()) {
            if (c != Colors.NONE) {
                deck.add(c.toString());
            }
        }
        randomGenerator = new Random();
        setReallyCreatedCards(numOfPlayers);

    }

    /**
     * Creates the necessary number of private objective cards, one for each player
     *
     * @param numOfPlayers is the match number of players
     */
    public void setReallyCreatedCards(int numOfPlayers) {
        int n;
        if (numOfPlayers == 1)
            n = 2;
        else n = numOfPlayers;
        for (int i = 0; i < n; i++) {

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
                default:
                    break;
            }
        }

    }
}

