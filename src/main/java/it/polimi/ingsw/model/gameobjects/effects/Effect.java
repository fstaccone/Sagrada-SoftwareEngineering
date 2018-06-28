package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gamelogic.Player;

import java.io.Serializable;

public interface Effect extends Serializable {
    boolean applyEffect(Player caller, Match match);
}
