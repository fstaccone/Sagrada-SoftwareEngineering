package it.polimi.ingsw.model.gameobjects.windowpatterncards;

import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

public class LuxAstram extends WindowPatternCard {

    public LuxAstram() {
        super("Lux Astram", 4, 5);
        this.setDifficulty(5);
        this.getWindow()[0][1].setValueConstraint(1);
        this.getWindow()[0][2].setColorConstraint(Colors.GREEN);
        this.getWindow()[0][3].setColorConstraint(Colors.VIOLET);
        this.getWindow()[0][4].setValueConstraint(4);
        this.getWindow()[1][0].setValueConstraint(6);
        this.getWindow()[1][1].setColorConstraint(Colors.VIOLET);
        this.getWindow()[1][2].setValueConstraint(2);
        this.getWindow()[1][3].setValueConstraint(5);
        this.getWindow()[1][4].setColorConstraint(Colors.GREEN);
        this.getWindow()[2][0].setValueConstraint(1);
        this.getWindow()[2][1].setColorConstraint(Colors.GREEN);
        this.getWindow()[2][2].setValueConstraint(5);
        this.getWindow()[2][3].setValueConstraint(3);
        this.getWindow()[2][4].setColorConstraint(Colors.VIOLET);
    }
}
