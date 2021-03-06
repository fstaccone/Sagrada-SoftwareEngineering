package it.polimi.ingsw.model.gameobjects.windowpatterncards;

import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

public class SunCatcher extends WindowPatternCard {

    public SunCatcher() {
        super("Sun Catcher", 4, 5);
        this.setDifficulty(3);
        this.getWindow()[0][1].setColorConstraint(Colors.BLUE);
        this.getWindow()[0][2].setValueConstraint(2);
        this.getWindow()[0][4].setColorConstraint(Colors.YELLOW);
        this.getWindow()[1][1].setValueConstraint(4);
        this.getWindow()[1][3].setColorConstraint(Colors.RED);
        this.getWindow()[2][2].setValueConstraint(5);
        this.getWindow()[2][3].setColorConstraint(Colors.YELLOW);
        this.getWindow()[3][0].setColorConstraint(Colors.GREEN);
        this.getWindow()[3][1].setValueConstraint(3);
        this.getWindow()[3][4].setColorConstraint(Colors.VIOLET);
    }
}
