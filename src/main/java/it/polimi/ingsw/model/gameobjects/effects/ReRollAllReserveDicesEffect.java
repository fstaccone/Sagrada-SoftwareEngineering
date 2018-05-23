package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.Dice;
import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.model.gameobjects.Reserve;

import java.util.List;

public class ReRollAllReserveDicesEffect implements Effect{

    public ReRollAllReserveDicesEffect() {
    }

    @Override
    public boolean applyEffect(Player player, Match match) {
        List<Dice> dicesToThrow;
        Reserve reserve = match.getBoard().getReserve();
        dicesToThrow = reserve.removeAllDices();
        reserve.throwDices(dicesToThrow);
        return false;
    }
}
