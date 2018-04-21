package it.polimi.ingsw.model.gameobjects.windowpatterncards;

import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

public class ChromaticSplendor extends WindowPatternCard {

    public ChromaticSplendor() {
        super("Chromatic Splendor",4,5);
        this.setDifficulty(4);
        this.getWindow()[0][2].setColorConstraint(Colors.GREEN);
        this.getWindow()[1][0].setValueConstraint(2);
        this.getWindow()[1][1].setColorConstraint(Colors.YELLOW);
        this.getWindow()[1][2].setValueConstraint(5);
        this.getWindow()[1][3].setColorConstraint(Colors.BLUE);
        this.getWindow()[1][4].setValueConstraint(1);
        this.getWindow()[2][1].setColorConstraint(Colors.RED);
        this.getWindow()[2][2].setValueConstraint(3);
        this.getWindow()[2][3].setColorConstraint(Colors.VIOLET);
        this.getWindow()[3][0].setValueConstraint(1);
        this.getWindow()[3][2].setValueConstraint(6);
        this.getWindow()[4][4].setValueConstraint(4);
    }
}
