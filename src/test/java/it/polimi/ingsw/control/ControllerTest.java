package it.polimi.ingsw.control;

import it.polimi.ingsw.model.gamelogic.ConnectionStatus;
import it.polimi.ingsw.model.gamelogic.Lobby;
import it.polimi.ingsw.control.Controller;
import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.model.gameobjects.Dice;
import it.polimi.ingsw.model.gameobjects.ToolCard;
import it.polimi.ingsw.model.gameobjects.windowpatterncards.Firelight;
import it.polimi.ingsw.socket.requests.DiceColorRequest;
import it.polimi.ingsw.socket.requests.SetDiceValueRequest;
import it.polimi.ingsw.socket.responses.DiceColorResponse;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

public class ControllerTest {

    @Test
    public void Controller() throws RemoteException {
        Lobby lobby = new Lobby(100000, 10);
        Controller controller = new Controller(lobby);
        Assert.assertNotNull(controller);
    }

    @Test
    public void checkName() throws RemoteException {
        Lobby lobby = new Lobby(100000, 10);
        Controller controller = new Controller(lobby);
        controller.addPlayer("Player");
        Assert.assertEquals(ConnectionStatus.CONNECTED, controller.checkName("Player"));
    }

    @Test
    public void addPlayer() throws RemoteException {
        Lobby lobby = new Lobby(100000, 10);
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
        Lobby lobby = new Lobby(100000, 10);
        Controller controller = new Controller(lobby);
        controller.createMatch("Ancona",1,null);
        Assert.assertEquals(0, lobby.getSingleplayerMatches().get("Ancona").getMatchId());
    }

    @Test
    public void goThrough() throws RemoteException {
        Lobby lobby = new Lobby(100000, 100000);
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
        Lobby lobby = new Lobby(100000, 100000);
        Controller controller = new Controller(lobby);
        ObjectOutputStream objectOutputStream = mock(ObjectOutputStream.class);
        controller.createMatch("Ancona",1,objectOutputStream);
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
        Lobby lobby = new Lobby(100000, 100000);
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
        Lobby lobby = new Lobby(100000, 100000);
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
        Lobby lobby = new Lobby(100000, 100000);
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
        Assert.assertEquals(Colors.BLUE, diceColorResponse.getDiceColor());
    }

    @Test
    public void chooseWindow() throws RemoteException {
        Lobby lobby = new Lobby(100000, 10);
        Controller controller = new Controller(lobby);
        controller.createMatch("Ancona",1,null);
        lobby.getSingleplayerMatches().get("Ancona").windowsToBeProposed();
        controller.chooseWindow("Ancona", 0, true);
        Assert.assertNotNull(lobby.getSingleplayerMatches().get("Ancona").getPlayer().getSchemeCard());
        lobby.addToWaitingPlayers("Archi");
        lobby.addToWaitingPlayers("Bovalino");
        lobby.startMatch();
        lobby.getMultiplayerMatches().get("Archi").windowsToBeProposed();
        controller.chooseWindow("Archi", 0, false);
        Assert.assertNotNull(lobby.getMultiplayerMatches().get("Archi").getPlayer("Archi").getSchemeCard());
    }

