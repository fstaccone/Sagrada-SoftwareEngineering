package it.polimi.ingsw.model.gamelogic;

import org.junit.Assert;
import org.junit.Test;

public class MatchSingleplayerTest {

    @Test
    public void MatchSingleplayer(){
        int matchId = 0;
        String name ="CowboyBebop";
        MatchSingleplayer matchSingleplayer = new MatchSingleplayer(matchId, name);
        Assert.assertNotNull(matchSingleplayer);
    }
}
