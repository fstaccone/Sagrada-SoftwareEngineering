package it.polimi.ingsw.model.gameobjects;


import java.util.Arrays;

public class PrivateObjectiveCard extends ObjectiveCard{
    private Colors color;

    public PrivateObjectiveCard(Colors color) {
        super("PrivateObjectiveCard " + color.toString());
        this.color = color;
    }

    public Colors getColor() { return color; }

    @Override
    public void useCard(Player player) {
        player.setPoints(player.getPoints() +
                Arrays.stream(player.getSchemeCard().getWindow())
                .flatMap(Arrays::stream)
                .filter(s -> s.getDice() != null)
                .filter(s -> s.getDice().getColor() == color)
                .mapToInt(s -> s.getDice().getValue()).sum());
    }

    @Override
    public String toString() {
        return "PrivateObjectiveCard{" +
                "color=" + color +
                '}';
    }

}
