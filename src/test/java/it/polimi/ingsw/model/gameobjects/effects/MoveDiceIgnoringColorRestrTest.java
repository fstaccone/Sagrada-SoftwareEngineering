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

public class MoveDiceIgnoringColorRestrTest {
    private ToolCard toolCard;
    private PlayerMultiplayer player;
    private PlayerSingleplayer singleplayer;
    private MatchMultiplayer match;
    private MatchSingleplayer matchSingleplayer;

    @Before
    public void before() {
        Board board = mock(Board.class);
        match = mock(MatchMultiplayer.class);
        matchSingleplayer = mock(MatchSingleplayer.class);
        player = new PlayerMultiplayer("player");
        singleplayer = new PlayerSingleplayer("Archi");
        KaleidoscopicDream schemeCard = new KaleidoscopicDream();
        player.setSchemeCard(schemeCard);
        singleplayer.setSchemeCard(schemeCard);
        Reserve reserve = mock(Reserve.class);

        List<Dice> list = new ArrayList<>();
        Dice d = new Dice(Colors.BLUE);
        d.setValue(4);
        list.add(d);
        list.add(d);

        Dice dy = new Dice(Colors.YELLOW);
        dy.setValue(1);

        Dice dg = new Dice(Colors.GREEN);
        dg.setValue(5);

        Dice dr = new Dice(Colors.RED);
        dr.setValue(3);

        Dice db = new Dice(Colors.BLUE);
        db.setValue(2);

        Dice dv = new Dice(Colors.VIOLET);
        dv.setValue(2);

        player.getSchemeCard().putDice(dy, 0, 0);
        player.getSchemeCard().putDice(dg, 1, 0);
        player.getSchemeCard().putDice(dr, 2, 0);
        player.getSchemeCard().putDice(db, 3, 0);

        singleplayer.getSchemeCard().putDice(dy, 0, 0);
        singleplayer.getSchemeCard().putDice(dg, 1, 0);
        singleplayer.getSchemeCard().putDice(dr, 2, 0);
        singleplayer.getSchemeCard().putDice(db, 3, 0);


        player.setStartX1(1);
        player.setStartY1(0);
        player.setFinalX1(0);
        player.setFinalY1(1);

        singleplayer.setStartX1(1);
        singleplayer.setStartY1(0);
        singleplayer.setFinalX1(0);
        singleplayer.setFinalY1(1);
        singleplayer.setDiceToBeSacrificed(0);

        toolCard = new ToolCard("Pennello per Eglomise", "tool2");
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
        Assert.assertEquals(Colors.GREEN, player.getSchemeCard().getWindow()[0][1].getDice().getColor());
    }

    @Test
    public void singleplayer() {
        toolCard.useCard(singleplayer, matchSingleplayer);
        System.out.println(singleplayer.getSchemeCard().toString());
        Assert.assertEquals(Colors.GREEN, singleplayer.getSchemeCard().getWindow()[0][1].getDice().getColor());
    }
}