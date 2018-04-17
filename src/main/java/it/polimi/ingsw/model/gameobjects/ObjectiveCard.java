package it.polimi.ingsw.model.gameobjects;

public abstract class ObjectiveCard extends Card{
    public ObjectiveCard(String name) {
        super(name);
    }

    public abstract int getScore(WindowPatternCard card);

}
