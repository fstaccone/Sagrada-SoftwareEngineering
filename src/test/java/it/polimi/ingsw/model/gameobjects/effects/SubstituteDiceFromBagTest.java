package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.Room;
import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
import it.polimi.ingsw.model.gameobjects.*;
import it.polimi.ingsw.model.gameobjects.windowpatterncards.KaleidoscopicDream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SubstituteDiceFromBagTest {
    private KaleidoscopicDream schemeCard;
    private ToolCard toolCard;
    private Player player;
    private MatchMultiplayer match;
    private Room room;

    @Before
    public void before() {
        room = mock(Room.class);
        match = mock(MatchMultiplayer.class);
        // modificato in seguito all'introduzione di Lobby
        player = new PlayerMultiplayer("player", match);
        schemeCard = new KaleidoscopicDream();
        player.setSchemeCard(schemeCard);
        Bag bag = new Bag(18);
        when(match.getBag()).thenReturn(bag);

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
        player.setPickedDice(db);

        toolCard = new ToolCard("Diluente per Pasta Salda");
        ByteArrayInputStream in = new ByteArrayInputStream("5 3 1".getBytes());
        System.setIn(in);
    }

    @Test
    public void checkPoints() {
        System.out.println(player.getPickedDice().toString());
        System.out.println(player.getSchemeCard().toString());
        toolCard.useCard(player, match);
        Assert.assertEquals(5, schemeCard.getWindow()[3][1].getDice().getValue());
        System.out.println(player.getSchemeCard().toString());
    }
}
