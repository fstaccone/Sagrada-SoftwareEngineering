package it.polimi.ingsw.model.gameobjects;

import org.junit.Assert;
import org.junit.Test;

public class PublicObjectiveCardDeckTest {

    @Test
    public void PublicObjectiveCardDeck(){
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
