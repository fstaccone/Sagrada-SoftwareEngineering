package it.polimi.ingsw.model.gameobjects.windowpatterncards;

import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

public class LuxMundi extends WindowPatternCard {

    public LuxMundi() {
        super("Lux Mundi", 4, 5);
        this.setDifficulty(6);
        this.getWindow()[0][2].setValueConstraint(1);
        this.getWindow()[1][0].setValueConstraint(1);
        this.getWindow()[1][1].setColorConstraint(Colors.GREEN);
        this.getWindow()[1][2].setValueConstraint(3);
        this.getWindow()[1][3].setColorConstraint(Colors.BLUE);
        this.getWindow()[1][4].setValueConstraint(2);
        this.getWindow()[2][0].setColorConstraint(Colors.BLUE);
        this.getWindow()[2][1].setValueConstraint(5);
        this.getWindow()[2][2].setValueConstraint(4);
        this.getWindow()[2][3].setValueConstraint(6);
        this.getWindow()[2][4].setColorConstraint(Colors.GREEN);
        this.getWindow()[3][1].setColorConstraint(Colors.BLUE);
        this.getWindow()[3][2].setValueConstraint(5);
        this.getWindow()[3][3].setColorConstraint(Colors.GREEN);
    }
}
