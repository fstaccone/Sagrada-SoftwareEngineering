package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gameobjects.*;

public class ColoredDiagonalsEffect implements Effect {
    private final int pointsToBeAssigned=1;//ASTERISCO
    public ColoredDiagonalsEffect() {
    }

    /**
     * Gives one point to the player for every dice diagonally adjacent to another of the same color in his scheme card.
     * @param player is the player using the private objective card
     * @param match is the player's current match
     * @return
     */
    @Override
    public boolean applyEffect(Player player, Match match) {

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
                            schema[i][j].setChecked(true);
                            temp = lookForColor(schema, color, i, j, temp);
                        }
                        if(temp>0) temp++;
                        score = score + temp;
                    }
                }

            }
        }
        player.setPoints(score);
        return false;
    }

    /**
     * Checks if there's a diagonally adjacent dice of the chosen dice that also has the same color
     * @param schema is the current scheme card
     * @param color is the color of the chosen dice
     * @param row is the row index of the chosen dice position
     * @param column is the column index of the chosen dice position
     * @param score is the player's current score
     * @return the updated score
     */
    public int lookForColor(Square[][] schema, Colors color, int row, int column, int score){
        Square upLeft = null;
        Square upRight = null;
        Square bottomLeft = null;
        Square bottomRight = null;

        if(row-1>=0 && column-1>=0) {
            upLeft = schema[row - 1][column - 1];
            if (!upLeft.isChecked() && upLeft.getDice() != null && upLeft.getDice().getColor() == color) {
                schema[row - 1][column - 1].setChecked(true);
                score = lookForColor(schema, color, row - 1, column - 1, score + 1);
            }
        }
        if(row-1>=0 && column+1<schema[row].length) {
            upRight = schema[row - 1][column + 1];
            if (!upRight.isChecked() && upRight.getDice() != null && upRight.getDice().getColor() == color) {
                schema[row - 1][column + 1].setChecked(true);
                score = lookForColor(schema, color, row - 1, column + 1, score + 1);
            }
        }
        if(row+1<schema.length && column-1>=0) {
            bottomLeft = schema[row + 1][column - 1];
            if (!bottomLeft.isChecked() && bottomLeft.getDice() != null && bottomLeft.getDice().getColor() == color) {
                schema[row + 1][column - 1].setChecked(true);
                score = lookForColor(schema, color, row + 1, column - 1, score + 1);
            }
        }
        if(row+1<schema.length && column+1<schema[row].length) {
            bottomRight = schema[row + 1][column + 1];
            if (!bottomRight.isChecked() && bottomRight.getDice() != null && bottomRight.getDice().getColor() == color) {
                schema[row + 1][column + 1].setChecked(true);
                score = lookForColor(schema, color, row + 1, column + 1, score + 1);
            }
        }
        if(upLeft!=null && !upLeft.isChecked() && upLeft.getDice()!= null && upLeft.getDice().getColor()==color){
            schema[row-1][column-1].setChecked(true);
            score = lookForColor(schema, color, row-1, column-1,score + 1);
        }

        return score;
    }
}
