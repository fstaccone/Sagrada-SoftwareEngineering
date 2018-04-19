package it.polimi.ingsw.model.gameobjects;

public class Player {
    private String name;
    private WindowPatternCard schemeCard;
    private Dice pickedDice;
    private int points;

    public Player(String name){
        this.name = name;
    }

    // getter
    public String getName() {
        return name;
    }
    public Dice getPickedDice() {
        return pickedDice;
    }
    public WindowPatternCard getSchemeCard() {
        return schemeCard;
    }
    public int getPoints() {
        return points;
    }

    // setter
    public void setPickedDice(Dice pickedDice) {
        this.pickedDice = pickedDice;
    }
    public void setSchemeCard(WindowPatternCard schemeCard) {
        this.schemeCard = schemeCard;
    }
    public void setPoints(int points) {
        this.points = points;
    }

}
