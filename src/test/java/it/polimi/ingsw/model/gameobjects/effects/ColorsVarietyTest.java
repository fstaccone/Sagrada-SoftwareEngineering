package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.Room;
import it.polimi.ingsw.model.gameobjects.*;
import it.polimi.ingsw.model.gameobjects.windowpatterncards.ViaLux;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class ColorsVarietyTest {

    private ViaLux schemeCard;
    private PublicObjectiveCard publicCard;
    private Player player;
    private Match match;
    private Room room;

    @Before
    public void before() {
        room = mock(Room.class);
        match=mock(Match.class);
        // modificato in seguito all'introduzione di Lobby
        player = new PlayerMultiplayer("player");
        schemeCard = new ViaLux();
        player.setSchemeCard(schemeCard);

        Dice dy=new Dice(Colors.YELLOW);
        dy.setValue(2);

        Dice dg=new Dice (Colors.GREEN);
        dg.setValue(1);

        Dice db=new Dice(Colors.BLUE);
        db.setValue(4);

        Dice dv=new Dice(Colors.VIOLET);
        dv.setValue(5);

        Dice dr=new Dice(Colors.RED);
        dr.setValue(6);

        Dice dr1=new Dice(Colors.RED);
        dr1.setValue(3);

        player.getSchemeCard().putFirstDice(dy,0,0);
        player.getSchemeCard().putDice(dg,1,1);
        player.getSchemeCard().putDice(db,0,1);
        player.getSchemeCard().putDice(dv,1,0);
        player.getSchemeCard().putDice(dr,2,2);
        player.getSchemeCard().putDice(dr1,0,2);

        publicCard = new PublicObjectiveCard("Varietà di colore");
    }

    @Test
    public void checkPoints() {
        publicCard.useCard(player, match);
        System.out.println(player.getSchemeCard().toString());
        Assert.assertEquals(4,player.getPoints());
    }
}