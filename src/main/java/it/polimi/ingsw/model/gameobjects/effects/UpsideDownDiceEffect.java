package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.Dice;
import it.polimi.ingsw.model.gameobjects.Match;
import it.polimi.ingsw.model.gameobjects.Player;

public class UpsideDownDiceEffect implements Effect {

    public UpsideDownDiceEffect() {
    }

    @Override
    public void applyEffect(Player player, Match match) {
        // TODO: Decide if the Dice has to be picked now or before using the card
        int val = player.getPickedDice().getValue();
        Dice modified;

        switch (val) {
            case 1:
                modified = player.getPickedDice();
                modified.setValue(6);
                player.setPickedDice(modified);
                break;
            case 2:
                modified = player.getPickedDice();
                modified.setValue(5);
                player.setPickedDice(modified);
                break;
            case 3:
                modified = player.getPickedDice();
                modified.setValue(4);
                player.setPickedDice(modified);
                break;
            case 4:
                modified = player.getPickedDice();
                modified.setValue(3);
                player.setPickedDice(modified);
                break;
            case 5:
                modified = player.getPickedDice();
                modified.setValue(2);
                player.setPickedDice(modified);
                break;
            case 6:
                modified = player.getPickedDice();
                modified.setValue(1);
                player.setPickedDice(modified);
                break;
            default:
                player.getPickedDice().setValue(0);
                break;
        }

    }
}
