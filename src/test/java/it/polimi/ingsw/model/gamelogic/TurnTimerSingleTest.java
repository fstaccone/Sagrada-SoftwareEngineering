package it.polimi.ingsw.model.gamelogic;

import org.junit.Assert;
import org.junit.Test;

/**
 * All the @Test have the same name of the method they test, check the method implementation for a detailed description
 */
public class TurnTimerSingleTest {

    @Test
    public void TurnTimerSingle() {
        Lobby l = new Lobby(10000, 100000);
        MatchSingleplayer m = new MatchSingleplayer(0, "archi", 1, 10, l, null);
        TurnTimerSingle t = new TurnTimerSingle(m);
        t.run();
        Assert.assertNotNull(t);
    }
}
