package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
import it.polimi.ingsw.model.gameobjects.*;
import it.polimi.ingsw.model.gameobjects.windowpatterncards.Firmitas;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IncrDecrDiceValueTest {
    private WindowPatternCard schemeCard;
    private ToolCard toolCard;
    private PlayerMultiplayer player;
    private MatchMultiplayer match;
    private Board board;
    private Reserve reserve;

    @Before
    public void before() {
        board = mock(Board.class);
        match = mock(MatchMultiplayer.class);
        // modificato in seguito all'introduzione di Lobby
        player = new PlayerMultiplayer("player");
        schemeCard = new Firmitas();
        player.setSchemeCard(schemeCard);
        Dice dice = new Dice(Colors.BLUE);
        dice.setValue(6);
        toolCard = new ToolCard("Pinza Sgrossatrice", "tool1");
        reserve = mock(Reserve.class);
        List<Dice> diceList = new ArrayList<>();
        diceList.add(dice);
        Dice d = new Dice(Colors.BLUE);
        d.setValue(1);
        diceList.add(d);
        player.setDice(0);
        player.setNumFavorTokens(3);
        when(match.getBoard()).thenReturn(board);
        when(match.getBoard().getReserve()).thenReturn(reserve);
        when(match.getBoard().getReserve().getDices()).thenReturn(diceList);
    }

    @Test
    public void checkDice() {
        player.setChoise("+");
        toolCard.useCard(player, match);
        Assert.assertEquals(false, toolCard.useCard(player, match));
        player.setChoise("-");
        toolCard.useCard(player, match);
        Assert.assertEquals(5, reserve.getDices().get(0).getValue());
        player.setChoise("+");
        toolCard.useCard(player, match);
        Assert.assertEquals(6, reserve.getDices().get(0).getValue());
        player.setChoise("a");
        Assert.assertEquals(false, toolCard.useCard(player, match));
        player.setChoise("-");
        toolCard.useCard(player, match);
        Assert.assertEquals(1, reserve.getDices().get(1).getValue());
        player.setChoise("+");
        Assert.assertEquals(false, toolCard.useCard(player, match));
    }
}
