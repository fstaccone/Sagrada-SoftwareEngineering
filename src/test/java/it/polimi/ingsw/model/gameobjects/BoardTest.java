package it.polimi.ingsw.model.gameobjects;

import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
import it.polimi.ingsw.model.gamelogic.PlayerMultiplayer;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * All the @Test have the same name of the method they test, check the method implementation for a detailed description
 */
public class BoardTest {

    @Test
    public void Board() {
        ToolCard toolCard1 = new ToolCard("Alesatore per Lamina di Rame", "tool3");
        ToolCard toolCard2 = new ToolCard("Lathekin", "tool4");
        ToolCard toolCard3 = new ToolCard("Taglierina Circolare", "tool5");
        PlayerMultiplayer playerMultiplayer = new PlayerMultiplayer("Archi");
        MatchMultiplayer m = mock(MatchMultiplayer.class);
        List<ToolCard> pickedToolCards = new ArrayList<>();
        pickedToolCards.add(toolCard1);
        pickedToolCards.add(toolCard2);
        pickedToolCards.add(toolCard3);
        ToolCard toolcard = mock(ToolCard.class);
        pickedToolCards.add(toolcard);
        when(toolcard.getToolID()).thenReturn("tool30");
        when(toolcard.useCard(playerMultiplayer, m)).thenReturn(true);
        PublicObjectiveCard publicObjectiveCard1 = new PublicObjectiveCard("Sfumature chiare");
        List<PublicObjectiveCard> pickedPublicObjectiveCards = new ArrayList<>();
        pickedPublicObjectiveCards.add(publicObjectiveCard1);
        Board board = new Board(pickedToolCards, pickedPublicObjectiveCards);
        Assert.assertEquals(pickedToolCards, board.getPickedToolCards());
        Assert.assertEquals(pickedPublicObjectiveCards, board.getPickedPublicObjectiveCards());
        Assert.assertTrue(board.findAndUseToolCard(30, playerMultiplayer, m));
        Assert.assertFalse(board.findAndUseToolCard(31, playerMultiplayer, m));
    }

}
