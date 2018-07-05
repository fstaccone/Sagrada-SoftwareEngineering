package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.socket.SocketHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.ObjectOutputStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * All the @Test have the same name of the method they test, check the method implementation for a detailed description
 */
public class TurnManagerSingleplayerTest {

    @Test
    public void TurnManagerSingleplayer() {
        Lobby lobby = new Lobby(10, 10);
        SocketHandler socketHandler = mock(SocketHandler.class);
        lobby.createSingleplayerMatch("Archi", 1, socketHandler.getOut());
        ObjectOutputStream objectOutputStream = mock(ObjectOutputStream.class);
        when(socketHandler.getOut()).thenReturn(objectOutputStream);
        Assert.assertNotNull(lobby.getSingleplayerMatches().get("Archi").getTurnManager());
    }
}
