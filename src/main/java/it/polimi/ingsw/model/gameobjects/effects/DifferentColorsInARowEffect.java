package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.*;

import java.util.ArrayList;

public class DifferentColorsInARowEffect implements Effect{
    private final int pointsToBeAssigned=6;
    public DifferentColorsInARowEffect() {
    }

    @Override
    public void applyEffect(Player player, Match match) {
        int temp = player.getPoints();
        ArrayList<Colors> colorsPerRow = new ArrayList<>();
        Square[][] schema = player.getSchemeCard().getWindow();
        for(int i=0; i<schema.length; i++){
            int count = 0;
            colorsPerRow.clear();
            for(int j=0; j<schema[i].length; j++){
                Colors color = schema[i][j].getDice().getColor();
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
    }
}
