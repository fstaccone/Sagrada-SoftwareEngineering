package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
import it.polimi.ingsw.model.gameobjects.*;
import it.polimi.ingsw.model.gameobjects.windowpatterncards.KaleidoscopicDream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MoveDiceNotAdjacentToAnotherTest {
    private KaleidoscopicDream schemeCard;
    private ToolCard toolCard;
    private Player player;
    private MatchMultiplayer match;
    private Board board;
    private Reserve reserve;
    @Before
    public void before() {
        board = mock(Board.class);
        match = mock(MatchMultiplayer.class);
        reserve = mock(Reserve.class);
        // modificato in seguito all'introduzione di Lobby
        player = new PlayerMultiplayer("player", match);
        schemeCard = new KaleidoscopicDream();
        player.setSchemeCard(schemeCard);

        Dice dy= new Dice(Colors.YELLOW);
        dy.setValue(1);

        Dice dg= new Dice(Colors.GREEN);
        dg.setValue(5);

        Dice dr= new Dice(Colors.RED);
        dr.setValue(3);

        Dice db= new Dice(Colors.BLUE);
        db.setValue(2);

        Dice dplayer= new Dice(Colors.GREEN);
        dplayer.setValue(4);
        List<Dice> list = new ArrayList<>();
        list.add(dplayer);
        list.add(db);
        player.setPickedDice(dplayer);
        player.setDice(0);

        player.getSchemeCard().putFirstDice(dy,0,0);
        player.getSchemeCard().putDice(dg,1,0);
        player.getSchemeCard().putDice(dr,2,0);
        player.getSchemeCard().putDice(db,3,0);

        toolCard = new ToolCard("Riga in Sughero", "tool9");
        player.setFinalX1(2);
        player.setFinalY1(4);

        when(match.getBoard()).thenReturn(board);
        when(match.getBoard().getReserve()).thenReturn(reserve);
        when(reserve.getDices()).thenReturn(list);
        when(match.getBoard().getReserve().getDices()).thenReturn(list);
    }

    @Test
    public void checkPoints() {
        toolCard.useCard(player, match);
        System.out.println(player.getSchemeCard().toString());
        Assert.assertEquals(Colors.GREEN,player.getSchemeCard().getWindow()[2][4].getDice().getColor());
        Assert.assertEquals(4, player.getSchemeCard().getWindow()[2][4].getDice().getValue());
    }
}
