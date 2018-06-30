package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.control.Controller;
import it.polimi.ingsw.socket.SocketHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.ObjectOutputStream;
import java.rmi.RemoteException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TurnManagerSingleplayerTest {

    @Test
    public void TurnManagerSingleplayer() throws RemoteException {
        Lobby lobby = new Lobby(10, 10);
        SocketHandler socketHandler = mock(SocketHandler.class);
        lobby.createSingleplayerMatch("Archi", 1, socketHandler.getOut());
        ObjectOutputStream objectOutputStream = mock(ObjectOutputStream.class);
        when(socketHandler.getOut()).thenReturn(objectOutputStream);
        Assert.assertNotNull(lobby.getSingleplayerMatches().get("Archi").getTurnManager());
    }
}
