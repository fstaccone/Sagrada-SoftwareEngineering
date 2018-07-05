package it.polimi.ingsw.model.gamelogic;

import org.junit.Assert;
import org.junit.Test;

public class MatchStarterTest {

    @Test
    public void MatchStarter() {
        Lobby lobby = new Lobby(10, 10);
        MatchStarter matchStarter = new MatchStarter(lobby);
        Assert.assertNotNull(matchStarter);
    }
}
