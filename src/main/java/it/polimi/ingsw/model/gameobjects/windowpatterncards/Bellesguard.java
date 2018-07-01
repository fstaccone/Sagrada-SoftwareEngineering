package it.polimi.ingsw.model.gameobjects.windowpatterncards;

import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

public class Bellesguard extends WindowPatternCard {

    public Bellesguard() {
        super("Bellesguard", 4, 5);
        this.setDifficulty(3);
        this.getWindow()[0][0].setColorConstraint(Colors.BLUE);
        this.getWindow()[0][1].setValueConstraint(6);
        this.getWindow()[0][4].setColorConstraint(Colors.YELLOW);
        this.getWindow()[1][1].setValueConstraint(3);
        this.getWindow()[1][2].setColorConstraint(Colors.BLUE);
        this.getWindow()[2][1].setValueConstraint(5);
        this.getWindow()[2][2].setValueConstraint(6);
        this.getWindow()[2][3].setValueConstraint(2);
        this.getWindow()[3][1].setValueConstraint(4);
        this.getWindow()[3][3].setValueConstraint(1);
        this.getWindow()[3][4].setColorConstraint(Colors.GREEN);
    }
}
