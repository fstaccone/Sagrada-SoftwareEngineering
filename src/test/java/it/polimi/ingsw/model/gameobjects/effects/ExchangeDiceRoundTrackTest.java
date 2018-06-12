package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
import it.polimi.ingsw.model.gameobjects.*;
import it.polimi.ingsw.model.gameobjects.windowpatterncards.AuroraSagradis;
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
    private WindowPatternCard schemeCard;
    private ToolCard toolCard;
    private PlayerMultiplayer player;
    private MatchMultiplayer match;
    private RoundTrack roundTrack;
    private Reserve reserve;

    @Before
    public void before() {
        match = mock(MatchMultiplayer.class);
        Board board = mock(Board.class);
        reserve = mock(Reserve.class);
        Dice dice = new Dice(Colors.BLUE);
        dice.setValue(4);
        List<Dice> list = new ArrayList<>();
        list.add(dice);
        Dice d = new Dice(Colors.YELLOW);
        d.setValue(4);
        list.add(d);
        // modificato in seguito all'introduzione di Lobby
        player = new PlayerMultiplayer("player");
        schemeCard = new AuroraSagradis();
        player.setSchemeCard(schemeCard);
        player.setPickedDice(dice);
        player.setDice(0);
        player.setRound(2);
        player.setDiceChosenFromRound(0);
        toolCard = new ToolCard("Taglierina Circolare", "tool5");
        roundTrack = new RoundTrack();
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
        roundTrack.putDices(list0, 0);
        roundTrack.putDices(list1, 1);
        roundTrack.showRoundTrack();
        when(match.getBoard()).thenReturn(board);
        when(match.getBoard().getReserve()).thenReturn(reserve);
        when(reserve.getDices()).thenReturn(list);
        when(match.getBoard().getReserve().getDices()).thenReturn(list);
        when(match.getBoard().getRoundTrack()).thenReturn(roundTrack);
        ByteArrayInputStream in = new ByteArrayInputStream("2 0".getBytes());
        System.setIn(in);
    }

    @Test
    public void checkDice() {
        System.out.println(player.getPickedDice().toString());
        player.setNumFavorTokens(4);
        toolCard.useCard(player, match);
        roundTrack.showRoundTrack();
        player.setPickedDice(reserve.getDices().get(player.getDice()));
        System.out.println(player.getPickedDice().toString());
        Assert.assertEquals(5, player.getPickedDice().getValue());
        Assert.assertEquals(Colors.YELLOW, player.getPickedDice().getColor());
    }
}
