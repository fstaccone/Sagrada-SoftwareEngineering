package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.Lobby;
import it.polimi.ingsw.socket.SocketHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.ObjectOutputStream;

import static org.mockito.Mockito.mock;

public class MatchSingleplayerTest {

    private Lobby lobby = mock(Lobby.class);

    @Test
    public void MatchSingleplayer() {
        ObjectOutputStream objectOutputStream = mock(ObjectOutputStream.class);
        int turnTime = 300000;
        int matchId = 0;
        String name ="Archi";
        MatchSingleplayer matchSingleplayer = new MatchSingleplayer(matchId, name,3, turnTime, lobby, objectOutputStream);
        Assert.assertNotNull(matchSingleplayer);
        Assert.assertEquals("Archi", matchSingleplayer.getPlayer().getName());
    }

}
