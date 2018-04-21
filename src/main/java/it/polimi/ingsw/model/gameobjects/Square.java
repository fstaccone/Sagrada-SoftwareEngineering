package it.polimi.ingsw.model.gameobjects;

public class Square {
    private Colors colorConstraint;
    private int valueConstraint;
    private Dice dice;

    public Square(){
        colorConstraint = null;
        valueConstraint = 0;
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
