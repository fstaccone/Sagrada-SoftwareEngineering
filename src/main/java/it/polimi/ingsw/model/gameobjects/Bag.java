package it.polimi.ingsw.model.gameobjects;

import java.util.ArrayList;
import java.util.*;
import java.util.Random;

public class Bag {

    private List<Dice> dices;

    /**
     * Initializes the bag with n = num dices of each color
     * @param num represents the number of dices in the bag for each color
     */
    public Bag(int num) {
        dices = new ArrayList<>();
        for (int j = 0; j < Colors.values().length-1; j++) { //lenght-1 because of NONE color
            for (int i = 0; i < num; i++) {
                //System.out.println(Colors.values()[j] + " dice put into bag.");
                Dice dice = new Dice(Colors.values()[j]);
                dices.add(dice);
            }
        }
    }

    /**
     *
     * @return the number of dices in the bag
     */
    public int getSize(){
        return dices.size();
    }

    /**
     * Takes n = (num*2 + 1) random dices from the bag, adds them in a list and removes them from the bag
     * @param num is the number of players of the match
     * @return a list of n = (num*2 + 1) dices taken from the bag
     */
    public List<Dice> pickDices(int num) {
        List<Dice> result = new ArrayList<>();
        int item;
        for (int i = 0; i < (2*num) + 1; i++) {
            if(dices.size()!=1) {
                item = new Random().nextInt(dices.size() - 1);
            }
            else item = 0;
            for (int j=0; j<=item; j++) {
                if (j == item) {
                    result.add( dices.get(j));
                    dices.remove(j);
                }
            }
        }
        return result;
    }

    /**
     * Picks a single random dice from the bag
     * @return a dice or null if the bag is empty
     */
    public Dice pickSingleDice(){
        int item;
        if(dices.size() == 1)  item = 0;
        else item = new Random().nextInt(dices.size()-1);
        for (int j=0; j<=item; j++) {
            if (j == item) {
                Dice d = dices.get(j);
                dices.remove(j);
                return d;
            }
        }
        return null;
    }

    /**
     * Puts a dice back in the bag
     * @param dice is the dice to put in the bag
     */
    public void putDiceInBag(Dice dice){
        dices.add(dice);
    }

    /*public static void main(String args[]) {
        Bag bag = new Bag(2);
        System.out.println(bag.getAllDices().toString());//Just to check constructor behavior

        List<Dice> pescata = bag.pickDices(4);
        System.out.println(pescata.toString());

        Dice ex = new Dice(Colors.BLUE);
        bag.putDiceInBag(ex);
        System.out.println("Sacchetto aggiornato :"+bag.getAllDices().toString());
    }*/
}
