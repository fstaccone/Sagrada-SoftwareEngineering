package it.polimi.ingsw.model.gameobjects;

public class DecksContainer {

    private PrivateObjectiveCardDeck privateObjectiveDeck;
    private WindowPatternCardDeck windowPatternDeck;
    private PublicObjectiveCardDeck publicObjectiveCardDeck;
    private ToolCardDeck toolCardDeck;

    public DecksContainer(int numOfPlayers,int difficulty) {
        this.publicObjectiveCardDeck = new PublicObjectiveCardDeck(numOfPlayers);
        this.toolCardDeck = new ToolCardDeck(numOfPlayers,difficulty);
        this.privateObjectiveDeck = new PrivateObjectiveCardDeck(numOfPlayers);
        this.windowPatternDeck = new WindowPatternCardDeck(numOfPlayers);
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
}