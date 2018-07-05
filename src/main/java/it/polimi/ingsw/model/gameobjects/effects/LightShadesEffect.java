package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.model.gameobjects.Square;

public class LightShadesEffect implements Effect {

    public LightShadesEffect() {
    }

    /**
     * Gives 2 points to the player for every set of dices with value 1 and 2 in his scheme card
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
        int onesCounter = 0;
        int twosCounter = 0;
        for (Square[] row : schema) {
            for (Square square : row) {
                int val = 0;
                if (square.getDice() != null)
                    val = square.getDice().getValue();
                if (val == 1)
                    onesCounter++;
                if (val == 2)
                    twosCounter++;

            }
        }
        if (onesCounter <= twosCounter)
            temp = temp + onesCounter * pointsToBeAssigned;
        else temp = temp + twosCounter * pointsToBeAssigned;
        player.setPoints(temp);
        return false;
    }
}
