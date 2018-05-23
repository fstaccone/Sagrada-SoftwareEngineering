package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.model.gameobjects.Reserve;

public class ChooseAnotherDiceEffect implements Effect {

    public ChooseAnotherDiceEffect() {
    }

    @Override
    public boolean applyEffect(Player player, Match match) {
        Reserve reserve = match.getBoard().getReserve();
      //  player.setPickedDice(reserve.chooseDice());
        return false;
    }
}