    @Test
    public void placeDice() throws RemoteException {
        Lobby lobby = new Lobby(100000, 1000);
        Controller controller = new Controller(lobby);
        controller.createMatch("Ancona",1,null);
        lobby.getSingleplayerMatches().get("Ancona").getPlayer().setSchemeCard(new Firelight());
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.BLUE);
        dices.add(d);
        lobby.getSingleplayerMatches().get("Ancona").getBoard().getReserve().throwDices(dices);
        controller.placeDice(0, 1, 0, "Ancona", true);
        Assert.assertEquals(Colors.BLUE, lobby.getSingleplayerMatches().get("Ancona").getPlayer().getSchemeCard().getWindow()[1][0].getDice().getColor());
        lobby.addToWaitingPlayers("Archi");
        lobby.addToWaitingPlayers("Bovalino");
        lobby.startMatch();
        lobby.getMultiplayerMatches().get("Archi").getPlayer("Archi").setSchemeCard(new Firelight());
        lobby.getMultiplayerMatches().get("Archi").getBoard().getReserve().throwDices(dices);
        controller.placeDice(0, 1, 0, "Archi", false);
        Assert.assertEquals(Colors.BLUE, lobby.getMultiplayerMatches().get("Archi").getPlayer("Archi").getSchemeCard().getWindow()[1][0].getDice().getColor());
    }

    @Test
    public void placeDiceTool11() throws RemoteException {
        Lobby lobby = new Lobby(100000, 1000);
        Controller controller = new Controller(lobby);
        controller.createMatch("Ancona",1,null);
        lobby.getSingleplayerMatches().get("Ancona").getPlayer().setSchemeCard(new Firelight());
        Dice d = new Dice(Colors.BLUE);
        d.setValue(3);
        lobby.getSingleplayerMatches().get("Ancona").getPlayer().setDiceFromBag(d);
        Assert.assertEquals(Colors.BLUE, controller.askForDiceColor("Ancona",true));
        controller.setDiceValue(4, "Ancona", true);
        Assert.assertEquals(4, lobby.getSingleplayerMatches().get("Ancona").getPlayer().getDiceFromBag().getValue());
        controller.placeDiceTool11(1,0,"Ancona", true);
        Assert.assertEquals(Colors.BLUE, lobby.getSingleplayerMatches().get("Ancona").getPlayer().getSchemeCard().getWindow()[1][0].getDice().getColor());
        lobby.addToWaitingPlayers("Archi");
        lobby.addToWaitingPlayers("Bovalino");
        lobby.startMatch();
        lobby.getMultiplayerMatches().get("Archi").getPlayer("Archi").setSchemeCard(new Firelight());
        lobby.getMultiplayerMatches().get("Archi").getPlayer("Archi").setDiceFromBag(d);
        controller.placeDiceTool11(1,0,"Archi", false);
        Assert.assertEquals(Colors.BLUE, lobby.getMultiplayerMatches().get("Archi").getPlayer("Archi").getSchemeCard().getWindow()[1][0].getDice().getColor());
    }

    @Test
    public void useToolCard1() throws RemoteException {
        Lobby lobby = new Lobby(100000, 1000);
        Controller controller = new Controller(lobby);
        //Single player
        controller.createMatch("Ancona",1,null);
        lobby.getSingleplayerMatches().get("Ancona").getPlayer().setSchemeCard(new Firelight());
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.BLUE);
        Dice d1 = new Dice(Colors.VIOLET);
        d.setValue(3);
        d1.setValue(4);
        dices.add(d);
        dices.add(d1);
        lobby.getSingleplayerMatches().get("Ancona").getBoard().getReserve().throwDices(dices);
        lobby.getSingleplayerMatches().get("Ancona").getBoard().getReserve().getDices().get(0).setValue(4);
        lobby.getSingleplayerMatches().get("Ancona").getBoard().getPickedToolCards().add(new ToolCard("Pinza Sgrossatrice", "tool1"));
        controller.useToolCard1(1, 0, "+", "Ancona", true);
        Assert.assertEquals(5, lobby.getSingleplayerMatches().get("Ancona").getBoard().getReserve().getDices().get(0).getValue());
        //Multi player
        lobby.addToWaitingPlayers("Archi");
        lobby.addToWaitingPlayers("Archi2");
        lobby.startMatch();
        lobby.getMultiplayerMatches().get("Archi").getPlayer("Archi").setSchemeCard(new Firelight());
        lobby.getMultiplayerMatches().get("Archi").getBoard().getReserve().throwDices(dices);
        lobby.getMultiplayerMatches().get("Archi").getBoard().getReserve().getDices().get(0).setValue(4);
        lobby.getMultiplayerMatches().get("Archi").getBoard().getPickedToolCards().add(new ToolCard("Pinza Sgrossatrice", "tool1"));
        controller.useToolCard1(1, 0, "+", "Archi", false);
        Assert.assertEquals(5, lobby.getMultiplayerMatches().get("Archi").getBoard().getReserve().getDices().get(0).getValue());
    }




}
