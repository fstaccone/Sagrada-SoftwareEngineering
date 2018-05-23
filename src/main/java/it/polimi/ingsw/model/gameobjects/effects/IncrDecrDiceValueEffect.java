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
    public boolean applyEffect(Player player, Match match) {/*
        if(player.getPickedDice()==null)
            player.setPickedDice(match.getBoard().getReserve().chooseDice());*/
        //System.out.println("Picked dice: "+player.getPickedDice().toString()+"\n"+
        //"Type '+' if you want to increment the dice value by 1 (not valid for value=6), " +
        //"type '-' if you want to decrement the dice value by 1 (not valid for value=1)");
        //Dice modified = player.getPickedDice();
        //int value = modified.getValue();
        //Scanner scan = new Scanner(System.in);
        //int token = 0;
        //while(token == 0) {

            //String answer = scan.nextLine();
            String plusOrMinus= player.getChoise();
            int value=match.getBoard().getReserve().getDices().get(player.getDice()).getValue();
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
