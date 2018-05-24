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

        int row = caller.getStartX();

        int column = caller.getStartY();
        Dice dice = schema.removeDice(row, column);
        if(dice!=null){
            int newRow = caller.getFinalX();
            int newColumn = caller.getFinalY();
            schema.putDiceIgnoringColorConstraint(dice, newRow, newColumn); //DA RIVEDERE
            if(dice.equals(schema.getWindow()[newRow][newColumn].getDice())) // NULL POINTER EXCEPTION AL MOMENTO
                return true;
            else
                return false;
        }else return false;
    }

}
