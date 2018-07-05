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

public class PlaceDiceNotAdjacentToAnotherTest {
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
        // modificato in seguito all'introduzione di Lobby
        player = new PlayerMultiplayer("player");
        singleplayer = new PlayerSingleplayer("Archi");
        schemeCard = new KaleidoscopicDream();
        player.setSchemeCard(schemeCard);
        singleplayer.setSchemeCard(schemeCard);

        Dice dy = new Dice(Colors.YELLOW);
        dy.setValue(1);

        Dice dg = new Dice(Colors.GREEN);
        dg.setValue(5);

        Dice dr = new Dice(Colors.RED);
        dr.setValue(3);

        Dice db = new Dice(Colors.BLUE);
        db.setValue(2);

        Dice dplayer = new Dice(Colors.GREEN);
        dplayer.setValue(4);
        Dice d = new Dice(Colors.YELLOW);
        d.setValue(4);
        List<Dice> list = new ArrayList<>();
        list.add(dplayer);
        list.add(db);
        list.add(d);
        player.setPickedDice(dplayer);
        player.setDice(0);
        singleplayer.setDice(0);
        singleplayer.setDiceToBeSacrificed(2);

        player.getSchemeCard().putFirstDice(dy, 0, 0);
        player.getSchemeCard().putDice(dg, 1, 0);
        player.getSchemeCard().putDice(dr, 2, 0);
        player.getSchemeCard().putDice(db, 3, 0);

        singleplayer.getSchemeCard().putFirstDice(dy, 0, 0);
        singleplayer.getSchemeCard().putDice(dg, 1, 0);
        singleplayer.getSchemeCard().putDice(dr, 2, 0);
        singleplayer.getSchemeCard().putDice(db, 3, 0);

        toolCard = new ToolCard("Riga in Sughero", "tool9");
        player.setFinalX1(2);
        player.setFinalY1(4);
        singleplayer.setFinalX1(2);
        singleplayer.setFinalY1(4);

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
        Assert.assertEquals(Colors.GREEN, player.getSchemeCard().getWindow()[2][4].getDice().getColor());
        Assert.assertEquals(4, player.getSchemeCard().getWindow()[2][4].getDice().getValue());
    }

    @Test
    public void singleplayer(){
        toolCard.useCard(singleplayer, matchSingleplayer);
        System.out.println(singleplayer.getSchemeCard().toString());
        Assert.assertEquals(Colors.GREEN, singleplayer.getSchemeCard().getWindow()[2][4].getDice().getColor());
        Assert.assertEquals(4, singleplayer.getSchemeCard().getWindow()[2][4].getDice().getValue());
    }
}
