package it.polimi.ingsw.model.gameobjects;


import it.polimi.ingsw.model.gameobjects.effects.*;

public class ToolCard{
    private String cardname;
    Effect effect;

    public ToolCard(String name) {
        this.cardname = name;
        switch (cardname) {
            case "tool1":
                this.effect= new IncrDecrDiceValueEffect();
                break;
            case "tool2":
                this.effect= new MoveDiceIgnoringColorRestrEffect();
                break;
            case "tool3":
                this.effect= new MoveDiceIgnoringValueRestrEffect();
                break;
            case "tool4":
                this.effect= new MoveTwoDicesEffect();
                break;
            case "tool5":
                this.effect= new ExchangeDiceRoundTrackEffect();
                break;
            case "tool6":
                this.effect= new ReRollDiceEffect();
                break;
            case "tool7":
                this.effect= new ReRollDiceEffect();
                break;
            case "tool8":
                this.effect= new ReRollDiceEffect();
                break;
            case "tool9":
                this.effect= new ReRollDiceEffect();
                break;
            case "tool10":
                this.effect= new ReRollDiceEffect();
                break;
            case "tool11":
                this.effect= new ReRollDiceEffect();
                break;
            case "tool12":
                this.effect= new ReRollDiceEffect();
                break;
            default: cardname = "Invalid card from ToolCard";
                break;
        }
    }

    public void useCard(Player caller, WindowPatternCard schemecard){//CONSIDERIAMO PER ESEMPIO LA TOOLCARD4

        effect.applyEffect( caller,  schemecard);
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
