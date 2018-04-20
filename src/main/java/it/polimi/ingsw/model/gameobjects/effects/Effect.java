package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

import java.io.Serializable;

public interface Effect extends Serializable{
    public void applyEffect(Player caller, WindowPatternCard schemecard);
}
