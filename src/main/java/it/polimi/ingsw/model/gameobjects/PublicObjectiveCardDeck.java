package it.polimi.ingsw.model.gameobjects;

import java.util.*;

public  class PublicObjectiveCardDeck extends Deck<PublicObjectiveCard>{

    private Random randomGenerator;

    /**
     * Initializes the deck with all the public objective card id names and then it really creates 2 cards if it's a
     * single player match, 3 if it's a multiplayer match
     * @param numOfPlayers
     */
    public PublicObjectiveCardDeck(int numOfPlayers) {
        super();
        for(int i=1; i<11; i++) {
            this.deck.add("public"+i);
        }
        randomGenerator = new Random();
        setReallyCreatedCards(numOfPlayers);
    }

    /**
     * Creates a number of cards based on the number of players of the match (2 for single player, 3 for multiplayer)
     * @param numOfPlayers is the number of players of the match
     */
    public void setReallyCreatedCards(int numOfPlayers){
        int n;
        if(numOfPlayers == 1)
            n=2;
        else n=3;
        for(int j=0;j<n;j++) {
            int publicIndex = randomGenerator.nextInt(deck.size());
            System.out.println("Numero: " + publicIndex);//CANCELLA
            String publicName = deck.get(publicIndex);
            switch (publicName) {

                case "public1":
                    publicName = "Varietà di colore";
                    PublicObjectiveCard card1 = new PublicObjectiveCard(publicName);
                    this.pickedCards.add(card1);
                    this.deck.remove("public1");
                    break;
                case "public2":
                    publicName = "Diagonali colorate";
                    PublicObjectiveCard card2 = new PublicObjectiveCard(publicName);
                    this.pickedCards.add(card2);
                    this.deck.remove("public2");
                    break;
                case "public3":
                    publicName = "Sfumature diverse";
                    PublicObjectiveCard card3 = new PublicObjectiveCard(publicName);
                    this.pickedCards.add(card3);
                    this.deck.remove("public3");
                    break;
                case "public4":
                    publicName = "Sfumature scure";
                    PublicObjectiveCard card4 = new PublicObjectiveCard(publicName);
                    this.pickedCards.add(card4);
                    this.deck.remove("public4");
                    break;
                case "public5":
                    publicName = "Sfumature medie";
                    PublicObjectiveCard card5 = new PublicObjectiveCard(publicName);
                    this.pickedCards.add(card5);
                    this.deck.remove("public5");
                    break;
                case "public6":
                    publicName = "Sfumature chiare";
                    PublicObjectiveCard card6 = new PublicObjectiveCard(publicName);
                    this.pickedCards.add(card6);
                    this.deck.remove("public6");
                    break;
                case "public7":
                    publicName = "Sfumature diverse - Colonna";
                    PublicObjectiveCard card7 = new PublicObjectiveCard(publicName);
                    this.pickedCards.add(card7);
                    this.deck.remove("public7");
                    break;
                case "public8":
                    publicName = "Sfumature diverse - Riga";
                    PublicObjectiveCard card8 = new PublicObjectiveCard(publicName);
                    this.pickedCards.add(card8);
                    this.deck.remove("public8");
                    break;
                case "public9":
                    publicName = "Colori diversi - Colonna";
                    PublicObjectiveCard card9 = new PublicObjectiveCard(publicName);
                    this.pickedCards.add(card9);
                    this.deck.remove("public9");
                    break;
                case "public10":
                    publicName = "Colori diversi - Riga";
                    PublicObjectiveCard card10 = new PublicObjectiveCard(publicName);
                    this.pickedCards.add(card10);
                    this.deck.remove("public10");
                    break;
                default:
                    break;
            }

        }
    }

}
