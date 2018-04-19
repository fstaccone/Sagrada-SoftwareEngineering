package it.polimi.ingsw.model.gameobjects;

public class Square {
    private Colors colorConstraint;
    private int valueConstraint;
    private Dice dice;

    //3 different constructors based on the square constraint (color, value, no constraints)
    public Square(Colors color){
        colorConstraint = color;
        valueConstraint = 0;
    }

    public Square(int value){
        colorConstraint = null;
        valueConstraint = value;
    }

    public Square(){
        colorConstraint = null;
        valueConstraint = 0;
    }
    //end of constructors

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

    public boolean occupiedSquare(){
        return getDice() != null;
    }

    public boolean satisfiedConstraints(Dice dice){
        if(getColorConstraint()!=null) return dice.getColor()==colorConstraint;
        if(getValueConstraint()!=0) return dice.getValue()==valueConstraint;
        return true;
    }

    public void putDice(Dice dice){
        if(!occupiedSquare() && satisfiedConstraints(dice))
            this.dice = dice;
        else {
            //Maybe 3 different Exception: OccupiedSquareException, ColorConstraintException and ValueConstraintException
            System.out.println("Error: the selected square is already occupied or constraints ignored");
        }
    }

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
}
