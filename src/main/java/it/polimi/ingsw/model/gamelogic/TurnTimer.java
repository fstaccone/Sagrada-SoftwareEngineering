package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.ConnectionStatus;
import it.polimi.ingsw.model.gameobjects.PlayerMultiplayer;

import java.util.TimerTask;

public class TurnTimer extends TimerTask {

    private PlayerMultiplayer player;

    public TurnTimer(PlayerMultiplayer player) {
        this.player = player;
    }

    @Override
    public void run() {
        // debug
        System.out.println("Player " + player.getName() + " is not READY");
        player.expiredTimer();
        player.setStatus(ConnectionStatus.CONNECTED);
    }
}
