package it.polimi.ingsw.model.gameobjects.windowpatterncards;

import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

public class ViaLux extends WindowPatternCard {
    public ViaLux() {
        super("Via Lux", 4, 5);
        this.setDifficulty(4);
        this.getWindow()[0][0].setColorConstraint(Colors.YELLOW);
        this.getWindow()[0][2].setValueConstraint(6);
        this.getWindow()[1][1].setValueConstraint(1);
        this.getWindow()[1][2].setValueConstraint(5);
        this.getWindow()[1][4].setValueConstraint(2);
        this.getWindow()[2][0].setValueConstraint(3);
        this.getWindow()[2][1].setColorConstraint(Colors.YELLOW);
        this.getWindow()[2][2].setColorConstraint(Colors.RED);
        this.getWindow()[2][3].setColorConstraint(Colors.VIOLET);
        this.getWindow()[3][2].setValueConstraint(4);
        this.getWindow()[3][3].setValueConstraint(3);
        this.getWindow()[3][4].setColorConstraint(Colors.RED);

    }
}
