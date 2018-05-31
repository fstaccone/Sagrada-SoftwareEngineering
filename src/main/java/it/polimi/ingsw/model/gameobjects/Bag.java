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
        for (int i = 0; i < (2*num) + 1; i++) {
            item = new Random().nextInt(dices.size()-1);
            for (int j=0; j<=item; j++) {
                if (j == item) {
                    result.add( dices.get(j));
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
}
