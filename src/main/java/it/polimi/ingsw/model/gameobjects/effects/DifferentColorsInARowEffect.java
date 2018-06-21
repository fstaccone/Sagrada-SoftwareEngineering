package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gameobjects.*;

import java.util.ArrayList;

public class DifferentColorsInARowEffect implements Effect{
    private final int pointsToBeAssigned=6;
    public DifferentColorsInARowEffect() {
    }

    /**
     * Gives 6 points to the player for every row in his scheme card that is fully completed without dices with the same
     * color
     * @param player is the player that uses this public objective card
     * @param match is the player's current match
     * @return
     */
    @Override
    public boolean applyEffect(Player player, Match match) {
        int temp = player.getPoints();
        ArrayList<Colors> colorsPerRow = new ArrayList<>();
        Square[][] schema = player.getSchemeCard().getWindow();
        for(int i=0; i<schema.length; i++){
            int count = 0;
            colorsPerRow.clear();
            for(int j=0; j<schema[i].length; j++){
                Colors color = null;
                if(schema[i][j].getDice()!=null)
                        color = schema[i][j].getDice().getColor();
                if(color==null || colorsPerRow.contains(color)){
                    j=schema[i].length;
                    count = 0;
                }
                else{
                    count++;
                    colorsPerRow.add(color);
                }
            }
            if(count==schema[i].length)
                temp = temp + pointsToBeAssigned;
        }
        player.setPoints(temp);
        return false;
    }
}
