package it.polimi.ingsw.model.gameobjects;

import it.polimi.ingsw.model.gamelogic.Match;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundTrack {
    //rounds is a list of (list of dices) since you may have more than one dice in the same position
    private List<List<Dice>> rounds;

    /**
     * It makes an ArrayList of 10 (constant declared in Match) Lists of Dices
     */
    public RoundTrack() {
        rounds = new LinkedList<>();
        for (int i = 0; i < Match.NUMBER_OF_ROUNDS; i++) {
            rounds.add(new ArrayList<>());
        }
    }

    /**
     * At the end of each round, the remaining dices in the reserve are put in the round track, in the position
     * corresponding to the round
     *
     * @param dicesToPut is the list of dices to place in the round track
     * @param position   is the position corresponding to the round that has just ended
     */
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
                rounds.get(i).forEach(e -> {
                    string.append(j.getAndIncrement());
                    string.append(") ");
                    string.append(e.toString());
                    string.append("\t");
                });
                string.append("\n");
            }
        }
        return string.toString();
    }

    /**
     * @return a dice from the roundTrack, useful to test putDice
     */
    public Dice getDice() {
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

    /**
     * Switches a dice of the round track with the player's picked dice
     *
     * @param diceToSwitch        is the player's picked dice to place in the round track
     * @param chosenRound         is the round slot in the round track from which the player wants to take a dice
     * @param chosenDiceFromRound is the position of the dice in the eventual pile of dices in the round track slot
     * @return the dice chosen from the round track
     */
    public Dice switchDice(Dice diceToSwitch, int chosenRound, int chosenDiceFromRound) {
        if (chosenRound > 0 && chosenRound <= rounds.size() && chosenDiceFromRound >= 0 && chosenDiceFromRound < rounds.get(chosenRound - 1).size()) {
            Dice returnValue = rounds.get(chosenRound - 1).remove(chosenDiceFromRound);
            rounds.get(chosenRound - 1).add(chosenDiceFromRound, diceToSwitch);
            return returnValue;
        } else return null;
    }

    public int sumForSinglePlayer() {
        return rounds.stream().flatMap(Collection::stream).filter(Objects::nonNull).mapToInt(Dice::getValue).sum();
    }
}
