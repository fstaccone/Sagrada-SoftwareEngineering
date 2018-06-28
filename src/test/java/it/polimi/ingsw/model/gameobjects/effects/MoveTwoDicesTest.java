package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
import it.polimi.ingsw.model.gamelogic.MatchSingleplayer;
import it.polimi.ingsw.model.gamelogic.PlayerMultiplayer;
import it.polimi.ingsw.model.gamelogic.PlayerSingleplayer;
import it.polimi.ingsw.model.gameobjects.*;
import it.polimi.ingsw.model.gameobjects.windowpatterncards.SymphonyOfLight;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MoveTwoDicesTest {
    private SymphonyOfLight schemeCard;
    private ToolCard toolCard;
    private PlayerMultiplayer player;
    private PlayerSingleplayer singleplayer;
    private MatchMultiplayer match;
    private MatchSingleplayer matchSingleplayer;
    Reserve reserve;
    Board board;

    @Before
    public void before() {
        match = mock(MatchMultiplayer.class);
        matchSingleplayer = mock(MatchSingleplayer.class);
        board = mock(Board.class);
        reserve = mock(Reserve.class);
        List<Dice> list = new ArrayList<>();
        Dice d = new Dice(Colors.YELLOW);
        list.add(d);
        list.add(d);
        player = new PlayerMultiplayer("player");
        singleplayer = new PlayerSingleplayer("Archi");
        schemeCard = new SymphonyOfLight();
        player.setSchemeCard(schemeCard);
        singleplayer.setSchemeCard(schemeCard);
        singleplayer.setDiceToBeSacrificed(0);

        Dice dy = new Dice(Colors.YELLOW);
        dy.setValue(6);

        Dice dg = new Dice(Colors.GREEN);
        dg.setValue(3);

        Dice dr = new Dice(Colors.RED);
        dr.setValue(3);

        Dice db = new Dice(Colors.BLUE);
        db.setValue(4);

        player.getSchemeCard().putDice(dg, 0, 1);
        player.getSchemeCard().putDice(dy, 1, 0);
        player.getSchemeCard().putDice(dr, 2, 0);
        player.getSchemeCard().putDice(db, 3, 0);

        singleplayer.getSchemeCard().putDice(dg, 0, 1);
        singleplayer.getSchemeCard().putDice(dy, 1, 0);
        singleplayer.getSchemeCard().putDice(dr, 2, 0);
        singleplayer.getSchemeCard().putDice(db, 3, 0);

        player.setStartX1(0);
        player.setStartY1(1);
        player.setStartX2(1);
        player.setStartY2(0);
        player.setFinalX1(3);
        player.setFinalY1(1);
        player.setFinalX2(1);
        player.setFinalY2(1);

        singleplayer.setStartX1(0);
        singleplayer.setStartY1(1);
        singleplayer.setStartX2(1);
        singleplayer.setStartY2(0);
        singleplayer.setFinalX1(3);
        singleplayer.setFinalY1(1);
        singleplayer.setFinalX2(1);
        singleplayer.setFinalY2(1);

        toolCard = new ToolCard("Lathekin", "tool4");

        when(match.getBoard()).thenReturn(board);
        when(match.getBoard().getReserve()).thenReturn(reserve);
        when(matchSingleplayer.getBoard()).thenReturn(board);
        when(matchSingleplayer.getBoard().getReserve()).thenReturn(reserve);
        when(reserve.getDices()).thenReturn(list);
        when(match.getBoard().getReserve().getDices()).thenReturn(list);
        when(matchSingleplayer.getBoard().getReserve().getDices()).thenReturn(list);
    }

    @Test
    public void correctMoves() {
        toolCard.useCard(player, match);
        Assert.assertEquals(Colors.GREEN, player.getSchemeCard().getWindow()[3][1].getDice().getColor());
        Assert.assertEquals(3, player.getSchemeCard().getWindow()[3][1].getDice().getValue());
        Assert.assertEquals(Colors.YELLOW, player.getSchemeCard().getWindow()[1][1].getDice().getColor());
        Assert.assertEquals(6, player.getSchemeCard().getWindow()[1][1].getDice().getValue());
    }

    @Test
    public void correctMovesSingle(){
        toolCard.useCard(singleplayer, matchSingleplayer);
        Assert.assertEquals(Colors.GREEN, singleplayer.getSchemeCard().getWindow()[3][1].getDice().getColor());
        Assert.assertEquals(3, singleplayer.getSchemeCard().getWindow()[3][1].getDice().getValue());
        Assert.assertEquals(Colors.YELLOW, singleplayer.getSchemeCard().getWindow()[1][1].getDice().getColor());
        Assert.assertEquals(6, singleplayer.getSchemeCard().getWindow()[1][1].getDice().getValue());
    }

    @Test
    public void wrongMoves(){
        player.setNumFavorTokens(8);
        player.setStartX1(0);
        player.setStartY1(1);
        player.setStartX2(1);
        player.setStartY2(0);
        player.setFinalX1(3);
        player.setFinalY1(1);
        player.setFinalX2(2);
        player.setFinalY2(1);
        toolCard.useCard(player, match);
        System.out.println(player.getSchemeCard().toString());
        Assert.assertEquals(Colors.GREEN, player.getSchemeCard().getWindow()[0][1].getDice().getColor());
        Assert.assertEquals(3, player.getSchemeCard().getWindow()[0][1].getDice().getValue());
        Assert.assertEquals(Colors.YELLOW, player.getSchemeCard().getWindow()[1][0].getDice().getColor());
        Assert.assertEquals(6, player.getSchemeCard().getWindow()[1][0].getDice().getValue());
        player.setStartX1(0);
        player.setStartY1(1);
        player.setStartX2(1);
        player.setStartY2(0);
        player.setFinalX1(2);
        player.setFinalY1(1);
        player.setFinalX2(1);
        player.setFinalY2(1);
        toolCard.useCard(player, match);
        System.out.println(player.getSchemeCard().toString());
        Assert.assertEquals(Colors.GREEN, player.getSchemeCard().getWindow()[0][1].getDice().getColor());
        Assert.assertEquals(3, player.getSchemeCard().getWindow()[0][1].getDice().getValue());
        Assert.assertEquals(Colors.YELLOW, player.getSchemeCard().getWindow()[1][0].getDice().getColor());
        Assert.assertEquals(6, player.getSchemeCard().getWindow()[1][0].getDice().getValue());
    }

    @Test
    public void wrongMovesSingle(){
        singleplayer.setStartX1(0);
        singleplayer.setStartY1(1);
        singleplayer.setStartX2(1);
        singleplayer.setStartY2(0);
        singleplayer.setFinalX1(3);
        singleplayer.setFinalY1(1);
        singleplayer.setFinalX2(2);
        singleplayer.setFinalY2(1);
        toolCard.useCard(singleplayer, matchSingleplayer);
        System.out.println(singleplayer.getSchemeCard().toString());
        Assert.assertEquals(Colors.GREEN, singleplayer.getSchemeCard().getWindow()[0][1].getDice().getColor());
        Assert.assertEquals(3, singleplayer.getSchemeCard().getWindow()[0][1].getDice().getValue());
        Assert.assertEquals(Colors.YELLOW, singleplayer.getSchemeCard().getWindow()[1][0].getDice().getColor());
        Assert.assertEquals(6, singleplayer.getSchemeCard().getWindow()[1][0].getDice().getValue());
        singleplayer.setStartX1(0);
        singleplayer.setStartY1(1);
        singleplayer.setStartX2(1);
        singleplayer.setStartY2(0);
        singleplayer.setFinalX1(2);
        singleplayer.setFinalY1(1);
        singleplayer.setFinalX2(1);
        singleplayer.setFinalY2(1);
        toolCard.useCard(singleplayer, matchSingleplayer);
        System.out.println(singleplayer.getSchemeCard().toString());
        Assert.assertEquals(Colors.GREEN, singleplayer.getSchemeCard().getWindow()[0][1].getDice().getColor());
        Assert.assertEquals(3, singleplayer.getSchemeCard().getWindow()[0][1].getDice().getValue());
        Assert.assertEquals(Colors.YELLOW, singleplayer.getSchemeCard().getWindow()[1][0].getDice().getColor());
        Assert.assertEquals(6, singleplayer.getSchemeCard().getWindow()[1][0].getDice().getValue());
    }
}
