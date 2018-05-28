package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.model.gameobjects.PlayerMultiplayer;

public class ChooseAnotherDiceEffect implements Effect {

    private int price;

    public ChooseAnotherDiceEffect() {
        price = 1;
    }

    @Override
    public boolean applyEffect(Player player, Match match) {
        PlayerMultiplayer p = (PlayerMultiplayer) player;

        if(p.getNumFavorTokens() >= price){
            match.setDiceAction(false);
            p.setTurnsLeft(0);
            p.setNumFavorTokens(p.getNumFavorTokens() - price);
            price = 2;
            return true;
        }

        return false;
    }
}
