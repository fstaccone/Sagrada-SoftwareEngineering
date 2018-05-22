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

    public boolean satisfiedColorConstraint(Dice dice) {
        return getColorConstraint() == null || dice.getColor() == colorConstraint;
    }

    public boolean satisfiedValueConstraint(Dice dice){
        return getValueConstraint() == 0 || dice.getValue() == valueConstraint;
    }


    public void putDice(Dice dice){
        if(!occupiedSquare() && satisfiedConstraints(dice)) {
            System.out.println("prima put in square " + dice.toString());
            this.dice = dice;
            System.out.println("Dopo put in square " + this.dice.toString());
        }
        else {
            //Maybe 3 different Exception: OccupiedSquareException, ColorConstraintException and ValueConstraintException
            System.out.println("Error: the selected square is already occupied or constraints ignored");
        }
    }

    public void putDiceIgnoringColorConstraint(Dice dice){
        if(!occupiedSquare() && satisfiedValueConstraint(dice))
            this.dice = dice;
        else System.out.println("You have to respect value constraints.");
    }

    public void putDiceIgnoringValueConstraint(Dice dice){
        if(!occupiedSquare() && satisfiedColorConstraint(dice))
            this.dice = dice;
        else System.out.println("You have to respect color constraints.");
    }

    public void putDiceIgnoringAllConstraints(Dice dice){
        if(!occupiedSquare())
            this.dice = dice;
        else System.out.println("This square is already occupied");
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
