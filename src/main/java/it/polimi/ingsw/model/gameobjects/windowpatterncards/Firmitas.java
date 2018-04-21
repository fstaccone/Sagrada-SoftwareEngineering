package it.polimi.ingsw.model.gameobjects.windowpatterncards;

import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

public class Firmitas extends WindowPatternCard {
    public Firmitas() {
        super("Firmitas", 4, 5);
        this.setDifficulty(5);
        this.getWindow()[0][0].setColorConstraint(Colors.VIOLET);
        this.getWindow()[0][1].setValueConstraint(6);
        this.getWindow()[0][4].setValueConstraint(3);
        this.getWindow()[1][0].setValueConstraint(5);
        this.getWindow()[1][1].setColorConstraint(Colors.VIOLET);
        this.getWindow()[1][2].setValueConstraint(3);
        this.getWindow()[2][1].setValueConstraint(2);
        this.getWindow()[2][2].setColorConstraint(Colors.VIOLET);
        this.getWindow()[2][3].setValueConstraint(1);
        this.getWindow()[3][1].setValueConstraint(1);
        this.getWindow()[3][2].setValueConstraint(5);
        this.getWindow()[3][3].setColorConstraint(Colors.VIOLET);
        this.getWindow()[3][4].setValueConstraint(4);

    }
}
