package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.Dice;
import it.polimi.ingsw.model.gameobjects.Match;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

import java.util.Scanner;

public class MoveTwoDicesEffect implements Effect{

    public MoveTwoDicesEffect() {
    }

    @Override
    public void applyEffect(Player caller, Match match) {
        //QUI IL GIOCATORE DOVRà, ATTRAVERSO I METODI DI WINDOWPATTERNCARD DI CUI HA IL RIFERIMENTO, SCEGLIERE DUE DADI DA TOGLIERE DALLA SCHEMECARD E POI REINSERIRE)
        //BISOGNA CONSIDERARE CHE IL TUTTO VA GESTITO ATTRAVERSO N.B.   S C E L T E   DA PARTE DEL CLIENT CHE ATTRAVERSO IL CONTROLLER RICHIAMA METODI DEL PLAYER CHE AGISCONO SUL SUO STATO E QUINDI SULLA SUA CARTA SCHEMA
        Dice[] chosenDices = new Dice[2];
        WindowPatternCard schema = caller.getSchemeCard();
        Scanner scan = new Scanner(System.in);
        for(int i=0; i<2; i++) {
            int token = 0;
            while(token == 0) {
                System.out.println(schema.toString());
                System.out.println("Choose dice " + (i+1) + " row:");
                int row = scan.nextInt();
                System.out.println("Choose dice " + (i+1) + " column:");
                int column = scan.nextInt();
                Dice dice = schema.removeDice(row, column);
                if (dice != null) {
                    token = 1;
                    System.out.println("You've chosen the dice: " + dice.toString());
                    chosenDices[i]=dice;
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
        System.out.println(schema.toString());
    }
}
