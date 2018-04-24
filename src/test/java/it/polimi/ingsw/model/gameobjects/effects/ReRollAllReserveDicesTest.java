package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.*;
import it.polimi.ingsw.model.gameobjects.windowpatterncards.KaleidoscopicDream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class ReRollAllReserveDicesTest {
    private KaleidoscopicDream schemeCard;
    private ToolCard toolCard;
    private Player player;
    private Match match;
    private Room room;
    @Before
    public void before() {
        room = mock(Room.class);
        match = mock(Match.class);
        player = new PlayerMultiplayer("player", room);
        schemeCard = new KaleidoscopicDream();
        player.setSchemeCard(schemeCard);
        toolCard = new ToolCard("Martelletto");
    }
    @Test
    public void checkDice(){
        toolCard.useCard(player, match);
        match.getBoard().getReserve().showReserve();
        Assert.assertEquals(4, match.getBoard().getReserve());
    }
}
