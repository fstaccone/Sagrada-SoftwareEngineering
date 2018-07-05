package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.model.gameobjects.Square;

public class DifferentShadesEffect implements Effect {

    public DifferentShadesEffect() {
    }

    /**
     * Gives 5 points to the player for every set of dices with all the 5 different values in his scheme card
     *
     * @param caller is the player that uses this public objective card
     * @param match  is the player's current match
     * @return
     */
    @Override
    public boolean applyEffect(Player caller, Match match) {
        int pointsToBeAssigned = 5;
        Square[][] schema = caller.getSchemeCard().getWindow();
        int temp = caller.getPoints();
        int onesCounter = 0;
        int twosCounter = 0;
        int threesCounter = 0;
        int foursCounter = 0;
        int fivesCounter = 0;
        int sixCounter = 0;
        for (Square[] row : schema) {
            for (Square square : row) {
                int val = 0;
                if (square.getDice() != null)
                    val = square.getDice().getValue();
                switch (val) {
                    case 1:
                        onesCounter++;
                        break;
                    case 2:
                        twosCounter++;
                        break;
                    case 3:
                        threesCounter++;
                        break;
                    case 4:
                        foursCounter++;
                        break;
                    case 5:
                        fivesCounter++;
                        break;
                    case 6:
                        sixCounter++;
                        break;
                    default:
                        break;
                }
            }
        }
        int[] counters = {onesCounter, twosCounter, threesCounter, foursCounter, fivesCounter, sixCounter};
        int min = counters[0];
        for (int counter : counters) {
            if (counter <= min)
                min = counter;
        }
        temp = temp + min * pointsToBeAssigned;
        caller.setPoints(temp);
        return false;
    }
}
