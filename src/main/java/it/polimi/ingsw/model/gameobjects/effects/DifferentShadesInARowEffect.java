package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.model.gameobjects.Match;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.model.gameobjects.Square;

import java.util.ArrayList;

public class DifferentShadesInARowEffect implements Effect {
    private final int pointsToBeAssigned=5;
    public DifferentShadesInARowEffect() {
    }

    @Override
    public void applyEffect(Player player, Match match) {
        int temp = player.getPoints();
        ArrayList<Integer> valuesPerRow = new ArrayList<>();
        Square[][] schema = player.getSchemeCard().getWindow();
        for(int i=0; i<schema.length; i++){
            int count = 0;
            valuesPerRow.clear();
            for(int j=0; j<schema[i].length; j++){
                Integer value = 0;
                if(schema[i][j].getDice()!=null)
                    value = schema[i][j].getDice().getValue();
                if(value==0 || valuesPerRow.contains(value)){
                    j=schema[i].length;
                    count = 0;
                }
                else{
                    count++;
                    valuesPerRow.add(value);
                }
            }
            if(count==schema[i].length)
                temp = temp + pointsToBeAssigned;
        }
        player.setPoints(temp);
    }
}
