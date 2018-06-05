package it.polimi.ingsw.model.gameobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Reserve {

    private List<Dice> dices;

    /**
     * Creates a new reserve with no dices
     */
    public Reserve() { dices = new ArrayList<>(); }

    /**
     * Populates the reserve with the dices
     * @param init is the list of dices to put in the reserve, a random value between 1 and 6 is assigned to each dice
     */
    public void throwDices(List<Dice> init) {
        Random rand = new Random();

        for (Dice dice : init) {
            int val = rand.nextInt(6) + 1;
            dice.setValue(val);
            dices.add(dice);
        }
    }

    public List<Dice> getDices() { return dices; }

    @Override
    public String toString() {

        StringBuilder s;
        s = new StringBuilder();
        s.append("This is the state of the RESERVE:\n");
        for (int i = 0; i < dices.size(); i++) {
            s.append(i);
            s.append(") ");
            s.append(dices.get(i).toString());
            s.append("\n");
        }

        return s.toString();
    }

    /**
     * Takes a dice from the reserve
     * @param index is the index of the dice the player wants to pick
     * @return returns the dice chosen by the player
     */
    public Dice chooseDice(int index) {
        if (!(dices.isEmpty())) {
            return dices.remove(index);
        } else {
            System.out.println("The reserve is empty."); //andrebbe fatto lanciando ExceptionEmptyReserve
            return null;
        }
    }

    /**
     * It removes all dices in dices and returns them to be put into the roundtrack
     */
    public List<Dice> removeAllDices() {

        ArrayList<Dice> dicesLeft;
        dicesLeft = new ArrayList<>(dices);
        dices.clear();

        return dicesLeft;
    }

}