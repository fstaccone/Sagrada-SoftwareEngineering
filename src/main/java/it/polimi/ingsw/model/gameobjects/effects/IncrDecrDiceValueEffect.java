package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.Dice;
import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.model.gameobjects.PlayerMultiplayer;

import java.util.Scanner;

public class IncrDecrDiceValueEffect implements Effect{

    public IncrDecrDiceValueEffect() {
    }

    @Override
    public boolean applyEffect(Player player, Match match) {

            String plusOrMinus= player.getChoise();
            //player.setChoise(null);
            int value=match.getBoard().getReserve().getDices().get(player.getDice()).getValue();
            //player.setDice(0);
            System.out.println(value);
            switch (plusOrMinus) {
                case "+":
                    if (value != 6) {
                        value = value + 1;
                        match.getBoard().getReserve().getDices().get(player.getDice()).setValue(value);
                        return true;
                    } else return false;

                case "-":
                    if(value!=1) {
                        value=value-1;
                        match.getBoard().getReserve().getDices().get(player.getDice()).setValue(value);
                        return true;
                    }
                    else return false;

                default:
                    return false;
            }
        }
    }
