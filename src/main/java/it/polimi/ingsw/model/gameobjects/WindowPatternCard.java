package it.polimi.ingsw.model.gameobjects;

import java.util.Arrays;

public class WindowPatternCard {

    private String name;
    private int difficulty; //difficulty is a value between 3 and 6
    private Square[][] window;
    private int rows;
    private int columns;
    private boolean empty;

    //constructor gives window a name and creates a double array of squares without constraints (for now)
    //it should be modified to allow constraints in specific squares

    /**
     * The constructor gives the new window a name and creates a double array of squares
     * @param name is the new window name
     * @param rows is the number of rows
     * @param columns is the number of columns
     */
    public WindowPatternCard(String name, int rows, int columns) {
        this.name = name;
        this.empty = true;
        this.rows = rows;
        this.columns = columns;
        window = new Square[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++)
                window[i][j] = new Square();
        }
    }

    public int getRows() {
        return rows;
    }
    public int getColumns() { return columns;}
    public int getDifficulty() {
        return difficulty;
    }
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
    private boolean isEmpty() { return empty; }
    public String getName() {
        return name;
    }
    public Square[][] getWindow() {
        return window;
    }

    private void setEmpty(boolean empty) { this.empty = empty; }

    //method created by copying part of checkPos but it's useful to have both

    /**
     * Checks if there's any dice adjacent to a specific position
     * @param row of the position we want to check
     * @param column of the position we want to check
     * @return true if there's any dice adjacent to the specific position, false otherwise
     */
    public boolean existsAdjacentDice(int row, int column){
        Dice northern;
        Dice eastern;
        Dice southern;
        Dice western;
        Dice northEastern = null;
        Dice southEastern = null;
        Dice southWestern = null;
        Dice northWestern = null;
        //If we are putting a dice in the upper border we don't have a northern square to check
        if (row == 0) {
            northern = null;
        } else {
            northern = window[row - 1][column].getDice();
            if (column < window[row].length - 1)
                northEastern = window[row - 1][column + 1].getDice();
            if (column > 0)
                northWestern = window[row - 1][column - 1].getDice();
        }
        //If we are putting a dice in the right border we don't have an eastern square to check
        if (column == window[row].length - 1) {
            eastern = null;
            northEastern = null;
        } else {
            eastern = window[row][column + 1].getDice();
            if(row<window.length-1)
                southEastern = window[row+1][column+1].getDice();
        }
        //If we are putting a dice in the bottom border we don't have an southern square to check
        if (row == window.length - 1) {
            southern = null;
            southEastern = null;
        } else {
            southern = window[row + 1][column].getDice();
            if(column > 0)
                southWestern = window[row+1][column-1].getDice();
        }
        //If we are putting a dice in the left border we don't have an western square to check
        if (column == 0) {
            western = null;
        } else {
            western = window[row][column - 1].getDice();
        }
        return (northern!=null || eastern!=null || southern!=null || western!=null || northEastern!=null || southEastern!=null || southWestern!=null || northWestern!=null);
    }

    /**
     * method used by checkPos
     * @param adjacentDices is an array of dices we want to check
     * @return true if at least one of the dices in adjacentDices is not null, false otherwise
     */
    public boolean existsAdjacentDice(Dice[] adjacentDices){
        for(Dice dice : adjacentDices){
            if(dice!=null) return true;
        }
        return false;
    }

    /**
     * This method checks if there's at least one adjacent dice to the position where we want to place a new dice,
     * it also checks that the ortogonally adjacent dices have a different color and value compared to the new dice
     * @param d is the new dice we want to place
     * @param row is the row of the position of the new dice
     * @param column is the column of the position of the new dice
     * @return true if there's at least one adjacent dice to the chosen position and it has a different color and value
     * compared to the dice d, false otherwise
     */
    public boolean checkPos(Dice d, int row, int column) {
        //Declaration of the four adjacent dices to check
        //We also have to check that we have at least one adjacent dice
        Dice northern;
        Dice eastern;
        Dice southern;
        Dice western;
        Dice northEastern = null;
        Dice southEastern = null;
        Dice southWestern = null;
        Dice northWestern = null;
        //If we are putting a dice in the upper border we don't have a northern square to check
        if (row == 0) {
            northern = null;
            northEastern = null;
            northWestern = null;
        } else {
            northern = window[row - 1][column].getDice();
            if (column < window[row].length - 1)
                northEastern = window[row - 1][column + 1].getDice();
            if (column > 0)
                northWestern = window[row - 1][column - 1].getDice();
        }
        //If we are putting a dice in the right border we don't have an eastern square to check
        if (column == window[row].length - 1) {
            eastern = null;
            northEastern = null;
            southEastern = null;
        } else {
            eastern = window[row][column + 1].getDice();
            if(row<window.length-1)
                southEastern = window[row+1][column+1].getDice();
        }
        //If we are putting a dice in the bottom border we don't have an southern square to check
        if (row == window.length - 1) {
            southern = null;
            southEastern = null;
            southWestern = null;
        } else {
            southern = window[row + 1][column].getDice();
            if(column > 0)
                southWestern = window[row+1][column-1].getDice();
        }
        //If we are putting a dice in the left border we don't have an western square to check
        if (column == 0) {
            western = null;
            northWestern = null;
            southWestern = null;
        } else {
            western = window[row][column - 1].getDice();
        }
        Dice[] adjacentDices = {northern, eastern, southern, western, northEastern, southEastern, southWestern, northWestern};
        //check if we are putting the dice next to at least another one
        if (existsAdjacentDice(adjacentDices)) {
            //check on eventual dice in northern square with same color or value
            if (northern != null && (northern.getColor() == d.getColor() || northern.getValue() == d.getValue()))
                return false;
            //check on eventual dice in eastern square with same color or value
            if (eastern != null && (eastern.getColor() == d.getColor() || eastern.getValue() == d.getValue()))
                return false;
            //check on eventual dice in southern square with same color or value
            if (southern != null && (southern.getColor() == d.getColor() || southern.getValue() == d.getValue()))
                return false;
            //check on eventual dice in western square with same color or value
            if (western != null && (western.getColor() == d.getColor() || western.getValue() == d.getValue()))
                return false;
            return true;
        }
        return false;
    }

    //putDice calls checkPos, then putDice calls Square methods to check empty place and satisfied constrictions

    /**
     * If all the conditions are satisfied, it places the dice d in the chosen position
     * @param d the dice to place
     * @param row the row of the chosen position
     * @param column the column of the chosen position
     * @return true if the dice is placed correctly, false otherwise
     */
    public boolean putDice(Dice d, int row, int column) {
        if(isEmpty()){
            boolean result = putFirstDice(d, row, column);
            setEmpty(!result);
            return result;
        }else{
            if (checkPos(d, row, column)) {
                return window[row][column].putDice(d);
            }
        }
        return false;
    }

    /**
     * Used when after a wrong usage of a toolcard the player has to put the dice back to its original position
     * @param d the dice to put back
     * @param row the row index of the original position of the dice
     * @param column the column index of the original position of the dice
     */
    public void putDiceBack(Dice d, int row, int column) {
            window[row][column].putDiceIgnoringAllConstraints(d);
    }

    /**
     * places a new dice d without checking the eventual value constraint of the chosen position
     * @param d is the new dice to place
     * @param row is the row of the chosen position
     * @param column is the column of the chosen position
     */
    public void putDiceIgnoringValueConstraint(Dice d, int row, int column){
        if(checkPos(d, row, column)) {
            System.out.println("ORA LO PIAZZO");
            window[row][column].putDiceIgnoringValueConstraint(d);
        }
    }

    /**
     * places a new dice d without checking the eventual color constraint of the chosen position
     * @param d is the new dice to place
     * @param row is the row of the chosen position
     * @param column is the column of the chosen position
     */
    public void putDiceIgnoringColorConstraint(Dice d, int row, int column){
        if(checkPos(d, row, column)) {
            System.out.println("ORA LO PIAZZO");
            window[row][column].putDiceIgnoringColorConstraint(d);
        }
    }
    /**
     * places a new dice d without checking any constraints of the chosen position
     * @param d is the new dice to place
     * @param row is the row of the chosen position
     * @param column is the column of the chosen position
     */
    /*public void putDiceIgnoringAllConstraints(Dice d, int row, int column){
        if(checkPos(d, row, column))
            window[row][column]. putDiceIgnoringAllConstraints(d);
    }*/

    /**
     * Checks if a column is completed (has a dice placed in every square)
     * @param z is the index of the column we want to check
     * @return true if there's a dice in every square of the column, false otherwise
     */
   /* public boolean fullColumn(int z){
            boolean res = false;
            boolean ris = true;
            for(int i=0;i<4;i++){
                if (this.getWindow()[i][z].getDice()==null){
                        res = false;
                        ris = false;
                    }
                    else
                        res = true;
                }
            return ris;
        }*/

    /**
     * Places the first dice in the scheme card, it has to be placed on a slot in the borders of the scheme card,
     * it has to satisfy the square color or value constraints
     * @param d is the new dice to place
     * @param row is the index of the row of the chosen position
     * @param column is the index of the column of the chosen position
     * @return true if the dice is placed correctly, false otherwise.
     */
    public boolean putFirstDice(Dice d, int row, int column){
        if(row==0 || column==0 || column == window[row].length - 1 || row == window.length - 1) {
            return window[row][column].putDice(d);

        }
        return false;
    }

    /**
     * Places a dice without checking if it would be adjacent to an existing one or to one with the same color or value
     * @param d is the new dice to place
     * @param row is the index of the row of the chosen position
     * @param column is the index of the column of the chosen position
     */
    public void putDiceWithoutCheckPos(Dice d, int row, int column){
        if(isEmpty()){
            window[row][column].putDice(d);
            empty = false;
        }
        else {
            window[row][column].putDice(d);
        }
    }

    /**
     * Removes a dice from a chosen position
     * @param row is the index of the row of the chosen position
     * @param column is the index of the column of the chosen position
     * @return the removed dice if present, null if the square in the chosen position is empty
     */
    public Dice removeDice(int row, int column) {
        return window[row][column].removeDice();
    }

    /**
     * Returns the dice from the chosen position, without removing it
     * @param row is the index of the row of the chosen position
     * @param column is the index of the column of the chosen position
     * @return the dice from the chosen position, if present, null if the square in the chosen position is empty
     */
    public Dice getDice(int row, int column) {
        return window[row][column].getDice();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String newline = "\n";

        result.append(newline);
        result.append("Nome: ");
        result.append(this.name);
        result.append(newline);
        result.append("Difficoltà: ");
        result.append(this.difficulty);
        result.append(newline);
        result.append("--------------------------");
        result.append(newline);

        for (int i = 0; i < window.length; i++) {
            result.append(Arrays.toString(window[i]));
            result.append(newline);
        }
        result.append("--------------------------");

        return result.toString();
    }


    // it returns the number of free cells (to be used for points' calculation)

    /**
     * it returns the number of empty squares in the scheme card(to be used for points' calculation)
     * @return the number of empty squares in the scheme card
     */
    public int countFreeSquares(){
       return (int) Arrays.stream(window)
                .flatMap(Arrays::stream)
                .filter(s -> s.getDice() == null)
                .count();
    }
}

