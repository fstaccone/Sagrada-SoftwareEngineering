package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.Room;
import it.polimi.ingsw.model.gameobjects.*;
import it.polimi.ingsw.model.gameobjects.windowpatterncards.KaleidoscopicDream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExchangeDiceRoundTrackTest {
    private KaleidoscopicDream schemeCard;
    private ToolCard toolCard;
    private Player player;
    private Match match;
    private Room room;
    @Before
    public void before() {
        room = mock(Room.class);
        match = mock(Match.class);
        Board board = mock(Board.class);
        Dice dice = new Dice(Colors.BLUE);
        dice.setValue(4);
        player = new PlayerMultiplayer("player", room);
        schemeCard = new KaleidoscopicDream();
        player.setSchemeCard(schemeCard);
        player.setPickedDice(dice);
        toolCard = new ToolCard("Taglierina Circolare");
        RoundTrack roundTrack = new RoundTrack();
        roundTrack.showRoundTrack();
        List<Dice> list0 = new LinkedList<>();
        Dice d00 = new Dice(Colors.RED);
        d00.setValue(1);
        list0.add(d00);
        List<Dice> list1 = new LinkedList<>();
        Dice d10 = new Dice(Colors.YELLOW);
        d10.setValue(5);
        list1.add(d10);
        Dice d11 = new Dice(Colors.BLUE);
        d11.setValue(2);
        list1.add(d11);
        roundTrack.putDices(list0,0);
        roundTrack.putDices(list1, 1);
        when(match.getBoard()).thenReturn(board);
        when(match.getBoard().getRoundTrack()).thenReturn(roundTrack);
        ByteArrayInputStream in = new ByteArrayInputStream("1 0".getBytes());
        System.setIn(in);
    }
    @Test
    public void checkDice(){
        System.out.println(player.getPickedDice().toString());
        toolCard.useCard(player, match);
        System.out.println(player.getPickedDice().toString());
        Assert.assertEquals(5,player.getPickedDice().getValue());
        Assert.assertEquals(Colors.YELLOW, player.getPickedDice().getColor());
    }
}
