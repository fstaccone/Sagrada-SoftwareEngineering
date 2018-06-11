package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
import it.polimi.ingsw.model.gameobjects.*;
import it.polimi.ingsw.model.gameobjects.windowpatterncards.KaleidoscopicDream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MoveDiceIgnoringValueRestrTest {
    private KaleidoscopicDream schemeCard;
    private ToolCard toolCard;
    private Player player;
    private MatchMultiplayer match;

    @Before
    public void before() {
        match = mock(MatchMultiplayer.class);
        // modificato in seguito all'introduzione di Lobby
        player = new PlayerMultiplayer("player");
        schemeCard = new KaleidoscopicDream();
        player.setSchemeCard(schemeCard);

        Dice dy = new Dice(Colors.YELLOW);
        dy.setValue(2);

        Dice dg = new Dice(Colors.GREEN);
        dg.setValue(5);

        Dice dr = new Dice(Colors.RED);
        dr.setValue(3);

        Dice db = new Dice(Colors.BLUE);
        db.setValue(2);

        Dice dv = mock(Dice.class);
        when(dv.getValue()).thenReturn(3);
        when(dv.getColor()).thenReturn(Colors.VIOLET);

        player.getSchemeCard().putFirstDice(dy, 0, 0);
        player.getSchemeCard().putDice(dg, 1, 0);
        player.getSchemeCard().putDice(dr, 2, 0);
        player.getSchemeCard().putDice(db, 3, 0);
        player.getSchemeCard().putDice(dv, 1, 1);

        toolCard = new ToolCard("Alesatore per Lamina di Rame", "tool3");
        player.setStartX1(3);
        player.setStartY1(0);
        player.setFinalX1(1);
        player.setFinalY1(2);
        ByteArrayInputStream in = new ByteArrayInputStream("3 0 1 2".getBytes());
        System.setIn(in);
    }

    @Test
    public void checkPoints() {
        toolCard.useCard(player, match);
        System.out.println(player.getSchemeCard().toString());
        Assert.assertEquals(2, player.getSchemeCard().getWindow()[1][2].getDice().getValue());
    }
}