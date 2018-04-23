package it.polimi.ingsw.model.gameobjects;

import it.polimi.ingsw.model.gameobjects.windowpatterncards.LuzCelestial;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.rmi.RemoteException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ColoredDiagonalsTest {
    private LuzCelestial schemeCard;
    private PublicObjectiveCard publicCard;
    private Player player;
    private Match match;
    private Room room;
    @Before
    public void Before() throws RemoteException {
        room = mock(Room.class);
        match = mock(Match.class);
        player = new Player("guido", room);
        schemeCard = new LuzCelestial();
        player.setSchemeCard(schemeCard);

        Dice dice1 = mock(Dice.class);
        when(dice1.getValue()).thenReturn(4);
        when(dice1.getColor()).thenReturn(Colors.GREEN);

        Dice dice2 = mock(Dice.class);
        when(dice2.getValue()).thenReturn(2);
        when(dice2.getColor()).thenReturn(Colors.BLUE);

        Dice dice3 = mock(Dice.class);
        when(dice3.getValue()).thenReturn(4);
        when(dice3.getColor()).thenReturn(Colors.YELLOW);

        Dice dice4 = mock(Dice.class);
        when(dice4.getValue()).thenReturn(3);
        when(dice4.getColor()).thenReturn(Colors.RED);

        player.getSchemeCard().putFirstDice(player.getPickedDice(), 0, 0);
        player.getSchemeCard().putDice(player.getPickedDice(), 1, 3);
        player.setPickedDice(dice2);
        player.getSchemeCard().putDice(player.getPickedDice(), 0, 1);
        player.getSchemeCard().putDice(player.getPickedDice(), 1, 1);
        player.getSchemeCard().putDice(player.getPickedDice(), 0, 2);
        player.getSchemeCard().putDice(player.getPickedDice(), 0, 3);
        player.setPickedDice(dice1);
        player.getSchemeCard().putDice(player.getPickedDice(), 1, 1);
        player.setPickedDice(dice2);
        player.getSchemeCard().putDice(player.getPickedDice(), 1, 2);
        player.setPickedDice(dice4);
        player.getSchemeCard().putDice(player.getPickedDice(), 2, 1);
        player.getSchemeCard().putDice(player.getPickedDice(), 0, 2);
        player.setPickedDice(dice3);
        player.getSchemeCard().putDice(player.getPickedDice(), 3, 1);
        player.getSchemeCard().putDice(player.getPickedDice(), 2, 2);
        player.setPickedDice(dice2);
        player.getSchemeCard().putDice(player.getPickedDice(), 3, 2);
        player.getSchemeCard().putDice(player.getPickedDice(), 2, 3);
        player.setPickedDice(dice1);
        player.getSchemeCard().putDice(player.getPickedDice(), 2, 4);
        player.getSchemeCard().putDice(player.getPickedDice(), 3, 3);
        player.setPickedDice(dice2);
        player.getSchemeCard().putDice(player.getPickedDice(), 3, 4);

        publicCard = new PublicObjectiveCard("Diagonali colorate");
    }

    @Test
    public void checkPoints() throws RemoteException{
        publicCard.useCard(player, match);
        Assert.assertEquals(11,player.getPoints());
    }
}
