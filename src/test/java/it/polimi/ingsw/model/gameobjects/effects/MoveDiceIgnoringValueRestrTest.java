package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
import it.polimi.ingsw.model.gamelogic.MatchSingleplayer;
import it.polimi.ingsw.model.gameobjects.*;
import it.polimi.ingsw.model.gameobjects.windowpatterncards.KaleidoscopicDream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MoveDiceIgnoringValueRestrTest {
    private KaleidoscopicDream schemeCard;
    private ToolCard toolCard;
    private PlayerMultiplayer player;
    private PlayerSingleplayer singleplayer;
    private MatchMultiplayer match;
    private MatchSingleplayer matchSingleplayer;
    private Board board;
    private Reserve reserve;

    @Before
    public void before() {
        board = mock(Board.class);
        match = mock(MatchMultiplayer.class);
        matchSingleplayer = mock(MatchSingleplayer.class);
        reserve = mock(Reserve.class);
        player = new PlayerMultiplayer("player");
        singleplayer = new PlayerSingleplayer("Archi");
        schemeCard = new KaleidoscopicDream();
        player.setSchemeCard(schemeCard);
        singleplayer.setSchemeCard(schemeCard);

        List<Dice> list = new ArrayList<>();
        Dice d = new Dice(Colors.RED);
        d.setValue(4);
        list.add(d);

        Dice dy = new Dice(Colors.YELLOW);
        dy.setValue(2);

        Dice dg = new Dice(Colors.GREEN);
        dg.setValue(5);

        Dice dr = new Dice(Colors.RED);
        dr.setValue(3);

        Dice db = new Dice(Colors.BLUE);
        db.setValue(2);

        Dice dv = new Dice(Colors.VIOLET);
        dv.setValue(3);

        list.add(dr);

        player.getSchemeCard().putFirstDice(dy, 0, 0);
        player.getSchemeCard().putDice(dg, 1, 0);
        player.getSchemeCard().putDice(dr, 2, 0);
        player.getSchemeCard().putDice(db, 3, 0);
        player.getSchemeCard().putDice(dv, 1, 1);

        singleplayer.getSchemeCard().putFirstDice(dy, 0, 0);
        singleplayer.getSchemeCard().putDice(dr, 2, 0);
        singleplayer.getSchemeCard().putDice(db, 3, 0);
        singleplayer.getSchemeCard().putDice(dv, 1, 1);

        toolCard = new ToolCard("Alesatore per Lamina di Rame", "tool3");
        player.setStartX1(3);
        player.setStartY1(0);
        player.setFinalX1(1);
        player.setFinalY1(2);

        singleplayer.setDiceToBeSacrificed(0);
        singleplayer.setStartX1(3);
        singleplayer.setStartY1(0);
        singleplayer.setFinalX1(1);
        singleplayer.setFinalY1(2);

        when(match.getBoard()).thenReturn(board);
        when(match.getBoard().getReserve()).thenReturn(reserve);
        when(matchSingleplayer.getBoard()).thenReturn(board);
        when(matchSingleplayer.getBoard().getReserve()).thenReturn(reserve);
        when(reserve.getDices()).thenReturn(list);
        when(match.getBoard().getReserve().getDices()).thenReturn(list);
        when(matchSingleplayer.getBoard().getReserve().getDices()).thenReturn(list);
    }

    @Test
    public void checkPoints() {
        toolCard.useCard(player, match);
        System.out.println(player.getSchemeCard().toString());
        Assert.assertEquals(2, player.getSchemeCard().getWindow()[1][2].getDice().getValue());
        player.setStartX1(1);
        player.setStartY1(0);
        player.setFinalX1(2);
        player.setFinalY1(2);
        toolCard.useCard(player, match);
        Assert.assertEquals(5, singleplayer.getSchemeCard().getWindow()[1][0].getDice().getValue());
    }

    @Test
    public void singleplayerPutDiceInNewPosition(){
        toolCard.useCard(singleplayer, matchSingleplayer);
        System.out.println(singleplayer.getSchemeCard().toString());
        Assert.assertEquals(2, singleplayer.getSchemeCard().getWindow()[1][2].getDice().getValue());
    }

    @Test
    public void singleplayerPutDiceBack() {
        singleplayer.setDiceToBeSacrificed(0);
        singleplayer.setStartX1(1);
        singleplayer.setStartY1(0);
        singleplayer.setFinalX1(2);
        singleplayer.setFinalY1(2);
        toolCard.useCard(singleplayer, matchSingleplayer);
        System.out.println(singleplayer.getSchemeCard().toString());
        Assert.assertEquals(5, singleplayer.getSchemeCard().getWindow()[1][0].getDice().getValue());
    }
}