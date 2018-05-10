package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.Dice;
import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

import java.util.Scanner;

public class MoveDiceNotAdjacentToAnotherEffect implements Effect {

    public MoveDiceNotAdjacentToAnotherEffect() {
    }

    @Override
    public void applyEffect(Player player, Match match) {
        if(player.getPickedDice()==null)
            player.setPickedDice(match.getBoard().getReserve().chooseDice());
        Dice dice = player.getPickedDice();
        WindowPatternCard schema = player.getSchemeCard();
        Scanner scan = new Scanner(System.in);
        int result = 0;
        while(result == 0) {
            System.out.println("Please put the row where you want to put your dice: ");
            int newRow = scan.nextInt();
            System.out.println("Please put the column where you want to put your dice: ");
            int newColumn = scan.nextInt();
            if(!(schema.existsAdjacentDice(newRow, newColumn))){
                schema.putDiceWithoutCheckPos(dice, newRow, newColumn);
                if (dice.equals(schema.getWindow()[newRow][newColumn].getDice()))
                    result = 1;
            }
            else System.out.println("Please choose a different position.");
        }
    }
}
