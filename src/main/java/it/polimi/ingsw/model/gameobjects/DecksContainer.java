package it.polimi.ingsw.model.gameobjects;

public class DecksContainer {

    private PrivateObjectiveCardDeck  privateObjectiveDeck;
    private PublicObjectiveCardDeck publicObjectiveDeck;
    private WindowPatternCardDeck windowPatternDeck;
    private WindowFramePlayerBoardDeck windowFrameDeck;
    private ToolCardDeck toolDeck;

    public DecksContainer() {
        this.privateObjectiveDeck = new PrivateObjectiveCardDeck();
        this.publicObjectiveDeck = new PublicObjectiveCardDeck();
        this.windowPatternDeck = new WindowPatternCardDeck();
        this.windowFrameDeck = new WindowFramePlayerBoardDeck();
        this.toolDeck = new ToolCardDeck();
    }

    public PrivateObjectiveCardDeck getPrivateObjectiveDeck() {
        return privateObjectiveDeck;
    }

    public PublicObjectiveCardDeck  getPublicObjectiveDeck() {
        return publicObjectiveDeck;
    }

    public WindowPatternCardDeck getWindowPatternDeck() {
        return windowPatternDeck;
    }

    public WindowFramePlayerBoardDeck getWindowFrameDeck() {
        return windowFrameDeck;
    }

    public ToolCardDeck getToolDeck() {
        return toolDeck;
    }

    //non credo servano
    public void setPrivateObjectiveDeck(PrivateObjectiveCardDeck privateObjectiveDeck) {
        this.privateObjectiveDeck = privateObjectiveDeck;
    }

    public void setPublicObjectiveDeck(PublicObjectiveCardDeck publicObjectiveDeck) {
        this.publicObjectiveDeck = publicObjectiveDeck;
    }

    public void setWindowPatternDeck(WindowPatternCardDeck windowPatternDeck) {
        this.windowPatternDeck = windowPatternDeck;
    }

    public void setWindowFrameDeck(WindowFramePlayerBoardDeck windowFrameDeck) {
        this.windowFrameDeck = windowFrameDeck;
    }

    public void setToolDeck(ToolCardDeck toolDeck) {
        this.toolDeck = toolDeck;
    }
}