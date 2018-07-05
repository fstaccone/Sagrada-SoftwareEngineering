package it.polimi.ingsw.model.gameobjects;

import org.junit.Assert;
import org.junit.Test;

/**
 * All the @Test have the same name of the method they test, check the method implementation for a detailed description
 */
public class WindowPatternCardDeckTest {

    @Test
    public void WindowPatternCardDeck() {
        WindowPatternCardDeck windowPatternCardDeck = new WindowPatternCardDeck(4);
        Assert.assertEquals(16, windowPatternCardDeck.getPickedCards().size());
    }
}
