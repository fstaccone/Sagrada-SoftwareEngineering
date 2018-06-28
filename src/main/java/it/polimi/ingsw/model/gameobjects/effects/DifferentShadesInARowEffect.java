package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.model.gameobjects.Square;

import java.util.ArrayList;

public class DifferentShadesInARowEffect implements Effect {
    private final int pointsToBeAssigned = 5;

    public DifferentShadesInARowEffect() {
    }

    /**
     * Gives 5 points to the player for every row in his scheme card that is fully completed without dices with the same
     * value
     *
     * @param player is the player that uses this public objective card
     * @param match  is the player's current match
     * @return
     */
    @Override
    public boolean applyEffect(Player player, Match match) {
        int temp = player.getPoints();
        ArrayList<Integer> valuesPerRow = new ArrayList<>();
        Square[][] schema = player.getSchemeCard().getWindow();
        for (int i = 0; i < schema.length; i++) {
            int count = 0;
            valuesPerRow.clear();
            for (int j = 0; j < schema[i].length; j++) {
                Integer value = 0;
                if (schema[i][j].getDice() != null)
                    value = schema[i][j].getDice().getValue();
                if (value == 0 || valuesPerRow.contains(value)) {
                    j = schema[i].length;
                    count = 0;
                } else {
                    count++;
                    valuesPerRow.add(value);
                }
            }
            if (count == schema[i].length)
                temp = temp + pointsToBeAssigned;
        }
        player.setPoints(temp);
        return false;
    }
}
