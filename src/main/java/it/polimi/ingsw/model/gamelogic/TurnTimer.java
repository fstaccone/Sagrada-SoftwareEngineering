package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.model.gameobjects.PlayerMultiplayer;

import java.util.TimerTask;

public class TurnTimer extends TimerTask {

    private MatchMultiplayer match;
    private PlayerMultiplayer player;

    /**
     * Initializes the TurnTimer
     * @param match is the current match
     * @param player is the current player
     */
    public TurnTimer(MatchMultiplayer match, PlayerMultiplayer player) {
        this.match = match;
        this.player = player;
    }

    /**
     * It's called when timer is expired during the player's turn
     */
    @Override
    public void run() {
        // debug
        System.out.println("Timer is expired during the turn of the player: " + player.getName());

        match.getTurnManagerMultiplayer().setTimerExpiredTrue();
        match.goThrough();
    }
}
