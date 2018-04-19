package it.polimi.ingsw.model.gameobjects;

import java.util.ArrayList;
import java.util.*;
import java.util.Random;

public class Bag {

    private static ArrayList<Dice> dices;

    public Bag(int num) {
        dices = new ArrayList<>();
        for (int j = 0; j < Colors.values().length-1; j++) { //lenght-1 because of NONE color
            for (int i = 0; i < num; i++) {
                System.out.println(Colors.values()[j] + " dice put into bag.");
                Dice dice = new Dice(Colors.values()[j]);
                dices.add(dice);
            }
        }
    }

    public static List<Dice> getAllDices() {//Serve solo per test su funzionamento
        return dices;
    }

    public ArrayList<Dice> pesca(int num) {
        ArrayList<Dice> result = new ArrayList<>();
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

    public void riponi(Dice dice){
        System.out.println("Ripongo nel sacchetto il dado :"+dice.toString());
        dices.add(dice);
    }

    /*public static void main(String args[]) {
        Bag bag = new Bag(2);

        System.out.println(bag.getAllDices().toString());//Solo per test su funzionamento del costruttore

        List<Dice> pescata = bag.pesca(4);
        System.out.println(pescata.toString());

        Dice ex = new Dice(Colors.BLUE);
        bag.riponi(ex);
        System.out.println("Sacchetto aggiornato :"+bag.getAllDices().toString());
    }*/
}
