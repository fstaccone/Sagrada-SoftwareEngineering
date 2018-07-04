package it.polimi.ingsw.model.gameobjects.effects;


import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.model.gameobjects.Square;

import java.util.ArrayList;

public class DifferentColorsInAColumnEffect implements Effect {

    public DifferentColorsInAColumnEffect() {
    }

    /**
     * Gives 5 points to the player for every column in his scheme card that is fully completed without dices with the
     * same color
     *
     * @param player is the player that uses this public objective card
     * @param match  is the player's current match
     * @return false
     */
    @Override
    public boolean applyEffect(Player player, Match match) {
        int temp = player.getPoints();
        ArrayList<Colors> colorsPerColumn = new ArrayList<>();
        Square[][] schema = player.getSchemeCard().getWindow();
        for (int j = 0; j < schema[0].length; j++) {
            int count = 0;
            colorsPerColumn.clear();
            for (int i = 0; i < schema.length; i++) {
                Colors color = null;
                if (schema[i][j].getDice() != null)
                    color = schema[i][j].getDice().getColor();
                if (color == null || colorsPerColumn.contains(color)) {
                    count = 0;
                    i = schema.length;
                } else {
                    count++;
                    colorsPerColumn.add(color);
                }
            }
            int pointsToBeAssigned = 5;
            if (count == schema.length)
                temp = temp + pointsToBeAssigned;
        }
        player.setPoints(temp);
        return false;
    }
}
