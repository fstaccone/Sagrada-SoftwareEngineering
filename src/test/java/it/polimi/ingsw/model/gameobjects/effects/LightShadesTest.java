package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.model.gamelogic.PlayerMultiplayer;
import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.model.gameobjects.Dice;
import it.polimi.ingsw.model.gameobjects.PublicObjectiveCard;
import it.polimi.ingsw.model.gameobjects.windowpatterncards.ViaLux;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class LightShadesTest {

    private PublicObjectiveCard publicCard;
    private Player player;
    private MatchMultiplayer match;

    @Before
    public void before() {
        match = mock(MatchMultiplayer.class);
        player = new PlayerMultiplayer("player");
        ViaLux schemeCard = new ViaLux();
        player.setSchemeCard(schemeCard);

        Dice dy = new Dice(Colors.YELLOW);
        dy.setValue(2);

        Dice dg = new Dice(Colors.GREEN);
        dg.setValue(1);

        Dice db = new Dice(Colors.BLUE);
        db.setValue(1);

        Dice dv = new Dice(Colors.VIOLET);
        dv.setValue(2);

        Dice dr = new Dice(Colors.RED);
        dr.setValue(1);

        player.getSchemeCard().putFirstDice(dy, 0, 4);
        player.getSchemeCard().putDice(dg, 1, 3);
        player.getSchemeCard().putDice(db, 2, 4);
        player.getSchemeCard().putDice(dv, 2, 3);
        player.getSchemeCard().putDice(dr, 2, 2);

        publicCard = new PublicObjectiveCard("Sfumature chiare");
    }

    @Test
    public void checkPoints() {
        publicCard.useCard(player, match);
        System.out.println(player.getSchemeCard().toString());
        Assert.assertEquals(4, player.getPoints());
    }
}