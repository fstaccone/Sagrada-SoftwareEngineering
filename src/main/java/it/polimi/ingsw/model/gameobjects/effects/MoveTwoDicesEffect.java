package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.Dice;
import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

import java.util.Scanner;

public class MoveTwoDicesEffect implements Effect{

    public MoveTwoDicesEffect() {
    }

    @Override
    public boolean applyEffect(Player caller, Match match) {
        WindowPatternCard schema = caller.getSchemeCard();

        int row1 = caller.getStartX1();
        int column1 = caller.getStartY1();
        int row2 = caller.getStartX2();
        int column2 = caller.getStartY2();

        Dice dice1 = schema.getDice(row1, column1);
        Dice dice2;

        if (dice1 != null) {
            int newRow1 = caller.getFinalX1();
            int newColumn1 = caller.getFinalY1();
            schema.putDice(dice1, newRow1, newColumn1); //DA RIVEDERE
            if (dice1.equals(schema.getWindow()[newRow1][newColumn1].getDice())) {
                schema.removeDice(row1, column1); //LO PUò RIMETTERE NELLA STESSA POSIZIONE? SE Sì LA REMOVE NON VA FATTA QUI
                dice2 = schema.getDice(row2, column2);
                if (dice2 != null) {
                    int newRow2 = caller.getFinalX2();
                    int newColumn2 = caller.getFinalY2();
                    schema.putDice(dice2, newRow2, newColumn2); //DA RIVEDERE
                    if (dice2.equals(schema.getWindow()[newRow2][newColumn2].getDice())) {
                        schema.removeDice(row2, column2);//LO PUò RIMETTERE NELLA STESSA POSIZIONE? SE Sì LA REMOVE NON VA FATTA QUI
                        return true;
                    } else
                        return false;
                } else return false;
            } else return false;
        }
        else return false;
    }
}
