package it.polimi.ingsw.model.gameobjects;


import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gameobjects.effects.*;

public class ToolCard extends Card {

    private String toolID;
    private Effect effect;
    private String description;
    private Colors color;

    public ToolCard(String name, String toolID) {
        super(name);
        switch (this.name) {
            case "Pinza Sgrossatrice":
                effect = new IncrDecrDiceValueEffect();
                description = "After drafting you can increase or decrease the value of the drafted die by 1\n" +
                        "1 may not change to 6 or 6 to 1";
                color = Colors.VIOLET;
                break;
            case "Pennello per Eglomise":
                effect = new MoveDiceIgnoringColorRestrEffect();
                description = "Move any one die in your window ignoring color restrictions\n" +
                        "You must obey all other placement restrictions";
                color = Colors.BLUE;
                break;
            case "Alesatore per Lamina di Rame":
                effect = new MoveDiceIgnoringValueRestrEffect();
                description = "";
                color = Colors.NONE;
                break;
            case "Lathekin":
                effect = new MoveTwoDicesEffect();
                description = "Move exactly two dice obeying all placement restrictions";
                color = Colors.YELLOW;
                break;
            case "Taglierina Circolare":
                effect = new ExchangeDiceRoundTrackEffect();
                description = "";
                color = Colors.NONE;
                break;
            case "Pennello per Pasta Salda":
                effect = new ReRollDiceEffect();
                description = "";
                color = Colors.NONE;
                break;
            case "Martelletto":
                effect = new ReRollAllReserveDicesEffect();
                description = "";
                color = Colors.NONE;
                break;
            case "Tenaglia a Rotelle":
                effect = new ChooseAnotherDiceEffect();
                description = "";
                color = Colors.NONE;
                break;
            case "Riga in Sughero":
                effect = new MoveDiceNotAdjacentToAnotherEffect();
                description = "";
                color = Colors.NONE;
                break;
            case "Tampone Diamantato":
                effect = new UpsideDownDiceEffect();
                description = "";
                color = Colors.NONE;
                break;
            case "Diluente per Pasta Salda":
                effect = new SubstituteDiceFromBagEffect();
                description = "";
                color = Colors.NONE;
                break;
            case "Taglierina Manuale":
                effect = new MoveTwoDicesColorRoundTrackEffect();
                description = "";
                color = Colors.NONE;
                break;
            default:
                System.out.println("Invalid card from ToolCard");
                break;
        }
        this.toolID = toolID;
    }

    public boolean useCard(Player caller, Match match) {

        return effect.applyEffect(caller, match);
    }

    public String getName() {
        return name;
    }

    String getToolID() {
        return toolID;
    }

    @Override
    public String toString() {
        return toolID + ": " + name + "\ncolor: " + color + "\ndescription: " + description + "\n";
    }
}
