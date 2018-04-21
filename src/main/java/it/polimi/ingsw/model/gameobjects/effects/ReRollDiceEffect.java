package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.Dice;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

import java.util.Random;

public class ReRollDiceEffect implements Effect {

    // To be revised later depending on player management
    private Dice diceToBeRerolled;

    public ReRollDiceEffect() {
    }

    @Override
    public void applyEffect(Player player, WindowPatternCard schemeCard) {
        Random rand = new Random();
        int val = rand.nextInt(6)+1;
        diceToBeRerolled.setValue(val);
        player.setPickedDice(diceToBeRerolled);
    }
}
