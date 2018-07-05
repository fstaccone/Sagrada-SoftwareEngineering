package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.model.gameobjects.Square;

public class DarkShadesEffect implements Effect {

    public DarkShadesEffect() {
    }

    /**
     * Gives 2 points to the player for every set of dices with value 5 and 6 in his scheme card
     *
     * @param player is the player that uses this public objective card
     * @param match  is the player's current match
     * @return
     */
    @Override
    public boolean applyEffect(Player player, Match match) {
        int pointsToBeAssigned = 2;

        Square[][] schema = player.getSchemeCard().getWindow();
        int temp = player.getPoints();
        int fivesCounter = 0;
        int sixCounter = 0;
        for (Square[] row : schema) {
            for (Square square : row) {
                int val = 0;
                if (square.getDice() != null)
                    val = square.getDice().getValue();
                if (val == 5)
                    fivesCounter++;
                if (val == 6)
                    sixCounter++;
            }
        }
        if (fivesCounter <= sixCounter)
            temp = temp + fivesCounter * pointsToBeAssigned;
        else temp = temp + sixCounter * pointsToBeAssigned;
        player.setPoints(temp);
        return false;
    }
}
