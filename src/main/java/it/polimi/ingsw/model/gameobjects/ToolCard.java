package it.polimi.ingsw.model.gameobjects;


import it.polimi.ingsw.model.gameobjects.effects.Effect;

public class ToolCard{
    private String name;
    Effect effect;

    public ToolCard(String name, Effect effect) {
        this.name = name;
        this.effect = effect;
    }

    public void useCard(Player caller, Match match){
        effect.applyEffect();
    }

}
