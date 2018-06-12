package it.polimi.ingsw.model.gamelogic;

import java.util.TimerTask;

public class TurnTimerSingle extends TimerTask {
    private MatchSingleplayer match;

    /**
     * Initializes the TurnTimer
     *
     * @param match is the current match
     */
    public TurnTimerSingle(MatchSingleplayer match) {
        this.match = match;
    }

    /**
     * It's called when timer is expired during the current turn
     */
    @Override
    public void run() {
        match.getTurnManager().setExpiredTrue();
        match.goThrough();
    }

}
