package it.polimi.ingsw.model.gameobjects;

public class WindowPatternCard {
    private String name;

    private int difficulty; //difficulty is a value between 3 and 6
    private Square[][] window ;

    //constructor gives window a name and creates a double array of squares without constraints (for now)
    //it should be modified to allow constraints in specific squares
    public WindowPatternCard(String name, int rows, int columns){
        this.name = name;
        window = new Square[rows][columns];
        for(int i=0; i < rows; i++){
            for(int j=0; i < columns; j++)
                window[i][j] = new Square();
        }
    }

    public int getDifficulty() {
        return difficulty;
    }

    public String getName() {
        return name;
    }

    public Square[][] getWindow() {
        return window;
    }

    public boolean checkPos(Dice d, int row, int column){
        //Declaration of the four adiacent dices to check
        Dice northern;
        Dice eastern;
        Dice southern;
        Dice western;
        //If we are putting a dice in the upper border we don't have a northern square to check
        if(row==0) {
            northern = null;
        }
        else {
            northern = window[row-1][column].getDice();
        }
        //If we are putting a dice in the right border we don't have an eastern square to check
        if(column==window[row].length-1){
            eastern = null;
        }
        else {
            eastern = window[row][column + 1].getDice();
        }
        //If we are putting a dice in the bottom border we don't have an southern square to check
        if(row==window.length-1){
            southern = null;
        }
        else {
            southern = window[row + 1][column].getDice();
        }
        //If we are putting a dice in the left border we don't have an western square to check
        if(column==0){
            western = null;
        }
        else {
            western = window[row][column - 1].getDice();
        }

        //check on eventual dice in northern square with same color or value
        if(northern !=null && (northern.getColor()==d.getColor() || northern.getValue()==d.getValue() ))
            return false;
        //check on eventual dice in eastern square with same color or value
        if(eastern != null && (eastern.getColor()==d.getColor() || eastern.getValue()==d.getValue() ))
            return false;
        //check on eventual dice in southern square with same color or value
        if(southern != null && (southern.getColor()==d.getColor() || southern.getValue()==d.getValue() ))
            return false;
        //check on eventual dice in western square with same color or value
        if(western != null && (western.getColor()==d.getColor() || western.getValue()==d.getValue() ))
            return false;
        return true;
    }
    //putDice calls checkPos, then putDice calls Square methods to check empty place and satisfied constrictions
    public void putDice(Dice d, int row, int column){
        if(checkPos(d, row, column))
            window[row][column].putDice(d);
    }

    public Dice removeDice(int row, int column){
        return window[row][column].removeDice();
    }

}
