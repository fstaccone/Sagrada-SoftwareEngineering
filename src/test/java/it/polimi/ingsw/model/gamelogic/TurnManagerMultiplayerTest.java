package it.polimi.ingsw.model.gamelogic;

import org.junit.Assert;
import org.junit.Test;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * All the @Test have the same name of the method they test, check the method implementation for a detailed description
 */
public class TurnManagerMultiplayerTest {

    @Test
    public void TurnManager() {
        int matchId = 0;
        List<String> clients = new ArrayList<>();
        clients.add("client1");
        clients.add("client2");
        int turnTime = 10;
        Map<String, ObjectOutputStream> socketsOut = new HashMap<>();
        Lobby lobby = new Lobby(10, turnTime);
        MatchMultiplayer matchMultiplayer = new MatchMultiplayer(matchId, clients, turnTime, socketsOut, lobby);
        TurnManagerMultiplayer turnManagerMultiplayer = new TurnManagerMultiplayer(matchMultiplayer, turnTime);
        Assert.assertNotNull(turnManagerMultiplayer);
    }
}
