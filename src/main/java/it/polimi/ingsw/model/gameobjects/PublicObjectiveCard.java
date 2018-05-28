package it.polimi.ingsw.model.gameobjects;

import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gameobjects.effects.*;

public class PublicObjectiveCard extends ObjectiveCard {

    private Effect effect;
    private String description;
    private int value;

    public PublicObjectiveCard(String name) {
        super(name);
        switch (this.name) {
            case "Color Variety":
                effect = new ColorsVarietyEffect();
                description = "Sets of one of each color anywhere";
                value = 4;
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
            case "Light Shades":
                effect = new LightShadesEffect();
                description = "Sets of 1 & 2 values anywhere";
                value = 2;
                break;
            case "Sfumature diverse - Colonna":
                effect = new DifferentShadesInAColumnEffect();
                description = "description card 7";
                break;
            case "Sfumature diverse - Riga":
                effect = new DifferentShadesInARowEffect();
                description = "Columns with no repeated colors";
                value = 5;
                break;
            case "Column Color Variety":
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
        return "- name: " + name + "\n value: " + value + "\n description: " + description + '\n';
    }
}
