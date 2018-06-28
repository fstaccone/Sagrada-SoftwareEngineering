package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.model.gameobjects.PrivateObjectiveCard;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

import java.util.ArrayList;
import java.util.List;

public class PlayerSingleplayer extends Player {

    private List<PrivateObjectiveCard> privateObjectiveCards;
    /**
     * Creates a new Player for a single player match.
     *
     * @param name is the player's name
     */
    public PlayerSingleplayer(String name) {
        super(name);
        this.setColor(Colors.NONE);
        privateObjectiveCards = new ArrayList<>();
    }

    /**
     * Sets the player's scheme card
     *
     * @param schemeCard is the player's scheme card
     */
    @Override
    public void setSchemeCard(WindowPatternCard schemeCard) {
        this.schemeCard = schemeCard;
    }

    public List<PrivateObjectiveCard> getPrivateObjectiveCards() {
        return privateObjectiveCards;
    }

    public void setPrivateObjectiveCards(List<PrivateObjectiveCard> privateObjectiveCards) {
        this.privateObjectiveCards = privateObjectiveCards;
    }
}
