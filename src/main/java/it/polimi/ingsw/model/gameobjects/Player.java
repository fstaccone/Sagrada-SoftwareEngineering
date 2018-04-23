package it.polimi.ingsw.model.gameobjects;

import java.io.Serializable;

public abstract class Player implements Serializable{
    private final String name;
    protected WindowPatternCard schemeCard;
    private Dice pickedDice;
    private int points;
    private Room room;
    protected Colors color;

    public Player(String name, Room room){
        super(); // Perchè?
        this.name = name;
        this.room = room;
        this.points = 0;
    }

    // getters
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
    public Colors getColor() { return color; }
    // end of getters

    // setters
    public void setPickedDice(Dice pickedDice) {
        this.pickedDice = pickedDice;
    }
    public abstract void setSchemeCard(WindowPatternCard schemeCard);
    public void setPoints(int points) {
        this.points = points;
    }
    public void setColor(Colors color) { this.color = color; }

    // Useful methods for the game's flow
    public void useToolCard(ToolCard chosenToolCardToUse){//il controller fa player1.useToolCard(): può passare la carta scelta perchè il controller ha riferimento alla board e pertanto alle pickedToolCards(attributo di board) tra le quali fa scegliere al client quale usare(ammesso che possa-->va fatto un check)(si tratta di un'azione precedente)

        chosenToolCardToUse.useCard(this,this.room.getMatch());
    }

    // Passa il turno (può farlo anche senza aver fatto altre azioni e deve comunque farlo )
    public void goTrough(){
    }

}
