package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.Dice;
import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.model.gameobjects.PlayerMultiplayer;

import java.util.Random;

public class ReRollDiceEffect implements Effect {

    private int price;

    public ReRollDiceEffect() {
        price = 1;
    }

    @Override
    public boolean applyEffect(Player player, Match match) {

        PlayerMultiplayer p = (PlayerMultiplayer) player;

        Random rand = new Random();
        int newValue = rand.nextInt(6)+1;
        if(p.getNumFavorTokens() >= price) {
            if (player.getDice() < match.getBoard().getReserve().getDices().size()) {
                Dice dice = match.getBoard().getReserve().getDices().get(player.getDice());
                if (dice != null) {//PROBABILMENTE INUTILE
                    dice.setValue(newValue);
                    p.setNumFavorTokens(p.getNumFavorTokens() - price);
                    price = 2;
                    return true;
                } else return false;
            } else return false;
        }else
            return false;
    }
}
