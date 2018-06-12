package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.Lobby;
import it.polimi.ingsw.model.gameobjects.PlayerMultiplayer;
import org.junit.Assert;
import org.junit.Test;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TurnTimerTest {

    @Test
    public void TurnTimer() {
        int matchId = 0;
        List<String> clients = new ArrayList<>();
        clients.add("client1");
        clients.add("client2");
        int turnTime = 10;
        Map<String, ObjectOutputStream> socketsOut = new HashMap<>();
        Lobby lobby = new Lobby(10, turnTime);
        MatchMultiplayer matchMultiplayer = new MatchMultiplayer(matchId, clients, turnTime, socketsOut, lobby);
        PlayerMultiplayer playerMultiplayer = new PlayerMultiplayer("CowboyBebop");
        TurnTimer turnTimer = new TurnTimer(matchMultiplayer, playerMultiplayer);
        turnTimer.run();
        Assert.assertNotNull(turnTimer);
    }
}
