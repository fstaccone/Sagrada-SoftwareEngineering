package it.polimi.ingsw.model.gameobjects.windowpatterncards;

import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

public class KaleidoscopicDream extends WindowPatternCard {

    public KaleidoscopicDream() {
        super("Kaleidoscopic Dream", 4, 5);
        this.setDifficulty(4);
        this.getWindow()[0][0].setColorConstraint(Colors.YELLOW);
        this.getWindow()[0][1].setColorConstraint(Colors.BLUE);
        this.getWindow()[0][4].setValueConstraint(1);
        this.getWindow()[1][0].setColorConstraint(Colors.GREEN);
        this.getWindow()[1][2].setValueConstraint(5);
        this.getWindow()[1][4].setValueConstraint(4);
        this.getWindow()[2][0].setValueConstraint(3);
        this.getWindow()[2][2].setColorConstraint(Colors.RED);
        this.getWindow()[2][4].setColorConstraint(Colors.GREEN);
        this.getWindow()[3][0].setValueConstraint(2);
        this.getWindow()[3][3].setColorConstraint(Colors.BLUE);
        this.getWindow()[3][4].setColorConstraint(Colors.YELLOW);

    }
}
