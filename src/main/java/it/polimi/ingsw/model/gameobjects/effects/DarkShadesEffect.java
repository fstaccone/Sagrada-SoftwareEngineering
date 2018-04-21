package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.Match;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.model.gameobjects.Square;

public class DarkShadesEffect implements Effect {
    private final int pointsToBeAssigned=2;
    public DarkShadesEffect() {
    }

    @Override
    public void applyEffect(Player player, Match match) {
        Square[][] schema = player.getSchemeCard().getWindow();
        int temp = player.getPoints();
        int fivesCounter = 0;
        int sixCounter = 0;
        for(Square[] row : schema){
            for(Square square : row){
                int val = square.getDice().getValue();
                if(val==5)
                    fivesCounter++;
                if(val==6)
                    sixCounter++;
            }
        }
        if(fivesCounter<=sixCounter)
            temp = temp + fivesCounter*pointsToBeAssigned;
        else temp = temp + sixCounter*pointsToBeAssigned;
        player.setPoints(temp);
    }
}
