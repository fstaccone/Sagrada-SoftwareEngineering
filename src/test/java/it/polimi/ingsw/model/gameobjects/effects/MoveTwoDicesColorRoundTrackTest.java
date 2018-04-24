package it.polimi.ingsw.model.gameobjects.effects;

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

public class MoveTwoDicesColorRoundTrackTest {
    private KaleidoscopicDream schemeCard;
    private ToolCard toolCard;
    private Player player;
    private Match match;
    private Room room;
    @Before
    public void before() {
        room = mock(Room.class);
        player = new PlayerMultiplayer("player", room);
        List players = new ArrayList();
        players.add(player);
        Match match = new MatchMultiplayer(players);
        Board board =new Board();

        Dice d=new Dice(Colors.RED);
        d.setValue(6);
        List<Dice> dices= new ArrayList();
        dices.add(d);
        //roundTrack.putDices(dices,0);
        schemeCard = new KaleidoscopicDream();
        player.setSchemeCard(schemeCard);

        Dice dy=mock(Dice.class);
        when(dy.getValue()).thenReturn(1);
        when(dy.getColor()).thenReturn(Colors.YELLOW);

        Dice dg=mock(Dice.class);
        when(dg.getValue()).thenReturn(5);
        when(dg.getColor()).thenReturn(Colors.GREEN);

        Dice dr=mock(Dice.class);
        when(dr.getValue()).thenReturn(3);
        when(dr.getColor()).thenReturn(Colors.RED);

        Dice db=mock(Dice.class);
        when(db.getValue()).thenReturn(2);
        when(db.getColor()).thenReturn(Colors.BLUE);

        Dice dv=mock(Dice.class);
        when(dv.getValue()).thenReturn(2);
        when(dv.getColor()).thenReturn(Colors.VIOLET);

        player.getSchemeCard().putFirstDice(dy,0,0);
        player.getSchemeCard().putDice(dg,1,0);
        player.getSchemeCard().putDice(dr,2,0);
        player.getSchemeCard().putDice(db,3,0);
        player.getSchemeCard().putDice(dr,3,1);

        toolCard = new ToolCard("Taglierina Manuale");
        ByteArrayInputStream in = new ByteArrayInputStream("0 0 2 0 1 1 3 1 0 2".getBytes());
        System.setIn(in);
    }

    @Test
    public void checkPoints() {
        toolCard.useCard(player, match);
        System.out.println(player.getSchemeCard().toString());
        Assert.assertEquals(Colors.RED,player.getSchemeCard().getWindow()[2][1].getDice().getColor());
        Assert.assertEquals(Colors.RED,player.getSchemeCard().getWindow()[0][2].getDice().getColor());
    }
}