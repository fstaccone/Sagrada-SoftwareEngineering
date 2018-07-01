package it.polimi.ingsw.control;

import it.polimi.ingsw.model.gamelogic.ConnectionStatus;
import it.polimi.ingsw.model.gamelogic.Lobby;
import it.polimi.ingsw.control.Controller;
import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.model.gameobjects.Dice;
import it.polimi.ingsw.model.gameobjects.PrivateObjectiveCard;
import it.polimi.ingsw.model.gameobjects.ToolCard;
import it.polimi.ingsw.model.gameobjects.windowpatterncards.Firelight;
import it.polimi.ingsw.model.gameobjects.windowpatterncards.RipplesOfLight;
import it.polimi.ingsw.socket.SocketHandler;
import it.polimi.ingsw.socket.requests.*;
import it.polimi.ingsw.socket.responses.*;
import it.polimi.ingsw.view.MatchObserver;
import it.polimi.ingsw.view.gui.RmiGui;
import it.polimi.ingsw.view.gui.WaitingScreenHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    /*@Test
    public void quitGame() throws RemoteException {
        Lobby lobby = new Lobby(1000, 1000);
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
    }*/

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
        UseToolCard1Request useToolCard1Request = new UseToolCard1Request(1, 0, "+", "Ancona", true);
        ToolCardEffectAppliedResponse response = (ToolCardEffectAppliedResponse)controller.handle(useToolCard1Request);
        Assert.assertTrue(response.isEffectApplied());
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

    @Test
    public void useToolCard2or3() throws RemoteException {
        Lobby lobby = new Lobby(100000, 1000);
        Controller controller = new Controller(lobby);
        //Single player
        controller.createMatch("Ancona",1,null);
        lobby.getSingleplayerMatches().get("Ancona").getPlayer().setSchemeCard(new Firelight());
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.BLUE);
        Dice d1 = new Dice(Colors.VIOLET);
        dices.add(d);
        dices.add(d1);
        dices.add(d);
        dices.add(d);
        lobby.getSingleplayerMatches().get("Ancona").getBoard().getReserve().throwDices(dices);
        lobby.getSingleplayerMatches().get("Ancona").getBoard().getReserve().getDices().get(0).setValue(3);
        lobby.getSingleplayerMatches().get("Ancona").placeDice("Ancona", 0, 0, 0);
        lobby.getSingleplayerMatches().get("Ancona").getBoard().getReserve().getDices().get(0).setValue(4);
        lobby.getSingleplayerMatches().get("Ancona").setDiceAction(false);
        lobby.getSingleplayerMatches().get("Ancona").placeDice("Ancona", 0, 1, 0);
        lobby.getSingleplayerMatches().get("Ancona").getBoard().getPickedToolCards().add(new ToolCard("Pennello per Eglomise", "tool2"));
        lobby.getSingleplayerMatches().get("Ancona").getBoard().getPickedToolCards().add(new ToolCard("Alesatore per Lamina di Rame", "tool3"));
        UseToolCard2or3Request useToolCard2or3Request = new UseToolCard2or3Request(0, 2, 0, 0, 2, 0, "Ancona", true);
        ToolCardEffectAppliedResponse response = (ToolCardEffectAppliedResponse)controller.handle(useToolCard2or3Request);
        Assert.assertTrue(response.isEffectApplied());
        Assert.assertEquals(3, lobby.getSingleplayerMatches().get("Ancona").getPlayer().getSchemeCard().getDice(2,0).getValue());
        Assert.assertFalse(lobby.getSingleplayerMatches().get("Ancona").useToolCard2or3(0, 2, 0, 0, 2, 0, "Ancona"));
        //Multi player
        lobby.addToWaitingPlayers("Archi");
        lobby.addToWaitingPlayers("Archi2");
        lobby.startMatch();
        lobby.getMultiplayerMatches().get("Archi").getPlayer("Archi").setSchemeCard(new Firelight());
        List<Dice> dices2 = new ArrayList<>();
        Dice d3 = new Dice(Colors.BLUE);
        Dice d4 = new Dice(Colors.VIOLET);
        dices2.add(d3);
        dices2.add(d4);
        dices2.add(d3);
        dices2.add(d3);
        lobby.getMultiplayerMatches().get("Archi").getBoard().getReserve().throwDices(dices2);
        lobby.getMultiplayerMatches().get("Archi").getBoard().getReserve().getDices().get(0).setValue(3);
        lobby.getMultiplayerMatches().get("Archi").placeDice("Archi", 0, 0, 0);
        lobby.getMultiplayerMatches().get("Archi").getBoard().getReserve().getDices().get(0).setValue(4);
        lobby.getMultiplayerMatches().get("Archi").setDiceAction(false);
        lobby.getMultiplayerMatches().get("Archi").placeDice("Archi", 0, 1, 0);
        lobby.getMultiplayerMatches().get("Archi").getBoard().getPickedToolCards().add(new ToolCard("Pennello per Eglomise", "tool2"));
        lobby.getMultiplayerMatches().get("Archi").getBoard().getPickedToolCards().add(new ToolCard("Alesatore per Lamina di Rame", "tool3"));
        controller.useToolCard2or3(0, 2, 0, 0, 2, 0, "Archi", false);
        Assert.assertEquals(3, lobby.getMultiplayerMatches().get("Archi").getPlayer("Archi").getSchemeCard().getDice(2,0).getValue());
        Assert.assertFalse(lobby.getMultiplayerMatches().get("Archi").useToolCard2or3(0, 2, 0, 0, 2, 0, "Archi"));
    }

    @Test
    public void useToolCard4() throws RemoteException {
        Lobby lobby = new Lobby(100000, 1000);
        Controller controller = new Controller(lobby);
        //Single player
        controller.createMatch("Ancona",1,null);
        lobby.getSingleplayerMatches().get("Ancona").getPlayer().setSchemeCard(new Firelight());
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.YELLOW);
        Dice d1 = new Dice(Colors.RED);
        Dice d2 = new Dice(Colors.BLUE);
        dices.add(d);
        dices.add(d1);
        dices.add(d2);
        dices.add(d);
        lobby.getSingleplayerMatches().get("Ancona").getBoard().getReserve().throwDices(dices);
        lobby.getSingleplayerMatches().get("Ancona").getBoard().getReserve().getDices().get(0).setValue(3);
        lobby.getSingleplayerMatches().get("Ancona").placeDice("Ancona", 0, 0, 0);
        lobby.getSingleplayerMatches().get("Ancona").getBoard().getReserve().getDices().get(0).setValue(4);
        lobby.getSingleplayerMatches().get("Ancona").setDiceAction(false);
        lobby.getSingleplayerMatches().get("Ancona").placeDice("Ancona", 0, 1, 0);
        lobby.getSingleplayerMatches().get("Ancona").setDiceAction(false);
        lobby.getSingleplayerMatches().get("Ancona").getBoard().getReserve().getDices().get(0).setValue(6);
        lobby.getSingleplayerMatches().get("Ancona").placeDice("Ancona", 0, 2, 0);
        lobby.getSingleplayerMatches().get("Ancona").getBoard().getPickedToolCards().add(new ToolCard("Lathekin", "tool4"));
        UseToolCard4Request useToolCard4Request = new UseToolCard4Request(0,0,0,2,0,2,0,1,1, "Ancona", true);
        ToolCardEffectAppliedResponse response = (ToolCardEffectAppliedResponse)controller.handle(useToolCard4Request);
        Assert.assertTrue(response.isEffectApplied());
        Assert.assertEquals(3, lobby.getSingleplayerMatches().get("Ancona").getPlayer().getSchemeCard().getDice(2,0).getValue());
        Assert.assertEquals(Colors.YELLOW, lobby.getSingleplayerMatches().get("Ancona").getPlayer().getSchemeCard().getDice(2,0).getColor());
        Assert.assertEquals(6, lobby.getSingleplayerMatches().get("Ancona").getPlayer().getSchemeCard().getDice(1,1).getValue());
        Assert.assertEquals(Colors.BLUE, lobby.getSingleplayerMatches().get("Ancona").getPlayer().getSchemeCard().getDice(1,1).getColor());
        //Multi player
        lobby.addToWaitingPlayers("Archi");
        lobby.addToWaitingPlayers("Archi2");
        lobby.startMatch();
        lobby.getMultiplayerMatches().get("Archi").getPlayer("Archi").setSchemeCard(new Firelight());
        List<Dice> dices2 = new ArrayList<>();
        Dice d3 = new Dice(Colors.YELLOW);
        Dice d4 = new Dice(Colors.RED);
        Dice d5 = new Dice(Colors.BLUE);
        dices2.add(d3);
        dices2.add(d4);
        dices2.add(d5);
        dices2.add(d3);
        lobby.getMultiplayerMatches().get("Archi").getBoard().getReserve().throwDices(dices2);
        lobby.getMultiplayerMatches().get("Archi").getBoard().getReserve().getDices().get(0).setValue(3);
        lobby.getMultiplayerMatches().get("Archi").placeDice("Archi", 0, 0, 0);
        lobby.getMultiplayerMatches().get("Archi").getBoard().getReserve().getDices().get(0).setValue(4);
        lobby.getMultiplayerMatches().get("Archi").setDiceAction(false);
        lobby.getMultiplayerMatches().get("Archi").placeDice("Archi", 0, 1, 0);
        lobby.getMultiplayerMatches().get("Archi").setDiceAction(false);
        lobby.getMultiplayerMatches().get("Archi").getBoard().getReserve().getDices().get(0).setValue(6);
        lobby.getMultiplayerMatches().get("Archi").placeDice("Archi", 0, 2, 0);
        lobby.getMultiplayerMatches().get("Archi").getBoard().getPickedToolCards().add(new ToolCard("Lathekin", "tool4"));
        controller.useToolCard4(0,0,0,2,0,2,0,1,1, "Archi", false);
        Assert.assertEquals(3, lobby.getMultiplayerMatches().get("Archi").getPlayer("Archi").getSchemeCard().getDice(2,0).getValue());
        Assert.assertEquals(Colors.YELLOW, lobby.getMultiplayerMatches().get("Archi").getPlayer("Archi").getSchemeCard().getDice(2,0).getColor());
        Assert.assertEquals(6, lobby.getMultiplayerMatches().get("Archi").getPlayer("Archi").getSchemeCard().getDice(1,1).getValue());
        Assert.assertEquals(Colors.BLUE, lobby.getMultiplayerMatches().get("Archi").getPlayer("Archi").getSchemeCard().getDice(1,1).getColor());
        Assert.assertFalse(lobby.getMultiplayerMatches().get("Archi").useToolCard4(0,0,0,2,0,2,0,1,1, "Archi"));
    }

    @Test
    public void useToolCard5() throws RemoteException {
        Lobby lobby = new Lobby(100000, 1000);
        Controller controller = new Controller(lobby);
        //Single player
        controller.createMatch("Ancona",1,null);
        lobby.getSingleplayerMatches().get("Ancona").getPlayer().setSchemeCard(new Firelight());
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.GREEN);
        Dice d1 = new Dice(Colors.RED);
        dices.add(d);
        dices.add(d1);
        lobby.getSingleplayerMatches().get("Ancona").getBoard().getReserve().throwDices(dices);
        List<Dice> list0 = new LinkedList<>();
        Dice d00 = new Dice(Colors.RED);
        d00.setValue(1);
        list0.add(d00);
        List<Dice> list1 = new LinkedList<>();
        Dice d10 = new Dice(Colors.YELLOW);
        d10.setValue(5);
        list1.add(d10);
        Dice d11 = new Dice(Colors.BLUE);
        d11.setValue(2);
        list1.add(d11);
        lobby.getSingleplayerMatches().get("Ancona").getBoard().getRoundTrack().putDices(list0, 0);
        lobby.getSingleplayerMatches().get("Ancona").getBoard().getRoundTrack().putDices(list1, 1);
        lobby.getSingleplayerMatches().get("Ancona").getBoard().getPickedToolCards().add(new ToolCard("Taglierina Circolare", "tool5"));
        MatchObserver matchObserver = mock(MatchObserver.class);
        lobby.getSingleplayerMatches().get("Ancona").observeMatchRemote(matchObserver);
        UseToolCard5Request useToolCard5Request = new UseToolCard5Request(0, 1, 2, 0, "Ancona", true);
        ToolCardEffectAppliedResponse response = (ToolCardEffectAppliedResponse)controller.handle(useToolCard5Request);
        Assert.assertTrue(response.isEffectApplied());
        Assert.assertEquals(5, lobby.getSingleplayerMatches().get("Ancona").getBoard().getReserve().getDices().get(0).getValue());
        Assert.assertEquals(Colors.YELLOW, lobby.getSingleplayerMatches().get("Ancona").getBoard().getReserve().getDices().get(0).getColor());
        //Multi player
        lobby.addToWaitingPlayers("Archi");
        lobby.addToWaitingPlayers("Archi2");
        lobby.startMatch();
        lobby.getMultiplayerMatches().get("Archi").getPlayer("Archi").setSchemeCard(new Firelight());
        lobby.getMultiplayerMatches().get("Archi2").getPlayer("Archi2").setSchemeCard(new RipplesOfLight());
        List<Dice> dices2 = new ArrayList<>();
        Dice d3 = new Dice(Colors.GREEN);
        Dice d4 = new Dice(Colors.RED);
        dices2.add(d3);
        dices2.add(d4);
        lobby.getMultiplayerMatches().get("Archi").getBoard().getReserve().throwDices(dices2);
        List<Dice> list00 = new LinkedList<>();
        Dice da = new Dice(Colors.RED);
        da.setValue(1);
        list00.add(da);
        List<Dice> list10 = new LinkedList<>();
        Dice db = new Dice(Colors.YELLOW);
        db.setValue(5);
        list10.add(db);
        Dice dc = new Dice(Colors.BLUE);
        dc.setValue(2);
        list10.add(dc);
        lobby.getMultiplayerMatches().get("Archi").getBoard().getRoundTrack().putDices(list00, 0);
        lobby.getMultiplayerMatches().get("Archi").getBoard().getRoundTrack().putDices(list10, 1);
        lobby.getMultiplayerMatches().get("Archi").getBoard().getPickedToolCards().add(new ToolCard("Taglierina Circolare", "tool5"));
        MatchObserver matchObserver3 = mock(MatchObserver.class);
        //MatchObserver matchObserver2 = mock(MatchObserver.class);
        lobby.getMultiplayerMatches().get("Archi").observeMatchRemote(matchObserver3, "Archi");
        //matchMultiplayer.observeMatchRemote(matchObserver2, "Archi2");
        lobby.getMultiplayerMatches().get("Archi").setToolAction(false);
        controller.useToolCard5(0, 1, 2, 0, "Archi", false);
        Assert.assertEquals(5, lobby.getMultiplayerMatches().get("Archi").getBoard().getReserve().getDices().get(1).getValue());
        Assert.assertEquals(Colors.YELLOW, lobby.getMultiplayerMatches().get("Archi").getBoard().getReserve().getDices().get(1).getColor());
        Assert.assertFalse(lobby.getMultiplayerMatches().get("Archi").useToolCard5(0, 1, 2, 0, "Archi"));
    }

    @Test
    public void useToolCard6() throws RemoteException {
        Lobby lobby = new Lobby(100000, 1000);
        Controller controller = new Controller(lobby);
        //Single player
        controller.createMatch("Ancona",1,null);
        lobby.getSingleplayerMatches().get("Ancona").getPlayer().setSchemeCard(new Firelight());
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.VIOLET);
        Dice d1 = new Dice(Colors.BLUE);
        dices.add(d);
        dices.add(d1);
        lobby.getSingleplayerMatches().get("Ancona").getBoard().getReserve().throwDices(dices);
        lobby.getSingleplayerMatches().get("Ancona").getBoard().getPickedToolCards().add(new ToolCard("Pennello per Pasta Salda", "tool6"));
        lobby.getSingleplayerMatches().get("Ancona").setToolAction(false);
        UseToolCard6Request useToolCard6Request = new UseToolCard6Request(0,1,"Ancona", true);
        ToolCardEffectAppliedResponse response = (ToolCardEffectAppliedResponse)controller.handle(useToolCard6Request);
        Assert.assertTrue(response.isEffectApplied());
        Assert.assertEquals(1, lobby.getSingleplayerMatches().get("Ancona").getBoard().getReserve().getDices().size());
        Assert.assertEquals(Colors.BLUE, lobby.getSingleplayerMatches().get("Ancona").getBoard().getReserve().getDices().get(0).getColor());
        Assert.assertFalse(controller.useToolCard6(0,1,"Ancona", true));
        //Multi player
        lobby.addToWaitingPlayers("Archi");
        lobby.addToWaitingPlayers("Archi2");
        lobby.startMatch();
        lobby.getMultiplayerMatches().get("Archi").getPlayer("Archi").setSchemeCard(new Firelight());
        List<Dice> dices2 = new ArrayList<>();
        Dice d3 = new Dice(Colors.VIOLET);
        Dice d4 = new Dice(Colors.BLUE);
        dices2.add(d3);
        dices2.add(d4);
        lobby.getMultiplayerMatches().get("Archi").getBoard().getReserve().throwDices(dices2);
        lobby.getMultiplayerMatches().get("Archi").getBoard().getPickedToolCards().add(new ToolCard("Pennello per Pasta Salda", "tool6"));
        lobby.getMultiplayerMatches().get("Archi").setToolAction(false);
        Assert.assertTrue(controller.useToolCard6(0,1,"Archi", false));
        Assert.assertEquals(2, lobby.getMultiplayerMatches().get("Archi").getBoard().getReserve().getDices().size());
        Assert.assertEquals(Colors.BLUE, lobby.getMultiplayerMatches().get("Archi").getBoard().getReserve().getDices().get(1).getColor());
        Assert.assertFalse(controller.useToolCard6(0,1,"Archi", false));
    }

    @Test
    public void useToolCard7() throws RemoteException {
        Lobby lobby = new Lobby(100000, 1000);
        Controller controller = new Controller(lobby);
        //Single player
        controller.createMatch("Ancona",1,null);
        lobby.getSingleplayerMatches().get("Ancona").getPlayer().setSchemeCard(new Firelight());
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.BLUE);
        Dice d1 = new Dice(Colors.RED);
        Dice d2 = new Dice(Colors.BLUE);
        dices.add(d);
        dices.add(d1);
        dices.add(d2);
        lobby.getSingleplayerMatches().get("Ancona").getBoard().getReserve().throwDices(dices);
        lobby.getSingleplayerMatches().get("Ancona").getBoard().getPickedToolCards().add(new ToolCard("Martelletto", "tool7"));
        lobby.getSingleplayerMatches().get("Ancona").setToolAction(false);
        lobby.getSingleplayerMatches().get("Ancona").getPlayer().setTurnsLeft(1);
        UseToolCard7Request useToolCard7Request = new UseToolCard7Request(0,"Ancona", true);
        ToolCardEffectAppliedResponse response = (ToolCardEffectAppliedResponse) controller.handle(useToolCard7Request);
        Assert.assertTrue(response.isEffectApplied());
        Assert.assertEquals(2, lobby.getSingleplayerMatches().get("Ancona").getBoard().getReserve().getDices().size());
        Assert.assertFalse(controller.useToolCard7(0,"Ancona", true));
        //Multi player
        lobby.addToWaitingPlayers("Archi");
        lobby.addToWaitingPlayers("Archi2");
        lobby.startMatch();
        lobby.getMultiplayerMatches().get("Archi").getPlayer("Archi").setSchemeCard(new Firelight());
        List<Dice> dices2 = new ArrayList<>();
        Dice d3 = new Dice(Colors.BLUE);
        Dice d4 = new Dice(Colors.RED);
        Dice d5 = new Dice(Colors.BLUE);
        dices2.add(d3);
        dices2.add(d4);
        dices2.add(d5);
        lobby.getMultiplayerMatches().get("Archi").getBoard().getReserve().throwDices(dices2);
        lobby.getMultiplayerMatches().get("Archi").getBoard().getPickedToolCards().add(new ToolCard("Martelletto", "tool7"));
        lobby.getMultiplayerMatches().get("Archi").setToolAction(false);
        lobby.getMultiplayerMatches().get("Archi").getPlayer("Archi").setTurnsLeft(1);
        Assert.assertTrue(controller.useToolCard7(0,"Archi", false));
        Assert.assertEquals(3, lobby.getMultiplayerMatches().get("Archi").getBoard().getReserve().getDices().size());
        Assert.assertFalse(controller.useToolCard7(0,"Archi", false));
    }

    @Test
    public void useToolCard8() throws RemoteException {
        Lobby lobby = new Lobby(100000, 1000);
        Controller controller = new Controller(lobby);
        //Single player
        controller.createMatch("Ancona",1,null);
        lobby.getSingleplayerMatches().get("Ancona").getPlayer().setSchemeCard(new Firelight());
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.RED);
        Dice d1 = new Dice(Colors.RED);
        Dice d2 = new Dice(Colors.BLUE);
        dices.add(d);
        dices.add(d1);
        dices.add(d2);
        lobby.getSingleplayerMatches().get("Ancona").setDiceAction(true);
        lobby.getSingleplayerMatches().get("Ancona").getBoard().getReserve().throwDices(dices);
        lobby.getSingleplayerMatches().get("Ancona").getBoard().getPickedToolCards().add(new ToolCard("Tenaglia a Rotelle", "tool8"));
        UseToolCard8Request useToolCard8Request = new UseToolCard8Request(0, "Ancona", true);
        ToolCardEffectAppliedResponse response = (ToolCardEffectAppliedResponse)controller.handle(useToolCard8Request);
        Assert.assertTrue(response.isEffectApplied());
        Assert.assertFalse(lobby.getSingleplayerMatches().get("Ancona").isDiceAction());
        Assert.assertFalse(controller.useToolCard8(0, "Ancona", true));
        //Multi player
        lobby.addToWaitingPlayers("Archi");
        lobby.addToWaitingPlayers("Archi2");
        lobby.startMatch();
        lobby.getMultiplayerMatches().get("Archi").getPlayer("Archi").setSchemeCard(new Firelight());
        List<Dice> dices2 = new ArrayList<>();
        Dice d3 = new Dice(Colors.RED);
        Dice d4 = new Dice(Colors.RED);
        Dice d5 = new Dice(Colors.BLUE);
        dices2.add(d3);
        dices2.add(d4);
        dices2.add(d5);
        lobby.getMultiplayerMatches().get("Archi").setDiceAction(true);
        lobby.getMultiplayerMatches().get("Archi").getBoard().getReserve().throwDices(dices2);
        lobby.getMultiplayerMatches().get("Archi").getBoard().getPickedToolCards().add(new ToolCard("Tenaglia a Rotelle", "tool8"));
        Assert.assertTrue(controller.useToolCard8(0, "Archi", false));
        Assert.assertFalse(lobby.getMultiplayerMatches().get("Archi").isDiceAction());
        Assert.assertFalse(controller.useToolCard8(0, "Archi", false));
    }

    @Test
    public void useToolCard9() throws RemoteException {
        Lobby lobby = new Lobby(100000, 1000);
        Controller controller = new Controller(lobby);
        //Single player
        controller.createMatch("Ancona",1,null);
        lobby.getSingleplayerMatches().get("Ancona").getPlayer().setSchemeCard(new Firelight());
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.YELLOW);
        Dice d1 = new Dice(Colors.RED);
        Dice d2 = new Dice(Colors.BLUE);
        dices.add(d);
        dices.add(d1);
        dices.add(d2);
        lobby.getSingleplayerMatches().get("Ancona").getBoard().getReserve().throwDices(dices);
        lobby.getSingleplayerMatches().get("Ancona").placeDice("Archi", 1, 1, 0);
        lobby.getSingleplayerMatches().get("Ancona").getBoard().getPickedToolCards().add(new ToolCard("Riga in Sughero", "tool9"));
        lobby.getSingleplayerMatches().get("Ancona").setDiceAction(false);
        UseToolCard9Request useToolCard9Request = new UseToolCard9Request(0,1,2,2, "Ancona", true);
        ToolCardEffectAppliedResponse response = (ToolCardEffectAppliedResponse)controller.handle(useToolCard9Request);
        Assert.assertTrue(response.isEffectApplied());
        Assert.assertFalse(controller.useToolCard9(0,1,2,2, "Ancona", true));
        //Multi player
        lobby.addToWaitingPlayers("Archi");
        lobby.addToWaitingPlayers("Archi2");
        lobby.startMatch();
        lobby.getMultiplayerMatches().get("Archi").getPlayer("Archi").setSchemeCard(new Firelight());
        List<Dice> dices2 = new ArrayList<>();
        Dice d3 = new Dice(Colors.YELLOW);
        Dice d4 = new Dice(Colors.RED);
        Dice d5 = new Dice(Colors.BLUE);
        dices2.add(d3);
        dices2.add(d4);
        dices2.add(d5);
        lobby.getMultiplayerMatches().get("Archi").getBoard().getReserve().throwDices(dices2);
        lobby.getMultiplayerMatches().get("Archi").placeDice("Archi", 1, 1, 0);
        lobby.getMultiplayerMatches().get("Archi").getBoard().getPickedToolCards().add(new ToolCard("Riga in Sughero", "tool9"));
        lobby.getMultiplayerMatches().get("Archi").setDiceAction(false);
        Assert.assertTrue(controller.useToolCard9(0,1,2,2, "Archi", false));
        Assert.assertFalse(controller.useToolCard9(0,1,2,2, "Archi", false));
    }

    @Test
    public void useToolCard10() throws RemoteException {
        Lobby lobby = new Lobby(100000, 1000);
        Controller controller = new Controller(lobby);
        //Single player
        controller.createMatch("Ancona",1,null);
        lobby.getSingleplayerMatches().get("Ancona").getPlayer().setSchemeCard(new Firelight());
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.GREEN);
        Dice d1 = new Dice(Colors.BLUE);
        dices.add(d);
        dices.add(d1);
        lobby.getSingleplayerMatches().get("Ancona").getBoard().getReserve().throwDices(dices);
        lobby.getSingleplayerMatches().get("Ancona").getBoard().getPickedToolCards().add(new ToolCard("Tampone Diamantato", "tool10"));
        UseToolCard10Request useToolCard10Request = new UseToolCard10Request(0,1, "Ancona", true);
        ToolCardEffectAppliedResponse response = (ToolCardEffectAppliedResponse)controller.handle(useToolCard10Request);
        Assert.assertTrue(response.isEffectApplied());
        Assert.assertFalse(controller.useToolCard10(0,1, "Ancona", true));
        //Multi player
        lobby.addToWaitingPlayers("Archi");
        lobby.addToWaitingPlayers("Archi2");
        lobby.startMatch();
        lobby.getMultiplayerMatches().get("Archi").getPlayer("Archi").setSchemeCard(new Firelight());
        List<Dice> dices2 = new ArrayList<>();
        Dice d3 = new Dice(Colors.GREEN);
        Dice d4 = new Dice(Colors.BLUE);
        dices2.add(d3);
        dices2.add(d4);
        lobby.getMultiplayerMatches().get("Archi").getBoard().getReserve().throwDices(dices2);
        lobby.getMultiplayerMatches().get("Archi").getBoard().getPickedToolCards().add(new ToolCard("Tampone Diamantato", "tool10"));
        Assert.assertTrue(controller.useToolCard10(0,1, "Archi", false));
        Assert.assertFalse(controller.useToolCard10(0,1, "Archi", false));
    }

    @Test
    public void useToolCard11() throws RemoteException {
        Lobby lobby = new Lobby(100000, 1000);
        Controller controller = new Controller(lobby);
        //Single player
        controller.createMatch("Ancona",1,null);
        lobby.getSingleplayerMatches().get("Ancona").getPlayer().setSchemeCard(new Firelight());
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.VIOLET);
        Dice d1 = new Dice(Colors.BLUE);
        dices.add(d);
        dices.add(d1);
        lobby.getSingleplayerMatches().get("Ancona").getBoard().getReserve().throwDices(dices);
        lobby.getSingleplayerMatches().get("Ancona").getBoard().getPickedToolCards().add(new ToolCard("Diluente per Pasta Salda", "tool11"));
        UseToolCard11Request useToolCard11Request = new UseToolCard11Request(0,1, "Ancona", true);
        ToolCardEffectAppliedResponse response = (ToolCardEffectAppliedResponse)controller.handle(useToolCard11Request);
        Assert.assertTrue(response.isEffectApplied());
        Assert.assertFalse(controller.useToolCard11(0,1, "Ancona", true));
        //Multi player
        lobby.addToWaitingPlayers("Archi");
        lobby.addToWaitingPlayers("Archi2");
        lobby.startMatch();
        lobby.getMultiplayerMatches().get("Archi").getPlayer("Archi").setSchemeCard(new Firelight());
        List<Dice> dices2 = new ArrayList<>();
        Dice d3 = new Dice(Colors.VIOLET);
        Dice d4 = new Dice(Colors.BLUE);
        dices2.add(d3);
        dices2.add(d4);
        lobby.getMultiplayerMatches().get("Archi").getBoard().getReserve().throwDices(dices2);
        lobby.getMultiplayerMatches().get("Archi").getBoard().getPickedToolCards().add(new ToolCard("Diluente per Pasta Salda", "tool11"));
        Assert.assertTrue(controller.useToolCard11(0,1, "Archi", false));
        Assert.assertFalse(controller.useToolCard11(0,1, "Archi", false));
    }

    @Test
    public void useCard12() throws RemoteException {
        Lobby lobby = new Lobby(100000, 1000);
        Controller controller = new Controller(lobby);
        //Single player
        controller.createMatch("Ancona",1,null);
        lobby.getSingleplayerMatches().get("Ancona").getPlayer().setSchemeCard(new Firelight());
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.BLUE);
        Dice d1 = new Dice(Colors.RED);
        Dice d2 = new Dice(Colors.YELLOW);
        Dice d3 = new Dice(Colors.RED);
        dices.add(d);
        dices.add(d1);
        dices.add(d2);
        dices.add(d3);
        lobby.getSingleplayerMatches().get("Ancona").getBoard().getReserve().throwDices(dices);
        lobby.getSingleplayerMatches().get("Ancona").getBoard().getPickedToolCards().add(new ToolCard("Taglierina Manuale", "tool12"));
        List<Dice> list0 = new LinkedList<>();
        Dice d00 = new Dice(Colors.RED);
        d00.setValue(1);
        list0.add(d00);
        List<Dice> list1 = new LinkedList<>();
        Dice d10 = new Dice(Colors.YELLOW);
        d10.setValue(5);
        list1.add(d10);
        Dice d11 = new Dice(Colors.BLUE);
        d11.setValue(2);
        list1.add(d11);
        lobby.getSingleplayerMatches().get("Ancona").getBoard().getRoundTrack().putDices(list0, 0);
        lobby.getSingleplayerMatches().get("Ancona").getBoard().getRoundTrack().putDices(list1, 1);
        lobby.getSingleplayerMatches().get("Ancona").getBoard().getReserve().getDices().get(1).setValue(3);
        lobby.getSingleplayerMatches().get("Ancona").placeDice("Ancona",1,0,0);
        lobby.getSingleplayerMatches().get("Ancona").setDiceAction(false);
        lobby.getSingleplayerMatches().get("Ancona").getBoard().getReserve().getDices().get(1).setValue(5);
        lobby.getSingleplayerMatches().get("Ancona").placeDice("Ancona",1,1,0);
        lobby.getSingleplayerMatches().get("Ancona").setDiceAction(false);
        lobby.getSingleplayerMatches().get("Ancona").getBoard().getReserve().getDices().get(1).setValue(4);
        lobby.getSingleplayerMatches().get("Ancona").placeDice("Ancona",1,2,0);
        UseToolCard12Request useToolCard12Request = new UseToolCard12Request(0,1,0,0,0,2,1,2,0,0,1,"Ancona", true);
        ToolCardEffectAppliedResponse response = (ToolCardEffectAppliedResponse)controller.handle(useToolCard12Request);
        Assert.assertTrue(response.isEffectApplied());
        Assert.assertFalse(controller.useToolCard12(0,1,0,0,0,2,1,2,0,0,1,"Ancona", true));
        //Multi player
        lobby.addToWaitingPlayers("Archi");
        lobby.addToWaitingPlayers("Archi2");
        lobby.startMatch();
        lobby.getMultiplayerMatches().get("Archi").getPlayer("Archi").setSchemeCard(new Firelight());
        List<Dice> dices2 = new ArrayList<>();
        Dice d4 = new Dice(Colors.BLUE);
        Dice d5 = new Dice(Colors.RED);
        Dice d6 = new Dice(Colors.YELLOW);
        Dice d7 = new Dice(Colors.RED);
        dices2.add(d4);
        dices2.add(d5);
        dices2.add(d6);
        dices2.add(d7);
        lobby.getMultiplayerMatches().get("Archi").getBoard().getReserve().throwDices(dices2);
        lobby.getMultiplayerMatches().get("Archi").getBoard().getPickedToolCards().add(new ToolCard("Taglierina Manuale", "tool12"));
        List<Dice> list00 = new LinkedList<>();
        Dice d01 = new Dice(Colors.RED);
        d01.setValue(1);
        list00.add(d01);
        List<Dice> list10 = new LinkedList<>();
        Dice d12 = new Dice(Colors.YELLOW);
        d12.setValue(5);
        list10.add(d12);
        Dice d13 = new Dice(Colors.BLUE);
        d13.setValue(2);
        list10.add(d13);
        lobby.getMultiplayerMatches().get("Archi").getBoard().getRoundTrack().putDices(list00, 0);
        lobby.getMultiplayerMatches().get("Archi").getBoard().getRoundTrack().putDices(list10, 1);
        lobby.getMultiplayerMatches().get("Archi").getBoard().getReserve().getDices().get(1).setValue(3);
        lobby.getMultiplayerMatches().get("Archi").placeDice("Archi",1,0,0);
        lobby.getMultiplayerMatches().get("Archi").setDiceAction(false);
        lobby.getMultiplayerMatches().get("Archi").getBoard().getReserve().getDices().get(1).setValue(5);
        lobby.getMultiplayerMatches().get("Archi").placeDice("Archi",1,1,0);
        lobby.getMultiplayerMatches().get("Archi").setDiceAction(false);
        lobby.getMultiplayerMatches().get("Archi").getBoard().getReserve().getDices().get(1).setValue(4);
        lobby.getMultiplayerMatches().get("Archi").placeDice("Archi",1,2,0);
        Assert.assertTrue(controller.useToolCard12(0,1,0,0,0,2,1,2,0,0,1,"Archi", false));
        Assert.assertFalse(controller.useToolCard12(0,1,0,0,0,2,1,2,0,0,1,"Archi", false));
    }

    @Test
    public void removeMatch() throws RemoteException {
        Lobby lobby = new Lobby(100000, 1000);
        Controller controller = new Controller(lobby);
        lobby.addToWaitingPlayers("Archi");
        lobby.addToWaitingPlayers("Archi2");
        lobby.startMatch();
        controller.removeMatch("Archi2");
        Assert.assertNull(lobby.getMultiplayerMatches().get("Archi2"));
    }

    @Test
    public void observeMatch() throws RemoteException {
        Lobby lobby = new Lobby(1000, 1000);
        Controller controller = new Controller(lobby);
        //Single player
        controller.createMatch("Ancona",1,null);
        RmiGui rmiGui = mock(RmiGui.class);
        controller.observeMatch("Ancona", rmiGui, true, false);
        Assert.assertNotNull(lobby.getSingleplayerMatches().get("Ancona").getObserverRmi());
        //Reconnection
        lobby.addToWaitingPlayers("Archi");
        lobby.addToWaitingPlayers("Archi2");
        lobby.addToWaitingPlayers("Archi3");
        lobby.startMatch();
        lobby.getMultiplayerMatches().get("Archi").getPlayer("Archi2").setPrivateObjectiveCard(new PrivateObjectiveCard(Colors.BLUE));
        RmiGui rmiGui2 = mock(RmiGui.class);
        controller.observeMatch("Archi2", rmiGui2, false, true);
    }

    @Test
    public void observeLobby() throws RemoteException {
        Lobby lobby = new Lobby(1000, 1000);
        Controller controller = new Controller(lobby);
        WaitingScreenHandler waitingScreenHandler = mock(WaitingScreenHandler.class);
        controller.observeLobby("Archi", waitingScreenHandler);
        Assert.assertEquals(1, lobby.getRemoteObservers().keySet().size());
    }

    /*
    @Test
    public void handleAddPlayerRequest() throws IOException {
        Lobby lobby = new Lobby(1000, 1000);
        Controller controller = new Controller(lobby);
        SocketHandler socketHandler = mock(SocketHandler.class);
        SocketHandler socketHandler2 = mock(SocketHandler.class);
        SocketHandler socketHandler3 = mock(SocketHandler.class);
        ObjectOutputStream objectOutputStream = mock(ObjectOutputStream.class);
        controller.addSocketHandler(socketHandler);
        controller.addSocketHandler(socketHandler2);
        controller.addSocketHandler(socketHandler3);
        when(socketHandler.getOut()).thenReturn(objectOutputStream);
        when(socketHandler2.getOut()).thenReturn(objectOutputStream);
        when(socketHandler3.getOut()).thenReturn(objectOutputStream);
        AddPlayerRequest request = new AddPlayerRequest("Archi");
        controller.handle(request);
    }
    */

    @Test
    public void handleCheckUsernameRequest() throws RemoteException {
        Lobby lobby = new Lobby(1000, 1000);
        Controller controller = new Controller(lobby);
        lobby.addUsername("Archi");
        lobby.addUsername("Archi2");
        lobby.addToWaitingPlayers("Archi");
        lobby.addToWaitingPlayers("Archi2");
        lobby.startMatch();
        CheckUsernameRequest checkUsernameRequest = new CheckUsernameRequest("Archi");
        Response response = controller.handle(checkUsernameRequest);
        NameAlreadyTakenResponse response1 = (NameAlreadyTakenResponse)response;
        ConnectionStatus connectionStatus = response1.getStatus();
        Assert.assertEquals(ConnectionStatus.CONNECTED, connectionStatus);
    }

    @Test
    public void handleCreateMatchRequest() throws IOException {
        CreateMatchRequest createMatchRequest = new CreateMatchRequest("Ancona", 5);
        Lobby lobby = new Lobby(1000, 1000);
        Controller controller = new Controller(lobby);
        SocketHandler socketHandler = mock(SocketHandler.class);
        controller.addSocketHandler(socketHandler);
        controller.handle(createMatchRequest);
        Assert.assertNotNull(lobby.getSingleplayerMatches().get("Ancona"));
    }

    /*@Test
    public void addPlayerRequest() throws RemoteException {
        AddPlayerRequest addPlayerRequest = new AddPlayerRequest("Ancona");
        Lobby lobby = new Lobby(1000, 1000);
        Controller controller = new Controller(lobby);
        SocketHandler socketHandler = mock(SocketHandler.class);
        SocketHandler socketHandler2 = mock(SocketHandler.class);
        controller.addSocketHandler(socketHandler);
        controller.addSocketHandler(socketHandler2);
        ObjectOutputStream objectOutputStream = mock(ObjectOutputStream.class);
        when(socketHandler2.getOut()).thenReturn(objectOutputStream);
        controller.handle(addPlayerRequest);
    }*/

    @Test
    public void handleRemoveFromWaitingPlayersRequest() throws RemoteException {
        RemoveFromWaitingPlayersRequest removeFromWaitingPlayersRequest = new RemoveFromWaitingPlayersRequest("Ancona");
        Lobby lobby = new Lobby(1000, 1000);
        Controller controller = new Controller(lobby);
        lobby.addToWaitingPlayers("Ancona");
        lobby.addToWaitingPlayers("Bovalino");
        controller.handle(removeFromWaitingPlayersRequest);
        Assert.assertEquals(1, lobby.getWaitingPlayers().size());
    }

    @Test
    public void handleGoThroughRequest() throws RemoteException {
        GoThroughRequest goThroughRequest = new GoThroughRequest("Ancona", true);
        Lobby lobby = new Lobby(1000, 1000);
        Controller controller = new Controller(lobby);
        controller.createMatch("Ancona", 5, null);
        controller.handle(goThroughRequest);
        Assert.assertNotNull(lobby.getSingleplayerMatches().get("Ancona"));
    }

    @Test
    public void handleChooseWindowRequest() throws RemoteException {
        ChooseWindowRequest chooseWindowRequest = new ChooseWindowRequest("Ancona", 2, true);
        Lobby lobby = new Lobby(1000, 1000);
        Controller controller = new Controller(lobby);
        controller.createMatch("Ancona", 5, null);
        lobby.getSingleplayerMatches().get("Ancona").windowsToBeProposed();
        controller.handle(chooseWindowRequest);
        Assert.assertNotNull(lobby.getSingleplayerMatches().get("Ancona").getPlayer().getSchemeCard());
    }

    @Test
    public void handlePlaceDiceRequest() throws RemoteException {
        PlaceDiceRequest placeDiceRequest = new PlaceDiceRequest(0, 1, 0, "Ancona", true);
        Lobby lobby = new Lobby(100000, 1000);
        Controller controller = new Controller(lobby);
        controller.createMatch("Ancona",1,null);
        lobby.getSingleplayerMatches().get("Ancona").getPlayer().setSchemeCard(new Firelight());
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.BLUE);
        dices.add(d);
        lobby.getSingleplayerMatches().get("Ancona").getBoard().getReserve().throwDices(dices);
        controller.handle(placeDiceRequest);
        Assert.assertEquals(Colors.BLUE, lobby.getSingleplayerMatches().get("Ancona").getPlayer().getSchemeCard().getWindow()[1][0].getDice().getColor());
    }

    @Test
    public void handlePlaceDiceTool11Request() throws RemoteException {
        PlaceDiceTool11Request placeDiceTool11Request = new PlaceDiceTool11Request(1, 0, "Ancona", true);
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
        DicePlacedResponse response = (DicePlacedResponse) controller.handle(placeDiceTool11Request);
        Assert.assertTrue(response.isDone());
        Assert.assertEquals(Colors.BLUE, lobby.getSingleplayerMatches().get("Ancona").getPlayer().getSchemeCard().getWindow()[1][0].getDice().getColor());
    }

    @Test
    public void choosePrivateCard() throws RemoteException {
        Lobby lobby = new Lobby(100000, 1000);
        Controller controller = new Controller(lobby);
        controller.createMatch("Ancona",1,null);
        PrivateCardChosenRequest privateCardChosenRequest = new PrivateCardChosenRequest("Ancona", 1);
        controller.handle(privateCardChosenRequest);
        Assert.assertTrue(lobby.getSingleplayerMatches().get("Ancona").isPrivateCardChosen());
    }

    @Test
    public void handleTerminateMatchRequest() throws RemoteException {
        TerminateMatchRequest terminateMatchRequest = new TerminateMatchRequest("Ancona");
        Lobby lobby = new Lobby(100000, 1000);
        Controller controller = new Controller(lobby);
        lobby.addToWaitingPlayers("Ancona");
        lobby.addToWaitingPlayers("Bovalino");
        lobby.startMatch();
        controller.handle(terminateMatchRequest);
        Assert.assertNull(lobby.getSingleplayerMatches().get("Ancona"));
    }

}
