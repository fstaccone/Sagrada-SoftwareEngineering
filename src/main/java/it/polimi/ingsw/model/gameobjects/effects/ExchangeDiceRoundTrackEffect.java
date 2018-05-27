package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.Dice;
import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.model.gameobjects.RoundTrack;

public class ExchangeDiceRoundTrackEffect implements Effect{

    public ExchangeDiceRoundTrackEffect() {
    }

    @Override
    public boolean applyEffect(Player player, Match match) {


        if (player.getDice()>=0 && player.getDice()< match.getBoard().getReserve().getDices().size()) {
            Dice dice = match.getBoard().getReserve().getDices().remove(player.getDice());
            RoundTrack track = match.getBoard().getRoundTrack();
            dice = track.switchDice(dice,player.getRound(),player.getDiceChosenFromRound());
            if (dice!=null) {
                match.getBoard().getReserve().getDices().add(player.getDice(), dice);
                return true;
            }
            else
                return false;
        }
        return false;
    }
}
