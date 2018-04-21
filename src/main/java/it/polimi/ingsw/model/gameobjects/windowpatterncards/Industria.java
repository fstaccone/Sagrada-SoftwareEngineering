package it.polimi.ingsw.model.gameobjects.windowpatterncards;

import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

public class Industria extends WindowPatternCard {
    public Industria() {
        super("Industria", 4, 5);
        this.setDifficulty(5);
        this.getWindow()[0][0].setValueConstraint(1);
        this.getWindow()[0][1].setColorConstraint(Colors.RED);
        this.getWindow()[0][2].setValueConstraint(3);
        this.getWindow()[0][4].setValueConstraint(6);
        this.getWindow()[1][0].setValueConstraint(5);
        this.getWindow()[1][1].setValueConstraint(4);
        this.getWindow()[1][2].setColorConstraint(Colors.RED);
        this.getWindow()[1][3].setValueConstraint(2);
        this.getWindow()[2][2].setValueConstraint(5);
        this.getWindow()[2][3].setColorConstraint(Colors.RED);
        this.getWindow()[2][4].setValueConstraint(1);
        this.getWindow()[3][3].setValueConstraint(3);
        this.getWindow()[3][4].setColorConstraint(Colors.RED);

    }
}
