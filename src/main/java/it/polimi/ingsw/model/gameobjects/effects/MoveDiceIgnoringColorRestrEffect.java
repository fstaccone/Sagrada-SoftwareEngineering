package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.Dice;
import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

import java.util.Scanner;

public class MoveDiceIgnoringColorRestrEffect implements Effect {

    public MoveDiceIgnoringColorRestrEffect() {
    }

    @Override
    public boolean applyEffect(Player caller, Match match) {
        WindowPatternCard schema = caller.getSchemeCard();

        int row = caller.getStartX1();
        int column = caller.getStartY1();
        Dice dice = schema.getDice(row, column);

        if(dice!=null){
            int newRow = caller.getFinalX1();
            int newColumn = caller.getFinalY1();
            schema.putDiceIgnoringColorConstraint(dice, newRow, newColumn); //DA RIVEDERE
            if(dice.equals(schema.getWindow()[newRow][newColumn].getDice())) {//SERVE VERAMENTE?
                schema.removeDice(row, column);//LO PUò RIMETTERE NELLA STESSA POSIZIONE? SE Sì LA REMOVE NON VA FATTA QUI
                caller.setStartX1(5);//UNREACHABLE VALUE, USED TO RESET
                caller.setFinalX1(5);
                caller.setStartY1(4);//UNREACHABLE VALUE, USED TO RESET
                caller.setFinalY1(4);
                return true;
            }
            else {
                caller.setStartX1(5);//UNREACHABLE VALUE, USED TO RESET
                caller.setFinalX1(5);
                caller.setStartY1(4);//UNREACHABLE VALUE, USED TO RESET
                caller.setFinalY1(4);
                return false;
            }
        }else {
            caller.setStartX1(5);//UNREACHABLE VALUE, USED TO RESET
            caller.setFinalX1(5);
            caller.setStartY1(4);//UNREACHABLE VALUE, USED TO RESET
            caller.setFinalY1(4);
            return false;
        }


    }

}
