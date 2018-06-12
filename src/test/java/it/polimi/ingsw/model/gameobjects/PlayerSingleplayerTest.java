package it.polimi.ingsw.model.gameobjects;

import it.polimi.ingsw.model.gameobjects.windowpatterncards.KaleidoscopicDream;
import it.polimi.ingsw.model.gameobjects.windowpatterncards.SymphonyOfLight;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PlayerSingleplayerTest {

    @Test
    public void PlayerSinglePlayer(){
        PlayerSingleplayer playerSingleplayer = new PlayerSingleplayer("CowboyBebop");
        Assert.assertEquals("CowboyBebop", playerSingleplayer.getName());
        Assert.assertEquals(Colors.NONE, playerSingleplayer.getColor());

    }

    @Test
    public void setSchemeCard(){
        WindowPatternCard schemeCard = new KaleidoscopicDream();
        PlayerSingleplayer playerSingleplayer = new PlayerSingleplayer("CowboyBebop");
        playerSingleplayer.setSchemeCard(schemeCard);
        Assert.assertEquals(schemeCard, playerSingleplayer.getSchemeCard());
        List<PrivateObjectiveCard> cards = new ArrayList<>();
        cards.add(new PrivateObjectiveCard(Colors.RED));
        cards.add(new PrivateObjectiveCard(Colors.BLUE));
        playerSingleplayer.setPrivateObjectiveCards(cards);
        Assert.assertEquals(cards, playerSingleplayer.getPrivateObjectiveCards());
    }
}
