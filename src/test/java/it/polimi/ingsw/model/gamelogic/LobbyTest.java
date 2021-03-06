package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.view.LobbyObserver;
import it.polimi.ingsw.view.MatchObserver;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.Mockito.mock;

/**
 * All the @Test have the same name of the method they test, check the method implementation for a detailed description
 */
public class LobbyTest {

    @Test
    public void Lobby() {
        Lobby lobby = new Lobby(10, 10);
        Assert.assertNotNull(lobby);
        Assert.assertEquals(new HashMap<>(), lobby.getMultiplayerMatches());
        Assert.assertEquals(new HashMap<>(), lobby.getSingleplayerMatches());
        Assert.assertEquals(new ArrayList<>(), lobby.getWaitingPlayers());
        Assert.assertEquals(new ConcurrentHashMap<>(), lobby.getRemoteObservers());
        Assert.assertEquals(new ConcurrentHashMap<>(), lobby.getSocketObservers());
    }

    @Test
    public void AddUsernameandCheckName() {
        Lobby lobby = new Lobby(10, 10);
        lobby.addUsername("CowboyBebop");
        Assert.assertEquals(ConnectionStatus.CONNECTED, lobby.checkName("CowboyBebop"));
        Assert.assertEquals(ConnectionStatus.ABSENT, lobby.checkName("cowboybebop"));
    }

    @Test
    public void addToWaitingPlayers() {
        Lobby lobby = new Lobby(10, 10);
        lobby.addToWaitingPlayers("CowboyBebop");
        lobby.addToWaitingPlayers("CowboyBebop1");
        lobby.addToWaitingPlayers("CowboyBebop2");
        Assert.assertEquals("CowboyBebop", lobby.getWaitingPlayers().get(0));
        Assert.assertEquals("CowboyBebop1", lobby.getWaitingPlayers().get(1));
        Assert.assertEquals("CowboyBebop2", lobby.getWaitingPlayers().get(2));
    }

    @Test
    public void removeFromWaitingPlayers() {
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
    }

    @Test
    public void createSingleplayerMatch() {
        Lobby lobby = new Lobby(10, 10);
        lobby.createSingleplayerMatch("Ancona", 1, null);
        Assert.assertEquals(0, lobby.getSingleplayerMatches().get("Ancona").getMatchId());
        Assert.assertEquals("Ancona", lobby.getSingleplayerMatches().get("Ancona").getPlayer().getName());
    }

    @Test
    public void addPlayer() {
        Lobby lobby = new Lobby(10, 10);
        lobby.addToWaitingPlayers("CowboyBebop");
        Assert.assertEquals("CowboyBebop", lobby.getWaitingPlayers().get(0));
    }

    @Test
    public void startMatch() {
        Lobby lobby = new Lobby(1000000, 10);
        lobby.addToWaitingPlayers("Archi");
        lobby.addToWaitingPlayers("Bovalino");
        lobby.addToWaitingPlayers("Condofuri");
        lobby.startMatch();
        Assert.assertEquals("Archi", lobby.getMultiplayerMatches().get("Archi").getPlayer("Archi").getName());
        Assert.assertEquals("Bovalino", lobby.getMultiplayerMatches().get("Condofuri").getPlayer("Bovalino").getName());
        Assert.assertNull(lobby.getMultiplayerMatches().get("Archi").getPlayer("Diamante"));
        Assert.assertNull(lobby.getMultiplayerMatches().get("Taormina"));
    }

    @Test
    public void disconnect() {
        Lobby lobby = new Lobby(10000000, 10);
        lobby.addToWaitingPlayers("Archi");
        lobby.addToWaitingPlayers("Bovalino");
        lobby.addToWaitingPlayers("Condofuri");
        lobby.startMatch();
        lobby.disconnect("Bovalino");
        Assert.assertEquals(ConnectionStatus.DISCONNECTED, lobby.checkName("Bovalino"));
    }

