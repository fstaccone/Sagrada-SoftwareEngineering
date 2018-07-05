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

public class ReRollDiceTest {
    private KaleidoscopicDream schemeCard;
    private ToolCard toolCard;
    private PlayerMultiplayer player;
    private PlayerSingleplayer singleplayer;
    private MatchMultiplayer match;
    private MatchSingleplayer matchSingleplayer;
    private Reserve reserve;
    private Board board;

    @Before
    public void before() {
        match = mock(MatchMultiplayer.class);
        matchSingleplayer = mock(MatchSingleplayer.class);
        board = mock(Board.class);
        Dice dice = new Dice(Colors.BLUE);
        Dice d1 = new Dice(Colors.YELLOW);
        Dice d2 = new Dice(Colors.VIOLET);
        reserve = new Reserve();
        List<Dice> list = new ArrayList<>();
        list.add(d1);
        list.add(dice);
        list.add(d2);
        reserve.throwDices(list);
        // modificato in seguito all'introduzione di Lobby
        player = new PlayerMultiplayer("player");
        schemeCard = new KaleidoscopicDream();
        player.setSchemeCard(schemeCard);
        player.setPickedDice(dice);
        player.setDice(1);
        player.setNumFavorTokens(4);
        singleplayer = new PlayerSingleplayer("Archi");
        singleplayer.setDiceToBeSacrificed(2);
        singleplayer.setDice(1);
        toolCard = new ToolCard("Pennello per Pasta Salda", "tool6");
        when(match.getBoard()).thenReturn(board);
        when(matchSingleplayer.getBoard()).thenReturn(board);
        when(board.getReserve()).thenReturn(reserve);
        when(match.getBoard().getReserve()).thenReturn(reserve);
        when(matchSingleplayer.getBoard().getReserve()).thenReturn(reserve);
    }

    @Test
    public void checkDice() {
        toolCard.useCard(player, match);
        Assert.assertEquals(Colors.BLUE, reserve.chooseDice(player.getDice()).getColor());
    }

    @Test
    public void singlePlayer(){
        toolCard.useCard(singleplayer, matchSingleplayer);
        Assert.assertEquals(Colors.BLUE, reserve.chooseDice(singleplayer.getDice()).getColor());
    }
}