package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.Dice;
import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.model.gameobjects.PlayerMultiplayer;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

import java.util.Scanner;

public class MoveDiceIgnoringColorRestrEffect implements Effect {

    private int price;

    public MoveDiceIgnoringColorRestrEffect() {

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
                schema.putDiceIgnoringColorConstraint(dice, newRow, newColumn); //DA RIVEDERE
                System.out.println(newRow);
                System.out.println(newColumn);
                System.out.println(row);
                System.out.println(column);
                if (dice.equals(schema.getWindow()[newRow][newColumn].getDice())) {//SERVE VERAMENTE?
                    schema.removeDice(row, column);//LO PUò RIMETTERE NELLA STESSA POSIZIONE? SE Sì LA REMOVE NON VA FATTA QUI
                    caller.setStartX1(5);//UNREACHABLE VALUE, USED TO RESET
                    caller.setFinalX1(5);
                    caller.setStartY1(4);//UNREACHABLE VALUE, USED TO RESET
                    caller.setFinalY1(4);
                    p.setNumFavorTokens(p.getNumFavorTokens() - price);
                    price = 2;
                    return true;
                } else {
                    caller.setStartX1(5);//UNREACHABLE VALUE, USED TO RESET
                    caller.setFinalX1(5);
                    caller.setStartY1(4);//UNREACHABLE VALUE, USED TO RESET
                    caller.setFinalY1(4);
                    return false;
                }
            } else {
                caller.setStartX1(5);//UNREACHABLE VALUE, USED TO RESET
                caller.setFinalX1(5);
                caller.setStartY1(4);//UNREACHABLE VALUE, USED TO RESET
                caller.setFinalY1(4);
                return false;
            }
        }else
            return false;

    }

}
