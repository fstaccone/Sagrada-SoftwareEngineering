package it.polimi.ingsw.model.gameobjects;

public class DecksContainer {

    private PrivateObjectiveCardDeck  privateObjectiveDeck;
    private WindowPatternCardDeck windowPatternDeck;
    private WindowFramePlayerBoardDeck windowFrameDeck;
    private PublicObjectiveCardDeck publicObjectiveCardDeck;
    private ToolCardDeck toolCardDeck;

    public DecksContainer(int numOfPlayers) {
        this.publicObjectiveCardDeck= new PublicObjectiveCardDeck(numOfPlayers);
        this.toolCardDeck=new ToolCardDeck();
        this.privateObjectiveDeck = new PrivateObjectiveCardDeck(numOfPlayers);
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

    public WindowFramePlayerBoardDeck getWindowFramePlayerBoardDeck() {
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

}