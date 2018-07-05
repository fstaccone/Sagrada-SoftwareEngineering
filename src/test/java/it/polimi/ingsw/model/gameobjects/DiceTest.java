package it.polimi.ingsw.model.gameobjects;

import org.junit.Assert;
import org.junit.Test;

public class DiceTest {

    @Test
    public void Dice(){
        Dice d1 = new Dice(Colors.RED);
        Dice d2 = new Dice(Colors.YELLOW);
        Dice d3 = new Dice(Colors.VIOLET);
        Dice d4 = new Dice(Colors.BLUE);
        Dice d5 = new Dice(Colors.GREEN);
        Assert.assertEquals(Colors.RED, d1.getColor());
        Assert.assertEquals(Colors.YELLOW, d2.getColor());
        Assert.assertEquals(Colors.VIOLET, d3.getColor());
        Assert.assertEquals(Colors.BLUE, d4.getColor());
        Assert.assertEquals(Colors.GREEN, d5.getColor());
    }
}
