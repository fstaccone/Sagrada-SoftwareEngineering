package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.Dice;
import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

import java.util.Scanner;

public class SubstituteDiceFromBagEffect implements Effect {

    public SubstituteDiceFromBagEffect() {
    }

    @Override
    public boolean applyEffect(Player player, Match match) {
        if(player.getPickedDice()==null)
          //  player.setPickedDice(match.getBoard().getReserve().chooseDice());
        match.getBag().putDiceInBag(player.getPickedDice());
        Dice diceFromBag = match.getBag().pickDices(1).remove(0); //pickDices returns a list of one element
        System.out.println("You got this dice: "+diceFromBag.toString());
        Scanner scan = new Scanner(System.in);
        int token = 0;
        while(token ==0){
            System.out.println("Please choose a value:");
            int value = scan.nextInt();
            if(value>0 && value<7){
                diceFromBag.setValue(value);
                player.setPickedDice(diceFromBag);
                token = 1;
            }
        }
        Dice dice = player.getPickedDice();
        int result = 0;
        WindowPatternCard schema = player.getSchemeCard();
        while(result==0) {
            System.out.println("Please put row where you want to move your dice: ");
            int newRow = scan.nextInt();
            System.out.println("Please put column where you want to move your dice: ");
            int newColumn = scan.nextInt();
            schema.putDice(dice, newRow, newColumn);
            //Checking if putDice worked, else get a different new position
            if(dice.equals(schema.getWindow()[newRow][newColumn].getDice()))
                result = 1;
            else System.out.println("Please choose a different position.");
        }
        player.setSchemeCard(schema);
        return false;
    }
}
