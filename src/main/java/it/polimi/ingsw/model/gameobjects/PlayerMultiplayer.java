package it.polimi.ingsw.model.gameobjects;

public class PlayerMultiplayer extends Player {
    private int numFavorTokens;
    private PrivateObjectiveCard privateObjectiveCard;
    private Room room;

    public PlayerMultiplayer(String name, Room room) {
        super(name, room);
    }

    // setter
    public void setNumFavorTokens(int numFavorTokens) {
        this.numFavorTokens = numFavorTokens;
    }

    public void setPrivateObjectiveCard(Card privateObjectiveCard) {
        this.privateObjectiveCard = (PrivateObjectiveCard)privateObjectiveCard;
    }

    // getter
    public int getNumFavorTokens() {
        return numFavorTokens;
    }

    public PrivateObjectiveCard getPrivateObjectiveCard() {
        return privateObjectiveCard;
    }

    public Colors getColor() { return this.color; }

    @Override
    public void setSchemeCard(WindowPatternCard schemeCard) {
        this.schemeCard = schemeCard;
        this.setNumFavorTokens(schemeCard.getDifficulty());
    }

}
