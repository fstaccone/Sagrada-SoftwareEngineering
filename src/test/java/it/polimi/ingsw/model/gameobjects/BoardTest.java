package it.polimi.ingsw.model.gameobjects;

import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

public class BoardTest {

    @Test
    public void Board(){
        MatchMultiplayer match = mock(MatchMultiplayer.class);
        ToolCard toolCard1 = new ToolCard("Alesatore per Lamina di Rame", "tool3");
        ToolCard toolCard2 = new ToolCard("Lathekin", "tool4");
        ToolCard toolCard3 = new ToolCard("Taglierina Circolare", "tool5");
        List<ToolCard> pickedToolCards = new ArrayList<>();
        pickedToolCards.add(toolCard1);
        pickedToolCards.add(toolCard2);
        pickedToolCards.add(toolCard3);
        PublicObjectiveCard publicObjectiveCard1 = new PublicObjectiveCard("Sfumature chiare");
        List<PublicObjectiveCard> pickedPublicObjectiveCards = new ArrayList<>();
        pickedPublicObjectiveCards.add(publicObjectiveCard1);
        Board board = new Board(match, pickedToolCards, pickedPublicObjectiveCards);
        Assert.assertEquals(pickedToolCards, board.getPickedToolCards());
        Assert.assertEquals(pickedPublicObjectiveCards, board.getPickedPublicObjectiveCards());
    }

}
