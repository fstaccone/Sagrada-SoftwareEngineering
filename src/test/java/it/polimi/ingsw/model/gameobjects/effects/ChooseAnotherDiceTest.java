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

public class ChooseAnotherDiceTest {
    private KaleidoscopicDream schemeCard;
    private ToolCard toolCard;
    private Player player;
    private MatchMultiplayer match;
    private Room room;
    private Board board;
    @Before
    public void before() {
        room = mock(Room.class);
        match = mock(MatchMultiplayer.class);
        board = mock(Board.class);

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
        // modificato in seguito all'introduzione di Lobby
        player = new PlayerMultiplayer("player", match);
        schemeCard = new KaleidoscopicDream();
        player.setSchemeCard(schemeCard);
        //toolCard = new ToolCard("Tenaglia a Rotelle");
        ByteArrayInputStream in = new ByteArrayInputStream("1".getBytes());
        System.setIn(in);
    }
    @Test
    public void checkReserve(){
        Assert.assertEquals(null, player.getPickedDice());
        toolCard.useCard(player, match);
        //match.getBoard().getReserve().showReserve();
        Assert.assertNotNull( match.getBoard().getReserve());
        Assert.assertEquals(Colors.YELLOW, player.getPickedDice().getColor());
    }
}
