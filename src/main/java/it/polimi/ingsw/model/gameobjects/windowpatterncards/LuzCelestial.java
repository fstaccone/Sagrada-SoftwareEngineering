package it.polimi.ingsw.model.gameobjects.windowpatterncards;

import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

public class LuzCelestial extends WindowPatternCard {

    public LuzCelestial() {
        super("Luz Celestial", 4, 5);
        this.setDifficulty(3);
        this.getWindow()[0][2].setColorConstraint(Colors.RED);
        this.getWindow()[0][3].setValueConstraint(5);
        this.getWindow()[1][0].setColorConstraint(Colors.VIOLET);
        this.getWindow()[1][1].setValueConstraint(4);
        this.getWindow()[1][3].setColorConstraint(Colors.GREEN);
        this.getWindow()[1][4].setValueConstraint(3);
        this.getWindow()[2][0].setValueConstraint(6);
        this.getWindow()[2][3].setColorConstraint(Colors.BLUE);
        this.getWindow()[3][1].setColorConstraint(Colors.YELLOW);
        this.getWindow()[3][2].setValueConstraint(2);
    }
}
