package it.polimi.ingsw.model.gameobjects;

import java.io.Serializable;

public abstract class Player implements Serializable {
    protected final String name;
    protected WindowPatternCard schemeCard;
    private Dice pickedDice;
    private int points;
    protected Colors color;
    protected int turnsLeft;

    private int dice;
    private String choise;

    private int roundChosen;
    private int diceChosenFromRound;

    private int startX1;
    private int startY1;
    private int finalX1;
    private int finalY1;
    private int startX2;
    private int startY2;
    private int finalX2;
    private int finalY2;

    public int getStartX1() {
        return startX1;
    }

    public int getStartY1() {
        return startY1;
    }

    public int getFinalX1() {
        return finalX1;
    }

    public int getFinalY1() {
        return finalY1;
    }

    public int getStartX2() {
        return startX2;
    }

    public int getStartY2() {
        return startY2;
    }

    public int getFinalX2() {
        return finalX2;
    }

    public int getFinalY2() {
        return finalY2;
    }

    public void setStartX1(int startX1) {
        this.startX1 = startX1;
    }

    public void setStartY1(int startY1) {
        this.startY1 = startY1;
    }

    public void setFinalX1(int finalX1) {
        this.finalX1 = finalX1;
    }

    public void setFinalY1(int finalY1) {
        this.finalY1 = finalY1;
    }

    public void setStartX2(int startX2) {
        this.startX2 = startX2;
    }

    public void setStartY2(int startY2) {
        this.startY2 = startY2;
    }

    public void setFinalX2(int finalX2) {
        this.finalX2 = finalX2;
    }

    public void setFinalY2(int finalY2) {
        this.finalY2 = finalY2;
    }

    public int getDice() {
        return dice;
    }

    public void setDice(int diceChosen) {
        dice = diceChosen;
    }

    public String getChoise() {
        return choise;
    }

    public void setChoise(String choise) {
        this.choise = choise;
    }

    public int getRound() {
        return this.roundChosen;
    }

    public void setRound(int roundChosen) {
        this.roundChosen = roundChosen;
    }

    public int getDiceChosenFromRound() {
        return this.diceChosenFromRound;
    }

    public void setDiceChosenFromRound(int diceChosenFromRound) {
        this.diceChosenFromRound = diceChosenFromRound;
    }


    // todo: check inheritance
    public Player(String name) {
        super(); // todo: Perchè?
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

    public int getTurnsLeft() { return turnsLeft; }
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
    // end of setters


}
