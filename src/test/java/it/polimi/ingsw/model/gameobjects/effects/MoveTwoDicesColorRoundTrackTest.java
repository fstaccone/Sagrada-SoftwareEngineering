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
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MoveTwoDicesColorRoundTrackTest {
    private KaleidoscopicDream schemeCard;
    private ToolCard toolCard;
    private PlayerMultiplayer player;
    private PlayerSingleplayer singleplayer;
    private MatchMultiplayer match;
    private MatchSingleplayer matchSingleplayer;
    private RoundTrack roundTrack;
    private Reserve reserve;

    @Before
    public void before() {
        match = mock(MatchMultiplayer.class);
        matchSingleplayer = mock(MatchSingleplayer.class);
        Board board = mock(Board.class);
        reserve = mock(Reserve.class);
        List<Dice> dices = new ArrayList<>();
        Dice d = new Dice(Colors.BLUE);
        d.setValue(3);
        dices.add(d);
        dices.add(d);
        dices.add(d);
        dices.add(d);
        // modificato in seguito all'introduzione di Lobby
        player = new PlayerMultiplayer("player");
        singleplayer = new PlayerSingleplayer("Archi");
        schemeCard = new KaleidoscopicDream();
        player.setSchemeCard(schemeCard);
        singleplayer.setSchemeCard(schemeCard);
        toolCard = new ToolCard("Taglierina Manuale", "tool12");
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
        when(reserve.getDices()).thenReturn(dices);
        when(match.getBoard()).thenReturn(board);
        when(matchSingleplayer.getBoard()).thenReturn(board);
        when(match.getBoard().getRoundTrack()).thenReturn(roundTrack);
        when(matchSingleplayer.getBoard().getRoundTrack()).thenReturn(roundTrack);
        when(board.getReserve()).thenReturn(reserve);
        when(match.getBoard().getReserve()).thenReturn(reserve);
        when(matchSingleplayer.getBoard().getReserve()).thenReturn(reserve);

        Dice dy = new Dice(Colors.YELLOW);
        dy.setValue(1);

        Dice dg = new Dice(Colors.GREEN);
        dg.setValue(5);

        Dice dr = new Dice(Colors.RED);
        dr.setValue(3);

        Dice dr2 = new Dice(Colors.RED);
        dr2.setValue(4);

        Dice db = new Dice(Colors.BLUE);
        db.setValue(2);

        Dice dv = new Dice(Colors.VIOLET);
        dv.setValue(2);

        player.getSchemeCard().putDice(dy, 0, 0);
        player.getSchemeCard().putDice(dg, 1, 0);
        player.getSchemeCard().putDice(dr, 2, 0);
        player.getSchemeCard().putDice(db, 3, 0);
        player.getSchemeCard().putDice(dr2, 3, 1);
        player.getSchemeCard().putDice(dy, 1, 1);
        System.out.println(player.getSchemeCard().toString());
        player.setRound(1);
        player.setDiceChosenFromRound(0);
        player.setNumFavorTokens(5);
        player.setStartX1(2);
        player.setStartY1(0);
        player.setStartX2(3);
        player.setStartY2(1);
        player.setFinalX1(2);
        player.setFinalY1(2);
        player.setFinalX2(0);
        player.setFinalY2(2);
        singleplayer.setDiceToBeSacrificed(0);
        singleplayer.setRound(1);
        singleplayer.setDiceChosenFromRound(0);

        toolCard = new ToolCard("Taglierina Manuale", "tool12");
    }

    @Test
    public void correctAction() {
        toolCard.useCard(player, match);
        Assert.assertNull(player.getSchemeCard().getWindow()[2][0].getDice());
        Assert.assertNull(player.getSchemeCard().getWindow()[3][1].getDice());
        Assert.assertEquals(Colors.RED, player.getSchemeCard().getWindow()[2][2].getDice().getColor());
        Assert.assertEquals(3, player.getSchemeCard().getWindow()[2][2].getDice().getValue());
        Assert.assertEquals(Colors.RED, player.getSchemeCard().getWindow()[0][2].getDice().getColor());
        Assert.assertEquals(4, player.getSchemeCard().getWindow()[0][2].getDice().getValue());
    }

    @Test
    public void correctActionSingle(){
        singleplayer.setStartX1(2);
        singleplayer.setStartY1(0);
        singleplayer.setStartX2(3);
        singleplayer.setStartY2(1);
        singleplayer.setFinalX1(2);
        singleplayer.setFinalY1(2);
        singleplayer.setFinalX2(0);
        singleplayer.setFinalY2(2);
        toolCard.useCard(singleplayer, matchSingleplayer);
        Assert.assertNull(singleplayer.getSchemeCard().getWindow()[2][0].getDice());
        Assert.assertNull(singleplayer.getSchemeCard().getWindow()[3][1].getDice());
        Assert.assertEquals(Colors.RED, singleplayer.getSchemeCard().getWindow()[2][2].getDice().getColor());
        Assert.assertEquals(3, singleplayer.getSchemeCard().getWindow()[2][2].getDice().getValue());
        Assert.assertEquals(Colors.RED, singleplayer.getSchemeCard().getWindow()[0][2].getDice().getColor());
        Assert.assertEquals(4, singleplayer.getSchemeCard().getWindow()[0][2].getDice().getValue());
    }

    @Test
    public void wrongMoves(){
        player.setStartX1(2);
        player.setStartY1(0);
        player.setStartX2(3);
        player.setStartY2(1);
        player.setFinalX1(2);
        player.setFinalY1(2);
        player.setFinalX2(3);
        player.setFinalY2(3);
        toolCard.useCard(player, match);
        Assert.assertEquals(Colors.RED, player.getSchemeCard().getWindow()[2][0].getDice().getColor());
        Assert.assertEquals(3, player.getSchemeCard().getWindow()[2][0].getDice().getValue());
        Assert.assertEquals(Colors.RED, player.getSchemeCard().getWindow()[3][1].getDice().getColor());
        Assert.assertEquals(4, player.getSchemeCard().getWindow()[3][1].getDice().getValue());
        player.setNumFavorTokens(8);
        player.setStartX1(2);
        player.setStartY1(0);
        player.setStartX2(3);
        player.setStartY2(1);
        player.setFinalX1(3);
        player.setFinalY1(3);
        player.setFinalX2(0);
        player.setFinalY2(2);
        toolCard.useCard(player, match);
        Assert.assertEquals(Colors.RED, player.getSchemeCard().getWindow()[2][0].getDice().getColor());
        Assert.assertEquals(3, player.getSchemeCard().getWindow()[2][0].getDice().getValue());
        Assert.assertEquals(Colors.RED, player.getSchemeCard().getWindow()[3][1].getDice().getColor());
        Assert.assertEquals(4, player.getSchemeCard().getWindow()[3][1].getDice().getValue());
    }

    @Test
    public void wrongMovesSingle(){
        singleplayer.setStartX1(2);
        singleplayer.setStartY1(0);
        singleplayer.setStartX2(3);
        singleplayer.setStartY2(1);
        singleplayer.setFinalX1(2);
        singleplayer.setFinalY1(2);
        singleplayer.setFinalX2(3);
        singleplayer.setFinalY2(3);
        toolCard.useCard(singleplayer, matchSingleplayer);
        Assert.assertEquals(Colors.RED, singleplayer.getSchemeCard().getWindow()[2][0].getDice().getColor());
        Assert.assertEquals(3, singleplayer.getSchemeCard().getWindow()[2][0].getDice().getValue());
        Assert.assertEquals(Colors.RED, singleplayer.getSchemeCard().getWindow()[3][1].getDice().getColor());
        Assert.assertEquals(4, singleplayer.getSchemeCard().getWindow()[3][1].getDice().getValue());
        singleplayer.setStartX1(2);
        singleplayer.setStartY1(0);
        singleplayer.setStartX2(3);
        singleplayer.setStartY2(1);
        singleplayer.setFinalX1(3);
        singleplayer.setFinalY1(3);
        singleplayer.setFinalX2(0);
        singleplayer.setFinalY2(2);
        toolCard.useCard(singleplayer, matchSingleplayer);
        Assert.assertEquals(Colors.RED, singleplayer.getSchemeCard().getWindow()[2][0].getDice().getColor());
        Assert.assertEquals(3, singleplayer.getSchemeCard().getWindow()[2][0].getDice().getValue());
        Assert.assertEquals(Colors.RED, singleplayer.getSchemeCard().getWindow()[3][1].getDice().getColor());
        Assert.assertEquals(4, singleplayer.getSchemeCard().getWindow()[3][1].getDice().getValue());
    }

    @Test
    public void movingOneDiceOnly(){
        player.setStartX1(2);
        player.setStartY1(0);
        player.setStartX2(-1);
        player.setStartY2(-1);
        player.setFinalX1(2);
        player.setFinalY1(2);
        toolCard.useCard(player, match);
        Assert.assertNull(player.getSchemeCard().getWindow()[2][0].getDice());
        Assert.assertEquals(Colors.RED, player.getSchemeCard().getWindow()[3][1].getDice().getColor());
        Assert.assertEquals(4, player.getSchemeCard().getWindow()[3][1].getDice().getValue());
        Assert.assertEquals(Colors.RED, player.getSchemeCard().getWindow()[2][2].getDice().getColor());
        Assert.assertEquals(3, player.getSchemeCard().getWindow()[2][2].getDice().getValue());
    }

    @Test
    public void movingOneDiceOnlySingle(){
        singleplayer.setStartX1(2);
        singleplayer.setStartY1(0);
        singleplayer.setStartX2(-1);
        singleplayer.setStartY2(-1);
        singleplayer.setFinalX1(2);
        singleplayer.setFinalY1(2);
        toolCard.useCard(singleplayer, matchSingleplayer);
        Assert.assertNull(singleplayer.getSchemeCard().getWindow()[2][0].getDice());
        Assert.assertEquals(Colors.RED, singleplayer.getSchemeCard().getWindow()[3][1].getDice().getColor());
        Assert.assertEquals(4, singleplayer.getSchemeCard().getWindow()[3][1].getDice().getValue());
        Assert.assertEquals(Colors.RED, singleplayer.getSchemeCard().getWindow()[2][2].getDice().getColor());
        Assert.assertEquals(3, singleplayer.getSchemeCard().getWindow()[2][2].getDice().getValue());
    }
}