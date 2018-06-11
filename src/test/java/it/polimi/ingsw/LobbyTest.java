package it.polimi.ingsw;

import it.polimi.ingsw.model.gamelogic.MatchSingleplayer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LobbyTest {

    @Test
    public void Lobby(){
        Lobby lobby = new Lobby(10, 10);
        Assert.assertNotNull(lobby);
        Assert.assertEquals(new HashMap<>(), lobby.getMultiplayerMatches());
        Assert.assertEquals(new HashMap<>(), lobby.getSingleplayerMatches());
        Assert.assertEquals(new ArrayList<>(), lobby.getWaitingPlayers());
        Assert.assertEquals(new ConcurrentHashMap<>(), lobby.getRemoteObservers());
        Assert.assertEquals(new ConcurrentHashMap<>(), lobby.getSocketObservers());
    }

    @Test
    public void AddUsernameandCheckName(){
        Lobby lobby = new Lobby(10, 10);
        lobby.addUsername("CowboyBebop");
        Assert.assertEquals(ConnectionStatus.CONNECTED, lobby.checkName("CowboyBebop"));
        Assert.assertEquals(ConnectionStatus.ABSENT, lobby.checkName("cowboybebop"));
    }

    @Test
    public void addToWaitingPlayers(){
        Lobby lobby = new Lobby(10, 10);
        lobby.addToWaitingPlayers("CowboyBebop");
        lobby.addToWaitingPlayers("CowboyBebop1");
        lobby.addToWaitingPlayers("CowboyBebop2");
        Assert.assertEquals("CowboyBebop", lobby.getWaitingPlayers().get(0));
        Assert.assertEquals("CowboyBebop1", lobby.getWaitingPlayers().get(1));
        Assert.assertEquals("CowboyBebop2", lobby.getWaitingPlayers().get(2));
    }

    @Test
    public void removeFromWaitingPlayers(){
        Lobby lobby = new Lobby(10, 10);
        lobby.addToWaitingPlayers("CowboyBebop");
        lobby.addToWaitingPlayers("CowboyBebop1");
        lobby.addToWaitingPlayers("CowboyBebop2");
        Assert.assertEquals("CowboyBebop", lobby.getWaitingPlayers().get(0));
        Assert.assertEquals("CowboyBebop1", lobby.getWaitingPlayers().get(1));
        Assert.assertEquals("CowboyBebop2", lobby.getWaitingPlayers().get(2));
        lobby.removeFromWaitingPlayers("CowboyBebop1");
        Assert.assertEquals("CowboyBebop2", lobby.getWaitingPlayers().get(1));
        lobby.removeFromWaitingPlayers("CowboyBebop");
        Assert.assertEquals("CowboyBebop2", lobby.getWaitingPlayers().get(0));
    };

    @Test
    public void createSingleplayerMatch(){
        Lobby lobby = new Lobby(10, 10);
        lobby.createSingleplayerMatch("Ancona",1);
        Assert.assertEquals(0, lobby.getSingleplayerMatches().get("Ancona").getMatchId());
        Assert.assertEquals("Ancona", lobby.getSingleplayerMatches().get("Ancona").getPlayer().getName());
    }

    @Test
    public void addPlayer(){
        Lobby lobby = new Lobby(10, 10);
        lobby.addPlayer("CowboyBebop");
        Assert.assertEquals("CowboyBebop", lobby.getWaitingPlayers().get(0));
    }

    @Test
    public void startMatch(){
        Lobby lobby = new Lobby(10, 10);
        lobby.addPlayer("Archi");
        lobby.addPlayer("Bovalino");
        lobby.addPlayer("Condofuri");
        lobby.startMatch();
        Assert.assertEquals("Archi", lobby.getMultiplayerMatches().get("Archi").getPlayer("Archi").getName());
        Assert.assertEquals("Bovalino", lobby.getMultiplayerMatches().get("Condofuri").getPlayer("Bovalino").getName());
        Assert.assertNull(lobby.getMultiplayerMatches().get("Archi").getPlayer("Diamante"));
        Assert.assertNull(lobby.getMultiplayerMatches().get("Taormina"));
    }

    @Test
    public void disconnect(){
        Lobby lobby = new Lobby(10, 10);
        lobby.addPlayer("Archi");
        lobby.addPlayer("Bovalino");
        lobby.addPlayer("Condofuri");
        lobby.startMatch();
        lobby.disconnect("Bovalino");
        Assert.assertEquals(ConnectionStatus.DISCONNECTED, lobby.checkName("Bovalino"));
        lobby.disconnect("Archi");
        Assert.assertEquals(ConnectionStatus.DISCONNECTED, lobby.checkName("Archi"));
    }

    @Test
    public void reconnect(){
        Lobby lobby = new Lobby(10, 10);
        lobby.addPlayer("Archi");
        lobby.addPlayer("Bovalino");
        lobby.addPlayer("Condofuri");
        lobby.startMatch();
        lobby.disconnect("Bovalino");
        Assert.assertEquals(ConnectionStatus.DISCONNECTED, lobby.checkName("Bovalino"));
        lobby.reconnect("Bovalino");
        Assert.assertEquals(ConnectionStatus.CONNECTED, lobby.checkName("Bovalino"));
    }

    @Test
    public void removeMatchSingleplayer(){
        Lobby lobby = new Lobby(10, 10);
        lobby.createSingleplayerMatch("Archi",1);
        lobby.createSingleplayerMatch("Bovalino",1);
        Assert.assertEquals(0, lobby.getSingleplayerMatches().get("Archi").getMatchId());
        Assert.assertEquals(1, lobby.getSingleplayerMatches().get("Bovalino").getMatchId());
        lobby.removeMatchSingleplayer("Archi");
        Assert.assertNull(lobby.getSingleplayerMatches().get("Archi"));
    }

}
