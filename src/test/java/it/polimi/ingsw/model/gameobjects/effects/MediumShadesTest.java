package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.*;
import it.polimi.ingsw.model.gameobjects.windowpatterncards.LuzCelestial;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.rmi.RemoteException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MediumShadesTest {
    private LuzCelestial schemeCard;
    private PublicObjectiveCard publicCard;
    private Player player;
    private Match match;
    private Room room;
    @Before
    public void Before() throws RemoteException {
        room = mock(Room.class);
        match = mock(Match.class);
        player = new Player("player", room);
        schemeCard = new LuzCelestial();
        player.setSchemeCard(schemeCard);

        Dice dg = mock(Dice.class);
        when(dg.getValue()).thenReturn(4);
        when(dg.getColor()).thenReturn(Colors.GREEN);

        Dice db = mock(Dice.class);
        when(db.getValue()).thenReturn(3);
        when(db.getColor()).thenReturn(Colors.BLUE);

        Dice dy = mock(Dice.class);
        when(dy.getValue()).thenReturn(4);
        when(dy.getColor()).thenReturn(Colors.YELLOW);

        Dice dr = mock(Dice.class);
        when(dr.getValue()).thenReturn(3);
        when(dr.getColor()).thenReturn(Colors.RED);

        player.getSchemeCard().putFirstDice(dg, 0, 0);
        player.getSchemeCard().putDice(db, 0, 1);
        player.getSchemeCard().putDice(dy, 1, 2);
        player.getSchemeCard().putDice(dr, 2, 2);
        player.getSchemeCard().putDice(dy, 2, 1);
        publicCard = new PublicObjectiveCard("Sfumature medie");
    }

    @Test
    public void checkPoints() throws RemoteException{
        publicCard.useCard(player, match);
        System.out.println(player.getSchemeCard().toString());
        Assert.assertEquals(4,player.getPoints());
    }
}
