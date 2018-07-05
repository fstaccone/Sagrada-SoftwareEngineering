package it.polimi.ingsw.model.gameobjects;

import org.junit.Assert;
import org.junit.Test;

public class SquareTest {

    @Test
    public void Square(){
        Square square = new Square();
        Assert.assertEquals(null, square.getColorConstraint());
        Assert.assertEquals(0, square.getValueConstraint());
    }

    @Test
    public void occupiedSquare(){
        Square square = new Square();
        Assert.assertEquals(false, square.occupiedSquare());
        Dice d = new Dice(Colors.YELLOW);
        square.putDice(d);
        Assert.assertEquals(true, square.occupiedSquare());
    }

    @Test
    public void satisfiedConstraints(){
        Square square = new Square();
        Dice d = new Dice(Colors.RED);
        Assert.assertEquals(true, square.satisfiedConstraints(d));
        square.setColorConstraint(Colors.BLUE);
        Assert.assertEquals(false, square.satisfiedConstraints(d));
        square.setColorConstraint(Colors.RED);
        Assert.assertEquals(true, square.satisfiedConstraints(d));
        d.setValue(4);
        Assert.assertEquals(true, square.satisfiedConstraints(d));
        square.setColorConstraint(null);
        square.setValueConstraint(3);
        Assert.assertEquals(false, square.satisfiedConstraints(d));
        square.setValueConstraint(4);
        Assert.assertEquals(true, square.satisfiedConstraints(d));
    }

    @Test
    public void satisfiedColorConstraints(){
        Square square = new Square();
        square.setValueConstraint(3);
        Dice d = new Dice(Colors.VIOLET);
        Assert.assertEquals(true, square.satisfiedColorConstraint(d));
        square.setColorConstraint(Colors.RED);
        Assert.assertEquals(false, square.satisfiedColorConstraint(d));
        square.setColorConstraint(Colors.VIOLET);
        Assert.assertEquals(true, square.satisfiedColorConstraint(d));
    }

    @Test
    public void satisfiedValueConstraint(){
        Square square = new Square();
        square.setColorConstraint(Colors.RED);
        Dice d = new Dice(Colors.BLUE);
        d.setValue(4);
        Assert.assertEquals(true, square.satisfiedValueConstraint(d));
        square.setValueConstraint(3);
        Assert.assertEquals(false, square.satisfiedValueConstraint(d));
        square.setValueConstraint(4);
        Assert.assertEquals(true, square.satisfiedValueConstraint(d));
    }

    @Test
    public void putDice(){
        Square square = new Square();
        Dice d = new Dice(Colors.GREEN);
        d.setValue(4);
        Assert.assertEquals(true, square.putDice(d));
        Assert.assertEquals(false, square.putDice(d));
        square.removeDice();
        square.setColorConstraint(Colors.YELLOW);
        Assert.assertEquals(false, square.putDice(d));
        square.setColorConstraint(Colors.GREEN);
        Assert.assertEquals(true, square.putDice(d));
        square.removeDice();
        square.setColorConstraint(null);
        square.setValueConstraint(3);
        Assert.assertEquals(false, square.putDice(d));
        square.setValueConstraint(4);
        Assert.assertEquals(true, square.putDice(d));
    }

    @Test
    public void putDiceIgnoringColorConstraint(){
        Square square = new Square();
        Dice d = new Dice(Colors.GREEN);
        d.setValue(4);
        square.putDiceIgnoringColorConstraint(d);
        Assert.assertEquals(d, square.getDice());
        square.removeDice();
        square.setColorConstraint(Colors.RED);
        square.putDiceIgnoringColorConstraint(d);
        Assert.assertEquals(d, square.getDice());
        square.removeDice();
        square.setValueConstraint(3);
        square.putDiceIgnoringColorConstraint(d);
        Assert.assertEquals(null, square.getDice());
        square.setValueConstraint(4);
        square.putDiceIgnoringColorConstraint(d);
        Assert.assertEquals(d, square.getDice());
        Dice d2 = new Dice(Colors.YELLOW);
        d2.setValue(3);
        square.putDiceIgnoringColorConstraint(d2);
        Assert.assertEquals(d, square.getDice());
        square.removeDice();
        square.putDiceIgnoringColorConstraint(d2);
        Assert.assertEquals(null, square.getDice());
    }

    @Test
    public void putDiceIgnoringValueConstraint(){
        Square square = new Square();
        square.setValueConstraint(2);
        Dice d = new Dice(Colors.BLUE);
        d.setValue(4);
        Dice d2 = new Dice(Colors.VIOLET);
        d.setValue(3);
        Dice d3 = new Dice(Colors.BLUE);
        d.setValue(3);
        square.putDiceIgnoringValueConstraint(d);
        Assert.assertEquals(d, square.getDice());
        square.putDiceIgnoringValueConstraint(d2);
        Assert.assertEquals(d, square.getDice());
        square.removeDice();
        square.setColorConstraint(Colors.BLUE);
        square.putDiceIgnoringValueConstraint(d2);
        Assert.assertEquals(null, square.getDice());
        square.putDiceIgnoringValueConstraint(d);
        Assert.assertEquals(d, square.getDice());
        square.putDiceIgnoringValueConstraint(d3);
        Assert.assertEquals(d, square.getDice());
    }

    @Test
    public void putDiceIgnoringAllConstraints(){
        Square square = new Square();
        square.setValueConstraint(2);
        square.setColorConstraint(Colors.YELLOW);
        Dice d = new Dice(Colors.BLUE);
        d.setValue(4);
        Dice d2 = new Dice(Colors.VIOLET);
        d.setValue(3);
        square.putDiceIgnoringAllConstraints(d);
        Assert.assertEquals(d, square.getDice());
        square.putDiceIgnoringAllConstraints(d2);
        Assert.assertEquals(d, square.getDice());
    }

    @Test
    public void removeDice(){
        Square square = new Square();
        Dice d = new Dice(Colors.YELLOW);
        square.putDice(d);
        Assert.assertEquals(d, square.removeDice());
        Assert.assertEquals(null, square.removeDice());
    }


}
