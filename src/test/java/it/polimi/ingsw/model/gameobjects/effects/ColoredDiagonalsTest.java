package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
import it.polimi.ingsw.model.gameobjects.*;
import it.polimi.ingsw.model.gameobjects.windowpatterncards.LuzCelestial;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class ColoredDiagonalsTest {
    private LuzCelestial schemeCard;
    private PublicObjectiveCard publicCard;
    private Player player;
    private MatchMultiplayer match;
    @Before
    public void Before() {
        match = mock(MatchMultiplayer.class);
        // modificato in seguito all'introduzione di Lobby
        player = new PlayerMultiplayer("player", match);
        schemeCard = new LuzCelestial();
        player.setSchemeCard(schemeCard);

        Dice dice1 = new Dice(Colors.GREEN);
        dice1.setValue(4);


        Dice dice2 = new Dice(Colors.BLUE);
        dice2.setValue(2);

        Dice dice3 = new Dice(Colors.YELLOW);
        dice3.setValue(4);

        Dice dice4 = new Dice(Colors.RED);
        dice4.setValue(3);

        player.setPickedDice(dice1);
        player.getSchemeCard().putDice(player.getPickedDice(), 0, 0);
        player.setPickedDice(dice2);
        player.getSchemeCard().putDice(player.getPickedDice(), 0, 1);
        player.setPickedDice(dice1);
        player.getSchemeCard().putDice(player.getPickedDice(), 1, 1);
        player.setPickedDice(dice2);
        player.getSchemeCard().putDice(player.getPickedDice(), 1, 2);
        player.setPickedDice(dice4);
        player.getSchemeCard().putDice(player.getPickedDice(), 2, 1);
        player.getSchemeCard().putDice(player.getPickedDice(), 0, 2);
        player.setPickedDice(dice3);
        player.getSchemeCard().putDice(player.getPickedDice(), 3, 1);
        player.setPickedDice(dice2);
        player.getSchemeCard().putDice(player.getPickedDice(), 3, 2);
        player.getSchemeCard().putDice(player.getPickedDice(), 2, 3);
        player.setPickedDice(dice3);
        player.getSchemeCard().putDice(player.getPickedDice(),2,2);
        player.setPickedDice(dice1);
        player.getSchemeCard().putDice(player.getPickedDice(), 2, 4);
        player.getSchemeCard().putDice(player.getPickedDice(), 3, 3);
        player.setPickedDice(dice2);
        player.getSchemeCard().putDice(player.getPickedDice(), 3, 4);
        System.out.println(player.getSchemeCard().toString());

        publicCard = new PublicObjectiveCard("Diagonali colorate");
    }

    @Test
    public void checkPoints() {
        publicCard.useCard(player, match);
        System.out.println(player.getSchemeCard().toString());
        Assert.assertEquals(11,player.getPoints());
    }
}
