package it.polimi.ingsw.model.gameobjects;

import it.polimi.ingsw.model.gamelogic.Match;

import java.io.Serializable;

public abstract class Player implements Serializable{
    protected final String name;
    protected WindowPatternCard schemeCard;
    private Dice pickedDice;
    private int points;
    protected Colors color;

    private int dice;
    private String choise;

    private int startX;
    private int startY;
    private int finalX;
    private int finalY;

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getFinalX() {
        return finalX;
    }

    public int getFinalY() {
        return finalY;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public void setFinalX(int finalX) {
        this.finalX = finalX;
    }

    public void setFinalY(int finalY) {
        this.finalY = finalY;
    }

    public void setDice(int diceChosen) {
        dice = diceChosen;
    }

    public void setChoise(String choise) {
        this.choise = choise;
    }

    public int getDice() {
        return dice;
    }

    public String getChoise() {
        return choise;
    }

    // todo: check inheritance
    public Player(String name){
        super(); // Perchè?
        this.name = name;
        this.points = 0;
    }

    // getters
    public String getName() {
        return name;
    }
    public Dice getPickedDice() {
        return pickedDice;
    }
    public WindowPatternCard getSchemeCard() {
        return schemeCard;
    }
    public int getPoints() {
        return points;
    }
    public Colors getColor() { return color; }
    // end of getters

    // setters
    public void setPickedDice(Dice pickedDice) {
        this.pickedDice = pickedDice;
    }
    public abstract void setSchemeCard(WindowPatternCard schemeCard);
    public void setPoints(int points) {
        this.points = points;
    }
    public void setColor(Colors color) { this.color = color; }

    // Useful methods for the game's flow
    public abstract void useToolCard(ToolCard chosenToolCardToUse);

    // potrebbe essere uguale per entrambi i tipi di match, ma forse è più facile gestirlo diversamente
    /*
    public void chooseDice(){
    }
    */

}
