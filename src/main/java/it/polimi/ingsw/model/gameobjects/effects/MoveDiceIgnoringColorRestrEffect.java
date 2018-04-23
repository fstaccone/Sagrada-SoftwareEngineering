package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.Dice;
import it.polimi.ingsw.model.gameobjects.Match;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

import java.util.Scanner;

public class MoveDiceIgnoringColorRestrEffect implements Effect {

    public MoveDiceIgnoringColorRestrEffect() {
    }

    @Override
    public void applyEffect(Player caller, Match match) {
        Dice dice = null;
        int token = 0;
        Scanner scan = new Scanner(System.in);
        WindowPatternCard schema = caller.getSchemeCard();
        System.out.println(schema.toString());
        while(token==0) {
            System.out.println("Please put row of the dice you want to move: ");
            int row = scan.nextInt();
            System.out.println("Please put column of the dice you want to move: ");
            int column = scan.nextInt();
            dice = schema.removeDice(row, column);
            if(dice!=null){
                token=1;
                System.out.println("You've chosen the dice: "+dice.toString());
            }
        }
        int result = 0;
        while(result==0) {
            System.out.println("Please put row where you want to move your dice: ");
            int newRow = scan.nextInt();
            System.out.println("Please put column where you want to move your dice: ");
            int newColumn = scan.nextInt();
            schema.putDiceIgnoringColorConstraint(dice, newRow, newColumn);
            //Checking if putDice worked, else get a different new position
            if(dice.equals(schema.getWindow()[newRow][newColumn].getDice()))
                result = 1;
            else System.out.println("Please choose a different position.");
        }
    }


}
