package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.Match;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.model.gameobjects.Square;

public class MediumShadesEffect implements Effect {
    private final int pointsToBeAssigned=2;
    public MediumShadesEffect() {
    }

    @Override
    public void applyEffect(Player player, Match match) {
        Square[][] schema = player.getSchemeCard().getWindow();
        int temp = player.getPoints();
        int threesCounter = 0;
        int foursCounter = 0;
        for(Square[] row : schema){
            for(Square square : row){
                int val = square.getDice().getValue();
                if(val==3)
                    threesCounter++;
                if(val==4)
                    foursCounter++;
            }
        }
        if(threesCounter<=foursCounter)
            temp = temp + threesCounter*pointsToBeAssigned;
        else temp = temp + foursCounter*pointsToBeAssigned;
        player.setPoints(temp);
    }
}
