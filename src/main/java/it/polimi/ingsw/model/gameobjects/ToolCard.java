package it.polimi.ingsw.model.gameobjects;


import it.polimi.ingsw.model.gameobjects.effects.*;

public class ToolCard{
    private String cardname;
    Effect effect;

    public ToolCard(String name) {
        this.cardname = name;
        switch (cardname) {
            case "1":  cardname = "tool1";
                this.effect= new IncrDecrDiceValueEffect();
                break;
            case "2":  cardname= "tool2";
                this.effect= new MoveDiceIgnoringColorRestrEffect();
                break;
            case "3":  cardname = "tool3";
                this.effect= new MoveDiceIgnoringValueRestrEffect();
                break;
            case "4":  cardname = "tool4";
                this.effect= new MoveTwoDicesEffect();
                break;
            case "5":  cardname = "tool5";
                this.effect= new ExchangeDiceRoundTrackEffect();
                break;
            case "6":  cardname = "tool6";
                this.effect= new ReRollDiceEffect();
                break;
            case "7":  cardname = "tool7";
                this.effect= new ReRollAllReserveDicesEffect();
                break;
            case "8":  cardname = "tool8";
                this.effect= new ChooseAnotherDiceEffect();
                break;
            case "9":  cardname = "tool9";
                this.effect= new MoveDiceNotAdjacentToAnotherEffect();
                break;
            case "10": cardname = "tool10";
                this.effect= new UpsideDownDiceEffect();
                break;
            case "11": cardname = "tool11";
                this.effect= new SubstituteDiceFromBagEffect();
                break;
            case "12": cardname = "tool12";
                this.effect= new MoveTwoDicesColorRoundTrackEffect();
                break;
            default: cardname = "Invalid card";
                break;
        }
    }

    public void useCard(Player caller, WindowPatternCard schemecard){//CONSIDERIAMO PER ESEMPIO LA TOOLCARD4

        effect.applyEffect( caller,  schemecard);
    }


}
