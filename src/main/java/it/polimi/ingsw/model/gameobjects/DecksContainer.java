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

    public ToolCardDeck getToolCardDeck() {
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

    public void setToolCardDeck(ToolCardDeck toolDeck) {
        this.toolDeck = toolDeck;
    }
}