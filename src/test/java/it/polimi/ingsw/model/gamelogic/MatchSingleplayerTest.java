package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.Lobby;
import it.polimi.ingsw.Server;
import it.polimi.ingsw.control.Controller;
import it.polimi.ingsw.model.gameobjects.PlayerSingleplayer;
import it.polimi.ingsw.socket.SocketHandler;
import it.polimi.ingsw.socket.responses.GameStartedResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;

public class MatchSingleplayerTest {

    @Test
    public void match() throws IOException {
        Lobby lobby = new Lobby(10, 10);
        Controller c = new Controller(lobby);
        SocketHandler socketHandler = mock(SocketHandler.class);
        PlayerSingleplayer singleplayer = new PlayerSingleplayer("Archi");
        lobby.createSingleplayerMatch("Archi", 1, socketHandler.getOut());
        ObjectOutputStream objectOutputStream = mock(ObjectOutputStream.class);
        when(socketHandler.getOut()).thenReturn(objectOutputStream);
        Assert.assertNotNull(lobby.getSingleplayerMatches().get("Archi"));
        Assert.assertEquals("Archi", lobby.getSingleplayerMatches().get("Archi").getPlayer().getName());
        lobby.getSingleplayerMatches().get("Archi").windowsToBeProposed();
        lobby.getSingleplayerMatches().get("Archi").setWindowPatternCard("Archi", 0);
    }

}
