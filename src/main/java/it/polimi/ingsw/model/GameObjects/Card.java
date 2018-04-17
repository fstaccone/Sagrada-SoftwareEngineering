package it.polimi.ingsw.model.GameObjects;

import java.io.Serializable;

public abstract class Card implements Serializable {

    private  String name;

    public Card(String name) {
        this.name=name;
    }

    public String getName() {
        return name;
    }
}
