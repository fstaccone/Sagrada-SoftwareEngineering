package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.ConnectionStatus;
import it.polimi.ingsw.model.gameobjects.PlayerMultiplayer;

import java.util.TimerTask;

public class TurnTimer extends TimerTask {

    private TurnManager turnManager;
    private PlayerMultiplayer player;

    public TurnTimer(TurnManager turnManager, PlayerMultiplayer player) {
        this.turnManager = turnManager;
        this.player = player;
    }

    @Override
    public void run() {
        // debug
        System.out.println("Player " + player.getName() + " is not READY");

        player.setStatus(ConnectionStatus.CONNECTED);

        turnManager.setExpiredTrue();

        // wakes up turn manager. It has been waiting for user action
        turnManager.notify();
    }
}
