package it.polimi.ingsw.model.gameobjects;


import it.polimi.ingsw.model.gamelogic.Player;

import java.util.Arrays;

public class PrivateObjectiveCard {

    private Colors color;

    /**
     * Constructor for PrivateObjectiveCard.
     *
     * @param color is the color of the dices considered by the private objective card for calculating the score.
     */
    public PrivateObjectiveCard(Colors color) {
        this.color = color;
    }

    public Colors getColor() {
        return color;
    }

    /**
     * Sums the values of all the dices of the same color of the private objective card that are placed in a
     * player's window pattern card.
     *
     * @param player is the player considered for calculating the score.
     */
    public void useCard(Player player) {
        player.setPointsByPrivateObjective(Arrays.stream(player.getSchemeCard().getWindow())
                .flatMap(Arrays::stream)
                .filter(s -> s.getDice() != null)
                .filter(s -> s.getDice().getColor() == color)
                .mapToInt(s -> s.getDice().getValue()).sum());

        player.setPoints(player.getPoints() + player.getPointsByPrivateObjective());
    }

    @Override
    public String toString() {
        return "color {" + color + "}";
    }

}
