package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.Lobby;
import it.polimi.ingsw.MatchObserver;
import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
import it.polimi.ingsw.model.gameobjects.*;
import it.polimi.ingsw.model.gameobjects.windowpatterncards.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.ObjectOutputStream;
import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MatchMultiplayerTest {

    @Test
    public void MatchMultiplayer() {
        int matchId = 0;
        List<String> clients = new ArrayList<>();
        clients.add("client1");
        clients.add("client2");
        int turnTime = 10;
        Map<String, ObjectOutputStream> socketsOut = new HashMap<>();
        Lobby lobby = new Lobby(10, turnTime);
        MatchMultiplayer matchMultiplayer = new MatchMultiplayer(matchId, clients, turnTime, socketsOut, lobby);
        Assert.assertNotNull(matchMultiplayer);
        Assert.assertEquals(lobby, matchMultiplayer.getLobby());
    }

    @Test
    public void windowsToBeProposed(){
        List<String> clients = new ArrayList<>();
        clients.add("Archi");
        clients.add("Archi2");
        Map<String, ObjectOutputStream> socketsOut = new HashMap<>();
        Lobby lobby = new Lobby(10, 10);
        MatchMultiplayer matchMultiplayer = new MatchMultiplayer(0, clients, 10, socketsOut, lobby);
        matchMultiplayer.windowsToBeProposed();
        Assert.assertEquals(4, matchMultiplayer.getWindowsProposed().size());
    }

    @Test
    public void placeDice(){
        Board board = mock(Board.class);
        List<String> clients = new ArrayList<>();
        clients.add("Archi");
        clients.add("Archi2");
        Map<String, ObjectOutputStream> socketsOut = new HashMap<>();
        Lobby lobby = new Lobby(10, 10);
        MatchMultiplayer matchMultiplayer = new MatchMultiplayer(0, clients, 10, socketsOut, lobby);
        matchMultiplayer.getPlayer("Archi").setSchemeCard(new Firelight());
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.BLUE);
        d.setValue(3);
        dices.add(d);
        dices.add(d);
        dices.add(d);
        Reserve reserve = mock(Reserve.class);
        when(board.getReserve()).thenReturn(reserve);
        when(board.getReserve().getDices()).thenReturn(dices);
        matchMultiplayer.board = board;
        matchMultiplayer.placeDice("Archi", 0, 0, 0);
        Assert.assertEquals(d, matchMultiplayer.getPlayer("Archi").getSchemeCard().getDice(0,0));
        Assert.assertFalse(matchMultiplayer.placeDice("Archi", 0, 0, 0));
    }

    @Test
    public void placeDiceTool11(){
        List<String> clients = new ArrayList<>();
        clients.add("Archi");
        clients.add("Archi2");
        Map<String, ObjectOutputStream> socketsOut = new HashMap<>();
        Lobby lobby = new Lobby(10, 10);
        MatchMultiplayer matchMultiplayer = new MatchMultiplayer(0, clients, 10, socketsOut, lobby);
        matchMultiplayer.getPlayer("Archi").setSchemeCard(new Firelight());
        Dice d = new Dice(Colors.BLUE);
        d.setValue(3);
        matchMultiplayer.getPlayer("Archi").setDiceFromBag(d);
        matchMultiplayer.placeDiceTool11("Archi", 0, 0);
        Assert.assertEquals(d, matchMultiplayer.getPlayer("Archi").getSchemeCard().getDice(0,0));
        matchMultiplayer.setDiceAction(true);
        Assert.assertEquals(false, matchMultiplayer.placeDiceTool11("Archi", 0, 0));
    }

    @Test
    public void useToolCard1(){
        List<String> clients = new ArrayList<>();
        clients.add("Archi");
        clients.add("Archi2");
        Map<String, ObjectOutputStream> socketsOut = new HashMap<>();
        Lobby lobby = new Lobby(10, 10);
        MatchMultiplayer matchMultiplayer = new MatchMultiplayer(0, clients, 10, socketsOut, lobby);
        matchMultiplayer.getPlayer("Archi").setSchemeCard(new Firelight());
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.BLUE);
        Dice d1 = new Dice(Colors.VIOLET);
        d.setValue(3);
        d1.setValue(4);
        dices.add(d);
        dices.add(d1);
        matchMultiplayer.getBoard().getReserve().throwDices(dices);
        matchMultiplayer.getBoard().getReserve().getDices().get(0).setValue(4);
        matchMultiplayer.getBoard().getPickedToolCards().add(new ToolCard("Pinza Sgrossatrice", "tool1"));
        matchMultiplayer.useToolCard1(1, 0, "+", "Archi");
        Assert.assertEquals(5, matchMultiplayer.getBoard().getReserve().getDices().get(0).getValue());
        matchMultiplayer.setToolAction(true);
        Assert.assertEquals(false, matchMultiplayer.useToolCard1(1,0,"+","Archi"));
    }

    @Test
    public void useToolCard2or3(){
        List<String> clients = new ArrayList<>();
        clients.add("Archi");
        clients.add("Archi2");
        Map<String, ObjectOutputStream> socketsOut = new HashMap<>();
        Lobby lobby = new Lobby(10, 10);
        MatchMultiplayer matchMultiplayer = new MatchMultiplayer(0, clients, 10, socketsOut, lobby);
        matchMultiplayer.getPlayer("Archi").setSchemeCard(new Firelight());
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.BLUE);
        Dice d1 = new Dice(Colors.VIOLET);
        dices.add(d);
        dices.add(d1);
        dices.add(d);
        dices.add(d);
        matchMultiplayer.getBoard().getReserve().throwDices(dices);
        matchMultiplayer.getBoard().getReserve().getDices().get(0).setValue(3);
        matchMultiplayer.placeDice("Archi", 0, 0, 0);
        matchMultiplayer.getBoard().getReserve().getDices().get(0).setValue(4);
        matchMultiplayer.setDiceAction(false);
        matchMultiplayer.placeDice("Archi", 0, 1, 0);
        System.out.println(matchMultiplayer.getPlayer("Archi").getSchemeCard().toString());
        matchMultiplayer.getBoard().getPickedToolCards().add(new ToolCard("Pennello per Eglomise", "tool2"));
        matchMultiplayer.getBoard().getPickedToolCards().add(new ToolCard("Alesatore per Lamina di Rame", "tool3"));
        matchMultiplayer.useToolCard2or3(0, 2, 0, 0, 2, 0, "Archi");
        Assert.assertEquals(3, matchMultiplayer.getPlayer("Archi").getSchemeCard().getDice(2,0).getValue());
        Assert.assertFalse(matchMultiplayer.useToolCard2or3(0, 2, 0, 0, 2, 0, "Archi"));
    }

    @Test
    public void useToolCard4(){
        List<String> clients = new ArrayList<>();
        clients.add("Archi");
        clients.add("Archi2");
        Map<String, ObjectOutputStream> socketsOut = new HashMap<>();
        Lobby lobby = new Lobby(10, 10);
        MatchMultiplayer matchMultiplayer = new MatchMultiplayer(0, clients, 10, socketsOut, lobby);
        matchMultiplayer.getPlayer("Archi").setSchemeCard(new Firelight());
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.YELLOW);
        Dice d1 = new Dice(Colors.RED);
        Dice d2 = new Dice(Colors.BLUE);
        dices.add(d);
        dices.add(d1);
        dices.add(d2);
        dices.add(d);
        matchMultiplayer.getBoard().getReserve().throwDices(dices);
        matchMultiplayer.getBoard().getReserve().getDices().get(0).setValue(3);
        matchMultiplayer.placeDice("Archi", 0, 0, 0);
        matchMultiplayer.getBoard().getReserve().getDices().get(0).setValue(4);
        matchMultiplayer.setDiceAction(false);
        matchMultiplayer.placeDice("Archi", 0, 1, 0);
        matchMultiplayer.setDiceAction(false);
        matchMultiplayer.getBoard().getReserve().getDices().get(0).setValue(6);
        matchMultiplayer.placeDice("Archi", 0, 2, 0);
        matchMultiplayer.getBoard().getPickedToolCards().add(new ToolCard("Lathekin", "tool4"));
        matchMultiplayer.useToolCard4(0,0,0,2,0,2,0,1,1, "Archi");
        Assert.assertEquals(3, matchMultiplayer.getPlayer("Archi").getSchemeCard().getDice(2,0).getValue());
        Assert.assertEquals(Colors.YELLOW, matchMultiplayer.getPlayer("Archi").getSchemeCard().getDice(2,0).getColor());
        Assert.assertEquals(6, matchMultiplayer.getPlayer("Archi").getSchemeCard().getDice(1,1).getValue());
        Assert.assertEquals(Colors.BLUE, matchMultiplayer.getPlayer("Archi").getSchemeCard().getDice(1,1).getColor());
        Assert.assertFalse(matchMultiplayer.useToolCard4(0,0,0,2,0,2,0,1,1, "Archi"));
    }

    @Test
    public void useToolCard5(){
        List<String> clients = new ArrayList<>();
        clients.add("Archi");
        clients.add("Archi2");
        Map<String, ObjectOutputStream> socketsOut = new HashMap<>();
        Lobby lobby = new Lobby(10, 10);
        MatchMultiplayer matchMultiplayer = new MatchMultiplayer(0, clients, 10, socketsOut, lobby);
        matchMultiplayer.getPlayer("Archi").setSchemeCard(new Firelight());
        matchMultiplayer.getPlayer("Archi2").setSchemeCard(new RipplesOfLight());
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.GREEN);
        Dice d1 = new Dice(Colors.RED);
        dices.add(d);
        dices.add(d1);
        matchMultiplayer.getBoard().getReserve().throwDices(dices);
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
        matchMultiplayer.getBoard().getRoundTrack().putDices(list0, 0);
        matchMultiplayer.getBoard().getRoundTrack().putDices(list1, 1);
        matchMultiplayer.getBoard().getPickedToolCards().add(new ToolCard("Taglierina Circolare", "tool5"));
        MatchObserver matchObserver = mock(MatchObserver.class);
        //MatchObserver matchObserver2 = mock(MatchObserver.class);
        matchMultiplayer.observeMatchRemote(matchObserver, "Archi");
        //matchMultiplayer.observeMatchRemote(matchObserver2, "Archi2");
        matchMultiplayer.setToolAction(false);
        matchMultiplayer.useToolCard5(0, 1, 2, 0, "Archi");
        Assert.assertEquals(5, matchMultiplayer.getBoard().getReserve().getDices().get(1).getValue());
        Assert.assertEquals(Colors.YELLOW, matchMultiplayer.getBoard().getReserve().getDices().get(1).getColor());
        Assert.assertFalse(matchMultiplayer.useToolCard5(0, 1, 2, 0, "Archi"));
    }

    @Test
    public void useToolCard6(){
        List<String> clients = new ArrayList<>();
        clients.add("Archi");
        clients.add("Archi2");
        Map<String, ObjectOutputStream> socketsOut = new HashMap<>();
        Lobby lobby = new Lobby(10, 10);
        MatchMultiplayer matchMultiplayer = new MatchMultiplayer(0, clients, 10, socketsOut, lobby);
        matchMultiplayer.getPlayer("Archi").setSchemeCard(new Firelight());
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.VIOLET);
        Dice d1 = new Dice(Colors.BLUE);
        dices.add(d);
        dices.add(d1);
        matchMultiplayer.getBoard().getReserve().throwDices(dices);
        matchMultiplayer.getBoard().getPickedToolCards().add(new ToolCard("Pennello per Pasta Salda", "tool6"));
        matchMultiplayer.setToolAction(false);
        Assert.assertTrue(matchMultiplayer.useToolCard6(0,1,"Archi"));
        Assert.assertEquals(2, matchMultiplayer.getBoard().getReserve().getDices().size());
        Assert.assertEquals(Colors.BLUE, matchMultiplayer.getBoard().getReserve().getDices().get(1).getColor());
        Assert.assertFalse(matchMultiplayer.useToolCard6(0,1,"Archi"));
    }

    @Test
    public void useToolCard7(){
        List<String> clients = new ArrayList<>();
        clients.add("Archi");
        clients.add("Archi2");
        Map<String, ObjectOutputStream> socketsOut = new HashMap<>();
        Lobby lobby = new Lobby(10, 10);
        MatchMultiplayer matchMultiplayer = new MatchMultiplayer(0, clients, 10, socketsOut, lobby);
        matchMultiplayer.getPlayer("Archi").setSchemeCard(new Firelight());
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.BLUE);
        Dice d1 = new Dice(Colors.RED);
        Dice d2 = new Dice(Colors.BLUE);
        dices.add(d);
        dices.add(d1);
        dices.add(d2);
        matchMultiplayer.getBoard().getReserve().throwDices(dices);
        matchMultiplayer.getBoard().getPickedToolCards().add(new ToolCard("Martelletto", "tool7"));
        matchMultiplayer.setToolAction(false);
        matchMultiplayer.getPlayer("Archi").setTurnsLeft(1);
        Assert.assertTrue(matchMultiplayer.useToolCard7(0,"Archi"));
        Assert.assertEquals(3, matchMultiplayer.getBoard().getReserve().getDices().size());
        Assert.assertFalse(matchMultiplayer.useToolCard7(0,"Archi"));
    }

    @Test
    public void useToolCard8(){
        List<String> clients = new ArrayList<>();
        clients.add("Archi");
        clients.add("Archi2");
        Map<String, ObjectOutputStream> socketsOut = new HashMap<>();
        Lobby lobby = new Lobby(10, 10);
        MatchMultiplayer matchMultiplayer = new MatchMultiplayer(0, clients, 10, socketsOut, lobby);
        matchMultiplayer.getPlayer("Archi").setSchemeCard(new Firelight());
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.RED);
        Dice d1 = new Dice(Colors.RED);
        Dice d2 = new Dice(Colors.BLUE);
        dices.add(d);
        dices.add(d1);
        dices.add(d2);
        matchMultiplayer.setDiceAction(true);
        matchMultiplayer.getBoard().getReserve().throwDices(dices);
        matchMultiplayer.getBoard().getPickedToolCards().add(new ToolCard("Tenaglia a Rotelle", "tool8"));
        Assert.assertTrue(matchMultiplayer.useToolCard8(0, "Archi"));
        Assert.assertFalse(matchMultiplayer.isDiceAction());
        Assert.assertFalse(matchMultiplayer.useToolCard8(0, "Archi"));
    }

    @Test
    public void useToolCard9(){
        List<String> clients = new ArrayList<>();
        clients.add("Archi");
        clients.add("Archi2");
        Map<String, ObjectOutputStream> socketsOut = new HashMap<>();
        Lobby lobby = new Lobby(10, 10);
        MatchMultiplayer matchMultiplayer = new MatchMultiplayer(0, clients, 10, socketsOut, lobby);
        matchMultiplayer.getPlayer("Archi").setSchemeCard(new Firelight());
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.YELLOW);
        Dice d1 = new Dice(Colors.RED);
        Dice d2 = new Dice(Colors.BLUE);
        dices.add(d);
        dices.add(d1);
        dices.add(d2);
        matchMultiplayer.getBoard().getReserve().throwDices(dices);
        matchMultiplayer.placeDice("Archi", 1, 1, 0);
        matchMultiplayer.getBoard().getPickedToolCards().add(new ToolCard("Riga in Sughero", "tool9"));
        matchMultiplayer.setDiceAction(false);
        Assert.assertTrue(matchMultiplayer.useToolCard9(0,1,2,2, "Archi"));
        Assert.assertFalse(matchMultiplayer.useToolCard9(0,1,2,2, "Archi"));
    }

    @Test
    public void useToolCard10(){
        List<String> clients = new ArrayList<>();
        clients.add("Archi");
        clients.add("Archi2");
        Map<String, ObjectOutputStream> socketsOut = new HashMap<>();
        Lobby lobby = new Lobby(10, 10);
        MatchMultiplayer matchMultiplayer = new MatchMultiplayer(0, clients, 10, socketsOut, lobby);
        matchMultiplayer.getPlayer("Archi").setSchemeCard(new Firelight());
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.GREEN);
        Dice d1 = new Dice(Colors.BLUE);
        dices.add(d);
        dices.add(d1);
        matchMultiplayer.getBoard().getReserve().throwDices(dices);
        matchMultiplayer.getBoard().getPickedToolCards().add(new ToolCard("Tampone Diamantato", "tool10"));
        Assert.assertTrue(matchMultiplayer.useToolCard10(0,1, "Archi"));
        Assert.assertFalse(matchMultiplayer.useToolCard10(0,1, "Archi"));
    }

    @Test
    public void useToolCard11(){
        List<String> clients = new ArrayList<>();
        clients.add("Archi");
        clients.add("Archi2");
        Map<String, ObjectOutputStream> socketsOut = new HashMap<>();
        Lobby lobby = new Lobby(10, 10);
        MatchMultiplayer matchMultiplayer = new MatchMultiplayer(0, clients, 10, socketsOut, lobby);
        matchMultiplayer.getPlayer("Archi").setSchemeCard(new Firelight());
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.VIOLET);
        Dice d1 = new Dice(Colors.BLUE);
        dices.add(d);
        dices.add(d1);
        matchMultiplayer.getBoard().getReserve().throwDices(dices);
        matchMultiplayer.getBoard().getPickedToolCards().add(new ToolCard("Diluente per Pasta Salda", "tool11"));
        Assert.assertTrue(matchMultiplayer.useToolCard11(0,1, "Archi"));
        Assert.assertFalse(matchMultiplayer.useToolCard11(0,1, "Archi"));
    }

    @Test
    public void useToolCard12(){
        List<String> clients = new ArrayList<>();
        clients.add("Archi");
        clients.add("Archi2");
        Map<String, ObjectOutputStream> socketsOut = new HashMap<>();
        Lobby lobby = new Lobby(10, 10);
        MatchMultiplayer matchMultiplayer = new MatchMultiplayer(0, clients, 10, socketsOut, lobby);
        matchMultiplayer.getPlayer("Archi").setSchemeCard(new Firelight());
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.BLUE);
        Dice d1 = new Dice(Colors.RED);
        Dice d2 = new Dice(Colors.YELLOW);
        Dice d3 = new Dice(Colors.RED);
        dices.add(d);
        dices.add(d1);
        dices.add(d2);
        dices.add(d3);
        matchMultiplayer.getBoard().getReserve().throwDices(dices);
        matchMultiplayer.getBoard().getPickedToolCards().add(new ToolCard("Taglierina Manuale", "tool12"));
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
        matchMultiplayer.getBoard().getRoundTrack().putDices(list0, 0);
        matchMultiplayer.getBoard().getRoundTrack().putDices(list1, 1);
        matchMultiplayer.getBoard().getReserve().getDices().get(1).setValue(3);
        matchMultiplayer.placeDice("Archi",1,0,0);
        matchMultiplayer.setDiceAction(false);
        matchMultiplayer.getBoard().getReserve().getDices().get(1).setValue(5);
        matchMultiplayer.placeDice("Archi",1,1,0);
        matchMultiplayer.setDiceAction(false);
        matchMultiplayer.getBoard().getReserve().getDices().get(1).setValue(4);
        matchMultiplayer.placeDice("Archi",1,2,0);
        System.out.println(matchMultiplayer.getPlayer("Archi").getSchemeCard().toString());
        Assert.assertTrue(matchMultiplayer.useToolCard12(0,1,0,0,0,2,1,2,0,0,1,"Archi"));
        System.out.println(matchMultiplayer.getPlayer("Archi").getSchemeCard().toString());
        Assert.assertFalse(matchMultiplayer.useToolCard12(0,1,0,0,0,2,1,2,0,0,1,"Archi"));
    }

    @Test
    public void setWindowPatternCard(){
        List<String> clients = new ArrayList<>();
        clients.add("Archi");
        clients.add("Archi2");
        Map<String, ObjectOutputStream> socketsOut = new HashMap<>();
        Lobby lobby = new Lobby(10, 10);
        MatchMultiplayer matchMultiplayer = new MatchMultiplayer(0, clients, 10, socketsOut, lobby);
        matchMultiplayer.windowsToBeProposed();
        WindowPatternCard schemeCard = matchMultiplayer.getWindowsProposed().get(0);
        matchMultiplayer.setWindowPatternCard("Archi", 0);
        Assert.assertEquals(schemeCard, matchMultiplayer.getPlayer("Archi").getSchemeCard());
    }

    @Test
    public void calculateFinalScore(){
        List<String> clients = new ArrayList<>();
        clients.add("Archi");
        clients.add("Archi2");
        Map<String, ObjectOutputStream> socketsOut = new HashMap<>();
        Lobby lobby = new Lobby(10, 10);
        MatchMultiplayer matchMultiplayer = new MatchMultiplayer(0, clients, 10, socketsOut, lobby);
        matchMultiplayer.getPlayer("Archi").setSchemeCard(new Firelight());
        matchMultiplayer.getPlayer("Archi").setNumFavorTokens(4);
        matchMultiplayer.getPlayer("Archi").setPrivateObjectiveCard(new PrivateObjectiveCard(Colors.RED));
        matchMultiplayer.getPlayer("Archi2").setSchemeCard(new SymphonyOfLight());
        matchMultiplayer.getPlayer("Archi2").setNumFavorTokens(4);
        matchMultiplayer.getPlayer("Archi2").setPrivateObjectiveCard(new PrivateObjectiveCard(Colors.BLUE));
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.BLUE);
        Dice d1 = new Dice(Colors.RED);
        Dice d2 = new Dice(Colors.YELLOW);
        Dice d3 = new Dice(Colors.RED);
        dices.add(d);
        dices.add(d1);
        dices.add(d2);
        dices.add(d3);
        matchMultiplayer.getBoard().getReserve().throwDices(dices);
        matchMultiplayer.getBoard().getReserve().getDices().get(0).setValue(4);
        matchMultiplayer.getBoard().getReserve().getDices().get(1).setValue(4);
        matchMultiplayer.placeDice("Archi",1,1,0);
        matchMultiplayer.setDiceAction(false);
        matchMultiplayer.placeDice("Archi2",0,0,1);
        System.out.println(matchMultiplayer.getPlayer("Archi").getSchemeCard().toString());
        System.out.println(matchMultiplayer.getPlayer("Archi2").getSchemeCard().toString());
        matchMultiplayer.calculateFinalScore();
        Assert.assertEquals(-11, matchMultiplayer.getPlayer("Archi").getPoints());
        Assert.assertEquals(-11, matchMultiplayer.getPlayer("Archi2").getPoints());
    }

    //TODO: QUESTO TEST LANCIA ECCEZIONE
    @Test
    public void afterReconnection(){
        Lobby lobby = new Lobby(10, 10);
        lobby.addToWaitingPlayers("Archi");
        lobby.addToWaitingPlayers("Archi2");
        lobby.addToWaitingPlayers("Archi3");
        lobby.startMatch();
        lobby.getMultiplayerMatches().get("Archi").getPlayer("Archi").setSchemeCard(new Firelight());
        lobby.getMultiplayerMatches().get("Archi").getPlayer("Archi").setNumFavorTokens(4);
        lobby.getMultiplayerMatches().get("Archi2").getPlayer("Archi2").setSchemeCard(new SymphonyOfLight());
        lobby.getMultiplayerMatches().get("Archi2").getPlayer("Archi2").setNumFavorTokens(4);
        lobby.getMultiplayerMatches().get("Archi3").getPlayer("Archi3").setSchemeCard(new ChromaticSplendor());
        lobby.getMultiplayerMatches().get("Archi3").getPlayer("Archi3").setNumFavorTokens(4);
        lobby.getMultiplayerMatches().get("Archi3").getPlayer("Archi3").setPrivateObjectiveCard(new PrivateObjectiveCard(Colors.BLUE));
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.BLUE);
        Dice d1 = new Dice(Colors.RED);
        Dice d2 = new Dice(Colors.YELLOW);
        Dice d3 = new Dice(Colors.RED);
        dices.add(d);
        dices.add(d1);
        dices.add(d2);
        dices.add(d3);
        lobby.getMultiplayerMatches().get("Archi").getBoard().getReserve().throwDices(dices);
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
        lobby.getMultiplayerMatches().get("Archi").getBoard().getRoundTrack().putDices(list0, 0);
        lobby.getMultiplayerMatches().get("Archi").getBoard().getRoundTrack().putDices(list1, 1);
        lobby.getMultiplayerMatches().get("Archi").placeDice("Archi3", 0, 0, 0);
        lobby.getMultiplayerMatches().get("Archi").setDiceAction(false);
        lobby.disconnect("Archi3");
        lobby.getMultiplayerMatches().get("Archi").placeDice("Archi", 0, 1, 0 );
        lobby.getMultiplayerMatches().get("Archi2").placeDice("Archi2", 0, 0, 1 );
        lobby.getMultiplayerMatches().get("Archi").afterReconnection("Archi3");
    }

}
