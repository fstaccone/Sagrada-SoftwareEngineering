package it.polimi.ingsw.model.GameObjects;

public class PublicObjectiveCard extends ObjectiveCard {

    PublicObjectiveCard(String name){
        super(name);
    }

    @Override
    public int getScore(WindowPatternCard card) {
        return 0;
    }
}
