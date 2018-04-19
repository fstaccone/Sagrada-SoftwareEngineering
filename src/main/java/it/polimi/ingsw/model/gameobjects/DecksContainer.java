package it.polimi.ingsw.model.gameobjects;
import java.util.ArrayList;

public class DecksContainer {

    private ArrayList<PrivateObjectiveCard> privateObjectiveDeck;
    private ArrayList<PublicObjectiveCard> publicObjectiveDeck;
    private ArrayList<WindowPatternCard> windowPatternDeck;
    private ArrayList<WindowFramePlayerBoard> windowFrameDeck;
    private ArrayList<ToolCard> toolDeck;

    public DecksContainer(ArrayList<PrivateObjectiveCard> privateObjectiveDeck, ArrayList<PublicObjectiveCard> publicObjectiveDeck, ArrayList<WindowPatternCard> windowPatternDeck, ArrayList<WindowFramePlayerBoard> windowFrameDeck, ArrayList<ToolCard> toolDeck) {

        this.privateObjectiveDeck = privateObjectiveDeck;
        this.publicObjectiveDeck = publicObjectiveDeck;
        this.windowPatternDeck = windowPatternDeck;
        this.windowFrameDeck = windowFrameDeck;
        this.toolDeck=toolDeck;
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