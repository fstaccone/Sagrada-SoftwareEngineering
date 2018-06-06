package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.Room;
import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
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

public class SubstituteDiceFromBagTest {
    private KaleidoscopicDream schemeCard;
    private ToolCard toolCard;
    private Player player;
    private MatchMultiplayer match;
    private Room room;
    private Reserve reserve;
    private Board board;

    @Before
    public void before() {
        room = mock(Room.class);
        match = mock(MatchMultiplayer.class);
        board = mock(Board.class);
        // modificato in seguito all'introduzione di Lobby
        player = new PlayerMultiplayer("player", match);
        schemeCard = new KaleidoscopicDream();
        player.setSchemeCard(schemeCard);
        Bag bag = new Bag(18);
        when(match.getBag()).thenReturn(bag);

        Dice dy= new Dice(Colors.YELLOW);
        dy.setValue(1);

        Dice dg= new Dice(Colors.GREEN);
        dg.setValue(5);

        Dice dr= new Dice(Colors.RED);
        dr.setValue(3);

        Dice db= new Dice(Colors.BLUE);

        Dice dv= new Dice(Colors.VIOLET);

        reserve = new Reserve();
        List<Dice> list = new ArrayList<>();
        list.add(dv);
        list.add(db);
        reserve.throwDices(list);
        player.getSchemeCard().putDice(dy,0,0);
        player.getSchemeCard().putDice(dg,1,0);
        player.getSchemeCard().putDice(dr,2,0);
        player.setPickedDice(db);
        player.setDice(1);

        toolCard = new ToolCard("Diluente per Pasta Salda", "tool11");
        when(match.getBag()).thenReturn(bag);
        when(match.getBoard()).thenReturn(board);
        when(board.getReserve()).thenReturn(reserve);
        when(match.getBoard().getReserve()).thenReturn(reserve);
    }

    @Test
    public void checkPoints() {
        toolCard.useCard(player, match);
        System.out.println(player.getDiceFromBag().toString());
        Assert.assertNotNull(player.getDiceFromBag());
    }
}
