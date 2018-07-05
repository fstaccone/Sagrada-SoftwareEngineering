package it.polimi.ingsw.model.gameobjects;

import it.polimi.ingsw.model.gameobjects.windowpatterncards.*;
import org.junit.Assert;
import org.junit.Test;

public class WindowPatternCardTest {

    @Test
    public void WindowPatternCard() {
        WindowPatternCard windowPatternCard = new WindowPatternCard("CowboyBebop", 4, 5);
        Assert.assertEquals("CowboyBebop", windowPatternCard.getName());
        Assert.assertEquals(4, windowPatternCard.getRows());
        Assert.assertEquals(5, windowPatternCard.getColumns());
        Assert.assertEquals(4, windowPatternCard.getWindow().length);
        Assert.assertEquals(5, windowPatternCard.getWindow()[0].length);
    }

    @Test
    public void existsAdjacentDice() {
        WindowPatternCard schemeCard = new Bellesguard();
        Assert.assertFalse(schemeCard.existsAdjacentDice(1, 1));
        Dice d = new Dice(Colors.BLUE);
        d.setValue(1);
        schemeCard.putDice(d, 0, 0);
        Assert.assertTrue(schemeCard.existsAdjacentDice(1, 1));
        Dice d1 = schemeCard.getDice(1, 0);
        Dice d2 = schemeCard.getDice(0, 1);
        Dice d3 = schemeCard.getDice(1, 1);
        Dice[] adjacentDices = {d1, d2, d3};
        Assert.assertFalse(schemeCard.existsAdjacentDice(adjacentDices));
        d2 = new Dice(Colors.BLUE);
        d2.setValue(3);
        schemeCard.putDice(d2, 1, 1);
        adjacentDices[1] = d2;
        Assert.assertTrue(schemeCard.existsAdjacentDice(adjacentDices));
    }

    @Test
    public void checkPos() {
        WindowPatternCard schemeCard = new FulgorDelCielo();
        Dice d1 = new Dice(Colors.YELLOW);
        d1.setValue(1);
        Assert.assertFalse(schemeCard.putDice(d1, 1, 1));
        Assert.assertTrue(schemeCard.putDice(d1, 0, 0));
        Dice d2 = new Dice(Colors.YELLOW);
        d2.setValue(2);
        Assert.assertFalse(schemeCard.checkPos(d2, 0, 1));
        Dice d3 = new Dice(Colors.BLUE);
        d3.setValue(1);
        Assert.assertFalse(schemeCard.checkPos(d3, 0, 1));
        d3.setValue(4);
        Assert.assertTrue(schemeCard.checkPos(d3, 0, 1));
    }

    @Test
    public void putDice() {
        WindowPatternCard schemeCard = new Gravitas();
        Dice d = new Dice(Colors.BLUE);
        d.setValue(3);
        Assert.assertFalse(schemeCard.putDice(d, 0, 0));
        d.setValue(1);
        Assert.assertTrue(schemeCard.putDice(d, 0, 0));
        Assert.assertEquals(d, schemeCard.getDice(0, 0));
    }

    @Test
    public void putDiceIgnoringValueConstraint() {
        WindowPatternCard schemeCard = new LuxMundi();
        Dice d = new Dice(Colors.YELLOW);
        d.setValue(3);
        schemeCard.putDice(d, 0, 1);
        Dice d2 = new Dice(Colors.BLUE);
        d2.setValue(2);
        schemeCard.putDiceIgnoringValueConstraint(d2, 0, 2);
        Assert.assertEquals(d2, schemeCard.getDice(0, 2));
    }

    @Test
    public void putDiceIgnoringColorConstraint() {
        WindowPatternCard schemeCard = new ChromaticSplendor();
        Dice d = new Dice(Colors.VIOLET);
        d.setValue(3);
        schemeCard.putDice(d, 0, 1);
        Dice d2 = new Dice(Colors.BLUE);
        d2.setValue(4);
        schemeCard.putDiceIgnoringColorConstraint(d2, 0, 2);
        Assert.assertEquals(d2, schemeCard.getDice(0, 2));
    }

    @Test
    public void putFirstDice() {
        WindowPatternCard schemeCard = new Firelight();
        Dice d = new Dice(Colors.BLUE);
        d.setValue(1);
        Assert.assertFalse(schemeCard.putFirstDice(d, 1, 1));
        Assert.assertTrue(schemeCard.putFirstDice(d, 1, 0));
        Assert.assertEquals(d, schemeCard.getDice(1, 0));
    }

    @Test
    public void putDiceWithoutCheckPos() {
        WindowPatternCard schemeCard = new FractalDrops();
        Dice d = new Dice(Colors.BLUE);
        schemeCard.putDiceWithoutCheckPos(d, 1, 1);
        Assert.assertEquals(d, schemeCard.getDice(1, 1));
        schemeCard.putDiceWithoutCheckPos(d, 1, 3);
        Assert.assertEquals(d, schemeCard.getDice(1, 3));
    }

    @Test
    public void removeDice() {
        WindowPatternCard schemeCard = new SunSGlory();
        Dice d = new Dice(Colors.BLUE);
        d.setValue(4);
        schemeCard.putDice(d, 0, 3);
        Assert.assertEquals(d, schemeCard.getDice(0, 3));
        Assert.assertEquals(d, schemeCard.removeDice(0, 3));
        Assert.assertNull(schemeCard.getDice(0, 3));
    }

    @Test
    public void countFreeSquares() {
        WindowPatternCard schemeCard = new WaterOfLife();
        Assert.assertEquals(20, schemeCard.countFreeSquares());
        Dice d = new Dice(Colors.BLUE);
        d.setValue(4);
        schemeCard.putDice(d, 0, 2);
        Assert.assertEquals(19, schemeCard.countFreeSquares());
    }


}
