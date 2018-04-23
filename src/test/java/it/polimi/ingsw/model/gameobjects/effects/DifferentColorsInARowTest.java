package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.*;
import it.polimi.ingsw.model.gameobjects.windowpatterncards.AuroraeMagnificus;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DifferentColorsInARowTest {
    private AuroraeMagnificus schemeCard;
    private PublicObjectiveCard publicCard;
    private Player player;
    private Match match;
    private Room room;
    @Before
    public void before() {
        room = mock(Room.class);
        match=mock(Match.class);
        player = new Player("player", room);
        schemeCard = new AuroraeMagnificus();
        player.setSchemeCard(schemeCard);

        Dice dy=mock(Dice.class);
        when(dy.getValue()).thenReturn(5);
        when(dy.getColor()).thenReturn(Colors.YELLOW);

        Dice dg=mock(Dice.class);
        when(dg.getValue()).thenReturn(6);
        when(dg.getColor()).thenReturn(Colors.GREEN);

        Dice db=mock(Dice.class);
        when(db.getValue()).thenReturn(3);
        when(db.getColor()).thenReturn(Colors.BLUE);

        Dice dv=mock(Dice.class);
        when(dv.getValue()).thenReturn(4);
        when(dv.getColor()).thenReturn(Colors.VIOLET);

        Dice dr=mock(Dice.class);
        when(dr.getValue()).thenReturn(2);
        when(dr.getColor()).thenReturn(Colors.RED);

        player.getSchemeCard().putFirstDice(dy,0,0);
        player.getSchemeCard().putDice(dg,0,1);
        player.getSchemeCard().putDice(db,0,2);
        player.getSchemeCard().putDice(dv,0,3);
        player.getSchemeCard().putDice(dr,0,4);
        player.getSchemeCard().putDice(dv,1,0);
        player.getSchemeCard().putDice(db,1,1);
        player.getSchemeCard().putDice(dr,1,2);
        player.getSchemeCard().putDice(dg,1,3);
        player.getSchemeCard().putDice(dy,1,4);

        publicCard = new PublicObjectiveCard("Colori diversi - Riga");
    }

    @Test
    public void checkPoints() {
        publicCard.useCard(player, match);
        Assert.assertEquals(12,player.getPoints());
    }
}