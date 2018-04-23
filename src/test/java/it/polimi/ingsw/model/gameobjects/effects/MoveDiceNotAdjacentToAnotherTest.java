package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.*;
import it.polimi.ingsw.model.gameobjects.windowpatterncards.KaleidoscopicDream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MoveDiceNotAdjacentToAnotherTest {
    private KaleidoscopicDream schemeCard;
    private ToolCard toolCard;
    private Player player;
    private Match match;
    private Room room;
    @Before
    public void before() {
        room = mock(Room.class);
        match=mock(Match.class);
        player = new Player("player", room);
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

        Dice dplayer=mock(Dice.class);
        when(dplayer.getValue()).thenReturn(4);
        when(dplayer.getColor()).thenReturn(Colors.GREEN);
        player.setPickedDice(dplayer);

        player.getSchemeCard().putFirstDice(dy,0,0);
        player.getSchemeCard().putDice(dg,1,0);
        player.getSchemeCard().putDice(dr,2,0);
        player.getSchemeCard().putDice(db,3,0);

        toolCard = new ToolCard("Riga in Sughero");
        ByteArrayInputStream in = new ByteArrayInputStream("2 4".getBytes());
        System.setIn(in);
    }

    @Test
    public void checkPoints() {
        toolCard.useCard(player, match);
        System.out.println(player.getSchemeCard().toString());
        Assert.assertEquals(Colors.GREEN,player.getSchemeCard().getWindow()[2][4].getDice().getColor());
        Assert.assertEquals(4, player.getSchemeCard().getWindow()[2][4].getDice().getValue());
    }
}
