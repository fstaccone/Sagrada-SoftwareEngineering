package it.polimi.ingsw.model.gameobjects;

import java.util.*;

public  class PublicObjectiveCardDeck extends Deck{

    private Random randomGenerator;
    private List<String> publicObjectiveDeck;
    private List<PublicObjectiveCard> pickedPublicObjectiveCards;

    public PublicObjectiveCardDeck() {
        this.publicObjectiveDeck = new ArrayList<>();
        this.pickedPublicObjectiveCards = new ArrayList<>();

        for(int i=1; i<11; i++) {
            this.publicObjectiveDeck.add("public"+i);
        }

        for(int j=0;j<3;j++) {
            randomGenerator = new Random();
            int publicIndex = randomGenerator.nextInt(publicObjectiveDeck.size() - 1);
            String publicName = publicObjectiveDeck.get(publicIndex);
            switch (publicName) {

                case "public1":
                    publicName = "Varietà di colore";
                    PublicObjectiveCard card1 = new PublicObjectiveCard(publicName);
                    this.pickedPublicObjectiveCards.add(card1);
                    this.publicObjectiveDeck.remove("public1");
                    break;
                case "public2":
                    publicName = "Diagonali colorate";
                    PublicObjectiveCard card2 = new PublicObjectiveCard(publicName);
                    this.pickedPublicObjectiveCards.add(card2);
                    this.publicObjectiveDeck.remove("public2");
                    break;
                case "public3":
                    publicName = "Sfumature diverse";
                    PublicObjectiveCard card3 = new PublicObjectiveCard(publicName);
                    this.pickedPublicObjectiveCards.add(card3);
                    this.publicObjectiveDeck.remove("public3");
                    break;
                case "public4":
                    publicName = "Sfumature scure";
                    PublicObjectiveCard card4 = new PublicObjectiveCard(publicName);
                    this.pickedPublicObjectiveCards.add(card4);
                    this.publicObjectiveDeck.remove("public4");
                    break;
                case "public5":
                    publicName = "Sfumature medie";
                    PublicObjectiveCard card5 = new PublicObjectiveCard(publicName);
                    this.pickedPublicObjectiveCards.add(card5);
                    this.publicObjectiveDeck.remove("public5");
                    break;
                case "public6":
                    publicName = "Sfumature chiare";
                    PublicObjectiveCard card6 = new PublicObjectiveCard(publicName);
                    this.pickedPublicObjectiveCards.add(card6);
                    this.publicObjectiveDeck.remove("public6");
                    break;
                case "public7":
                    publicName = "Sfumature diverse - Colonna";
                    PublicObjectiveCard card7 = new PublicObjectiveCard(publicName);
                    this.pickedPublicObjectiveCards.add(card7);
                    this.publicObjectiveDeck.remove("public7");
                    break;
                case "public8":
                    publicName = "Sfumature diverse - Riga";
                    PublicObjectiveCard card8 = new PublicObjectiveCard(publicName);
                    this.pickedPublicObjectiveCards.add(card8);
                    this.publicObjectiveDeck.remove("public8");
                    break;
                case "public9":
                    publicName = "Colori diversi - Colonna";
                    PublicObjectiveCard card9 = new PublicObjectiveCard(publicName);
                    this.pickedPublicObjectiveCards.add(card9);
                    this.publicObjectiveDeck.remove("public9");
                    break;
                case "public10":
                    publicName = "Colori diversi - Riga";
                    PublicObjectiveCard card10 = new PublicObjectiveCard(publicName);
                    this.pickedPublicObjectiveCards.add(card10);
                    this.publicObjectiveDeck.remove("public10");
                    break;
                default:
                    publicName = "Invalid card";
                    break;
            }

        }
    }

    public List<PublicObjectiveCard> getPickedPublicObjectiveCards() {
        return pickedPublicObjectiveCards;
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
