package it.polimi.ingsw.model.gameobjects;

import java.io.Serializable;

public class Dice implements Serializable{
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
        return "[" + value + " " + color + "]";
    }
}
