package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
import it.polimi.ingsw.model.gamelogic.MatchSingleplayer;
import it.polimi.ingsw.model.gamelogic.PlayerMultiplayer;
import it.polimi.ingsw.model.gamelogic.PlayerSingleplayer;
import it.polimi.ingsw.model.gameobjects.*;
import it.polimi.ingsw.model.gameobjects.windowpatterncards.AuroraSagradis;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExchangeDiceRoundTrackTest {
    private ToolCard toolCard;
    private PlayerMultiplayer player;
    private PlayerSingleplayer singleplayer;
    private MatchMultiplayer match;
    private MatchSingleplayer matchSingleplayer;
    private Reserve reserve;

    @Before
    public void before() {
        match = mock(MatchMultiplayer.class);
        matchSingleplayer = mock(MatchSingleplayer.class);
        Board board = mock(Board.class);
        reserve = mock(Reserve.class);
        Dice dice = new Dice(Colors.BLUE);
        dice.setValue(4);
        List<Dice> list = new ArrayList<>();
        list.add(dice);
        Dice d = new Dice(Colors.YELLOW);
        d.setValue(4);
        list.add(d);
        Dice d1 = new Dice(Colors.GREEN);
        d1.setValue(4);
        list.add(d1);
        list.add(d1);
        list.add(d1);
        list.add(d1);
        list.add(d1);
        list.add(d1);
        list.add(d1);
        list.add(d1);
        player = new PlayerMultiplayer("player");
        singleplayer = new PlayerSingleplayer("Archi");
        WindowPatternCard schemeCard = new AuroraSagradis();
        player.setSchemeCard(schemeCard);
        singleplayer.setSchemeCard(schemeCard);
        player.setPickedDice(dice);
        player.setDice(0);
        singleplayer.setDice(0);
        player.setRound(2);
        singleplayer.setRound(2);
        player.setDiceChosenFromRound(0);
        singleplayer.setDiceChosenFromRound(0);
        singleplayer.setDiceToBeSacrificed(2);
        toolCard = new ToolCard("Taglierina Circolare", "tool5");
        RoundTrack roundTrack = new RoundTrack();
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
        when(match.getBoard()).thenReturn(board);
        when(matchSingleplayer.getBoard()).thenReturn(board);
        when(match.getBoard().getReserve()).thenReturn(reserve);
        when(matchSingleplayer.getBoard().getReserve()).thenReturn(reserve);
        when(reserve.getDices()).thenReturn(list);
        when(match.getBoard().getReserve().getDices()).thenReturn(list);
        when(matchSingleplayer.getBoard().getReserve().getDices()).thenReturn(list);
        when(match.getBoard().getRoundTrack()).thenReturn(roundTrack);
        when(matchSingleplayer.getBoard().getRoundTrack()).thenReturn(roundTrack);
    }

    @Test
    public void checkDice() {
        player.setNumFavorTokens(4);
        toolCard.useCard(player, match);
        player.setPickedDice(reserve.getDices().get(player.getDice()));
        Assert.assertEquals(5, player.getPickedDice().getValue());
        Assert.assertEquals(Colors.YELLOW, player.getPickedDice().getColor());
    }

    @Test
    public void checkDiceSingle() {
        toolCard.useCard(singleplayer, matchSingleplayer);
        singleplayer.setPickedDice(reserve.getDices().get(singleplayer.getDice()));
        Assert.assertEquals(5, singleplayer.getPickedDice().getValue());
        Assert.assertEquals(Colors.YELLOW, singleplayer.getPickedDice().getColor());
    }
}
