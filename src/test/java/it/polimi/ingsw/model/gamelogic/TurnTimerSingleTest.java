package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.Lobby;
import org.junit.Assert;
import org.junit.Test;

import java.io.ObjectOutputStream;

import static org.mockito.Mockito.mock;

public class TurnTimerSingleTest {

    @Test
    public void TurnTimerSingle(){
        Lobby l = new Lobby(10000, 100000);
        MatchSingleplayer m = new MatchSingleplayer(0, "archi", 1, 10, l, null);
        TurnTimerSingle t = new TurnTimerSingle(m);
        t.run();
        Assert.assertNotNull(t);
    }
}
