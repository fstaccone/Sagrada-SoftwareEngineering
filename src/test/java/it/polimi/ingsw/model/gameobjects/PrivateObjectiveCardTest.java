package it.polimi.ingsw.model.gameobjects;

import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.model.gamelogic.PlayerMultiplayer;
import it.polimi.ingsw.model.gameobjects.windowpatterncards.LuzCelestial;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PrivateObjectiveCardTest {
    private PrivateObjectiveCard privateObjectiveCard;
    private Player player;

    @Before
    public void Before() {
        player = new PlayerMultiplayer("player");
        LuzCelestial schemeCard = new LuzCelestial();
        player.setSchemeCard(schemeCard);

        Dice dg = new Dice(Colors.GREEN);
        dg.setValue(5);

        Dice db = new Dice((Colors.BLUE));
        db.setValue(5);

        Dice dy = new Dice(Colors.YELLOW);
        dy.setValue(5);

        Dice dr = new Dice(Colors.RED);
        dr.setValue(6);

        player.getSchemeCard().putFirstDice(dr, 0, 0);
        player.getSchemeCard().putDice(db, 0, 1);
        player.getSchemeCard().putDice(dy, 1, 2);
        player.getSchemeCard().putDice(dr, 2, 2);
        player.getSchemeCard().putDice(dy, 2, 1);
        privateObjectiveCard = new PrivateObjectiveCard(Colors.YELLOW);
    }

    @Test
    public void checkPoints() {
        Assert.assertEquals(Colors.YELLOW, privateObjectiveCard.getColor());
        privateObjectiveCard.useCard(player);
        System.out.println(player.getSchemeCard().toString());
        Assert.assertEquals(10, player.getPoints());
    }
}
