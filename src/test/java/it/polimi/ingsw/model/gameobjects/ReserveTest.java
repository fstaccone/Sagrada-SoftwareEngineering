package it.polimi.ingsw.model.gameobjects;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReserveTest {
    @Test
    public void Reserve(){
        Reserve reserve = new Reserve();
        Assert.assertEquals(Collections.emptyList() ,reserve.getDices());
    }

    @Test
    public void throwDices(){
        Reserve reserve = new Reserve();
        List<Dice> dicesToThrow = new ArrayList<>();
        dicesToThrow.add(new Dice(Colors.YELLOW));
        dicesToThrow.add(new Dice(Colors.RED));
        dicesToThrow.add(new Dice(Colors.BLUE));
        reserve.throwDices(dicesToThrow);
        System.out.println(reserve.toString());
        Dice d1 = reserve.chooseDice(0);
        Dice d2 = reserve.chooseDice(0);
        Dice d3 = reserve.chooseDice(0);
        Assert.assertTrue(d1.getValue()>0 && d1.getValue()<7);
        Assert.assertTrue(d2.getValue()>0 && d1.getValue()<7);
        Assert.assertTrue(d3.getValue()>0 && d1.getValue()<7);
    }

    @Test
    public void chooseDice(){
        Reserve reserve = new Reserve();
        List<Dice> dicesToThrow = new ArrayList<>();
        dicesToThrow.add(new Dice(Colors.YELLOW));
        dicesToThrow.add(new Dice(Colors.RED));
        dicesToThrow.add(new Dice(Colors.BLUE));
        reserve.throwDices(dicesToThrow);
        Dice d1 = reserve.chooseDice(2);
        Dice d2 = reserve.chooseDice(0);
        Dice d3 = reserve.chooseDice(0);
        Assert.assertTrue(d1.getColor().equals(Colors.BLUE));
        Assert.assertTrue(d2.getColor().equals(Colors.YELLOW));
        Assert.assertTrue(d3.getColor().equals(Colors.RED));
        Assert.assertEquals(null, reserve.chooseDice(0));
    }

    @Test
    public void removeAllDices(){
        Reserve reserve = new Reserve();
        List<Dice> dicesToThrow = new ArrayList<>();
        dicesToThrow.add(new Dice(Colors.YELLOW));
        dicesToThrow.add(new Dice(Colors.RED));
        dicesToThrow.add(new Dice(Colors.BLUE));
        reserve.throwDices(dicesToThrow);
        Dice d1 = reserve.chooseDice(1);
        List<Dice> dicesLeft = reserve.removeAllDices();
        Assert.assertEquals(Collections.emptyList() ,reserve.getDices());
        Assert.assertTrue(dicesLeft.get(0).getColor().equals(Colors.YELLOW));
        Assert.assertTrue(dicesLeft.get(1).getColor().equals(Colors.BLUE));
    }
}
