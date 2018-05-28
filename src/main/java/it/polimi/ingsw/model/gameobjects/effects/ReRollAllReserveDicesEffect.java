package it.polimi.ingsw.model.gameobjects.effects;


import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.model.gameobjects.PlayerMultiplayer;
import it.polimi.ingsw.model.gameobjects.Reserve;


public class ReRollAllReserveDicesEffect implements Effect{

    private int price;

    public ReRollAllReserveDicesEffect() {
        price = 1;
    }

    @Override
    public boolean applyEffect(Player player, Match match) {
        PlayerMultiplayer p = (PlayerMultiplayer)player;

        if(p.getNumFavorTokens() >= price && player.getTurnsLeft() == 1) {
            Reserve reserve = match.getBoard().getReserve();
            reserve.throwDices(reserve.removeAllDices());
            p.setNumFavorTokens(p.getNumFavorTokens() - price);
            price = 2;
            return true;
        }
        return false;
    }
}
