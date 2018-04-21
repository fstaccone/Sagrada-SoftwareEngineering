package it.polimi.ingsw.model.gameobjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DecksContainer {

    private Random randomGenerator;
    private PrivateObjectiveCardDeck  privateObjectiveDeck;
    private PublicObjectiveCardDeck publicObjectiveDeck;
    private WindowPatternCardDeck windowPatternDeck;
    private WindowFramePlayerBoardDeck windowFrameDeck;

    private List<String> toolDeck;

    private List<ToolCard> pickedToolCards;

    public DecksContainer() {
        this.privateObjectiveDeck = new PrivateObjectiveCardDeck();
        this.publicObjectiveDeck = new PublicObjectiveCardDeck();
        this.windowPatternDeck = new WindowPatternCardDeck();
        this.windowFrameDeck = new WindowFramePlayerBoardDeck();

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

        for(int i=0;i<3;i++) {
            randomGenerator = new Random();
            int index = randomGenerator.nextInt(toolDeck.size() -i -1);
            String cardname = toolDeck.get(index);
            switch (cardname) {
                // In cardname inside the case statement we must put the real card's name
                case "tool1":  cardname = "tool1";
                    ToolCard card1 = new ToolCard(cardname);
                    this.pickedToolCards.add(card1);
                    this.toolDeck.remove("tool1");
                    break;
                case "tool2":  cardname= "tool2";
                    ToolCard card2= new ToolCard(cardname);
                    this.pickedToolCards.add(card2);
                    this.toolDeck.remove("tool2");
                    break;
                case "tool3":  cardname = "tool3";
                    ToolCard card3= new ToolCard(cardname);
                    this.pickedToolCards.add(card3);
                    this.toolDeck.remove("tool3");
                    break;
                case "tool4":  cardname = "tool4";
                    ToolCard card4= new ToolCard(cardname);
                    this.pickedToolCards.add(card4);
                    this.toolDeck.remove("tool4");
                    break;
                case "tool5":  cardname = "tool5";
                    ToolCard card5= new ToolCard(cardname);
                    this.pickedToolCards.add(card5);
                    this.toolDeck.remove("tool5");
                    break;
                case "tool6":  cardname = "tool6";
                    ToolCard card6= new ToolCard(cardname);
                    this.pickedToolCards.add(card6);
                    this.toolDeck.remove("tool6");
                    break;
                case "tool7":  cardname = "tool7";
                    ToolCard card7= new ToolCard(cardname);
                    this.pickedToolCards.add(card7);
                    this.toolDeck.remove("tool7");
                    break;
                case "tool8":  cardname = "tool8";
                    ToolCard card8= new ToolCard(cardname);
                    this.pickedToolCards.add(card8);
                    this.toolDeck.remove("tool8");
                    break;
                case "tool9":  cardname = "tool9";
                    ToolCard card9= new ToolCard(cardname);
                    this.pickedToolCards.add(card9);
                    this.toolDeck.remove("tool9");
                    break;
                case "tool10": cardname = "tool10";
                    ToolCard card10= new ToolCard(cardname);
                    this.pickedToolCards.add(card10);
                    this.toolDeck.remove("tool10");
                    break;
                case "tool11": cardname = "tool11";
                    ToolCard card11= new ToolCard(cardname);
                    this.pickedToolCards.add(card11);
                    this.toolDeck.remove("tool11");
                    break;
                case "tool12": cardname = "tool12";
                    ToolCard card12= new ToolCard(cardname);
                    this.pickedToolCards.add(card12);
                    this.toolDeck.remove("tool12");
                    break;
                default: cardname = "Invalid card";
                    break;
            }

        }

    }

    public PrivateObjectiveCardDeck getPrivateObjectiveCardDeck() {
        return privateObjectiveDeck;
    }

    public PublicObjectiveCardDeck  getPublicObjectiveCardDeck() {
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

    public void setPublicObjectiveCardDeck(PublicObjectiveCardDeck publicObjectiveDeck) {
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




    // Test for toolCardDeck
    public static void main (String[] args){
        DecksContainer decks = new DecksContainer();

        System.out.print(decks.getPickedToolCards().toString());

    }

}