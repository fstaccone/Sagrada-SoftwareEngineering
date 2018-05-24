package it.polimi.ingsw.model.gameobjects.windowpatterncards;

import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

public class RipplesOfLight extends WindowPatternCard {

    public RipplesOfLight() {
        super("Ripples of Light",4,5);
        this.setDifficulty(5);
        this.getWindow()[0][3].setColorConstraint(Colors.RED);
        this.getWindow()[0][4].setValueConstraint(5);
        this.getWindow()[1][2].setColorConstraint(Colors.VIOLET);
        this.getWindow()[1][3].setValueConstraint(4);
        this.getWindow()[1][4].setColorConstraint(Colors.BLUE);
        this.getWindow()[2][1].setColorConstraint(Colors.BLUE);
        this.getWindow()[2][2].setValueConstraint(3);
        this.getWindow()[2][3].setColorConstraint(Colors.YELLOW);
        this.getWindow()[2][4].setValueConstraint(6);
        this.getWindow()[3][0].setColorConstraint(Colors.YELLOW);
        this.getWindow()[3][1].setValueConstraint(2);
        this.getWindow()[3][2].setColorConstraint(Colors.GREEN);
        this.getWindow()[3][3].setValueConstraint(1);
        this.getWindow()[3][4].setColorConstraint(Colors.RED);
    }
}
