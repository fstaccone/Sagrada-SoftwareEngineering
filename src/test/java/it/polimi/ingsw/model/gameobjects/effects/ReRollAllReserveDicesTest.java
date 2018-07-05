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

public class ReRollAllReserveDicesTest {
    private ToolCard toolCard;
    private PlayerMultiplayer player;
    private PlayerSingleplayer singleplayer;
    private MatchMultiplayer match;
    private MatchSingleplayer matchSingleplayer;

    @Before
    public void before() {
        match = mock(MatchMultiplayer.class);
        Board board = mock(Board.class);

        Reserve reserve = new Reserve();
        List<Dice> dices = new ArrayList<>();
        Dice dr = new Dice(Colors.RED);
        Dice dy = new Dice(Colors.YELLOW);
        Dice dv = new Dice(Colors.VIOLET);
        Dice dg = new Dice(Colors.GREEN);
        Dice db = new Dice(Colors.BLUE);
        dices.add(dr);
        dices.add(dy);
        dices.add(dv);
        dices.add(dg);
        dices.add(db);
        reserve.throwDices(dices);
        when(match.getBoard()).thenReturn(board);
        when(board.getReserve()).thenReturn(reserve);
        when(match.getBoard().getReserve()).thenReturn(reserve);
        player = new PlayerMultiplayer("player");
        KaleidoscopicDream schemeCard = new KaleidoscopicDream();
        player.setSchemeCard(schemeCard);
        player.setNumFavorTokens(4);
        player.setTurnsLeft(1);
        toolCard = new ToolCard("Martelletto", "tool7");
        singleplayer = new PlayerSingleplayer("Archi");
        matchSingleplayer = mock(MatchSingleplayer.class);
        when(matchSingleplayer.getBoard()).thenReturn(board);
        when(board.getReserve()).thenReturn(reserve);
        when(matchSingleplayer.getBoard().getReserve()).thenReturn(reserve);
        singleplayer.setTurnsLeft(1);
        singleplayer.setDiceToBeSacrificed(4);
    }

    @Test
    public void checkReserve() {
        toolCard.useCard(player, match);
        Assert.assertNotNull(match.getBoard().getReserve());
        Assert.assertEquals(Colors.RED, match.getBoard().getReserve().chooseDice(0).getColor());
        Assert.assertEquals(Colors.YELLOW, match.getBoard().getReserve().chooseDice(0).getColor());
        Assert.assertEquals(Colors.VIOLET, match.getBoard().getReserve().chooseDice(0).getColor());
        Assert.assertEquals(Colors.GREEN, match.getBoard().getReserve().chooseDice(0).getColor());
        Assert.assertEquals(Colors.BLUE, match.getBoard().getReserve().chooseDice(0).getColor());
    }

    @Test
    public void singlePlayer() {
        toolCard.useCard(singleplayer, matchSingleplayer);
        Assert.assertNotNull(matchSingleplayer.getBoard().getReserve());
        Assert.assertEquals(Colors.RED, matchSingleplayer.getBoard().getReserve().chooseDice(0).getColor());
        Assert.assertEquals(Colors.YELLOW, matchSingleplayer.getBoard().getReserve().chooseDice(0).getColor());
        Assert.assertEquals(Colors.VIOLET, matchSingleplayer.getBoard().getReserve().chooseDice(0).getColor());
        Assert.assertEquals(Colors.GREEN, matchSingleplayer.getBoard().getReserve().chooseDice(0).getColor());
    }
}
