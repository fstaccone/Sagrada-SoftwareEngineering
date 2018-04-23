package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.*;
import it.polimi.ingsw.model.gameobjects.windowpatterncards.ViaLux;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LightShadesTest {

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
        when(db.getValue()).thenReturn(1);
        when(db.getColor()).thenReturn(Colors.BLUE);

        Dice dv=mock(Dice.class);
        when(dv.getValue()).thenReturn(2);
        when(dv.getColor()).thenReturn(Colors.VIOLET);

        Dice dr=mock(Dice.class);
        when(dr.getValue()).thenReturn(1);
        when(dr.getColor()).thenReturn(Colors.RED);

        player.getSchemeCard().putFirstDice(dy,0,4);
        player.getSchemeCard().putDice(dg,1,3);
        player.getSchemeCard().putDice(db,2,4);
        player.getSchemeCard().putDice(dv,2,3);
        player.getSchemeCard().putDice(dr,2,2);

        publicCard = new PublicObjectiveCard("Sfumature chiare");
    }

    @Test
    public void checkPoints() {
        publicCard.useCard(player, match);
        System.out.println(player.getSchemeCard().toString());
        Assert.assertEquals(4,player.getPoints());
    }
}