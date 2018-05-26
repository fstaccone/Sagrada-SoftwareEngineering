package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.Dice;
import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

import java.util.Scanner;

public class MoveDiceNotAdjacentToAnotherEffect implements Effect {

    public MoveDiceNotAdjacentToAnotherEffect() {
    }

    @Override
    public boolean applyEffect(Player caller, Match match) {

        WindowPatternCard schema = caller.getSchemeCard();

        int newRow = caller.getFinalX1();
        int newColumn = caller.getFinalY1();
        if (caller.getDice()< match.getBoard().getReserve().getDices().size()) {
            Dice dice = match.getBoard().getReserve().getDices().get(caller.getDice());
            if (dice != null) { //PROBABILMENTE INUTILE
                if (!(schema.existsAdjacentDice(newRow, newColumn))) {
                    schema.putDiceWithoutCheckPos(dice, newRow, newColumn);
                    if (dice.equals(schema.getWindow()[newRow][newColumn].getDice())) {
                        match.getBoard().getReserve().getDices().remove(caller.getDice());
                        return true;
                    } else return false;
                } else
                    return false;
            } else
                return false;
        }
        else
            return false;
    }
}
