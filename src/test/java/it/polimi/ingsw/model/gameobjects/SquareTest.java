package it.polimi.ingsw.model.gameobjects;

import org.junit.Assert;
import org.junit.Test;

public class SquareTest {

    @Test
    public void Square() {
        Square square = new Square();
        Assert.assertNull(square.getColorConstraint());
        Assert.assertEquals(0, square.getValueConstraint());
    }

    @Test
    public void occupiedSquare() {
        Square square = new Square();
        Assert.assertFalse(square.occupiedSquare());
        Dice d = new Dice(Colors.YELLOW);
        square.putDice(d);
        Assert.assertTrue(square.occupiedSquare());
    }

    @Test
    public void satisfiedConstraints() {
        Square square = new Square();
        Dice d = new Dice(Colors.RED);
        Assert.assertTrue(square.satisfiedConstraints(d));
        square.setColorConstraint(Colors.BLUE);
        Assert.assertFalse(square.satisfiedConstraints(d));
        square.setColorConstraint(Colors.RED);
        Assert.assertTrue(square.satisfiedConstraints(d));
        d.setValue(4);
        Assert.assertTrue(square.satisfiedConstraints(d));
        square.setColorConstraint(null);
        square.setValueConstraint(3);
        Assert.assertFalse(square.satisfiedConstraints(d));
        square.setValueConstraint(4);
        Assert.assertTrue(square.satisfiedConstraints(d));
    }

    @Test
    public void satisfiedColorConstraints() {
        Square square = new Square();
        square.setValueConstraint(3);
        Dice d = new Dice(Colors.VIOLET);
        Assert.assertTrue(square.satisfiedColorConstraint(d));
        square.setColorConstraint(Colors.RED);
        Assert.assertFalse(square.satisfiedColorConstraint(d));
        square.setColorConstraint(Colors.VIOLET);
        Assert.assertTrue(square.satisfiedColorConstraint(d));
    }

    @Test
    public void satisfiedValueConstraint() {
        Square square = new Square();
        square.setColorConstraint(Colors.RED);
        Dice d = new Dice(Colors.BLUE);
        d.setValue(4);
        Assert.assertTrue(square.satisfiedValueConstraint(d));
        square.setValueConstraint(3);
        Assert.assertFalse(square.satisfiedValueConstraint(d));
        square.setValueConstraint(4);
        Assert.assertTrue(square.satisfiedValueConstraint(d));
    }

    @Test
    public void putDice() {
        Square square = new Square();
        Dice d = new Dice(Colors.GREEN);
        d.setValue(4);
        Assert.assertTrue(square.putDice(d));
        Assert.assertFalse(square.putDice(d));
        square.removeDice();
        square.setColorConstraint(Colors.YELLOW);
        Assert.assertFalse(square.putDice(d));
        square.setColorConstraint(Colors.GREEN);
        Assert.assertTrue(square.putDice(d));
        square.removeDice();
        square.setColorConstraint(null);
        square.setValueConstraint(3);
        Assert.assertFalse(square.putDice(d));
        square.setValueConstraint(4);
        Assert.assertTrue(square.putDice(d));
    }

    @Test
    public void putDiceIgnoringColorConstraint() {
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
        Assert.assertNull(square.getDice());
        square.setValueConstraint(4);
        square.putDiceIgnoringColorConstraint(d);
        Assert.assertEquals(d, square.getDice());
        Dice d2 = new Dice(Colors.YELLOW);
        d2.setValue(3);
        square.putDiceIgnoringColorConstraint(d2);
        Assert.assertEquals(d, square.getDice());
        square.removeDice();
        square.putDiceIgnoringColorConstraint(d2);
        Assert.assertNull(square.getDice());
    }

    @Test
    public void putDiceIgnoringValueConstraint() {
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
        Assert.assertNull(square.getDice());
        square.putDiceIgnoringValueConstraint(d);
        Assert.assertEquals(d, square.getDice());
        square.putDiceIgnoringValueConstraint(d3);
        Assert.assertEquals(d, square.getDice());
    }

    @Test
    public void putDiceIgnoringAllConstraints() {
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
    public void removeDice() {
        Square square = new Square();
        Dice d = new Dice(Colors.YELLOW);
        square.putDice(d);
        Assert.assertEquals(d, square.removeDice());
        Assert.assertNull(square.removeDice());
    }


}
