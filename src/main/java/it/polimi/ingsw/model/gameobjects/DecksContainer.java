package it.polimi.ingsw.model.gameobjects;

public class DecksContainer {

    private PrivateObjectiveCardDeck privateObjectiveDeck;
    private WindowPatternCardDeck windowPatternDeck;
    private PublicObjectiveCardDeck publicObjectiveCardDeck;
    private ToolCardDeck toolCardDeck;

    /**
     * Constructor for DecksContainer.
     *
     * @param numOfPlayers is the number of players in the current match.
     * @param difficulty is the difficulty of the current match (if single player).
     */
    public DecksContainer(int numOfPlayers, int difficulty) {
        this.publicObjectiveCardDeck = new PublicObjectiveCardDeck(numOfPlayers);
        this.toolCardDeck = new ToolCardDeck(numOfPlayers, difficulty);
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
}