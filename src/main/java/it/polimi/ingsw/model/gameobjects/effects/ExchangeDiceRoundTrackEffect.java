package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.Dice;
import it.polimi.ingsw.model.gameobjects.Match;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.model.gameobjects.RoundTrack;

public class ExchangeDiceRoundTrackEffect implements Effect{

    public ExchangeDiceRoundTrackEffect() {
    }

    @Override
    public void applyEffect(Player player, Match match) {
        if(player.getPickedDice() == null)
            player.setPickedDice(match.getBoard().getReserve().chooseDice());
        Dice dice = player.getPickedDice();
        RoundTrack track = match.getBoard().getRoundTrack();
        dice = track.switchDice(dice);
        player.setPickedDice(dice);
    }
}
