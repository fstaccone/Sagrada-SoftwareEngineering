package it.polimi.ingsw.model.gameobjects.windowpatterncards;

import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

public class Gravitas extends WindowPatternCard {

    public Gravitas() {
        super("Gravitas", 4, 5);
        this.setDifficulty(5);
        this.getWindow()[0][0].setValueConstraint(1);
        this.getWindow()[0][2].setValueConstraint(3);
        this.getWindow()[0][3].setColorConstraint(Colors.BLUE);
        this.getWindow()[1][1].setValueConstraint(2);
        this.getWindow()[1][2].setColorConstraint(Colors.BLUE);
        this.getWindow()[2][0].setValueConstraint(6);
        this.getWindow()[2][1].setColorConstraint(Colors.BLUE);
        this.getWindow()[2][3].setValueConstraint(4);
        this.getWindow()[3][0].setColorConstraint(Colors.BLUE);
        this.getWindow()[3][1].setValueConstraint(5);
        this.getWindow()[3][2].setValueConstraint(2);
        this.getWindow()[3][4].setValueConstraint(1);
    }
}
