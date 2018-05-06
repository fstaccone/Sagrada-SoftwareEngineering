package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.Room;
import it.polimi.ingsw.model.gameobjects.*;
import it.polimi.ingsw.model.gameobjects.windowpatterncards.KaleidoscopicDream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MoveDiceIgnoringColorRestrTest {
    private KaleidoscopicDream schemeCard;
    private ToolCard toolCard;
    private Player player;
    private Match match;
    private Room room;
    @Before
    public void before() {
        room = mock(Room.class);
        match=mock(Match.class);
        player = new PlayerMultiplayer("player", room);
        schemeCard = new KaleidoscopicDream();
        player.setSchemeCard(schemeCard);

        Dice dy= new Dice(Colors.YELLOW);
        dy.setValue(1);

        Dice dg= new Dice(Colors.GREEN);
        dg.setValue(5);

        Dice dr= new Dice(Colors.RED);
        dr.setValue(3);

        Dice db= new Dice(Colors.BLUE);
        db.setValue(2);

        Dice dv= new Dice(Colors.VIOLET);
        dv.setValue(2);

        player.getSchemeCard().putFirstDice(dy,0,0);
        player.getSchemeCard().putDice(dg,1,0);
        player.getSchemeCard().putDice(dr,2,0);
        player.getSchemeCard().putDice(db,3,0);

        toolCard = new ToolCard("Pennello per Eglomise");
        ByteArrayInputStream in = new ByteArrayInputStream("1 0 0 1".getBytes());
        System.setIn(in);
    }

    @Test
    public void checkPoints() {
        toolCard.useCard(player, match);
        System.out.println(player.getSchemeCard().toString());
        Assert.assertEquals(Colors.GREEN,player.getSchemeCard().getWindow()[0][1].getDice().getColor());
    }
}