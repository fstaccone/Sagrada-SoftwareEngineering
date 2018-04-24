package it.polimi.ingsw.model.gameobjects;

import java.util.ArrayList;
import java.util.*;
import java.util.Random;

public class Bag {

    private List<Dice> dices;

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

    public List<Dice> getAllDices() {//Serve solo per test su funzionamento
        return dices;
    }

    public List<Dice> pickDices(int num) {
        List<Dice> result = new ArrayList<>();
        int item;
        for (int i = 0; i < num; i++) {
            System.out.println("Bag size: "+dices.size());
            item = new Random().nextInt(dices.size()-1);
            for (int j=0; j<=item; j++) {
                if (j == item) {
                    result.add( dices.get(j));
                    System.out.println("Pescato dado: "+dices.get(j).toString());
                    dices.remove(j);
                }
            }
        }
        return result;
    }

    public void putDiceInBag(Dice dice){
        System.out.println("Ripongo nel sacchetto il dado :"+dice.toString());
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
