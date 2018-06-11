package it.polimi.ingsw;

import it.polimi.ingsw.Lobby;
import it.polimi.ingsw.control.Controller;
import org.junit.Assert;
import org.junit.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ControllerTest {

    @Test
    public void Controller() throws RemoteException {
        Lobby lobby = new Lobby(10, 10);
        Controller controller = new Controller(lobby);
        Assert.assertNotNull(controller);
    }

    @Test
    public void checkName() throws RemoteException {
        Lobby lobby = new Lobby(10, 10);
        Controller controller = new Controller(lobby);
        controller.addPlayer("Player");
        Assert.assertEquals(ConnectionStatus.CONNECTED, controller.checkName("Player"));
    }

    @Test
    public void addPlayer() throws RemoteException {
        Lobby lobby = new Lobby(10, 10);
        Controller controller = new Controller(lobby);
        controller.addPlayer("CowboyBebop");
        Assert.assertEquals("CowboyBebop", lobby.getWaitingPlayers().get(0));
        controller.addPlayer("Ancona");
        Assert.assertEquals("Ancona", lobby.getWaitingPlayers().get(1));
        controller.removePlayer("CowboyBebop");
        Assert.assertEquals("Ancona", lobby.getWaitingPlayers().get(0));
    }

    @Test
    public void createMatch() throws RemoteException {
        Lobby lobby = new Lobby(10, 10);
        Controller controller = new Controller(lobby);
        controller.createMatch("Ancona",1,null);
        Assert.assertEquals(0, lobby.getSingleplayerMatches().get("Ancona").getMatchId());
    }

    @Test
    public void goThrough() throws RemoteException {
        Lobby lobby = new Lobby(10, 10);
        Controller controller = new Controller(lobby);
        controller.createMatch("Ancona",1,null);
        controller.goThrough("Ancona", true);
        Assert.assertEquals(true, lobby.getSingleplayerMatches().get("Ancona").isEndsTurn());
        controller.addPlayer("Bovalino");
        controller.addPlayer("Condofuri");
        lobby.startMatch();
        controller.goThrough("Bovalino", false);
        Assert.assertEquals(true, lobby.getMultiplayerMatches().get("Bovalino").isEndsTurn());
    }

}
