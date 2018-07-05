package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.model.gamelogic.PlayerMultiplayer;
import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.model.gameobjects.Dice;
import it.polimi.ingsw.model.gameobjects.PublicObjectiveCard;
import it.polimi.ingsw.model.gameobjects.windowpatterncards.LuzCelestial;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class DarkShadesTest {
    private PublicObjectiveCard publicCard;
    private Player player;
    private MatchMultiplayer match;

    @Before
    public void Before() {
        match = mock(MatchMultiplayer.class);
        player = new PlayerMultiplayer("player");
        LuzCelestial schemeCard = new LuzCelestial();
        player.setSchemeCard(schemeCard);

        Dice dg = new Dice(Colors.GREEN);
        dg.setValue(5);

        Dice db = new Dice(Colors.BLUE);
        db.setValue(6);

        Dice dy = new Dice(Colors.YELLOW);
        dy.setValue(5);

        Dice dr = new Dice(Colors.RED);
        dr.setValue(6);

        player.getSchemeCard().putFirstDice(dg, 0, 0);
        player.getSchemeCard().putDice(db, 0, 1);
        player.getSchemeCard().putDice(dy, 1, 2);
        player.getSchemeCard().putDice(dr, 2, 2);
        player.getSchemeCard().putDice(dy, 2, 1);
        publicCard = new PublicObjectiveCard("Sfumature scure");
    }

    @Test
    public void checkPoints() {
        publicCard.useCard(player, match);
        System.out.println(player.getSchemeCard().toString());
        Assert.assertEquals(4, player.getPoints());
    }
}