    @Test
    public void reconnect() {
        Lobby lobby = new Lobby(1000000000, 10);
        lobby.addToWaitingPlayers("Archi");
        lobby.addToWaitingPlayers("Bovalino");
        lobby.addToWaitingPlayers("Condofuri");
        lobby.startMatch();
        System.out.println("numero1 " + lobby.getMultiplayerMatches().get("Bovalino").getPlayers().size());
        lobby.disconnect("Bovalino");
        System.out.println("numero2 " + lobby.getMultiplayerMatches().get("Bovalino").getPlayers().size());
        Assert.assertEquals(ConnectionStatus.DISCONNECTED, lobby.checkName("Bovalino"));
        lobby.reconnect("Bovalino");
        System.out.println("numero3 " + lobby.getMultiplayerMatches().get("Bovalino").getPlayers().size());
        Assert.assertEquals(ConnectionStatus.CONNECTED, lobby.checkName("Bovalino"));
    }

    @Test
    public void removeMatchSingleplayer() {
        Lobby lobby = new Lobby(10, 10);
        lobby.createSingleplayerMatch("Archi", 1, null);
        lobby.createSingleplayerMatch("Bovalino", 1, null);
        Assert.assertEquals(0, lobby.getSingleplayerMatches().get("Archi").getMatchId());
        Assert.assertEquals(1, lobby.getSingleplayerMatches().get("Bovalino").getMatchId());
        lobby.removeMatchSingleplayer("Archi");
        Assert.assertNull(lobby.getSingleplayerMatches().get("Archi"));
    }

    @Test
    public void observeMatchRemote() {
        Lobby lobby = new Lobby(10, 10);
        lobby.createSingleplayerMatch("Archi", 1, null);
        MatchObserver m = mock(MatchObserver.class);
        lobby.observeMatchRemote("Archi", m, true);
        lobby.addToWaitingPlayers("Bagaladi");
        lobby.addToWaitingPlayers("Condofuri");
        lobby.startMatch();
        MatchObserver m2 = mock(MatchObserver.class);
        lobby.observeMatchRemote("Bagaladi", m2, false);
    }

    @Test
    public void observeLobbyRemote() {
        Lobby lobby = new Lobby(10, 10);
        LobbyObserver observer = mock(LobbyObserver.class);
        lobby.observeLobbyRemote("Archi", observer);
        Assert.assertTrue(lobby.getRemoteObservers().keySet().contains("Archi"));
    }

    @Test
    public void removeFromMatchMulti() {
        Lobby lobby = new Lobby(1000000, 10);
        lobby.addToWaitingPlayers("Archi");
        lobby.addToWaitingPlayers("Bovalino");
        lobby.addToWaitingPlayers("Condofuri");
        lobby.startMatch();
        lobby.removeFromMatchMulti("Condofuri");
        Assert.assertNull(lobby.getMultiplayerMatches().get("Condofuri"));
    }

    @Test
    public void removeDisconnectedPlayers() {
        Lobby lobby = new Lobby(1000000, 10);
        lobby.addToWaitingPlayers("Archi");
        lobby.addToWaitingPlayers("Bovalino");
        lobby.addToWaitingPlayers("Condofuri");
        lobby.startMatch();
        lobby.disconnect("Condofuri");
        lobby.disconnect("Archi");
        Assert.assertNull(lobby.getMultiplayerMatches().get("Condofuri"));
        Assert.assertNull(lobby.getMultiplayerMatches().get("Archi"));
    }

    @Test
    public void deleteDisconnectedClients() {
        Lobby lobby = new Lobby(1000000, 10);
        lobby.addToWaitingPlayers("Archi");
        lobby.addUsername("Archi");
        lobby.addToWaitingPlayers("Bovalino");
        lobby.addUsername("Bovalino");
        lobby.addToWaitingPlayers("Condofuri");
        lobby.addUsername("Condofuri");
        lobby.startMatch();
        lobby.disconnect("Condofuri");
        List<String> players = new ArrayList<>();
        players.add("Condofuri");
        lobby.deleteDisconnectedClients(players);
        Assert.assertNull(lobby.getMultiplayerMatches().get("Condofuri"));
    }

}
