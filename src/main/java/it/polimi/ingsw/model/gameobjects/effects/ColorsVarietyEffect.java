package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.model.gameobjects.Match;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.model.gameobjects.Square;

public class ColorsVarietyEffect implements Effect {
    private final int pointsToBeAssigned=4;
    public ColorsVarietyEffect() {

    }

    @Override
    public void applyEffect(Player caller, Match match) {
        Square[][] schema = caller.getSchemeCard().getWindow();
        int temp = caller.getPoints();
        int yellowCounter = 0;
        int redCounter = 0;
        int blueCounter = 0;
        int violetCounter = 0;
        int greenCounter = 0;
        for(Square[] row : schema){
            for(Square square : row){
                Colors color = Colors.NONE;
                if(square.getDice()!=null)
                    color = square.getDice().getColor();
                switch (color) {
                    case BLUE:
                        blueCounter++;
                        break;
                    case RED:
                        redCounter++;
                        break;
                    case GREEN:
                        greenCounter++;
                        break;
                    case VIOLET:
                        violetCounter++;
                        break;
                    case YELLOW:
                        yellowCounter++;
                        break;
                    default:
                        break;
                }
            }
        }
        int[] counters ={blueCounter, redCounter, greenCounter, violetCounter, yellowCounter};
        int min = counters[0];
        for(int counter : counters) {
            if (counter <= min)
                min = counter;
        }
        temp = temp + min*pointsToBeAssigned;
        caller.setPoints(temp);
    }
}
