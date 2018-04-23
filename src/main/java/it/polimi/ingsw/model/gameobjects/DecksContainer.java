package it.polimi.ingsw.model.gameobjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DecksContainer {

    private PrivateObjectiveCardDeck  privateObjectiveDeck;
    private WindowPatternCardDeck windowPatternDeck;
    private WindowFramePlayerBoardDeck windowFrameDeck;
    private PublicObjectiveCardDeck publicObjectiveCardDeck;
    private ToolCardDeck toolCardDeck;

    public DecksContainer(int numOfPlayers) {
        this.publicObjectiveCardDeck= new PublicObjectiveCardDeck();
        this.toolCardDeck=new ToolCardDeck();
        //this.privateObjectiveDeck = new PrivateObjectiveCardDeck();
        this.windowPatternDeck = new WindowPatternCardDeck(numOfPlayers);
        //this.windowFrameDeck = new WindowFramePlayerBoardDeck();
    }

    public PrivateObjectiveCardDeck getPrivateObjectiveCardDeck() {
        return privateObjectiveDeck;
    }

    public PublicObjectiveCardDeck getPublicObjectiveCardDeck() {
        return publicObjectiveCardDeck;
    }

    public WindowPatternCardDeck getWindowPatternCardDeck() {
        return windowPatternDeck;
    }

    public WindowFramePlayerBoardDeck getWindowFramePlaeyrBoardDeck() {
        return windowFrameDeck;
    }

    public ToolCardDeck getToolCardDeck() {
        return toolCardDeck;
    }

    //may not be useful
    public void setPrivateObjectiveCardDeck(PrivateObjectiveCardDeck privateObjectiveDeck) {
        this.privateObjectiveDeck = privateObjectiveDeck;
    }

    public void setWindowPatternCardDeck(WindowPatternCardDeck windowPatternDeck) {
        this.windowPatternDeck = windowPatternDeck;
    }

    public void setWindowFramePlayerBoardDeck(WindowFramePlayerBoardDeck windowFrameDeck) {
        this.windowFrameDeck = windowFrameDeck;
    }

    // Test for right creation of cards
    public static void main (String[] args){

        DecksContainer decks = new DecksContainer(1);
        System.out.println(decks.getToolCardDeck().getPickedToolCards().toString());
        System.out.println(decks.getPublicObjectiveCardDeck().getPickedPublicObjectiveCards().toString());
        System.out.println(decks.getWindowPatternCardDeck().getPickedWindowPatternCards().toString());
    }

}