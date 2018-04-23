package it.polimi.ingsw.model.gameobjects;


import it.polimi.ingsw.model.gameobjects.effects.*;

public class PublicObjectiveCard extends ObjectiveCard{
    private String cardname;

    private Effect effect;

    public PublicObjectiveCard(String name) {
        super(name);

        this.cardname = name;
        switch (cardname) {
            case "Varietà di colore":
                this.effect= new ColorsVarietyEffect();
                break;
            case "Diagonali colorate":
                this.effect= new ColoredDiagonalsEffect();
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

            default: cardname = "Invalid card from PublicObjectiveCard";
                break;
        }

    }

    @Override
    public int calculatePoints(WindowPatternCard card) {//CONTROLLA
        return 0;
    }

    public void useCard(Player caller, Match match){

        this.effect.applyEffect( caller,  match);
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
