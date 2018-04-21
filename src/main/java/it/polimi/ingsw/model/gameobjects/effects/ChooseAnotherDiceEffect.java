package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.Match;
import it.polimi.ingsw.model.gameobjects.Player;

public class ChooseAnotherDiceEffect implements Effect {

    public ChooseAnotherDiceEffect() {
    }

    @Override
    public void applyEffect(Player player, Match match) {
        player.setPickedDice(match.getBoard().getReserve().chooseDice());
    }
}
