package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.model.gameobjects.Match;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.model.gameobjects.Square;

import java.util.ArrayList;

public class DifferentShadesInAColumnEffect implements Effect {
    private final int pointsToBeAssigned=4;
    public DifferentShadesInAColumnEffect() {
    }

    @Override
    public void applyEffect(Player player, Match match) {
        int temp = player.getPoints();
        ArrayList<Integer> valuesPerColumn = new ArrayList<>();
        Square[][] schema = player.getSchemeCard().getWindow();
        for(int j=0; j<schema[0].length; j++){
            int count = 0;
            valuesPerColumn.clear();
            for(int i=0; i<schema.length; i++){
                Integer value = 0;
                if(schema[i][j].getDice()!=null)
                    value = schema[i][j].getDice().getValue();
                if(value==0 || valuesPerColumn.contains(value)){
                    count = 0;
                    i = schema.length;
                }
                else {
                    count++;
                    valuesPerColumn.add(value);
                }
            }
            if(count == schema.length)
                temp = temp + pointsToBeAssigned;
        }
        player.setPoints(temp);
    }
}
