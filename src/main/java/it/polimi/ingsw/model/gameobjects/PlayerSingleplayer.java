package it.polimi.ingsw.model.gameobjects;

public class PlayerSingleplayer extends Player {

    public PlayerSingleplayer(String name){
        super(name);
        this.setColor(Colors.NONE);
        System.out.println("Player creato: " + name);
    }

    @Override
    public void setSchemeCard(WindowPatternCard schemeCard) { this.schemeCard = schemeCard; }


}
