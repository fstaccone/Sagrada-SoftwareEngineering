package it.polimi.ingsw.model.gameobjects;

import java.util.ArrayList;

public class PrivateObjectiveCard extends ObjectiveCard{
    private Colors color;

    public PrivateObjectiveCard(Colors color) {
        super("PrivateObjectiveCard" + color.toString());
        this.color = color;
    }

    @Override
    public int calculatePoints(WindowPatternCard card) {
        int score = 0;
        Square[][] window = card.getWindow();
        //calculation algorithm
        for(Square[] row : window){
            for(Square spot : row){
                Colors diceColor = Colors.NONE;
                if(spot.getDice()!=null)
                    diceColor = spot.getDice().getColor();
                if(diceColor==color)
                    score = score + spot.getDice().getValue();
            }
        }

        return score;
    }

    @Override
    public String toString() {
        return "PrivateObjectiveCard{" +
                "color=" + color +
                '}';
    }
}
