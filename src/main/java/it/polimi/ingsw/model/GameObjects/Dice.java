package it.polimi.ingsw.model.GameObjects;

import java.io.Serializable;

public class Dice implements Serializable{

    private DiceColor color;
    private DiceValue value;

    public Dice(DiceColor color){
       this.color=color;
       this.value=value;
    }

    public DiceColor getColor(){
       return color;
    }

    public DiceValue getValue() {
        return value;
    }

    public void setColor(DiceColor color) {
        this.color = color;
    }

    public void setValue(DiceValue value) {
        this.value = value;
    }
}
