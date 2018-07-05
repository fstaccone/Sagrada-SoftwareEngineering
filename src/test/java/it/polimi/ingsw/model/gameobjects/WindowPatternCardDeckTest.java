package it.polimi.ingsw.model.gameobjects;

import org.junit.Assert;
import org.junit.Test;

public class WindowPatternCardDeckTest {

    @Test
    public void WindowPatternCardDeck(){
        WindowPatternCardDeck windowPatternCardDeck = new WindowPatternCardDeck(4);
        Assert.assertEquals(16, windowPatternCardDeck.getPickedCards().size());
    }
}
