package it.polimi.ingsw.model.gameobjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DecksContainer {

    private Random randomGenerator;
    private PrivateObjectiveCardDeck  privateObjectiveDeck;
    private WindowPatternCardDeck windowPatternDeck;
    private WindowFramePlayerBoardDeck windowFrameDeck;

    private List<String> publicObjectiveDeck;
    private List<PublicObjectiveCard> pickedPublicObjectiveCards;

    private List<String> toolDeck;
    private List<ToolCard> pickedToolCards;

    public DecksContainer() {
        //this.privateObjectiveDeck = new PrivateObjectiveCardDeck();
        //this.windowPatternDeck = new WindowPatternCardDeck();
        //this.windowFrameDeck = new WindowFramePlayerBoardDeck();

        this.pickedPublicObjectiveCards=new ArrayList<>();
        this.publicObjectiveDeck=new ArrayList<>();
        this.publicObjectiveDeck.add("public1");
        this.publicObjectiveDeck.add("public2");
        this.publicObjectiveDeck.add("public3");
        this.publicObjectiveDeck.add("public4");
        this.publicObjectiveDeck.add("public5");
        this.publicObjectiveDeck.add("public6");
        this.publicObjectiveDeck.add("public7");
        this.publicObjectiveDeck.add("public8");
        this.publicObjectiveDeck.add("public9");
        this.publicObjectiveDeck.add("public10");

        for(int i=0;i<3;i++) {
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

        this.pickedToolCards = new ArrayList<>();
        this.toolDeck = new ArrayList<>();
        this.toolDeck.add("tool1");
        this.toolDeck.add("tool2");
        this.toolDeck.add("tool3");
        this.toolDeck.add("tool4");
        this.toolDeck.add("tool5");
        this.toolDeck.add("tool6");
        this.toolDeck.add("tool7");
        this.toolDeck.add("tool8");
        this.toolDeck.add("tool9");
        this.toolDeck.add("tool10");
        this.toolDeck.add("tool11");
        this.toolDeck.add("tool12");

        for(int j=0;j<3;j++) {
            randomGenerator = new Random();
            int toolIndex = randomGenerator.nextInt(toolDeck.size() -1);
            String toolName = toolDeck.get(toolIndex);
            switch (toolName) {

                case "tool1":  toolName = "Pinza Sgrossatrice";
                    ToolCard card1 = new ToolCard(toolName);
                    this.pickedToolCards.add(card1);
                    this.toolDeck.remove("tool1");
                    break;
                case "tool2":  toolName= "Pennello per Eglomise";
                    ToolCard card2= new ToolCard(toolName);
                    this.pickedToolCards.add(card2);
                    this.toolDeck.remove("tool2");
                    break;
                case "tool3":  toolName = "Alesatore per Lamina di Rame";
                    ToolCard card3= new ToolCard(toolName);
                    this.pickedToolCards.add(card3);
                    this.toolDeck.remove("tool3");
                    break;
                case "tool4":  toolName = "Lathekin";
                    ToolCard card4= new ToolCard(toolName);
                    this.pickedToolCards.add(card4);
                    this.toolDeck.remove("tool4");
                    break;
                case "tool5":  toolName = "Taglierina Circolare";
                    ToolCard card5= new ToolCard(toolName);
                    this.pickedToolCards.add(card5);
                    this.toolDeck.remove("tool5");
                    break;
                case "tool6":  toolName = "Pennello per Pasta Salda";
                    ToolCard card6= new ToolCard(toolName);
                    this.pickedToolCards.add(card6);
                    this.toolDeck.remove("tool6");
                    break;
                case "tool7":  toolName = "Martelletto";
                    ToolCard card7= new ToolCard(toolName);
                    this.pickedToolCards.add(card7);
                    this.toolDeck.remove("tool7");
                    break;
                case "tool8":  toolName = "Tenaglia a Rotelle";
                    ToolCard card8= new ToolCard(toolName);
                    this.pickedToolCards.add(card8);
                    this.toolDeck.remove("tool8");
                    break;
                case "tool9":  toolName = "Riga in Sughero";
                    ToolCard card9= new ToolCard(toolName);
                    this.pickedToolCards.add(card9);
                    this.toolDeck.remove("tool9");
                    break;
                case "tool10": toolName = "Tampone Diamantato";
                    ToolCard card10= new ToolCard(toolName);
                    this.pickedToolCards.add(card10);
                    this.toolDeck.remove("tool10");
                    break;
                case "tool11": toolName = "Diluente per Pasta Salda";
                    ToolCard card11= new ToolCard(toolName);
                    this.pickedToolCards.add(card11);
                    this.toolDeck.remove("tool11");
                    break;
                case "tool12": toolName = "Taglierina Manuale";
                    ToolCard card12= new ToolCard(toolName);
                    this.pickedToolCards.add(card12);
                    this.toolDeck.remove("tool12");
                    break;
                default: toolName = "Invalid card";
                    break;
            }

        }

    }

    public PrivateObjectiveCardDeck getPrivateObjectiveCardDeck() {
        return privateObjectiveDeck;
    }

    public List getPublicObjectiveCardDeck() {
        return publicObjectiveDeck;
    }

    public WindowPatternCardDeck getWindowPatternCardDeck() {
        return windowPatternDeck;
    }

    public WindowFramePlayerBoardDeck getWindowFramePlaeyrBoardDeck() {
        return windowFrameDeck;
    }

    public List getToolCardDeck() {
        return toolDeck;
    }

    //may not be useful
    public void setPrivateObjectiveCardDeck(PrivateObjectiveCardDeck privateObjectiveDeck) {
        this.privateObjectiveDeck = privateObjectiveDeck;
    }

    public void setPublicObjectiveCardDeck(List publicObjectiveDeck) {
        this.publicObjectiveDeck = publicObjectiveDeck;
    }

    public void setWindowPatternCardDeck(WindowPatternCardDeck windowPatternDeck) {
        this.windowPatternDeck = windowPatternDeck;
    }

    public void setWindowFramePlayerBoardDeck(WindowFramePlayerBoardDeck windowFrameDeck) {
        this.windowFrameDeck = windowFrameDeck;
    }

    public void setToolCardDeck(List toolDeck) {
        this.toolDeck = toolDeck;
    }

    public List<ToolCard> getPickedToolCards() {
        return pickedToolCards;
    }

    public List<PublicObjectiveCard> getPickedPublicObjectiveCards(){
        return pickedPublicObjectiveCards;
    }






    /*// Test for toolCardDeck
    public static void main (String[] args){
        DecksContainer decks = new DecksContainer();

        System.out.println(decks.getPickedToolCards().toString());
        System.out.println(decks.getPickedPublicObjectiveCards().toString());
    }
    */
}