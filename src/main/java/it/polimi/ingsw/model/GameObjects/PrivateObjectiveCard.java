package it.polimi.ingsw.model.GameObjects;

public class PrivateObjectiveCard extends ObjectiveCard{

    public PrivateObjectiveCard(String name) {
        super( name);
    }

    @Override
    public int getScore(WindowPatternCard card) {
        return 0;
    }
}
