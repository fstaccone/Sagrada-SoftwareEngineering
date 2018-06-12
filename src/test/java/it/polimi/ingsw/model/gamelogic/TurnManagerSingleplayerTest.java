package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.Lobby;
import it.polimi.ingsw.MatchObserver;
import org.junit.Assert;
import org.junit.Test;

import java.io.ObjectOutputStream;

import static org.mockito.Mockito.mock;

public class TurnManagerSingleplayerTest {

    @Test
    public void TurnManagerSingleplayer(){
        Lobby l = new Lobby(10,10);
        ObjectOutputStream o = mock(ObjectOutputStream.class);
        MatchSingleplayer m = new MatchSingleplayer(0, "archi", 1, 10, l, o);
        TurnManagerSingleplayer t = new TurnManagerSingleplayer(m, 10);
        Assert.assertNotNull(t);
    }
}
