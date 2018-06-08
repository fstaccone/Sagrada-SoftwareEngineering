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

public class ReRollDiceTest {
    private KaleidoscopicDream schemeCard;
    private ToolCard toolCard;
    private Player player;
    private MatchMultiplayer match;
    private Reserve reserve;
    private Board board;
    @Before
    public void before() {
        match = mock(MatchMultiplayer.class);
        board = mock(Board.class);
        Dice dice = new Dice(Colors.BLUE);
        Dice d1 = new Dice(Colors.YELLOW);
        reserve = new Reserve();
        List<Dice> list = new ArrayList<>();
        list.add(d1);
        list.add(dice);
        reserve.throwDices(list);
        // modificato in seguito all'introduzione di Lobby
        player = new PlayerMultiplayer("player", match);
        schemeCard = new KaleidoscopicDream();
        player.setSchemeCard(schemeCard);
        player.setPickedDice(dice);
        player.setDice(1);
        toolCard = new ToolCard("Pennello per Pasta Salda", "tool6");
        when(match.getBoard()).thenReturn(board);
        when(board.getReserve()).thenReturn(reserve);
        when(match.getBoard().getReserve()).thenReturn(reserve);
    }
    @Test
    public void checkDice(){
        toolCard.useCard(player, match);
        Assert.assertEquals(Colors.BLUE, reserve.chooseDice(player.getDice()).getColor() );
    }
}