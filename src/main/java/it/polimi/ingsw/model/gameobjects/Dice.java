package it.polimi.ingsw.model.gameobjects;

public class Dice {
    private Colors color;
    private int value;

    public Dice (Colors color){
        this.color = color;
    }

    public Colors getColor() {
        return color;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString(){
        return "{" + value + " " + color + "}";
    }
}
