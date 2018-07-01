package it.polimi.ingsw.model.gameobjects.windowpatterncards;

import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

public class ShadowThief extends WindowPatternCard {
    public ShadowThief() {
        super("Shadow Thief", 4, 5);
        this.setDifficulty(5);
        this.getWindow()[0][0].setValueConstraint(6);
        this.getWindow()[0][1].setColorConstraint(Colors.VIOLET);
        this.getWindow()[0][4].setValueConstraint(5);
        this.getWindow()[1][0].setValueConstraint(5);
        this.getWindow()[1][2].setColorConstraint(Colors.VIOLET);
        this.getWindow()[2][0].setColorConstraint(Colors.RED);
        this.getWindow()[2][1].setValueConstraint(6);
        this.getWindow()[2][3].setColorConstraint(Colors.VIOLET);
        this.getWindow()[3][0].setColorConstraint(Colors.YELLOW);
        ;
        this.getWindow()[3][1].setColorConstraint(Colors.RED);
        this.getWindow()[3][2].setValueConstraint(5);
        this.getWindow()[3][3].setValueConstraint(4);
        this.getWindow()[3][4].setValueConstraint(3);

    }
}
