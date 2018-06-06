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
import java.util.LinkedList;
import java.util.List;


import static java.lang.System.setIn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MoveTwoDicesColorRoundTrackTest {
    private KaleidoscopicDream schemeCard;
    private ToolCard toolCard;
    private PlayerMultiplayer player;
    private MatchMultiplayer match;
    private RoundTrack roundTrack;
    private Reserve reserve;
    private Room room;
    @Before
    public void before() {
        room = mock(Room.class);
        match = mock(MatchMultiplayer.class);
        Board board = mock(Board.class);
        reserve = mock(Reserve.class);
        // modificato in seguito all'introduzione di Lobby
        player = new PlayerMultiplayer("player", match);
        schemeCard = new KaleidoscopicDream();
        player.setSchemeCard(schemeCard);
        toolCard = new ToolCard("Taglierina Manuale", "tool12");
        RoundTrack roundTrack = new RoundTrack();
        roundTrack.showRoundTrack();
        List<Dice> list0 = new LinkedList<>();
        Dice d00 = new Dice(Colors.RED);
        d00.setValue(1);
        list0.add(d00);
        List<Dice> list1 = new LinkedList<>();
        Dice d10 = new Dice(Colors.YELLOW);
        d10.setValue(5);
        list1.add(d10);
        Dice d11 = new Dice(Colors.BLUE);
        d11.setValue(2);
        list1.add(d11);
        roundTrack.putDices(list0,0);
        roundTrack.putDices(list1, 1);
        roundTrack.showRoundTrack();
        when(match.getBoard()).thenReturn(board);
        when(match.getBoard().getRoundTrack()).thenReturn(roundTrack);

        Dice dy = new Dice(Colors.YELLOW);
        dy.setValue(1);

        Dice dg = new Dice(Colors.GREEN);
        dg.setValue(5);

        Dice dr = new Dice(Colors.RED);
        dr.setValue(3);

        Dice dr2 = new Dice(Colors.RED);
        dr2.setValue(4);

        Dice db = new Dice(Colors.BLUE);
        db.setValue(2);

        Dice dv = new Dice(Colors.VIOLET);
        dv.setValue(2);

        player.getSchemeCard().putDice(dy,0,0);
        player.getSchemeCard().putDice(dg,1,0);
        player.getSchemeCard().putDice(dr,2,0);
        player.getSchemeCard().putDice(db,3,0);
        player.getSchemeCard().putDice(dr2,3,1);
        player.getSchemeCard().putDice(dy,1,1);
        System.out.println(player.getSchemeCard().toString());
        player.setRound(1);
        player.setDiceChosenFromRound(0);
        player.setNumFavorTokens(5);
        player.setStartX1(2);
        player.setStartY1(0);
        player.setStartX2(3);
        player.setStartY2(1);
        player.setFinalX1(2);
        player.setFinalY1(2);
        player.setFinalX2(0);
        player.setFinalY2(2);

        toolCard = new ToolCard("Taglierina Manuale", "tool12");
    }

    @Test
    public void checkPoints() {
        System.out.println(player.getSchemeCard().toString());
        toolCard.useCard(player, match);
        System.out.println(player.getSchemeCard().toString());
        Assert.assertEquals(Colors.RED,player.getSchemeCard().getWindow()[2][2].getDice().getColor());
        Assert.assertEquals(3, player.getSchemeCard().getWindow()[2][2].getDice().getValue());
        Assert.assertEquals(Colors.RED,player.getSchemeCard().getWindow()[0][2].getDice().getColor());
        Assert.assertEquals(4, player.getSchemeCard().getWindow()[0][2].getDice().getValue());
    }
}