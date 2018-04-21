package it.polimi.ingsw.model.gameobjects;


import it.polimi.ingsw.model.gameobjects.effects.*;

public class PublicObjectiveCard {
    private String cardname;
    private int pointsToBeAssigned;
    Effect effect;

    public PublicObjectiveCard(String name) {

        this.cardname = name;
        switch (cardname) {
            case "Varietà di colore":
                this.effect= new ColoursVarietyEffect();
                break;
            case "Diagonali colorate":
                this.effect= new ColouredDiagonalsEffect();
                break;
            case "Sfumature diverse":
                this.effect= new DifferentShadesEffect();
                break;
            case "Sfumature scure":
                this.effect= new DarkShadesEffect();
                break;
            case "Sfumature medie":
                this.effect= new MediumShadesEffect();
                break;
            case "Sfumature chiare":
                this.effect= new LightShadesEffect();
                break;
            case "Sfumature diverse - Colonna":
                this.effect= new DifferentShadesInAColumnEffect();
                break;
            case "Sfumature diverse - Riga":
                this.effect= new DifferentShadesInARowEffect();
                break;
            case "Colori diversi - Colonna":
                this.effect= new DifferentColorsInAColumnEffect();
                break;
            case "Colori diversi - Riga":
                this.effect= new DifferentColorsInARowEffect();
                break;

            default: cardname = "Invalid card from ToolCard";
                break;
        }

    }

    public void useCard(Player caller, Match match){//CONSIDERIAMO PER ESEMPIO LA TOOLCARD4

        effect.applyEffect( caller,  match);
    }

    public String getCardname() {
        return cardname;
    }

    @Override
    public String toString() {
        return "{" +
                "cardname='" + cardname + '\'' +
                '}';
    }
}
