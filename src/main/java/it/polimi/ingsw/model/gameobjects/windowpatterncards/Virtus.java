package it.polimi.ingsw.model.gameobjects.windowpatterncards;

import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

public class Virtus extends WindowPatternCard {

    public Virtus() {
        super("Virtus", 4, 5);
        this.setDifficulty(5);
        this.getWindow()[0][0].setValueConstraint(4);
        this.getWindow()[0][2].setValueConstraint(2);
        this.getWindow()[0][3].setValueConstraint(5);
        this.getWindow()[0][4].setColorConstraint(Colors.GREEN);
        this.getWindow()[1][3].setValueConstraint(6);
        this.getWindow()[1][3].setColorConstraint(Colors.GREEN);
        this.getWindow()[1][4].setValueConstraint(2);
        this.getWindow()[2][1].setValueConstraint(3);
        this.getWindow()[2][2].setColorConstraint(Colors.GREEN);
        this.getWindow()[2][3].setValueConstraint(4);
        this.getWindow()[3][0].setValueConstraint(5);
        this.getWindow()[3][1].setColorConstraint(Colors.GREEN);
        this.getWindow()[3][2].setValueConstraint(1);

    }
}
