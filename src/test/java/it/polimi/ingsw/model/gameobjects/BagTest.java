package it.polimi.ingsw.model.gameobjects;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class BagTest {
    @Test
    public void Bag(){
        Bag bag = new Bag(2);
        Assert.assertEquals(10 , bag.getSize() );
        int red = 0;
        int yellow = 0;
        int greeen = 0;
        int violet = 0;
        int blue = 0;
        List<Dice> allDices = bag.pickDices(10);
        for(Dice d : allDices){
            switch (d.getColor()){
                case YELLOW:
                    yellow++;
                    break;
                case BLUE:
                    blue++;
                    break;
                case VIOLET:
                    violet++;
                    break;
                case RED:
                    red++;
                    break;
                case GREEN:
                    greeen++;
                    break;
            }
        }
        Assert.assertEquals(2, red);
        Assert.assertEquals(2, yellow);
        Assert.assertEquals(2, greeen);
        Assert.assertEquals(2, violet);
        Assert.assertEquals(2, blue);
    }

    @Test
    public void putDiceInBag(){
        Bag bag = new Bag(3);
        Dice d = new Dice(Colors.YELLOW);
        bag.putDiceInBag(d);
        Assert.assertEquals(16, bag.getSize());
    }

    @Test
    public void pickSingleDice(){
        Bag bag = new Bag(1);
        Dice d = bag.pickSingleDice();
        Assert.assertEquals(4, bag.getSize());
    }

    @Test
    public void pickDices(){
        Bag bag = new Bag(5);
        Assert.assertEquals(25, bag.getSize());
        List<Dice> dices = bag.pickDices(5);
        Assert.assertEquals(20, bag.getSize());
    }

}
