package it.polimi.ingsw.model.gameobjects;


import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gameobjects.effects.*;

public class ToolCard extends Card{

    private Effect effect;

    public ToolCard(String name) {
       super(name);
        switch (this.name) {
            case "Pinza Sgrossatrice":
                this.effect= new IncrDecrDiceValueEffect();
                break;
            case "Pennello per Eglomise":
                this.effect= new MoveDiceIgnoringColorRestrEffect();
                break;
            case "Alesatore per Lamina di Rame":
                this.effect= new MoveDiceIgnoringValueRestrEffect();
                break;
            case "Lathekin":
                this.effect= new MoveTwoDicesEffect();
                break;
            case "Taglierina Circolare":
                this.effect= new ExchangeDiceRoundTrackEffect();
                break;
            case "Pennello per Pasta Salda":
                this.effect= new ReRollDiceEffect();
                break;
            case "Martelletto":
                this.effect= new ReRollAllReserveDicesEffect();
                break;
            case "Tenaglia a Rotelle":
                this.effect= new ChooseAnotherDiceEffect();
                break;
            case "Riga in Sughero":
                this.effect= new MoveDiceNotAdjacentToAnotherEffect();
                break;
            case "Tampone Diamantato":
                this.effect= new UpsideDownDiceEffect();
                break;
            case "Diluente per Pasta Salda":
                this.effect= new SubstituteDiceFromBagEffect();
                break;
            case "Taglierina Manuale":
                this.effect= new MoveTwoDicesColorRoundTrackEffect();
                break;
            default: this.name = "Invalid card from ToolCard";
                break;
        }
    }

    public void useCard(Player caller, Match match){//CONSIDERIAMO PER ESEMPIO LA TOOLCARD4

        effect.applyEffect( caller,  match);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "{" +
                "cardname='" + name + '\'' +
                '}';
    }
}
