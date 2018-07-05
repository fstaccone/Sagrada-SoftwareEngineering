package it.polimi.ingsw.model.gamelogic;

import java.util.TimerTask;

public class TurnTimer extends TimerTask {

    private MatchMultiplayer match;

    /**
     * Initializes the TurnTimer
     *
     * @param match  is the current match
     */
    public TurnTimer(MatchMultiplayer match) {
        this.match = match;
    }

    /**
     * It's called when timer is expired during the player's turn
     */
    @Override
    public void run() {
        match.getTurnManagerMultiplayer().setTimerExpiredTrue();
        match.goThrough();
    }
}
