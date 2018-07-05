package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.model.gameobjects.PrivateObjectiveCard;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

public class PlayerMultiplayer extends Player {
    private int numFavorTokens;
    private PrivateObjectiveCard privateObjectiveCard;
    private ConnectionStatus status;
    private boolean myTurn;

    /**
     * Creates a new Player for multiplayer match
     *
     * @param name is the name of the player
     */
    public PlayerMultiplayer(String name) {
        super(name);
        status = ConnectionStatus.CONNECTED;
        myTurn = false;
    }

    // setter
    public void setNumFavorTokens(int numFavorTokens) {
        this.numFavorTokens = numFavorTokens;
    }

    public void setPrivateObjectiveCard(PrivateObjectiveCard privateObjectiveCard) {
        this.privateObjectiveCard = privateObjectiveCard;
    }

    void setStatus(ConnectionStatus status) {
        this.status = status;
    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

    // getters
    public int getNumFavorTokens() {
        return numFavorTokens;
    }

    public PrivateObjectiveCard getPrivateObjectiveCard() {
        return privateObjectiveCard;
    }

    public ConnectionStatus getStatus() {
        return status;
    }

    public boolean isMyTurn() {
        return myTurn;
    }

    /**
     * Sets the new player's scheme card
     *
     * @param schemeCard is the new player's scheme card
     */
    @Override
    public void setSchemeCard(WindowPatternCard schemeCard) {
        this.schemeCard = schemeCard;
        this.setNumFavorTokens(schemeCard.getDifficulty());
    }
}
