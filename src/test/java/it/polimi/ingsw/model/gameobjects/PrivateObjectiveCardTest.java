package it.polimi.ingsw.model.gameobjects;

import it.polimi.ingsw.model.gameobjects.windowpatterncards.LuzCelestial;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PrivateObjectiveCardTest {
    private LuzCelestial schemeCard;
    private PrivateObjectiveCard privateObjectiveCard;
    private Player player;
    private Match match;
    private Room room;
    @Before
    public void Before(){
        room = mock(Room.class);
        match = mock(Match.class);
        player = new Player("player", room);
        schemeCard = new LuzCelestial();
        player.setSchemeCard(schemeCard);

        Dice dg = mock(Dice.class);
        when(dg.getValue()).thenReturn(5);
        when(dg.getColor()).thenReturn(Colors.GREEN);

        Dice db = mock(Dice.class);
        when(db.getValue()).thenReturn(5);
        when(db.getColor()).thenReturn(Colors.BLUE);

        Dice dy = mock(Dice.class);
        when(dy.getValue()).thenReturn(5);
        when(dy.getColor()).thenReturn(Colors.YELLOW);

        Dice dr = mock(Dice.class);
        when(dr.getValue()).thenReturn(6);
        when(dr.getColor()).thenReturn(Colors.RED);

        player.getSchemeCard().putFirstDice(dr, 0, 0);
        player.getSchemeCard().putDice(db, 0, 1);
        player.getSchemeCard().putDice(dy, 1, 2);
        player.getSchemeCard().putDice(dr, 2, 2);
        player.getSchemeCard().putDice(dy, 2, 1);
        privateObjectiveCard = new PrivateObjectiveCard(Colors.YELLOW);
    }

    @Test
    public void checkPoints() {
        privateObjectiveCard.useCard(player);
        System.out.println(player.getSchemeCard().toString());
        Assert.assertEquals(10,player.getPoints());
    }
}
