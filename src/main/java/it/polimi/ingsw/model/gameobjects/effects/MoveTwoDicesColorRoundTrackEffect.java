package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.*;

import java.util.Scanner;

/*public class MoveTwoDicesColorRoundTrackEffect implements Effect {

    public MoveTwoDicesColorRoundTrackEffect() {
    }

    @Override
    public void applyEffect(Player player, Match match) {
        Colors color = null;
        while(color == null)
            color = match.getRoundTrack().getColorOfAChosenDice();
        Dice[] chosenDices = new Dice[2];
        WindowPatternCard schema = player.getSchemeCard();
        Scanner scan = new Scanner(System.in);
        for(int i=0; i<2; i++) {
            int token = 0;
            while(token == 0) {
                System.out.println(schema.toString());
                System.out.println("Choose dice " + (i+1) + " row:");
                int row = scan.nextInt();
                System.out.println("Choose dice " + (i+1) + " column:");
                int column = scan.nextInt();
                //TODO: we should implement a get Dice method in WindowPatterncard instead of accessing the square
                if(schema.getWindow()[row][column].getDice().getColor()==color) {
                    Dice dice = schema.removeDice(row, column);
                    if (dice != null) {
                        token = 1;
                        System.out.println("You've chosen the dice: " + dice.toString());
                        chosenDices[i] = dice;
                    }
                }
            }
        }
        System.out.println(schema.toString());
        for( int i=0; i<2; i++){
            int result = 0;
            while(result==0) {
                System.out.println("Please put row where you want to move dice"+(i+1)+" : ");
                int newRow = scan.nextInt();
                System.out.println("Please put column where you want to move dice"+(i+1)+" : ");
                int newColumn = scan.nextInt();
                schema.putDice(chosenDices[i], newRow, newColumn);
                //Checking if putDice worked, else get a different new position
                if(chosenDices[i].equals(schema.getWindow()[newRow][newColumn].getDice()))
                    result = 1;
                else System.out.println("Please choose a different position.");
            }
        }
        player.setSchemeCard(schema);
        System.out.println(schema.toString());
    }
}*/
