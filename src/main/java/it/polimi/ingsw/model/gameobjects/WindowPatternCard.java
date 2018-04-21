package it.polimi.ingsw.model.gameobjects;

import it.polimi.ingsw.model.gameobjects.windowpatterncards.LuzCelestial;

import java.util.Arrays;

public class WindowPatternCard {
    private String name;

    private int difficulty; //difficulty is a value between 3 and 6
    private Square[][] window;

    //constructor gives window a name and creates a double array of squares without constraints (for now)
    //it should be modified to allow constraints in specific squares
    public WindowPatternCard(String name, int rows, int columns) {
        this.name = name;
        window = new Square[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++)
                window[i][j] = new Square();
        }
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getName() {
        return name;
    }

    public Square[][] getWindow() {
        return window;
    }

    public boolean checkPos(Dice d, int row, int column) {
        //Declaration of the four adiacent dices to check
        Dice northern;
        Dice eastern;
        Dice southern;
        Dice western;
        //If we are putting a dice in the upper border we don't have a northern square to check
        if (row == 0) {
            northern = null;
        } else {
            northern = window[row - 1][column].getDice();
        }
        //If we are putting a dice in the right border we don't have an eastern square to check
        if (column == window[row].length - 1) {
            eastern = null;
        } else {
            eastern = window[row][column + 1].getDice();
        }
        //If we are putting a dice in the bottom border we don't have an southern square to check
        if (row == window.length - 1) {
            southern = null;
        } else {
            southern = window[row + 1][column].getDice();
        }
        //If we are putting a dice in the left border we don't have an western square to check
        if (column == 0) {
            western = null;
        } else {
            western = window[row][column - 1].getDice();
        }

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

    //putDice calls checkPos, then putDice calls Square methods to check empty place and satisfied constrictions
    public void putDice(Dice d, int row, int column) {
        if (checkPos(d, row, column))
            window[row][column].putDice(d);
    }

    public void putDiceIgnoringValueConstraint(Dice d, int row, int column){
        if(checkPos(d, row, column))
            window[row][column].putDiceIgnoringValueConstraint(d);
    }

    public void putDiceIgnoringColorConstraint(Dice d, int row, int column){
        if(checkPos(d, row, column))
            window[row][column].putDiceIgnoringColorConstraint(d);
    }

    public Dice removeDice(int row, int column) {
        return window[row][column].removeDice();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String newline = "\n";

        result.append(newline);
        result.append("Name: " + this.name);
        result.append(newline);
        result.append("Difficulty: " + this.difficulty);
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
/*
    // Test for toString correctness and dice's placing
    public static void main(String[] args){
        WindowPatternCard window = new LuzCelestial();

        Player player = new PlayerMultiplayer("francesca");
        Dice dice = new Dice(Colors.GREEN);
        dice.setValue(4);

        player.setPickedDice(dice);

        System.out.print(window);

        player.setSchemeCard(window);

        player.getSchemeCard().putDice(player.getPickedDice(),0,0);
        player.getSchemeCard().putDice(player.getPickedDice(),1,3);
        player.getSchemeCard().putDice(player.getPickedDice(),0,2);
        player.getSchemeCard().putDice(player.getPickedDice(),1,1);
        player.getSchemeCard().putDice(player.getPickedDice(),0,2);
        player.getSchemeCard().putDice(player.getPickedDice(),0,3);
        player.getSchemeCard().putDice(player.getPickedDice(),1,0);

        System.out.print(window);
    }

*/
}

