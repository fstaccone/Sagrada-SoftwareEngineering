package it.polimi.ingsw.model.gameobjects;

import org.junit.Assert;
import org.junit.Test;

/**
 * All the @Test have the same name of the method they test, check the method implementation for a detailed description
 */
public class PublicObjectiveCardDeckTest {

    @Test
    public void PublicObjectiveCardDeck() {
        PublicObjectiveCardDeck publicObjectiveCardDeckSingle = new PublicObjectiveCardDeck(1);
        PublicObjectiveCardDeck publicObjectiveCardDeckMulti = new PublicObjectiveCardDeck(4);
        Assert.assertEquals(2, publicObjectiveCardDeckSingle.getPickedCards().size());
        Assert.assertEquals(3, publicObjectiveCardDeckMulti.getPickedCards().size());
        publicObjectiveCardDeckMulti.setReallyCreatedCards(2);
        publicObjectiveCardDeckMulti.setReallyCreatedCards(1);
        publicObjectiveCardDeckMulti.setReallyCreatedCards(1);
        Assert.assertEquals(10, publicObjectiveCardDeckMulti.getPickedCards().size());
    }
}
