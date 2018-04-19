package it.polimi.ingsw.model.gameobjects;

public class PrivateObjectiveCard {
    private String name;
    private Colors color;

    public PrivateObjectiveCard(String name, Colors color) {
        this.name = name;
        this.color = color;
    }

    public int CalculatePointsRelatedToThisColour(WindowPatternCard schemeCard){
        int sum=0;

        //..su this.color
        return sum;
    }
}
