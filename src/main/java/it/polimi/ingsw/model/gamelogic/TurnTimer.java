package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.ConnectionStatus;
import it.polimi.ingsw.model.gameobjects.PlayerMultiplayer;

import java.util.TimerTask;

public class TurnTimer extends TimerTask {

    private MatchMultiplayer match;
    private PlayerMultiplayer player;

    public TurnTimer(MatchMultiplayer match, PlayerMultiplayer player) {
        this.match = match;
        this.player = player;
    }

    @Override
    public void run() {

        // debug
        System.out.println("Player " + player.getName() + " is not READY");

        player.setStatus(ConnectionStatus.CONNECTED);

        match.getTurnManager().setExpiredTrue();

        // wakes up turn manager. It has been waiting for user action
        synchronized (match.getLock()) {
            match.getLock().notify();
        }
    }
}
