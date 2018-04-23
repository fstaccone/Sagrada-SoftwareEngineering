package it.polimi.ingsw.model.gameobjects;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RightSizeDecksCreationTest {
    DecksContainer decks;
    @Before
    public void before(){
        this.decks = new DecksContainer(3);

    }
    @Test
    public void checkRightSizeDecksCreation(){
        Assert.assertEquals(decks.getToolCardDeck().pickedCards.size(),3);
        Assert.assertEquals(decks.getPublicObjectiveCardDeck().pickedCards.size(),3);
        Assert.assertEquals(decks.getWindowPatternCardDeck().pickedCards.size(),12);
        Assert.assertEquals(decks.getPrivateObjectiveCardDeck().pickedCards.size(),3);
    }
}
