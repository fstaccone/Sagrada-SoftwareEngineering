package it.polimi.ingsw.model.gameobjects;

import it.polimi.ingsw.model.gameobjects.windowpatterncards.ViaLux;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ColorsVarietyTest {

    private ViaLux schemeCard;
    private PublicObjectiveCard publicCard;
    private Player player;
    private Match match;
    private Room room;

    @Before
    public void before() {
        room = mock(Room.class);
        match=mock(Match.class);
        player = new Player("player", room);
        schemeCard = new ViaLux();
        player.setSchemeCard(schemeCard);

        Dice dy=mock(Dice.class);
        when(dy.getValue()).thenReturn(2);
        when(dy.getColor()).thenReturn(Colors.YELLOW);

        Dice dg=mock(Dice.class);
        when(dg.getValue()).thenReturn(1);
        when(dg.getColor()).thenReturn(Colors.GREEN);

        Dice db=mock(Dice.class);
        when(db.getValue()).thenReturn(4);
        when(db.getColor()).thenReturn(Colors.BLUE);

        Dice dv=mock(Dice.class);
        when(dv.getValue()).thenReturn(5);
        when(dv.getColor()).thenReturn(Colors.VIOLET);

        Dice dr=mock(Dice.class);
        when(dr.getValue()).thenReturn(6);
        when(dr.getColor()).thenReturn(Colors.RED);

        Dice dr1=mock(Dice.class);
        when(dr1.getValue()).thenReturn(3);
        when(dr1.getColor()).thenReturn(Colors.RED);


        player.getSchemeCard().putFirstDice(dy,0,0);
        player.getSchemeCard().putDice(dg,1,1);
        player.getSchemeCard().putDice(db,0,1);
        player.getSchemeCard().putDice(dv,1,0);
        player.getSchemeCard().putDice(dr,2,2);
        player.getSchemeCard().putDice(dr1,0,2);

        publicCard = new PublicObjectiveCard("Varietà di colore");
    }

    @Test
    public void checkPoints() {
        publicCard.useCard(player, match);
        System.out.println(player.getSchemeCard().toString());
        Assert.assertEquals(4,player.getPoints());
    }
}