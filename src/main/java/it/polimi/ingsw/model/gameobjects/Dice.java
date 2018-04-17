package it.polimi.ingsw.model.gameobjects;

public class Dice {
    private DiceColor color;
    private DiceValue value;

    public Dice (DiceColor color){
        this.color = color;
    }

    public DiceColor getColor() {
        return color;
    }

    public DiceValue getValue() {
        return value;
    }

    public void setValue(DiceValue value) {
        this.value = value;
    }

    @Override
    public String toString(){
        return "{" + value + " " + color + "}";
    }
}
