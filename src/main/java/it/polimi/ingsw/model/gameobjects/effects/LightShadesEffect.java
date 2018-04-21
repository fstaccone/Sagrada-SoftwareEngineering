package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.Match;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.model.gameobjects.Square;

public class LightShadesEffect implements Effect {
    private final int pointsToBeAssigned=2;
    public LightShadesEffect() {
    }

    @Override
    public void applyEffect(Player player, Match match) {
        Square[][] schema = player.getSchemeCard().getWindow();
        int temp = player.getPoints();
        int onesCounter = 0;
        int twosCounter = 0;
        for(Square[] row : schema){
            for(Square square : row){
                int val = square.getDice().getValue();
                if(val==1)
                    onesCounter++;
                if(val==2)
                    twosCounter++;
            }
        }
        if(onesCounter<=twosCounter)
            temp = temp + onesCounter*pointsToBeAssigned;
        else temp = temp + twosCounter*pointsToBeAssigned;
        player.setPoints(temp);
    }
}
