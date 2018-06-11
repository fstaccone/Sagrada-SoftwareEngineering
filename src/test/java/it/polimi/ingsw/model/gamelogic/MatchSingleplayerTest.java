package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.Lobby;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class MatchSingleplayerTest {

    Lobby lobby = mock(Lobby.class);
    int turnTime = 300000;

    @Test
    public void MatchSingleplayer(){
        int matchId = 0;
        String name ="CowboyBebop";
        MatchSingleplayer matchSingleplayer = new MatchSingleplayer(matchId, name, turnTime, lobby,1);
        Assert.assertNotNull(matchSingleplayer);
    }
}
