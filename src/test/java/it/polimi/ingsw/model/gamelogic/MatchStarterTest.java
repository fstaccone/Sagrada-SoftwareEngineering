package it.polimi.ingsw.model.gamelogic;

import org.junit.Assert;
import org.junit.Test;

/**
 * All the @Test have the same name of the method they test, check the method implementation for a detailed description
 */
public class MatchStarterTest {

    @Test
    public void MatchStarter() {
        Lobby lobby = new Lobby(10, 10);
        MatchStarter matchStarter = new MatchStarter(lobby);
        Assert.assertNotNull(matchStarter);
    }
}
