package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.*;

public class ColoredDiagonalsEffect implements Effect {
    private final int pointsToBeAssigned=1;//ASTERISCO
    public ColoredDiagonalsEffect() {
    }

    @Override
    public void applyEffect(Player player, Match match) {
        Square[][] schema = player.getSchemeCard().getWindow();
        int score = player.getPoints();
        for(Colors color : Colors.values()){
            if(color!=Colors.NONE){
                for(int i = 0; i< schema.length; i++){
                    for(int j = 0; j< schema[i].length; j++){
                        int temp = 0;
                        Colors diceColor = Colors.NONE;
                        if(schema[i][j].getDice()!=null)
                            diceColor = schema[i][j].getDice().getColor();
                        if(diceColor == color){
                            schema[i][j].removeDice();
                            temp = lookForColor(schema, color, i, j, temp);
                        }
                        if(temp>0) temp++;
                        score = score + temp;
                    }
                }

            }
        }
        player.setPoints(score);
    }

    public int lookForColor(Square[][] schema, Colors color, int row, int column, int score){
        Dice upLeft = null;
        Dice upRight = null;
        Dice bottomLeft = null;
        Dice bottomRight = null;
        if(row-1>=0 && column-1>=0) {
            upLeft = schema[row - 1][column - 1].getDice();
            if (upLeft != null && upLeft.getColor() == color) {
                schema[row - 1][column - 1].removeDice();
                score = lookForColor(schema, color, row - 1, column - 1, score + 1);
            }
        }
        if(row-1>=0 && column+1<schema[row].length) {
            upRight = schema[row - 1][column + 1].getDice();
            if (upRight != null && upRight.getColor() == color) {
                schema[row - 1][column + 1].removeDice();
                score = lookForColor(schema, color, row - 1, column + 1, score + 1);
            }
        }
        if(row+1<schema.length && column-1>=0) {
            bottomLeft = schema[row + 1][column - 1].getDice();
            if (bottomLeft != null && bottomLeft.getColor() == color) {
                schema[row + 1][column - 1].removeDice();
                score = lookForColor(schema, color, row + 1, column - 1, score + 1);
            }
        }
        if(row+1<schema.length && column+1<schema[row].length) {
            bottomRight = schema[row + 1][column + 1].getDice();
            if (bottomRight != null && bottomRight.getColor() == color) {
                schema[row + 1][column + 1].removeDice();
                score = lookForColor(schema, color, row + 1, column + 1, score + 1);
            }
        }
        if(upLeft!= null && upLeft.getColor()==color){
            schema[row-1][column-1].removeDice();
            score = lookForColor(schema, color, row-1, column-1,score + 1);
        }

        return score;
    }
}
