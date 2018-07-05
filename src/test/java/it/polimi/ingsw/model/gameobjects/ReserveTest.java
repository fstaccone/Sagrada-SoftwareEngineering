package it.polimi.ingsw.model.gameobjects;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * All the @Test have the same name of the method they test, check the method implementation for a detailed description
 */
public class ReserveTest {
    @Test
    public void Reserve() {
        Reserve reserve = new Reserve();
        Assert.assertEquals(Collections.emptyList(), reserve.getDices());
    }

    @Test
    public void throwDices() {
        Reserve reserve = new Reserve();
        List<Dice> dicesToThrow = new ArrayList<>();
        dicesToThrow.add(new Dice(Colors.YELLOW));
        dicesToThrow.add(new Dice(Colors.RED));
        dicesToThrow.add(new Dice(Colors.BLUE));
        reserve.throwDices(dicesToThrow);
        Dice d1 = reserve.chooseDice(0);
        Dice d2 = reserve.chooseDice(0);
        Dice d3 = reserve.chooseDice(0);
        Assert.assertTrue(d1.getValue() > 0 && d1.getValue() < 7);
        Assert.assertTrue(d2.getValue() > 0 && d1.getValue() < 7);
        Assert.assertTrue(d3.getValue() > 0 && d1.getValue() < 7);
    }

    @Test
    public void chooseDice() {
        Reserve reserve = new Reserve();
        List<Dice> dicesToThrow = new ArrayList<>();
        dicesToThrow.add(new Dice(Colors.YELLOW));
        dicesToThrow.add(new Dice(Colors.RED));
        dicesToThrow.add(new Dice(Colors.BLUE));
        reserve.throwDices(dicesToThrow);
        Dice d1 = reserve.chooseDice(2);
        Dice d2 = reserve.chooseDice(0);
        Dice d3 = reserve.chooseDice(0);
        Assert.assertEquals(d1.getColor(), Colors.BLUE);
        Assert.assertEquals(d2.getColor(), Colors.YELLOW);
        Assert.assertEquals(d3.getColor(), Colors.RED);
        Assert.assertNull(reserve.chooseDice(0));
    }

    @Test
    public void removeAllDices() {
        Reserve reserve = new Reserve();
        List<Dice> dicesToThrow = new ArrayList<>();
        dicesToThrow.add(new Dice(Colors.YELLOW));
        dicesToThrow.add(new Dice(Colors.RED));
        dicesToThrow.add(new Dice(Colors.BLUE));
        reserve.throwDices(dicesToThrow);
        Dice d1 = reserve.chooseDice(1);
        List<Dice> dicesLeft = reserve.removeAllDices();
        Assert.assertEquals(Collections.emptyList(), reserve.getDices());
        Assert.assertEquals(dicesLeft.get(0).getColor(), Colors.YELLOW);
        Assert.assertEquals(dicesLeft.get(1).getColor(), Colors.BLUE);
    }
}
