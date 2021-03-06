package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
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

public class ChooseAnotherDiceTest {
    private ToolCard toolCard;
    private PlayerMultiplayer player;
    private PlayerMultiplayer player2;
    private MatchMultiplayer match;

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
        player2 = new PlayerMultiplayer("player2");
        KaleidoscopicDream schemeCard = new KaleidoscopicDream();
        player.setSchemeCard(schemeCard);
        player.setNumFavorTokens(4);
        player2.setNumFavorTokens(0);
        toolCard = new ToolCard("Tenaglia a Rotelle", "tool8");
    }

    @Test
    public void checkReserve() {
        Assert.assertEquals("Tenaglia a Rotelle", toolCard.getName());
        Assert.assertEquals("tool8", toolCard.getToolID());
        Assert.assertNull(player.getPickedDice());
        toolCard.useCard(player, match);
        toolCard.useCard(player2, match);
        Assert.assertNotNull(match.getBoard().getReserve());
        player.setPickedDice(new Dice(Colors.YELLOW));
        Assert.assertEquals(Colors.YELLOW, player.getPickedDice().getColor());
    }

    @Test
    public void applyEffect() {
        PlayerSingleplayer singleplayer = new PlayerSingleplayer("Archi");
        singleplayer.setDiceToBeSacrificed(0);
        toolCard.useCard(singleplayer, match);
        Assert.assertEquals(0, player.getTurnsLeft());
    }
}
