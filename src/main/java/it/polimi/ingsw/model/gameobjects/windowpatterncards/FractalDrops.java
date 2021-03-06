package it.polimi.ingsw.model.gameobjects.windowpatterncards;

import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

public class FractalDrops extends WindowPatternCard {

    public FractalDrops() {
        super("Fractal Drops", 4, 5);
        this.setDifficulty(3);
        this.getWindow()[0][1].setValueConstraint(4);
        this.getWindow()[0][3].setColorConstraint(Colors.YELLOW);
        this.getWindow()[0][4].setValueConstraint(6);
        this.getWindow()[1][0].setColorConstraint(Colors.RED);
        this.getWindow()[1][2].setValueConstraint(2);
        this.getWindow()[2][2].setColorConstraint(Colors.RED);
        this.getWindow()[2][3].setColorConstraint(Colors.VIOLET);
        this.getWindow()[2][4].setValueConstraint(1);
        this.getWindow()[3][0].setColorConstraint(Colors.BLUE);
        this.getWindow()[3][1].setColorConstraint(Colors.YELLOW);
    }
}
