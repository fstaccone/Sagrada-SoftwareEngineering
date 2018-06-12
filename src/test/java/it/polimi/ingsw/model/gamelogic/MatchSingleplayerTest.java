package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.Lobby;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class MatchSingleplayerTest {

    private Lobby lobby = mock(Lobby.class);

    @Test
    public void MatchSingleplayer(){
        int turnTime = 300000;
        int matchId = 0;
        String name ="CowboyBebop";
        //MatchSingleplayer matchSingleplayer = new MatchSingleplayer(matchId, name,3, turnTime, lobby);
       // Assert.assertNotNull(matchSingleplayer);
    }
}
