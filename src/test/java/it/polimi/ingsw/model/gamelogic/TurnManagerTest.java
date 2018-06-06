package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.Lobby;
import org.junit.Assert;
import org.junit.Test;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TurnManagerTest {

    @Test
    public void TurnManager(){
        int matchId = 0;
        List<String> clients = new ArrayList<>();
        clients.add("client1");
        clients.add("client2");
        int turnTime = 10;
        Map<String, ObjectOutputStream> socketsOut = new HashMap<>();
        Lobby lobby = new Lobby(10, turnTime);
        MatchMultiplayer matchMultiplayer = new MatchMultiplayer(matchId, clients, turnTime, socketsOut, lobby);
        TurnManager turnManager = new TurnManager(matchMultiplayer, turnTime);
        Assert.assertNotNull(turnManager);
    }
}
