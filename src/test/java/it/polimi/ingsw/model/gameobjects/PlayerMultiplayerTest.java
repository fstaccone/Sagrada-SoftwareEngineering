package it.polimi.ingsw.model.gameobjects;

import it.polimi.ingsw.ConnectionStatus;
import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
import it.polimi.ingsw.model.gameobjects.windowpatterncards.AuroraSagradis;
import it.polimi.ingsw.model.gameobjects.windowpatterncards.Comitas;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class PlayerMultiplayerTest {

    @Test
    public void PlayerMultiplayer(){
        MatchMultiplayer match = mock(MatchMultiplayer.class);
        PlayerMultiplayer playerMultiplayer = new PlayerMultiplayer("CowboyBebop", match);
        Assert.assertEquals("CowboyBebop", playerMultiplayer.getName());
        Assert.assertEquals(ConnectionStatus.CONNECTED, playerMultiplayer.getStatus());
        Assert.assertEquals(false, playerMultiplayer.isMyTurn());
        Assert.assertEquals(false, playerMultiplayer.isSchemeCardSet());
    }
    @Test
    public void setSchemeCard(){
        MatchMultiplayer match = mock(MatchMultiplayer.class);
        PlayerMultiplayer playerMultiplayer = new PlayerMultiplayer("CowboyBebop", match);
        WindowPatternCard schemeCard = new Comitas();
        playerMultiplayer.setSchemeCard(schemeCard);
        Assert.assertEquals(schemeCard, playerMultiplayer.getSchemeCard());
    }
}
