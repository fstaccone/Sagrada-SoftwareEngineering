package it.polimi.ingsw;

import it.polimi.ingsw.Lobby;
import it.polimi.ingsw.control.Controller;
import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.model.gameobjects.Dice;
import it.polimi.ingsw.socket.SocketHandler;
import it.polimi.ingsw.socket.requests.AddPlayerRequest;
import it.polimi.ingsw.socket.requests.DiceColorRequest;
import it.polimi.ingsw.socket.requests.SetDiceValueRequest;
import it.polimi.ingsw.socket.responses.DiceColorResponse;
import it.polimi.ingsw.socket.responses.Response;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;
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

    @Test
    public void quitGame() throws RemoteException {
        Lobby lobby = new Lobby(10, 10);
        Controller controller = new Controller(lobby);
        controller.createMatch("Ancona",1,null);
        controller.quitGame("Ancona", true);
        Assert.assertEquals(0, lobby.getSingleplayerMatches().size());
        lobby.addToWaitingPlayers("Ancona");
        lobby.addToWaitingPlayers("Bagaladi");
        lobby.addToWaitingPlayers("Condofuri");
        lobby.startMatch();
        controller.quitGame("Bagaladi", false);
        Assert.assertEquals(ConnectionStatus.DISCONNECTED, lobby.getMultiplayerMatches().get("Bagaladi").getPlayer("Bagaladi").getStatus());
        controller.reconnect("Bagaladi");
        Assert.assertEquals(ConnectionStatus.CONNECTED, lobby.getMultiplayerMatches().get("Bagaladi").getPlayer("Bagaladi").getStatus());
    }

    @Test
    public void askForDiceColor() throws RemoteException {
        Lobby lobby = new Lobby(10, 10);
        Controller controller = new Controller(lobby);
        lobby.addToWaitingPlayers("Ancona");
        lobby.addToWaitingPlayers("Bagaladi");
        lobby.addToWaitingPlayers("Condofuri");
        lobby.startMatch();
        lobby.getMultiplayerMatches().get("Ancona").getPlayer("Ancona").setDiceFromBag(new Dice(Colors.BLUE));
        Assert.assertEquals(Colors.BLUE, controller.askForDiceColor("Ancona", false));
    }

    @Test
    public void setDiceValue() throws RemoteException {
        Lobby lobby = new Lobby(10, 10);
        Controller controller = new Controller(lobby);
        lobby.addToWaitingPlayers("Ancona");
        lobby.addToWaitingPlayers("Bagaladi");
        lobby.addToWaitingPlayers("Condofuri");
        lobby.startMatch();
        lobby.getMultiplayerMatches().get("Ancona").getPlayer("Ancona").setDiceFromBag(new Dice(Colors.BLUE));
        controller.setDiceValue(4, "Ancona", false);
        Assert.assertEquals(4, lobby.getMultiplayerMatches().get("Ancona").getPlayer("Ancona").getDiceFromBag().getValue());
    }

    @Test
    public void handle() throws IOException {
        Lobby lobby = new Lobby(10, 10);
        Controller controller = new Controller(lobby);
        lobby.addToWaitingPlayers("Ancona");
        lobby.addToWaitingPlayers("Bagaladi");
        lobby.addToWaitingPlayers("Condofuri");
        lobby.startMatch();
        lobby.getMultiplayerMatches().get("Ancona").getPlayer("Ancona").setDiceFromBag(new Dice(Colors.BLUE));
        SetDiceValueRequest setDiceValueRequest = new SetDiceValueRequest(4, "Ancona", false);
        controller.handle(setDiceValueRequest);
        Assert.assertEquals(4, lobby.getMultiplayerMatches().get("Ancona").getPlayer("Ancona").getDiceFromBag().getValue());
        DiceColorRequest diceColorRequest = new DiceColorRequest("Ancona", false);
        DiceColorResponse diceColorResponse = (DiceColorResponse) controller.handle(diceColorRequest);
        Assert.assertEquals(Colors.BLUE, diceColorResponse.diceColor);
    }



}
