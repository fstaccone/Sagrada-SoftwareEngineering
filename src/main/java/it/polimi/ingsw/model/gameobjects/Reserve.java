package it.polimi.ingsw.model.gameobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Reserve {

    private List<Dice> dices;

    public Reserve() { dices = new ArrayList<>(); }

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