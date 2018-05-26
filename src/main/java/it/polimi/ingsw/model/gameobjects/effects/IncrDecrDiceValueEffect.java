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
            if (player.getDice()< match.getBoard().getReserve().getDices().size()) {
                Dice dice = match.getBoard().getReserve().getDices().get(player.getDice());
                if (dice != null) {//PROBABILMENTE INUTILE
                    int value = dice.getValue();
                    switch (plusOrMinus) {
                        case "+":
                            if (value != 6) {
                                value = value + 1;
                                match.getBoard().getReserve().getDices().get(player.getDice()).setValue(value); //player.getDice() è l'indice
                                return true;
                            } else return false;

                        case "-":
                            if (value != 1) {
                                value = value - 1;
                                match.getBoard().getReserve().getDices().get(player.getDice()).setValue(value);
                                return true;
                            } else return false;

                        default:
                            return false;
                    }
                } else return false;
            }else return false;
        }
    }
