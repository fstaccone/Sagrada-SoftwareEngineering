package it.polimi.ingsw;

import it.polimi.ingsw.model.gameobjects.MatchMultiplayer;

import java.util.TimerTask;

public class MatchStarter extends TimerTask{

    Lobby lobby;

    public MatchStarter(Lobby lobby) {
        this.lobby = lobby;
    }

    @Override
    public void run() {
        if(lobby.getWaitingPlayers().size() >= 2)
            lobby.startMatch();
    }
}
