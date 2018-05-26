package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.Dice;
import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gameobjects.Player;

import java.util.Random;

public class ReRollDiceEffect implements Effect {


    public ReRollDiceEffect() {
    }

    @Override
    public boolean applyEffect(Player player, Match match) {
        Random rand = new Random();
        int newValue = rand.nextInt(6)+1;
        if (player.getDice()< match.getBoard().getReserve().getDices().size()) {
            Dice dice = match.getBoard().getReserve().getDices().get(player.getDice());
            if (dice != null) {//PROBABILMENTE INUTILE
                dice.setValue(newValue);
                return true;
            } else return false;
        }
        else return false;
    }
}
