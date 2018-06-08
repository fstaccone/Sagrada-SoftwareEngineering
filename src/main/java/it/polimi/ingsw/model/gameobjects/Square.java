package it.polimi.ingsw.model.gameobjects;

import java.io.Serializable;

public class Square implements Serializable{
    private Colors colorConstraint;
    private int valueConstraint;
    private Dice dice;
    private boolean checked;
    /**
     * Creates a square with no constraints
     */
    public Square(){
        colorConstraint = null;
        valueConstraint = 0;
        checked = false;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    //getters
    public Colors getColorConstraint() {
        return colorConstraint;
    }

    public int getValueConstraint() {
        return valueConstraint;
    }

    public Dice getDice() {
        return dice;
    }
    //end of getters

    /**
     *
     * @return true if there's a dice placed in the square, false if the square is empty
     */
    public boolean occupiedSquare(){
        return getDice() != null;
    }

    /**
     * Checks if a dice satisfies color or value constraints of the square (it can't have both)
     * @param dice is the dice to check
     * @return true if the constraints are satisfied (or if there are no constraints), false if they aren't
     */
    public boolean satisfiedConstraints(Dice dice){
        if(getColorConstraint()!=null) return dice.getColor()==colorConstraint;
        if(getValueConstraint()!=0) return dice.getValue()==valueConstraint;
        return true;
    }

    /**
     * Checks if a dice satisfies the color constraint of the square
     * @param dice is the dice to check
     * @return true if the color constraint is satisfied (or not present), false otherwise.
     */
    public boolean satisfiedColorConstraint(Dice dice) {
        return getColorConstraint() == null || dice.getColor() == colorConstraint;
    }

    /**
     * Checks if a dice satisfies the value constraint of the square
     * @param dice is the dice to check
     * @return true if the value constraint is satisfied (or not present), false otherwise.
     */
    public boolean satisfiedValueConstraint(Dice dice){
        return getValueConstraint() == 0 || dice.getValue() == valueConstraint;
    }

    /**
     * Places a dice in the square if it's empty and the dice satisfies all the constraints
     * @param dice is the dice to place
     * @return true if the dice is placed correctly, false otherwise.
     */
    public boolean putDice(Dice dice){
        if(!occupiedSquare() && satisfiedConstraints(dice)) {
            this.dice = dice;
            return true;
        }
        else {
            // todo: definire le eccezioni (magari con una enum)
            //Maybe 3 different Exception: OccupiedSquareException, ColorConstraintException and ValueConstraintException
            System.out.println("Error: the selected square is already occupied or constraints ignored");
            return false;
        }
    }

    /**
     * Places a dice in the square ignoring color constraint
     * @param dice is the dice to place
     */
    public void putDiceIgnoringColorConstraint(Dice dice){
        if(!occupiedSquare() && satisfiedValueConstraint(dice))
            this.dice = dice;
        else System.out.println("You have to respect value constraints.");
    }

    /**
     * Places a dice in the square ignoring value constraint
     * @param dice is the dice to place
     */
    public void putDiceIgnoringValueConstraint(Dice dice){
        if(!occupiedSquare() && satisfiedColorConstraint(dice))
            this.dice = dice;
        else System.out.println("You have to respect color constraints.");
    }

    /**
     * Places a dice in the square ignoring both value and color constraints
     * @param dice is the dice to place
     */
    public void putDiceIgnoringAllConstraints(Dice dice){
        if(!occupiedSquare())
            this.dice = dice;
        else System.out.println("This square is already occupied");
    }

    /**
     * Removes the dice placed in the square, if there's one.
     * @return true if the dice is removed correctly, false if the square is already empty.
     */
    public Dice removeDice(){
        if(occupiedSquare()){
            Dice temp = this.dice;
            this.dice = null;
            return temp;
        }
        else{
            System.out.println("Error: the selected square is empty"); //ExceptionEmptySquare
            return null;
        }
    }

    // Constraints' setters
    public void setColorConstraint(Colors colorConstraint) {
        this.colorConstraint = colorConstraint;
    }
    public void setValueConstraint(int valueConstraint) {
        this.valueConstraint = valueConstraint;
    }

    @Override
    public String toString() {
        return "{" +
                "C=" + colorConstraint +
                ", V=" + valueConstraint +
                ", " + dice +
                "}\t";
    }
}
