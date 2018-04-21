package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.Match;
import it.polimi.ingsw.model.gameobjects.Player;

public class MoveTwoDicesEffect implements Effect{

    public MoveTwoDicesEffect() {
    }

    @Override
    public void applyEffect(Player caller, Match match) {
        //QUI IL GIOCATORE DOVRà, ATTRAVERSO I METODI DI WINDOWPATTERNCARD DI CUI HA IL RIFERIMENTO, SCEGLIERE DUE DADI DA TOGLIERE DALLA SCHEMECARD E POI REINSERIRE)
        //BISOGNA CONSIDERARE CHE IL TUTTO VA GESTITO ATTRAVERSO N.B.   S C E L T E   DA PARTE DEL CLIENT CHE ATTRAVERSO IL CONTROLLER RICHIAMA METODI DEL PLAYER CHE AGISCONO SUL SUO STATO E QUINDI SULLA SUA CARTA SCHEMA
    }
}
