package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.Dice;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

public class UpsideDownDiceEffect implements Effect {

    public UpsideDownDiceEffect() {
    }

    @Override
    public void applyEffect(Player player, WindowPatternCard schemeCard) {
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
