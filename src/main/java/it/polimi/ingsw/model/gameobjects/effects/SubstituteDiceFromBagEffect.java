package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.Dice;
import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.model.gameobjects.PlayerMultiplayer;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

import java.util.Scanner;

public class SubstituteDiceFromBagEffect implements Effect {

    private int price;

    public SubstituteDiceFromBagEffect() {
        price = 1;
    }

    @Override
    public boolean applyEffect(Player player, Match match) {
        PlayerMultiplayer p = (PlayerMultiplayer) player;


        if(p.getNumFavorTokens() >= price){
            Dice dice = match.getBoard().getReserve().getDices().remove(player.getDice());
            if(dice!=null) {
                match.getBag().putDiceInBag(dice);
                Dice diceFromBag = match.getBag().pickSingleDice();
                player.setDiceFromBag(diceFromBag);

                p.setNumFavorTokens(p.getNumFavorTokens() - price);
                price = 2;
                return true;
            }else
                return false;
        }
        return false;
    }
}
