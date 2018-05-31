package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.Dice;
import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.model.gameobjects.PlayerMultiplayer;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

import java.util.Scanner;

public class MoveTwoDicesEffect implements Effect{

    private int price;

    public MoveTwoDicesEffect() {
        price = 1;
    }

    @Override
    public boolean applyEffect(Player caller, Match match) {

        PlayerMultiplayer p = (PlayerMultiplayer) caller;

        WindowPatternCard schema = caller.getSchemeCard();

        int row1 = caller.getStartX1();
        int column1 = caller.getStartY1();
        int row2 = caller.getStartX2();
        int column2 = caller.getStartY2();

        Dice dice1 = schema.getDice(row1, column1);
        Dice dice2 = schema.getDice(row2, column2);
        if(p.getNumFavorTokens() >= price) {
            if (dice1 != null && dice2!=null) {
                int newRow1 = caller.getFinalX1();
                int newColumn1 = caller.getFinalY1();
                int newRow2 = caller.getFinalX2();
                int newColumn2 = caller.getFinalY2();
                if (schema.putDice(dice1, newRow1, newColumn1) && schema.putDice(dice2, newRow2, newColumn2)) {
                    schema.removeDice(row1, column1);
                    schema.removeDice(row2, column2);
                    p.setNumFavorTokens(p.getNumFavorTokens() - price);
                    price = 2;
                    return true;
                } else
                    return false;
            } else
                return false;
        } else
            return false;
    }
}
