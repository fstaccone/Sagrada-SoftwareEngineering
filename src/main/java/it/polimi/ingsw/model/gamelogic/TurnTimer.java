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
        System.out.println("Timer is expired during the turn of the player: " + player.getName());

        match.getTurnManager().setExpiredTrue();

        /*// wakes up turn manager. It has been waiting for user action
        synchronized (match.getLock()) {
            match.getLock().notify();
        }*/
        match.goThrough();
    }
}
