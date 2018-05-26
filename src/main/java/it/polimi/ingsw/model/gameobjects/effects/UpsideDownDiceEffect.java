package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.Dice;
import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gameobjects.Player;

public class UpsideDownDiceEffect implements Effect {

    public UpsideDownDiceEffect() {
    }

    @Override
    public boolean applyEffect(Player player, Match match) {
        if (player.getDice()< match.getBoard().getReserve().getDices().size()) {//CONTROLLO SU DICE!=NULL?

            int value = match.getBoard().getReserve().getDices().get(player.getDice()).getValue();
            switch (value) {
                case 1:
                    match.getBoard().getReserve().getDices().get(player.getDice()).setValue(6);
                    return true;
                case 2:
                    match.getBoard().getReserve().getDices().get(player.getDice()).setValue(5);
                    return true;
                case 3:
                    match.getBoard().getReserve().getDices().get(player.getDice()).setValue(4);
                    return true;
                case 4:
                    match.getBoard().getReserve().getDices().get(player.getDice()).setValue(3);
                    return true;
                case 5:
                    match.getBoard().getReserve().getDices().get(player.getDice()).setValue(2);
                    return true;
                case 6:
                    match.getBoard().getReserve().getDices().get(player.getDice()).setValue(1);
                    return true;
                default:
                    return false;
            }
        }
        else return false;
    }

}
