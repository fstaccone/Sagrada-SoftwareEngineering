package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.model.gamelogic.PlayerMultiplayer;
import it.polimi.ingsw.model.gameobjects.*;
import it.polimi.ingsw.model.gameobjects.windowpatterncards.KaleidoscopicDream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class DifferentColorsInAColumnTest {
    private KaleidoscopicDream schemeCard;
    private PublicObjectiveCard publicCard;
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
        dy.setValue(1);

        Dice dg = new Dice(Colors.GREEN);
        dg.setValue(2);

        Dice dr = new Dice(Colors.RED);
        dr.setValue(3);

        Dice db = new Dice(Colors.BLUE);
        db.setValue(4);

        Dice dv = new Dice(Colors.VIOLET);
        dv.setValue(2);

        player.getSchemeCard().putFirstDice(dy, 0, 0);
        player.getSchemeCard().putDice(dg, 1, 0);
        player.getSchemeCard().putDice(dr, 2, 0);
        player.getSchemeCard().putDice(dv, 3, 0);

        publicCard = new PublicObjectiveCard("Colori diversi - Colonna");
    }

    @Test
    public void checkPoints() {
        publicCard.useCard(player, match);
        Assert.assertEquals(5, player.getPoints());
    }
}