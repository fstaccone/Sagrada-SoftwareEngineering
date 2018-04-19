package it.polimi.ingsw.model.gameobjects;

public class PrivateObjectiveCard extends ObjectiveCard{
    private Colors color;

    public PrivateObjectiveCard(Colors color) {
        super("PrivateObjectiveCard" + color.toString());
        this.color = color;
    }

    @Override
    public int calculatePoints(WindowPatternCard card) {
        int score = 0;
        // TODO: implement calculation algorithm

        return score;
    }

    @Override
    public String toString() {
        return "PrivateObjectiveCard{" +
                "color=" + color +
                '}';
    }
}
