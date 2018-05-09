package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.Room;
import it.polimi.ingsw.model.gameobjects.*;
import it.polimi.ingsw.model.gameobjects.windowpatterncards.AuroraeMagnificus;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class DifferentColorsInARowTest {
    private AuroraeMagnificus schemeCard;
    private PublicObjectiveCard publicCard;
    private Player player;
    private Match match;
    private Room room;
    @Before
    public void before() {
        room = mock(Room.class);
        match=mock(Match.class);
        //// modificato in seguito all'introduzione di Lobby
        player = new PlayerMultiplayer("player");
        schemeCard = new AuroraeMagnificus();
        player.setSchemeCard(schemeCard);

        Dice dy= new Dice(Colors.YELLOW);
        dy.setValue(5);

        Dice dg= new Dice(Colors.GREEN);
        dg.setValue(6);

        Dice db= new Dice(Colors.BLUE);
        db.setValue(3);

        Dice dv= new Dice(Colors.VIOLET);
        dv.setValue(4);

        Dice dr= new Dice(Colors.RED);
        dr.setValue(2);

        player.getSchemeCard().putFirstDice(dy,0,0);
        player.getSchemeCard().putDice(dg,0,1);
        player.getSchemeCard().putDice(db,0,2);
        player.getSchemeCard().putDice(dv,0,3);
        player.getSchemeCard().putDice(dr,0,4);
        player.getSchemeCard().putDice(dv,1,0);
        player.getSchemeCard().putDice(db,1,1);
        player.getSchemeCard().putDice(dr,1,2);
        player.getSchemeCard().putDice(dg,1,3);
        player.getSchemeCard().putDice(dy,1,4);

        publicCard = new PublicObjectiveCard("Colori diversi - Riga");
    }

    @Test
    public void checkPoints() {
        publicCard.useCard(player, match);
        Assert.assertEquals(12,player.getPoints());
    }
}