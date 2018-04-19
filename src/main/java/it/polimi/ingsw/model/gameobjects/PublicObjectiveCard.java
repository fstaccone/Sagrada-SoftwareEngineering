package it.polimi.ingsw.model.gameobjects;


import it.polimi.ingsw.model.gameobjects.effects.Effect;

public class PublicObjectiveCard {
    private String name;
    private int pointsToBeAssigned;
    Effect effect;

    public PublicObjectiveCard(String name, int pointsToBeAssigned, Effect effect) {//costruito dal json?
        this.name = name;
        this.pointsToBeAssigned = pointsToBeAssigned;
        this.effect = effect;
    }
}
