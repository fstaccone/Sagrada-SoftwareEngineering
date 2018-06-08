package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.Dice;
import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.model.gameobjects.PlayerMultiplayer;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

import java.util.Scanner;

public class MoveDiceIgnoringValueRestrEffect implements Effect{

    private int price;

    public MoveDiceIgnoringValueRestrEffect() {
        price = 1;
    }

    @Override
    public boolean applyEffect(Player caller, Match match) {

        PlayerMultiplayer p = (PlayerMultiplayer) caller;

        WindowPatternCard schema = caller.getSchemeCard();

        int row = caller.getStartX1();
        int column = caller.getStartY1();
        Dice dice = schema.getDice(row, column);
        if(p.getNumFavorTokens() >= price) {
            if (dice != null) {
                int newRow = caller.getFinalX1();
                int newColumn = caller.getFinalY1();
                System.out.println(newRow);
                System.out.println(newColumn);
                System.out.println(dice.toString());
                schema.removeDice(row, column);
                schema.putDiceIgnoringValueConstraint(dice, newRow, newColumn); //DA RIVEDERE
                System.out.println(newRow);
                System.out.println(newColumn);
                System.out.println(row);
                System.out.println(column);
                if (dice.equals(schema.getWindow()[newRow][newColumn].getDice())) {
                    p.setNumFavorTokens(p.getNumFavorTokens() - price);
                    price = 2;
                    return true;
                } else
                    schema.putDice(dice,row,column);
                    return false;
            } else return false;
        }else
            return false;
    }
}
