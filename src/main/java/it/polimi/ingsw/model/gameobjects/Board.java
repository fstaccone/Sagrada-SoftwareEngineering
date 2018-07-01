package it.polimi.ingsw.model.gameobjects;

import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gamelogic.Player;

import java.util.List;

public class Board {
    private List<PublicObjectiveCard> pickedPublicObjectiveCards;
    private Reserve reserve;
    private List<ToolCard> pickedToolCards;
    private RoundTrack roundTrack;

    /**
     * Creates the board where the gameobjects will be placed
     *
     * @param pickedToolCards            are the toolcards for the current match
     * @param pickedPublicObjectiveCards are the public objective cards for the current match
     */
    public Board(List<ToolCard> pickedToolCards, List<PublicObjectiveCard> pickedPublicObjectiveCards) {
        // To be managed later, even in Match
        this.pickedToolCards = pickedToolCards;
        this.pickedPublicObjectiveCards = pickedPublicObjectiveCards;

        this.reserve = new Reserve();
        this.roundTrack = new RoundTrack();
    }

    /**
     * allows a player to use a toolcard
     *
     * @param i      is tool card number
     * @param player is the player that wants to use the toolcard
     * @param match  is the current match
     * @return true is the toolcard is available and used by the player, false otherwise.
     */
    public boolean findAndUseToolCard(int i, Player player, Match match) {
        for (ToolCard card : pickedToolCards) {
            if (Integer.parseInt(card.getToolID().replaceAll("tool", "")) == i) {
                return card.useCard(player, match);
            }
        }
        return false;
    }

    public Reserve getReserve() {
        return reserve;
    }


    public List<ToolCard> getPickedToolCards() {
        return pickedToolCards;
    }

    public List<PublicObjectiveCard> getPickedPublicObjectiveCards() {
        return pickedPublicObjectiveCards;
    }

    public RoundTrack getRoundTrack() {
        return roundTrack;
    }

}

