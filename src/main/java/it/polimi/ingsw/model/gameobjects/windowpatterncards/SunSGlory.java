package it.polimi.ingsw.model.gameobjects.windowpatterncards;

import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

public class SunSGlory extends WindowPatternCard {

    public SunSGlory() {
        super("Sun's Glory", 4, 5);
        this.setDifficulty(6);
        this.getWindow()[0][0].setValueConstraint(1);
        this.getWindow()[0][1].setColorConstraint(Colors.VIOLET);
        this.getWindow()[0][2].setColorConstraint(Colors.YELLOW);
        this.getWindow()[0][4].setValueConstraint(4);
        this.getWindow()[1][0].setColorConstraint(Colors.VIOLET);
        this.getWindow()[1][1].setColorConstraint(Colors.YELLOW);
        this.getWindow()[1][4].setValueConstraint(6);
        this.getWindow()[2][3].setValueConstraint(5);
        this.getWindow()[2][4].setValueConstraint(4);
        this.getWindow()[3][1].setValueConstraint(5);
        this.getWindow()[3][2].setValueConstraint(4);
        this.getWindow()[3][3].setValueConstraint(2);
        this.getWindow()[3][4].setValueConstraint(1);
    }
}
