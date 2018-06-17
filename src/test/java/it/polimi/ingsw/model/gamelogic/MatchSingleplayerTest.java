package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.Lobby;
import it.polimi.ingsw.MatchObserver;
import it.polimi.ingsw.Server;
import it.polimi.ingsw.control.Controller;
import it.polimi.ingsw.model.gameobjects.*;
import it.polimi.ingsw.model.gameobjects.windowpatterncards.Firelight;
import it.polimi.ingsw.socket.SocketHandler;
import it.polimi.ingsw.socket.responses.GameStartedResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

    @Test
    public void calculateFinalScore(){
        Board board = mock(Board.class);
        RoundTrack roundTrack = mock(RoundTrack.class);
        PublicObjectiveCard publicObjectiveCard = new PublicObjectiveCard("Varietà di colore");
        List<PublicObjectiveCard> list = new ArrayList<>();
        list.add(publicObjectiveCard);
        List<PrivateObjectiveCard> list1 = new ArrayList<>();
        list1.add(new PrivateObjectiveCard(Colors.BLUE));
        when(board.getRoundTrack()).thenReturn(roundTrack);
        when(board.getRoundTrack().sumForSinglePlayer()).thenReturn(5);
        when(board.getPickedPublicObjectiveCards()).thenReturn(list);
        Lobby lobby = mock(Lobby.class);
        MatchSingleplayer matchSingleplayer = new MatchSingleplayer(0, "Archi", 1, 10, lobby, null);
        matchSingleplayer.getPlayer().setSchemeCard(new Firelight());
        matchSingleplayer.getPlayer().setPrivateObjectiveCards(list1);
        matchSingleplayer.calculateFinalScore();
        Assert.assertEquals(-60, matchSingleplayer.getPlayer().getPoints());
    }

    @Test
    public void placeDice(){
        Board board = mock(Board.class);
        Lobby lobby = mock(Lobby.class);
        MatchSingleplayer matchSingleplayer = new MatchSingleplayer(0, "Archi", 1, 10, lobby, null);
        matchSingleplayer.getPlayer().setSchemeCard(new Firelight());
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.BLUE);
        d.setValue(3);
        dices.add(d);
        dices.add(d);
        dices.add(d);
        Reserve reserve = mock(Reserve.class);
        when(board.getReserve()).thenReturn(reserve);
        when(board.getReserve().getDices()).thenReturn(dices);
        matchSingleplayer.board = board;
        matchSingleplayer.placeDice("Archi", 0, 0, 0);
        Assert.assertEquals(d, matchSingleplayer.getPlayer().getSchemeCard().getDice(0,0));
        Assert.assertFalse(matchSingleplayer.placeDice("Archi", 0, 0, 0));
    }

    @Test
    public void placeDiceTool11(){
        Lobby lobby = mock(Lobby.class);
        MatchSingleplayer matchSingleplayer = new MatchSingleplayer(0, "Archi", 1, 10, lobby, null);
        matchSingleplayer.getPlayer().setSchemeCard(new Firelight());
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.BLUE);
        d.setValue(3);
        matchSingleplayer.getPlayer().setDiceFromBag(d);
        matchSingleplayer.placeDiceTool11("Archi", 0, 0);
        Assert.assertEquals(d, matchSingleplayer.getPlayer().getSchemeCard().getDice(0,0));
        matchSingleplayer.setDiceAction(true);
        Assert.assertEquals(false, matchSingleplayer.placeDiceTool11("Archi", 0, 0));
    }

    @Test
    public void useToolCard1(){
        Lobby lobby = mock(Lobby.class);
        MatchSingleplayer matchSingleplayer = new MatchSingleplayer(0, "Archi", 1, 10, lobby, null);
        matchSingleplayer.getPlayer().setSchemeCard(new Firelight());
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.BLUE);
        Dice d1 = new Dice(Colors.VIOLET);
        d.setValue(3);
        d1.setValue(4);
        dices.add(d);
        dices.add(d1);
        matchSingleplayer.getBoard().getReserve().throwDices(dices);
        matchSingleplayer.getBoard().getReserve().getDices().get(0).setValue(4);
        matchSingleplayer.getBoard().getPickedToolCards().add(new ToolCard("Pinza Sgrossatrice", "tool1"));
        matchSingleplayer.useToolCard1(1, 0, "+", "Archi");
        Assert.assertEquals(5, matchSingleplayer.getBoard().getReserve().getDices().get(0).getValue());
        matchSingleplayer.setToolAction(true);
        Assert.assertEquals(false, matchSingleplayer.useToolCard1(1,0,"+","Archi"));
    }

    @Test
    public void useToolCard2or3(){
        Lobby lobby = mock(Lobby.class);
        MatchSingleplayer matchSingleplayer = new MatchSingleplayer(0, "Archi", 1, 10, lobby, null);
        matchSingleplayer.getPlayer().setSchemeCard(new Firelight());
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.BLUE);
        Dice d1 = new Dice(Colors.VIOLET);
        dices.add(d);
        dices.add(d1);
        dices.add(d);
        dices.add(d);
        matchSingleplayer.getBoard().getReserve().throwDices(dices);
        matchSingleplayer.getBoard().getReserve().getDices().get(0).setValue(3);
        matchSingleplayer.placeDice("Archi", 0, 0, 0);
        matchSingleplayer.getBoard().getReserve().getDices().get(0).setValue(4);
        matchSingleplayer.setDiceAction(false);
        matchSingleplayer.placeDice("Archi", 0, 1, 0);
        System.out.println(matchSingleplayer.getPlayer().getSchemeCard().toString());
        matchSingleplayer.getBoard().getPickedToolCards().add(new ToolCard("Pennello per Eglomise", "tool2"));
        matchSingleplayer.getBoard().getPickedToolCards().add(new ToolCard("Alesatore per Lamina di Rame", "tool3"));
        matchSingleplayer.useToolCard2or3(0, 2, 0, 0, 2, 0, "Archi");
        Assert.assertEquals(3, matchSingleplayer.getPlayer().getSchemeCard().getDice(2,0).getValue());
        Assert.assertFalse(matchSingleplayer.useToolCard2or3(0, 2, 0, 0, 2, 0, "Archi"));
    }

    @Test
    public void useToolCard4(){
        Lobby lobby = mock(Lobby.class);
        MatchSingleplayer matchSingleplayer = new MatchSingleplayer(0, "Archi", 1, 10, lobby, null);
        matchSingleplayer.getPlayer().setSchemeCard(new Firelight());
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.YELLOW);
        Dice d1 = new Dice(Colors.RED);
        Dice d2 = new Dice(Colors.BLUE);
        dices.add(d);
        dices.add(d1);
        dices.add(d2);
        dices.add(d);
        matchSingleplayer.getBoard().getReserve().throwDices(dices);
        matchSingleplayer.getBoard().getReserve().getDices().get(0).setValue(3);
        matchSingleplayer.placeDice("Archi", 0, 0, 0);
        matchSingleplayer.getBoard().getReserve().getDices().get(0).setValue(4);
        matchSingleplayer.setDiceAction(false);
        matchSingleplayer.placeDice("Archi", 0, 1, 0);
        matchSingleplayer.setDiceAction(false);
        matchSingleplayer.getBoard().getReserve().getDices().get(0).setValue(6);
        matchSingleplayer.placeDice("Archi", 0, 2, 0);
        matchSingleplayer.getBoard().getPickedToolCards().add(new ToolCard("Lathekin", "tool4"));
        matchSingleplayer.useToolCard4(0,0,0,2,0,2,0,1,1, "Archi");
        Assert.assertEquals(3, matchSingleplayer.getPlayer().getSchemeCard().getDice(2,0).getValue());
        Assert.assertEquals(Colors.YELLOW, matchSingleplayer.getPlayer().getSchemeCard().getDice(2,0).getColor());
        Assert.assertEquals(6, matchSingleplayer.getPlayer().getSchemeCard().getDice(1,1).getValue());
        Assert.assertEquals(Colors.BLUE, matchSingleplayer.getPlayer().getSchemeCard().getDice(1,1).getColor());
    }

    @Test
    public void useToolCard5(){
        Lobby lobby = mock(Lobby.class);
        MatchSingleplayer matchSingleplayer = new MatchSingleplayer(0, "Archi", 1, 10, lobby, null);
        matchSingleplayer.getPlayer().setSchemeCard(new Firelight());
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.GREEN);
        Dice d1 = new Dice(Colors.RED);
        dices.add(d);
        dices.add(d1);
        matchSingleplayer.getBoard().getReserve().throwDices(dices);
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
        matchSingleplayer.getBoard().getRoundTrack().putDices(list0, 0);
        matchSingleplayer.getBoard().getRoundTrack().putDices(list1, 1);
        matchSingleplayer.getBoard().getPickedToolCards().add(new ToolCard("Taglierina Circolare", "tool5"));
        MatchObserver matchObserver = mock(MatchObserver.class);
        matchSingleplayer.observeMatchRemote(matchObserver);
        matchSingleplayer.useToolCard5(0, 1, 2, 0, "Archi");
        Assert.assertEquals(5, matchSingleplayer.getBoard().getReserve().getDices().get(0).getValue());
        Assert.assertEquals(Colors.YELLOW, matchSingleplayer.getBoard().getReserve().getDices().get(0).getColor());
    }

    @Test
    public void useToolCard6(){
        Lobby lobby = mock(Lobby.class);
        MatchSingleplayer matchSingleplayer = new MatchSingleplayer(0, "Archi", 1, 10, lobby, null);
        matchSingleplayer.getPlayer().setSchemeCard(new Firelight());
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.VIOLET);
        Dice d1 = new Dice(Colors.BLUE);
        dices.add(d);
        dices.add(d1);
        matchSingleplayer.getBoard().getReserve().throwDices(dices);
        matchSingleplayer.getBoard().getPickedToolCards().add(new ToolCard("Pennello per Pasta Salda", "tool6"));
        matchSingleplayer.setToolAction(false);
        Assert.assertTrue(matchSingleplayer.useToolCard6(0,1,"Archi"));
        Assert.assertEquals(1, matchSingleplayer.getBoard().getReserve().getDices().size());
        Assert.assertEquals(Colors.BLUE, matchSingleplayer.getBoard().getReserve().getDices().get(0).getColor());
        Assert.assertFalse(matchSingleplayer.useToolCard6(0,1,"Archi"));
    }

    @Test
    public void useToolCard7(){
        Lobby lobby = mock(Lobby.class);
        MatchSingleplayer matchSingleplayer = new MatchSingleplayer(0, "Archi", 1, 10, lobby, null);
        matchSingleplayer.getPlayer().setSchemeCard(new Firelight());
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.BLUE);
        Dice d1 = new Dice(Colors.RED);
        Dice d2 = new Dice(Colors.BLUE);
        dices.add(d);
        dices.add(d1);
        dices.add(d2);
        matchSingleplayer.getBoard().getReserve().throwDices(dices);
        matchSingleplayer.getBoard().getPickedToolCards().add(new ToolCard("Martelletto", "tool7"));
        matchSingleplayer.setToolAction(false);
        matchSingleplayer.getPlayer().setTurnsLeft(1);
        Assert.assertTrue(matchSingleplayer.useToolCard7(0,"Archi"));
        Assert.assertEquals(2, matchSingleplayer.getBoard().getReserve().getDices().size());
        Assert.assertFalse(matchSingleplayer.useToolCard7(0,"Archi"));
    }

    @Test
    public void useToolCard8(){
        Lobby lobby = mock(Lobby.class);
        MatchSingleplayer matchSingleplayer = new MatchSingleplayer(0, "Archi", 1, 10, lobby, null);
        matchSingleplayer.getPlayer().setSchemeCard(new Firelight());
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.RED);
        Dice d1 = new Dice(Colors.RED);
        Dice d2 = new Dice(Colors.BLUE);
        dices.add(d);
        dices.add(d1);
        dices.add(d2);
        matchSingleplayer.setDiceAction(true);
        matchSingleplayer.getBoard().getReserve().throwDices(dices);
        matchSingleplayer.getBoard().getPickedToolCards().add(new ToolCard("Tenaglia a Rotelle", "tool8"));
        Assert.assertTrue(matchSingleplayer.useToolCard8(0, "Archi"));
        Assert.assertFalse(matchSingleplayer.isDiceAction());
        Assert.assertFalse(matchSingleplayer.useToolCard8(0, "Archi"));
    }

    @Test
    public void useToolCard9(){
        Lobby lobby = mock(Lobby.class);
        MatchSingleplayer matchSingleplayer = new MatchSingleplayer(0, "Archi", 1, 10, lobby, null);
        matchSingleplayer.getPlayer().setSchemeCard(new Firelight());
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.YELLOW);
        Dice d1 = new Dice(Colors.RED);
        Dice d2 = new Dice(Colors.BLUE);
        dices.add(d);
        dices.add(d1);
        dices.add(d2);
        matchSingleplayer.getBoard().getReserve().throwDices(dices);
        matchSingleplayer.placeDice("Archi", 1, 1, 0);
        matchSingleplayer.getBoard().getPickedToolCards().add(new ToolCard("Riga in Sughero", "tool9"));
        matchSingleplayer.setDiceAction(false);
        Assert.assertTrue(matchSingleplayer.useToolCard9(0,1,2,2, "Archi"));
        Assert.assertFalse(matchSingleplayer.useToolCard9(0,1,2,2, "Archi"));
    }

    @Test
    public void useToolCard10(){
        Lobby lobby = mock(Lobby.class);
        MatchSingleplayer matchSingleplayer = new MatchSingleplayer(0, "Archi", 1, 10, lobby, null);
        matchSingleplayer.getPlayer().setSchemeCard(new Firelight());
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.GREEN);
        Dice d1 = new Dice(Colors.BLUE);
        dices.add(d);
        dices.add(d1);
        matchSingleplayer.getBoard().getReserve().throwDices(dices);
        matchSingleplayer.getBoard().getPickedToolCards().add(new ToolCard("Tampone Diamantato", "tool10"));
        Assert.assertTrue(matchSingleplayer.useToolCard10(0,1, "Archi"));
        Assert.assertFalse(matchSingleplayer.useToolCard10(0,1, "Archi"));
    }

    @Test
    public void useToolCard11(){
        Lobby lobby = mock(Lobby.class);
        MatchSingleplayer matchSingleplayer = new MatchSingleplayer(0, "Archi", 1, 10, lobby, null);
        matchSingleplayer.getPlayer().setSchemeCard(new Firelight());
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.VIOLET);
        Dice d1 = new Dice(Colors.BLUE);
        dices.add(d);
        dices.add(d1);
        matchSingleplayer.getBoard().getReserve().throwDices(dices);
        matchSingleplayer.getBoard().getPickedToolCards().add(new ToolCard("Diluente per Pasta Salda", "tool11"));
        Assert.assertTrue(matchSingleplayer.useToolCard11(0,1, "Archi"));
        Assert.assertFalse(matchSingleplayer.useToolCard11(0,1, "Archi"));
    }

    @Test
    public void useToolCard12(){
        Lobby lobby = mock(Lobby.class);
        MatchSingleplayer matchSingleplayer = new MatchSingleplayer(0, "Archi", 1, 10, lobby, null);
        matchSingleplayer.getPlayer().setSchemeCard(new Firelight());
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.BLUE);
        Dice d1 = new Dice(Colors.RED);
        Dice d2 = new Dice(Colors.YELLOW);
        Dice d3 = new Dice(Colors.RED);
        dices.add(d);
        dices.add(d1);
        dices.add(d2);
        dices.add(d3);
        matchSingleplayer.getBoard().getReserve().throwDices(dices);
        matchSingleplayer.getBoard().getPickedToolCards().add(new ToolCard("Taglierina Manuale", "tool12"));
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
        matchSingleplayer.getBoard().getRoundTrack().putDices(list0, 0);
        matchSingleplayer.getBoard().getRoundTrack().putDices(list1, 1);
        matchSingleplayer.getBoard().getReserve().getDices().get(1).setValue(3);
        matchSingleplayer.placeDice("Archi",1,0,0);
        matchSingleplayer.setDiceAction(false);
        matchSingleplayer.getBoard().getReserve().getDices().get(1).setValue(5);
        matchSingleplayer.placeDice("Archi",1,1,0);
        matchSingleplayer.setDiceAction(false);
        matchSingleplayer.getBoard().getReserve().getDices().get(1).setValue(4);
        matchSingleplayer.placeDice("Archi",1,2,0);
        System.out.println(matchSingleplayer.getPlayer().getSchemeCard().toString());
        Assert.assertTrue(matchSingleplayer.useToolCard12(0,1,0,0,0,2,1,2,0,0,1,"Archi"));
        System.out.println(matchSingleplayer.getPlayer().getSchemeCard().toString());
        Assert.assertFalse(matchSingleplayer.useToolCard12(0,1,0,0,0,2,1,2,0,0,1,"Archi"));
    }
}
