package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
import it.polimi.ingsw.model.gamelogic.MatchSingleplayer;
import it.polimi.ingsw.model.gameobjects.*;
import it.polimi.ingsw.model.gameobjects.windowpatterncards.KaleidoscopicDream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SubstituteDiceFromBagTest {
    private KaleidoscopicDream schemeCard;
    private ToolCard toolCard;
    private Player player;
    private MatchMultiplayer match;
    private MatchSingleplayer matchSingle;
    private Reserve reserve;
    private Board board;

    @Before
    public void before() {
        match = mock(MatchMultiplayer.class);
        matchSingle = mock(MatchSingleplayer.class);
        board = mock(Board.class);
        // modificato in seguito all'introduzione di Lobby
        player = new PlayerMultiplayer("player");
        schemeCard = new KaleidoscopicDream();
        player.setSchemeCard(schemeCard);
        Bag bag = new Bag(18);
        when(match.getBag()).thenReturn(bag);
        when(matchSingle.getBag()).thenReturn(bag);

        Dice dy = new Dice(Colors.YELLOW);
        dy.setValue(1);

        Dice dg = new Dice(Colors.GREEN);
        dg.setValue(5);

        Dice dr = new Dice(Colors.RED);
        dr.setValue(3);

        Dice db = new Dice(Colors.BLUE);

        Dice dv = new Dice(Colors.VIOLET);

        reserve = new Reserve();
        List<Dice> list = new ArrayList<>();
        list.add(dv);
        list.add(db);
        reserve.throwDices(list);
        player.getSchemeCard().putDice(dy, 0, 0);
        player.getSchemeCard().putDice(dg, 1, 0);
        player.getSchemeCard().putDice(dr, 2, 0);
        player.setPickedDice(db);
        player.setDice(1);

        toolCard = new ToolCard("Diluente per Pasta Salda", "tool11");
        when(match.getBag()).thenReturn(bag);
        when(match.getBoard()).thenReturn(board);
        when(board.getReserve()).thenReturn(reserve);
        when(match.getBoard().getReserve()).thenReturn(reserve);
        when(matchSingle.getBag()).thenReturn(bag);
        when(matchSingle.getBoard()).thenReturn(board);
        when(board.getReserve()).thenReturn(reserve);
        when(matchSingle.getBoard().getReserve()).thenReturn(reserve);
    }

    @Test
    public void diceFromBag() {
        toolCard.useCard(player, match);
        System.out.println(player.getDiceFromBag().toString());
        Assert.assertNotNull(player.getDiceFromBag());
    }

    @Test
    public void singlePlayer(){
        PlayerSingleplayer singleplayer = new PlayerSingleplayer("Archi");
        singleplayer.setDiceToBeSacrificed(0);
        singleplayer.setDice(1);
        SubstituteDiceFromBagEffect effect = new SubstituteDiceFromBagEffect();
        effect.applyEffect(singleplayer, matchSingle);
        Assert.assertNotNull(singleplayer.getDiceFromBag());
    }
}
