package it.polimi.ingsw.model.gameobjects;

import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gameobjects.effects.*;

public class PublicObjectiveCard extends ObjectiveCard {

    private Effect effect;
    private String description;

    public PublicObjectiveCard(String name) {
        super(name);
        switch (this.name) {
            case "Varietà di colore":
                effect = new ColorsVarietyEffect();
                description = "description card 1";
                break;
            case "Diagonali colorate":
                effect = new ColoredDiagonalsEffect();
                description = "description card 2";
                break;
            case "Sfumature diverse":
                effect = new DifferentShadesEffect();
                description = "description card 3";
                break;
            case "Sfumature scure":
                effect = new DarkShadesEffect();
                description = "description card 4";
                break;
            case "Sfumature medie":
                effect = new MediumShadesEffect();
                description = "description card 5";
                break;
            case "Sfumature chiare":
                effect = new LightShadesEffect();
                description = "description card 6";
                break;
            case "Sfumature diverse - Colonna":
                effect = new DifferentShadesInAColumnEffect();
                description = "description card 7";
                break;
            case "Sfumature diverse - Riga":
                effect = new DifferentShadesInARowEffect();
                description = "description card 8";
                break;
            case "Colori diversi - Colonna":
                effect = new DifferentColorsInAColumnEffect();
                description = "description card 9";
                break;
            case "Colori diversi - Riga":
                effect = new DifferentColorsInARowEffect();
                description = "description card 10";
                break;

            default:
                this.name = "Invalid card from PublicObjectiveCard";
                break;
        }

    }

    @Override
    public void useCard(Player caller, Match match) {

        this.effect.applyEffect(caller, match);
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("- cardname: ");
        string.append(name);
        string.append('\n');
        string.append("  description: ");
        string.append(description);
        string.append('\n');
        return string.toString();
    }
}
