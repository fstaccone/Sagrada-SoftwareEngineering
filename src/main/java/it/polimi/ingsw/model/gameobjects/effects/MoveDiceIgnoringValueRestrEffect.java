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
                schema.putDiceIgnoringValueConstraint(dice, newRow, newColumn); //DA RIVEDERE
                if (dice.equals(schema.getWindow()[newRow][newColumn].getDice())) {
                    schema.removeDice(row, column); //LO PUò RIMETTERE NELLA STESSA POSIZIONE? SE Sì LA REMOVE NON VA FATTA QUI
                    p.setNumFavorTokens(p.getNumFavorTokens() - price);
                    price = 2;
                    return true;
                } else
                    return false;
            } else return false;
        }else
            return false;
    }
}
