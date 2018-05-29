package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.Dice;
import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.model.gameobjects.PlayerMultiplayer;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

import java.util.Scanner;

public class MoveDiceNotAdjacentToAnotherEffect implements Effect {

    private int price;

    public MoveDiceNotAdjacentToAnotherEffect() {
        price = 1;
    }

    @Override
    public boolean applyEffect(Player caller, Match match) {

        PlayerMultiplayer p = (PlayerMultiplayer) caller;

        WindowPatternCard schema = caller.getSchemeCard();

        int newRow = caller.getFinalX1();
        int newColumn = caller.getFinalY1();

        if(p.getNumFavorTokens() >= price) {
            if (caller.getDice() < match.getBoard().getReserve().getDices().size()) {
                Dice dice = match.getBoard().getReserve().getDices().get(caller.getDice());
                if (dice != null) { //PROBABILMENTE INUTILE
                    if (!(schema.existsAdjacentDice(newRow, newColumn))) {
                        schema.putDiceWithoutCheckPos(dice, newRow, newColumn);
                        if (dice.equals(schema.getWindow()[newRow][newColumn].getDice())) {
                            match.getBoard().getReserve().getDices().remove(caller.getDice());
                            p.setNumFavorTokens(p.getNumFavorTokens() - price);
                            price = 2;
                            return true;
                        } else return false;
                    } else
                        return false;
                } else
                    return false;
            } else
                return false;
        }else
            return false;
    }
}
