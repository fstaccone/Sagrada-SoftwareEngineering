package it.polimi.ingsw.model.gameobjects;

import it.polimi.ingsw.ConnectionStatus;
import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;

public class PlayerMultiplayer extends Player {
    private int numFavorTokens;
    private PrivateObjectiveCard privateObjectiveCard;
    private ConnectionStatus status;
    private boolean myTurn;
    private MatchMultiplayer match;
    private boolean schemeCardSet;

    /**
     * Creates a new Player for multiplayer match
     * @param name is the name of the player
     * @param match is the match he is joining
     */
    public PlayerMultiplayer(String name, MatchMultiplayer match) {
        super(name);
        status = ConnectionStatus.CONNECTED;
        myTurn = false;
        schemeCardSet = false;
        this.match = match;
    }

    // setter
    public void setNumFavorTokens(int numFavorTokens) {
        this.numFavorTokens = numFavorTokens;
    }
    public void setPrivateObjectiveCard(PrivateObjectiveCard privateObjectiveCard) { this.privateObjectiveCard = privateObjectiveCard; }
    public void setStatus(ConnectionStatus status) { this.status = status; }
    public void setTurnsLeft(int turnsLeft) { this.turnsLeft = turnsLeft; }
    public void setMyTurn(boolean myTurn) { this.myTurn = myTurn; }
    public void setSchemeCardSet(boolean schemeCardSet) { this.schemeCardSet = schemeCardSet; }

    // getters
    public int getNumFavorTokens() {
        return numFavorTokens;
    }
    public PrivateObjectiveCard getPrivateObjectiveCard() {
        return privateObjectiveCard;
    }
    public ConnectionStatus getStatus() { return status; }
    public boolean isMyTurn() { return myTurn; }
    public boolean isSchemeCardSet() { return schemeCardSet; }

    /**
     * Sets the new player's scheme card
     * @param schemeCard is the new player's scheme card
     */
    @Override
    public void setSchemeCard(WindowPatternCard schemeCard) {
        this.schemeCard = schemeCard;
        this.setNumFavorTokens(schemeCard.getDifficulty());
    }
}
