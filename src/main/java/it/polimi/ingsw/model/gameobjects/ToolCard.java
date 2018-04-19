package it.polimi.ingsw.model.gameobjects;


import it.polimi.ingsw.model.gameobjects.effects.Effect;

public class ToolCard{
    private String name;
    Effect effect;

    public void setEffect( Effect effect ) {//da file json che associa ad ogni ToolCard uno e un solo effetto al momento della creazione
        this.effect = effect;
    }
}
