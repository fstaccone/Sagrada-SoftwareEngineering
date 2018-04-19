package it.polimi.ingsw.model.gameobjects;

public class DecksContainer {

    private PrivateObjectiveDeck <PrivateObjectiveCard> privateObjectiveDeck;
    private PublicObjectiveDeco<PublicObjectiveCard> publicObjectiveDeck;
    private WindowPatternDeck<WindowPatternCard> windowPatternDeck;
    private WindowFrameDeck<WindowFramePlayerBoard> windowFrameDeck;
    private ToolDeck<ToolCard> toolDeck;

    public DecksContainer() {
        this.privateObjectiveDeck = new PrivateObjectiveDeck();
        this.publicObjectiveDeck = new PublicObjectiveDeck();
        this.windowPatternDeck = new WindowPatternDeck();
        this.windowFrameDeck = new WindowFrameDeck();
        this.toolDeck = new ToolDeck();
    }

    public ArrayList<PrivateObjectiveCard> getPrivateObjectiveDeck() {
        return privateObjectiveDeck;
    }

    public ArrayList<PublicObjectiveCard> getPublicObjectiveDeck() {
        return publicObjectiveDeck;
    }

    public ArrayList<WindowPatternCard> getWindowPatternDeck() {
        return windowPatternDeck;
    }

    public ArrayList<WindowFramePlayerBoard> getWindowFrameDeck() {
        return windowFrameDeck;
    }

    public ArrayList<ToolCard> getToolDeck() {
        return toolDeck;
    }

    //non credo servano
    public void setPrivateObjectiveDeck(ArrayList<PrivateObjectiveCard> privateObjectiveDeck) {
        this.privateObjectiveDeck = privateObjectiveDeck;
    }

    public void setPublicObjectiveDeck(ArrayList<PublicObjectiveCard> publicObjectiveDeck) {
        this.publicObjectiveDeck = publicObjectiveDeck;
    }

    public void setWindowPatternDeck(ArrayList<WindowPatternCard> windowPatternDeck) {
        this.windowPatternDeck = windowPatternDeck;
    }

    public void setWindowFrameDeck(ArrayList<WindowFramePlayerBoard> windowFrameDeck) {
        this.windowFrameDeck = windowFrameDeck;
    }

    public void setToolDeck(ArrayList<ToolCard> toolDeck) {
        this.toolDeck = toolDeck;
    }
}