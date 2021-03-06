package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.model.gameobjects.Square;

import java.util.ArrayList;

public class DifferentShadesInAColumnEffect implements Effect {

    public DifferentShadesInAColumnEffect() {
    }

    /**
     * Gives 4 points to the player for every column in his scheme card which is fully completed without dices with the
     * same value
     *
     * @param player is the player that uses this public objective card
     * @param match  is the player's current match
     * @return false as a default value
     */
    @Override
    public boolean applyEffect(Player player, Match match) {
        int pointsToBeAssigned = 4;
        int temp = player.getPoints();
        ArrayList<Integer> valuesPerColumn = new ArrayList<>();
        Square[][] schema = player.getSchemeCard().getWindow();
        for (int j = 0; j < schema[0].length; j++) {
            int count = 0;
            valuesPerColumn.clear();
            for (int i = 0; i < schema.length; i++) {
                Integer value = 0;
                if (schema[i][j].getDice() != null)
                    value = schema[i][j].getDice().getValue();
                if (value == 0 || valuesPerColumn.contains(value)) {
                    count = 0;
                    i = schema.length;
                } else {
                    count++;
                    valuesPerColumn.add(value);
                }
            }
            if (count == schema.length)
                temp = temp + pointsToBeAssigned;
        }
        player.setPoints(temp);
        return false;
    }
}
