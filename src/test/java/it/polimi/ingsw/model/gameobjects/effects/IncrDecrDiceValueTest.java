package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
import it.polimi.ingsw.model.gamelogic.MatchSingleplayer;
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
        player = new PlayerMultiplayer("player");
        singleplayer = new PlayerSingleplayer("Archi");
        schemeCard = new Firmitas();
        player.setSchemeCard(schemeCard);
        singleplayer.setSchemeCard(schemeCard);
        Dice dice = new Dice(Colors.BLUE);
        dice.setValue(6);
        toolCard = new ToolCard("Pinza Sgrossatrice", "tool1");
        reserve = mock(Reserve.class);
        Dice d = new Dice(Colors.BLUE);
        Dice d1 = new Dice(Colors.VIOLET);
        d.setValue(1);
        d1.setValue(4);
        List<Dice> diceList = new ArrayList<>();
        diceList.add(dice);
        diceList.add(d);
        diceList.add(d1);
        diceList.add(d1);
        diceList.add(d1);
        diceList.add(d1);
        diceList.add(d1);
        diceList.add(d1);
        diceList.add(d1);
        diceList.add(d1);
        diceList.add(d1);
        diceList.add(d1);
        diceList.add(d1);
        diceList.add(d1);
        player.setDice(0);
        singleplayer.setDice(0);
        singleplayer.setDiceToBeSacrificed(2);
        player.setNumFavorTokens(3);
        when(match.getBoard()).thenReturn(board);
        when(match.getBoard().getReserve()).thenReturn(reserve);
        when(match.getBoard().getReserve().getDices()).thenReturn(diceList);
        when(matchSingleplayer.getBoard()).thenReturn(board);
        when(matchSingleplayer.getBoard().getReserve()).thenReturn(reserve);
        when(matchSingleplayer.getBoard().getReserve().getDices()).thenReturn(diceList);
    }

    @Test
    public void checkDice() {
        player.setChoice("+");
        toolCard.useCard(player, match);
        Assert.assertEquals(false, toolCard.useCard(player, match));
        player.setChoice("-");
        toolCard.useCard(player, match);
        Assert.assertEquals(5, reserve.getDices().get(0).getValue());
        player.setChoice("+");
        toolCard.useCard(player, match);
        Assert.assertEquals(6, reserve.getDices().get(0).getValue());
        player.setChoice("a");
        Assert.assertEquals(false, toolCard.useCard(player, match));
        player.setChoice("-");
        toolCard.useCard(player, match);
        Assert.assertEquals(1, reserve.getDices().get(1).getValue());
        player.setChoice("+");
        Assert.assertEquals(false, toolCard.useCard(player, match));
    }

    @Test
    public void checkDiceSingle(){
        singleplayer.setChoice("+");
        toolCard.useCard(singleplayer, matchSingleplayer);
        Assert.assertEquals(false, toolCard.useCard(singleplayer, matchSingleplayer));
        singleplayer.setChoice("-");
        toolCard.useCard(singleplayer, matchSingleplayer);
        Assert.assertEquals(5, reserve.getDices().get(0).getValue());
        singleplayer.setChoice("+");
        toolCard.useCard(singleplayer, matchSingleplayer);
        Assert.assertEquals(6, reserve.getDices().get(0).getValue());
        singleplayer.setChoice("a");
        Assert.assertEquals(false, toolCard.useCard(singleplayer, matchSingleplayer));
        singleplayer.setChoice("-");
        toolCard.useCard(singleplayer, matchSingleplayer);
        Assert.assertEquals(1, reserve.getDices().get(1).getValue());
    }
}
