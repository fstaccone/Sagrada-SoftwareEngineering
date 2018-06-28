package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.model.gameobjects.Square;

public class MediumShadesEffect implements Effect {
    private final int pointsToBeAssigned = 2;

    public MediumShadesEffect() {
    }

    /**
     * Gives 2 points to the player for every set of dices with value 3 and 4 in his scheme card
     *
     * @param player is the player that uses this public objective card
     * @param match  is the player's current match
     * @return
     */
    @Override
    public boolean applyEffect(Player player, Match match) {
        Square[][] schema = player.getSchemeCard().getWindow();
        int temp = player.getPoints();
        int threesCounter = 0;
        int foursCounter = 0;
        for (Square[] row : schema) {
            for (Square square : row) {
                int val = 0;
                if (square.getDice() != null)
                    val = square.getDice().getValue();
                if (val == 3)
                    threesCounter++;
                if (val == 4)
                    foursCounter++;
            }
        }
        if (threesCounter <= foursCounter)
            temp = temp + threesCounter * pointsToBeAssigned;
        else temp = temp + foursCounter * pointsToBeAssigned;
        player.setPoints(temp);
        return false;
    }
}
