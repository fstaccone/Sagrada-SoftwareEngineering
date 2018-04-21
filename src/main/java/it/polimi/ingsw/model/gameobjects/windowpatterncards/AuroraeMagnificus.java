package it.polimi.ingsw.model.gameobjects.windowpatterncards;

import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

public class AuroraeMagnificus extends WindowPatternCard {
    public AuroraeMagnificus() {
        super("Aurorae Magnificus", 4, 5);
        this.setDifficulty(5);
        this.getWindow()[0][0].setValueConstraint(5);
        this.getWindow()[0][1].setColorConstraint(Colors.GREEN);
        this.getWindow()[0][2].setColorConstraint(Colors.BLUE);
        this.getWindow()[0][3].setColorConstraint(Colors.VIOLET);
        this.getWindow()[0][4].setValueConstraint(2);
        this.getWindow()[1][0].setColorConstraint(Colors.VIOLET);
        this.getWindow()[1][4].setColorConstraint(Colors.YELLOW);
        this.getWindow()[2][0].setColorConstraint(Colors.YELLOW);
        this.getWindow()[2][2].setValueConstraint(6);
        this.getWindow()[2][4].setColorConstraint(Colors.VIOLET);
        this.getWindow()[3][0].setValueConstraint(1);
        this.getWindow()[3][3].setColorConstraint(Colors.GREEN);
        this.getWindow()[3][4].setValueConstraint(4);

    }
}
