package it.polimi.ingsw.model.gameobjects;

public class PlayerSingleplayer extends Player {
    /**
     * Creates a new Player for a single player match.
     * @param name is the player's name
     */
    public PlayerSingleplayer(String name){
        super(name);
        this.setColor(Colors.NONE);
        System.out.println("Player creato: " + name);
    }

    /**
     * Sets the player's scheme card
     * @param schemeCard is the player's scheme card
     */
    @Override
    public void setSchemeCard(WindowPatternCard schemeCard) { this.schemeCard = schemeCard; }


}
