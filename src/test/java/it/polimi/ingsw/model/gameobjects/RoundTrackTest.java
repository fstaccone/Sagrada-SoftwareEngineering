package it.polimi.ingsw.model.gameobjects;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * All the @Test have the same name of the method they test, check the method implementation for a detailed description
 */
public class RoundTrackTest {

    @Test
    public void putDices() {
        RoundTrack roundTrack = new RoundTrack();
        List<Dice> dicesToPut = new ArrayList<>();
        Dice blueDice = new Dice(Colors.BLUE);
        blueDice.setValue(3);
        Dice yellowDice = new Dice(Colors.YELLOW);
        yellowDice.setValue(5);
        dicesToPut.add(blueDice);
        dicesToPut.add(yellowDice);
        roundTrack.putDices(dicesToPut, 3);
        System.out.println(roundTrack.toString());
        ByteArrayInputStream in = new ByteArrayInputStream("3 0".getBytes());
        System.setIn(in);
        Dice d3 = roundTrack.getDice();
        Assert.assertEquals(blueDice, d3);
    }

    @Test
    public void switchDices() {
        RoundTrack roundTrack = new RoundTrack();
        List<Dice> dicesToPut = new ArrayList<>();
        Dice blueDice = new Dice(Colors.BLUE);
        blueDice.setValue(3);
        Dice yellowDice = new Dice(Colors.YELLOW);
        yellowDice.setValue(5);
        dicesToPut.add(blueDice);
        dicesToPut.add(yellowDice);
        roundTrack.putDices(dicesToPut, 3);
        Dice diceToSwitch = new Dice(Colors.GREEN);
        diceToSwitch.setValue(4);
        Dice d1 = roundTrack.switchDice(diceToSwitch, 4, 0);
        Assert.assertEquals(blueDice, d1);
        ByteArrayInputStream in = new ByteArrayInputStream("3 0".getBytes());
        System.setIn(in);
        Assert.assertEquals(diceToSwitch, roundTrack.getDice());
    }

    @Test
    public void sumForSingleplayer() {
        RoundTrack roundTrack = new RoundTrack();
        List<Dice> dicesToPut = new ArrayList<>();
        Dice blueDice = new Dice(Colors.BLUE);
        blueDice.setValue(3);
        Dice yellowDice = new Dice(Colors.YELLOW);
        yellowDice.setValue(5);
        dicesToPut.add(blueDice);
        dicesToPut.add(yellowDice);
        roundTrack.putDices(dicesToPut, 3);
        Assert.assertEquals(8, roundTrack.sumForSinglePlayer());
    }
}
