package it.polimi.ingsw.model.gameobjects;

import it.polimi.ingsw.model.gamelogic.Match;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundTrack {
    //rounds is a list of (list of dices) since you may have more than one dice in the same position
    private List<List<Dice>> rounds;

    public RoundTrack() {
        // It makes an ArrayList of 10 (constant declared in Match) Lists of Dices
        rounds = new LinkedList<>();
        for (int i = 0; i < Match.getNumberOfRounds(); i++) {
            rounds.add(new ArrayList<>());
        }
    }

    public void putDices(List<Dice> dicesToPut, int position) {
        rounds.set(position, dicesToPut);
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();

        for (int i = 0; i < rounds.size(); i++) {
            if (rounds.get(i).size() != 0) {
                AtomicInteger j = new AtomicInteger();
                string.append("Round ");
                string.append(i + 1);
                string.append("\n");
                rounds.get(i).forEach(e -> string.append(j.getAndIncrement() + ") " + e.toString() + "\t"));
                string.append("\n");
            }
        }
        return string.toString();
    }

    // todo: eliminare, anche gli utilizzi in testing
    public void showRoundTrack() {
        int j = 0;
        for (List<Dice> list : rounds) {
            System.out.println("List number: " + j);
            int i = 0;
            for (Dice d : list) {
                System.out.println(d.toString() + "id=" + i);
                i++;
            }
            j++;
        }
    }

    public Dice getDice() {
        showRoundTrack();
        System.out.println("Choose the number of the list from which you want to get a dice");
        Scanner scan = new Scanner(System.in);
        int listNumber = scan.nextInt();
        System.out.println(("Choose the dice id"));
        int diceId = scan.nextInt();
        int j = 0;
        for (List<Dice> list : rounds) {
            if (j == listNumber) {
                int i = 0;
                for (Dice d : list) {
                    if (i == diceId) {
                        list.remove(diceId);
                        return d;
                    }
                    i++;
                }
            }
            j++;
        }
        System.out.println("Error: wrong parameters.");
        return null;
    }


    public Colors getColorOfAChosenDice(int chosenRound, int chosenDiceFromRound) {
        Dice dice = rounds.get(chosenRound - 1).get(chosenDiceFromRound);
        return dice.getColor();
    }

    public Dice switchDice(Dice diceToSwitch, int chosenRound, int chosenDiceFromRound) {
        if (chosenRound > 0 && chosenRound <= rounds.size() && chosenDiceFromRound >= 0 && chosenDiceFromRound < rounds.get(chosenRound - 1).size()) {
            Dice returnValue = rounds.get(chosenRound - 1).remove(chosenDiceFromRound);
            rounds.get(chosenRound - 1).add(chosenDiceFromRound, diceToSwitch);
            return returnValue;
        } else return null;
    }
}
