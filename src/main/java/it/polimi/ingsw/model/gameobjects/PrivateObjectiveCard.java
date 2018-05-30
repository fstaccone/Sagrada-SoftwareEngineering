package it.polimi.ingsw.model.gameobjects;


import java.util.Arrays;

public class PrivateObjectiveCard {

    private Colors color;
    private String name;

    public PrivateObjectiveCard(Colors color) {
        this.name = "PrivateObjectiveCard " + color.toString();
        this.color = color;
    }

    public Colors getColor() {
        return color;
    }

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
