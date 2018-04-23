package it.polimi.ingsw.model.gameobjects;

import it.polimi.ingsw.model.gameobjects.windowpatterncards.KaleidoscopicDream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DifferentShadesInAColumnTest {
    private KaleidoscopicDream schemeCard;
    private PublicObjectiveCard publicCard;
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
        when(db.getValue()).thenReturn(1);
        when(db.getColor()).thenReturn(Colors.BLUE);

        Dice dv=mock(Dice.class);
        when(dv.getValue()).thenReturn(2);
        when(dv.getColor()).thenReturn(Colors.VIOLET);

        player.getSchemeCard().putFirstDice(dy,0,0);
        player.getSchemeCard().putDice(dg,1,0);
        player.getSchemeCard().putDice(dr,2,0);
        player.getSchemeCard().putDice(db,3,0);

        publicCard = new PublicObjectiveCard("Sfumature diverse - Colonna");
    }

    @Test
    public void checkPoints() {
        publicCard.useCard(player, match);
        System.out.println(player.getSchemeCard().toString());
        Assert.assertEquals(4,player.getPoints());
    }
}