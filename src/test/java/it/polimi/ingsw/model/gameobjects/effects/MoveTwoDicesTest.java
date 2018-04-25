package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.*;
import it.polimi.ingsw.model.gameobjects.windowpatterncards.KaleidoscopicDream;
import it.polimi.ingsw.model.gameobjects.windowpatterncards.SymphonyOfLight;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MoveTwoDicesTest {
    private SymphonyOfLight schemeCard;
    private ToolCard toolCard;
    private Player player;
    private Match match;
    private Room room;
    @Before
    public void before() {
        room = mock(Room.class);
        match = mock(Match.class);
        player = new PlayerMultiplayer("player", room);
        schemeCard = new SymphonyOfLight();
        player.setSchemeCard(schemeCard);

        Dice dy = new Dice(Colors.YELLOW);
        dy.setValue(6);

        Dice dg = new Dice(Colors.GREEN);
        dg.setValue(3);

        Dice dr = new Dice(Colors.RED);
        dr.setValue(3);

        Dice db = new Dice(Colors.BLUE);
        db.setValue(4);

        player.getSchemeCard().putFirstDice(dg, 0, 1);
        player.getSchemeCard().putDice(dy, 1, 0);
        player.getSchemeCard().putDice(dr, 2, 0);
        player.getSchemeCard().putDice(db, 3, 0);

        toolCard = new ToolCard("Lathekin");
        ByteArrayInputStream in = new ByteArrayInputStream("0 1 1 0 3 1 1 1".getBytes());
        System.setIn(in);
    }
    @Test
    public void checkPoints() {
        toolCard.useCard(player, match);
        System.out.println(schemeCard.toString());
        Assert.assertEquals(Colors.GREEN,player.getSchemeCard().getWindow()[3][1].getDice().getColor());
        Assert.assertEquals(3, player.getSchemeCard().getWindow()[3][1].getDice().getValue());
        Assert.assertEquals(Colors.YELLOW,player.getSchemeCard().getWindow()[1][1].getDice().getColor());
        Assert.assertEquals(6, player.getSchemeCard().getWindow()[1][1].getDice().getValue());
    }
}
