package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.Dice;
import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gameobjects.Player;

import java.util.Scanner;

public class IncrDecrDiceValueEffect implements Effect{

    public IncrDecrDiceValueEffect() {
    }

    @Override
    public void applyEffect(Player player, Match match) {/*
        if(player.getPickedDice()==null)
            player.setPickedDice(match.getBoard().getReserve().chooseDice());*/
        System.out.println("Picked dice: "+player.getPickedDice().toString()+"\n"+
        "Type '+' if you want to increment the dice value by 1 (not valid for value=6), " +
        "type '-' if you want to decrement the dice value by 1 (not valid for value=1)");
        Dice modified = player.getPickedDice();
        int value = modified.getValue();
        Scanner scan = new Scanner(System.in);
        int token = 0;
        while(token == 0) {
            String answer = scan.nextLine();
            switch (answer) {
                case "+":
                    if(value!=6) {
                        value++;
                        modified.setValue(value);
                        token = 1;
                    }
                    else System.out.println("The value of this dice is 6, you can't increment it.");
                    break;
                case "-":
                    if(value!=1) {
                        value--;
                        modified.setValue(value);
                        token = 1;
                    }
                    else System.out.println("The value of this dice is 1, you can't decrement it.");
                    break;
                default:
                    System.out.println("Wrong choice");
                    break;
            }
        }
        System.out.println(("New dice: "+modified.toString()));
        player.setPickedDice(modified);
    }
}
