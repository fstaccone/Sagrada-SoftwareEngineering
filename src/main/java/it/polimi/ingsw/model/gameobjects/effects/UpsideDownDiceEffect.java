package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.Dice;
import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.model.gameobjects.PlayerMultiplayer;

public class UpsideDownDiceEffect implements Effect {

    private int price;

    public UpsideDownDiceEffect() {
        price = 1;
    }

    @Override
    public boolean applyEffect(Player player, Match match) {

        PlayerMultiplayer p = (PlayerMultiplayer) player;

        if(p.getNumFavorTokens() >= price) {
            if (player.getDice() < match.getBoard().getReserve().getDices().size()) {//CONTROLLO SU DICE!=NULL?

                int value = match.getBoard().getReserve().getDices().get(player.getDice()).getValue();
                switch (value) {
                    case 1:
                        match.getBoard().getReserve().getDices().get(player.getDice()).setValue(6);
                        p.setNumFavorTokens(p.getNumFavorTokens() - price);
                        price = 2;
                        return true;
                    case 2:
                        match.getBoard().getReserve().getDices().get(player.getDice()).setValue(5);
                        p.setNumFavorTokens(p.getNumFavorTokens() - price);
                        price = 2;
                        return true;
                    case 3:
                        match.getBoard().getReserve().getDices().get(player.getDice()).setValue(4);
                        p.setNumFavorTokens(p.getNumFavorTokens() - price);
                        price = 2;
                        return true;
                    case 4:
                        match.getBoard().getReserve().getDices().get(player.getDice()).setValue(3);
                        p.setNumFavorTokens(p.getNumFavorTokens() - price);
                        price = 2;
                        return true;
                    case 5:
                        match.getBoard().getReserve().getDices().get(player.getDice()).setValue(2);
                        p.setNumFavorTokens(p.getNumFavorTokens() - price);
                        price = 2;
                        return true;
                    case 6:
                        match.getBoard().getReserve().getDices().get(player.getDice()).setValue(1);
                        p.setNumFavorTokens(p.getNumFavorTokens() - price);
                        price = 2;
                        return true;
                    default:
                        return false;
                }
            } else return false;
        }else return false;
    }

}
