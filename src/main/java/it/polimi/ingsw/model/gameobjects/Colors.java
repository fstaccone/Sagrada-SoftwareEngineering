package it.polimi.ingsw.model.gameobjects;

import java.io.Serializable;

public enum Colors implements Serializable {
    // NONE color will be used to show that a window pattern card's square has no colour limitation
    BLUE("Blu"), RED("Rosso"), YELLOW("Giallo"), GREEN("Verde"), VIOLET("Viola"), NONE("---");

    private String desc;

    Colors(String desc) {
        this.desc = desc;
    }

    public String getDescription() {
        return desc;
    }
}