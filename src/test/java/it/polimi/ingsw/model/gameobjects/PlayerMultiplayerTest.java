package it.polimi.ingsw.model.gameobjects;

import it.polimi.ingsw.model.gamelogic.ConnectionStatus;
import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
import it.polimi.ingsw.model.gamelogic.PlayerMultiplayer;
import it.polimi.ingsw.model.gameobjects.windowpatterncards.Comitas;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;

/**
 * All the @Test have the same name of the method they test, check the method implementation for a detailed description
 */
public class PlayerMultiplayerTest {

    @Test
    public void PlayerMultiplayer() {
        MatchMultiplayer match = mock(MatchMultiplayer.class);
        PlayerMultiplayer playerMultiplayer = new PlayerMultiplayer("CowboyBebop");
        Assert.assertEquals("CowboyBebop", playerMultiplayer.getName());
        Assert.assertEquals(ConnectionStatus.CONNECTED, playerMultiplayer.getStatus());
        Assert.assertFalse(playerMultiplayer.isMyTurn());
        Assert.assertFalse(playerMultiplayer.isSchemeCardSet());
    }

    @Test
    public void setSchemeCard() {
        MatchMultiplayer match = mock(MatchMultiplayer.class);
        PlayerMultiplayer playerMultiplayer = new PlayerMultiplayer("CowboyBebop");
        WindowPatternCard schemeCard = new Comitas();
        playerMultiplayer.setSchemeCard(schemeCard);
        Assert.assertEquals(schemeCard, playerMultiplayer.getSchemeCard());
        playerMultiplayer.setMyTurn(true);
        PrivateObjectiveCard card = new PrivateObjectiveCard(Colors.BLUE);
        playerMultiplayer.setPrivateObjectiveCard(card);
        Assert.assertEquals(card, playerMultiplayer.getPrivateObjectiveCard());
    }
}
