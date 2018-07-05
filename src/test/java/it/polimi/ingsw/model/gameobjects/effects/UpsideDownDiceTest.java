package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
import it.polimi.ingsw.model.gamelogic.MatchSingleplayer;
import it.polimi.ingsw.model.gamelogic.PlayerMultiplayer;
import it.polimi.ingsw.model.gamelogic.PlayerSingleplayer;
import it.polimi.ingsw.model.gameobjects.*;
import it.polimi.ingsw.model.gameobjects.windowpatterncards.KaleidoscopicDream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UpsideDownDiceTest {
    private KaleidoscopicDream schemeCard;
    private ToolCard toolCard;
    private PlayerMultiplayer player;
    private PlayerSingleplayer singleplayer;
    private MatchMultiplayer match;
    private MatchSingleplayer matchSingleplayer;
    private Reserve reserve;
    private Board board;

    @Before
    public void before() {
        match = mock(MatchMultiplayer.class);
        matchSingleplayer = mock(MatchSingleplayer.class);
        board = mock(Board.class);
        reserve = mock(Reserve.class);
        Dice d1 = new Dice(Colors.BLUE);
        d1.setValue(1);
        Dice d2 = new Dice(Colors.YELLOW);
        d2.setValue(2);
        Dice d3 = new Dice(Colors.VIOLET);
        d3.setValue(3);
        Dice d4 = new Dice(Colors.RED);
        d4.setValue(4);
        Dice d5 = new Dice(Colors.GREEN);
        d5.setValue(5);
        Dice d6 = new Dice(Colors.BLUE);
        d6.setValue(6);
        Dice d7 = new Dice(Colors.BLUE);
        d7.setValue(7);
        Dice dice = new Dice(Colors.GREEN);
        List<Dice> dices = new ArrayList<>();
        dices.add(d1);
        dices.add(d2);
        dices.add(d3);
        dices.add(d4);
        dices.add(d5);
        dices.add(d6);
        dices.add(d7);
        dices.add(dice);
        dices.add(dice);
        dices.add(dice);
        dices.add(dice);
        dices.add(dice);
        dices.add(dice);
        dices.add(dice);
        dices.add(dice);
        dices.add(dice);

        // modificato in seguito all'introduzione di Lobby
        player = new PlayerMultiplayer("player");
        singleplayer = new PlayerSingleplayer("Archi");
        schemeCard = new KaleidoscopicDream();
        player.setSchemeCard(schemeCard);
        toolCard = new ToolCard("Tampone Diamantato", "tool10");
        player.setNumFavorTokens(14);
        when(reserve.getDices()).thenReturn(dices);
        when(match.getBoard()).thenReturn(board);
        when(matchSingleplayer.getBoard()).thenReturn(board);
        when(board.getReserve()).thenReturn(reserve);
        when(match.getBoard().getReserve()).thenReturn(reserve);
        when(matchSingleplayer.getBoard().getReserve()).thenReturn(reserve);
    }

    @Test
    public void checkDice() {
        player.setDice(0);
        toolCard.useCard(player, match);
        Assert.assertEquals(6, reserve.getDices().get(player.getDice()).getValue());
        Assert.assertEquals(Colors.BLUE, reserve.getDices().get(player.getDice()).getColor());
        player.setDice(1);
        toolCard.useCard(player, match);
        Assert.assertEquals(5, reserve.getDices().get(player.getDice()).getValue());
        Assert.assertEquals(Colors.YELLOW, reserve.getDices().get(player.getDice()).getColor());
        player.setDice(2);
        toolCard.useCard(player, match);
        Assert.assertEquals(4, reserve.getDices().get(player.getDice()).getValue());
        Assert.assertEquals(Colors.VIOLET, reserve.getDices().get(player.getDice()).getColor());
        player.setDice(3);
        toolCard.useCard(player, match);
        Assert.assertEquals(3, reserve.getDices().get(player.getDice()).getValue());
        Assert.assertEquals(Colors.RED, reserve.getDices().get(player.getDice()).getColor());
        player.setDice(4);
        toolCard.useCard(player, match);
        Assert.assertEquals(2, reserve.getDices().get(player.getDice()).getValue());
        Assert.assertEquals(Colors.GREEN, reserve.getDices().get(player.getDice()).getColor());
        player.setDice(5);
        toolCard.useCard(player, match);
        Assert.assertEquals(1, reserve.getDices().get(player.getDice()).getValue());
        Assert.assertEquals(Colors.BLUE, reserve.getDices().get(player.getDice()).getColor());
        player.setDice(6);
        Assert.assertEquals(false, toolCard.useCard(player, match));
        player.setDice(4);
        toolCard.useCard(player, match);
        Assert.assertEquals(5, reserve.getDices().get(player.getDice()).getValue());
        Assert.assertEquals(Colors.GREEN, reserve.getDices().get(player.getDice()).getColor());
        Assert.assertEquals(false, toolCard.useCard(player, match));
    }

    @Test
    public void dice1() {
        singleplayer.setDice(0);
        singleplayer.setDiceToBeSacrificed(7);
        toolCard.useCard(singleplayer, matchSingleplayer);
        Assert.assertEquals(6, reserve.getDices().get(singleplayer.getDice()).getValue());
        Assert.assertEquals(Colors.BLUE, reserve.getDices().get(singleplayer.getDice()).getColor());
    }

    @Test
    public void dice2() {
        singleplayer.setDice(1);
        singleplayer.setDiceToBeSacrificed(7);
        toolCard.useCard(singleplayer, matchSingleplayer);
        Assert.assertEquals(5, reserve.getDices().get(singleplayer.getDice()).getValue());
        Assert.assertEquals(Colors.YELLOW, reserve.getDices().get(singleplayer.getDice()).getColor());
    }
    @Test
    public void dice3() {
        singleplayer.setDice(2);
        singleplayer.setDiceToBeSacrificed(7);
        toolCard.useCard(singleplayer, matchSingleplayer);
        Assert.assertEquals(4, reserve.getDices().get(singleplayer.getDice()).getValue());
        Assert.assertEquals(Colors.VIOLET, reserve.getDices().get(singleplayer.getDice()).getColor());
    }
    @Test
    public void dice4() {
        singleplayer.setDice(3);
        singleplayer.setDiceToBeSacrificed(7);
        toolCard.useCard(singleplayer, matchSingleplayer);
        Assert.assertEquals(3, reserve.getDices().get(singleplayer.getDice()).getValue());
        Assert.assertEquals(Colors.RED, reserve.getDices().get(singleplayer.getDice()).getColor());
    }
    @Test
    public void dice5() {
        singleplayer.setDice(4);
        singleplayer.setDiceToBeSacrificed(7);
        toolCard.useCard(singleplayer, matchSingleplayer);
        Assert.assertEquals(2, reserve.getDices().get(singleplayer.getDice()).getValue());
        Assert.assertEquals(Colors.GREEN, reserve.getDices().get(singleplayer.getDice()).getColor());
    }
    @Test
    public void dice6() {
        singleplayer.setDice(5);
        singleplayer.setDiceToBeSacrificed(7);
        toolCard.useCard(singleplayer, matchSingleplayer);
        Assert.assertEquals(1, reserve.getDices().get(singleplayer.getDice()).getValue());
        Assert.assertEquals(Colors.BLUE, reserve.getDices().get(singleplayer.getDice()).getColor());
        singleplayer.setDice(6);
        singleplayer.setDiceToBeSacrificed(7);
        Assert.assertEquals(false, toolCard.useCard(singleplayer, matchSingleplayer));
    }

}
