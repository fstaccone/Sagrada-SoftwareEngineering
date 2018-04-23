package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.*;
import it.polimi.ingsw.model.gameobjects.windowpatterncards.KaleidoscopicDream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class UpsideDownDiceTest {
    private KaleidoscopicDream schemeCard;
    private ToolCard toolCard;
    private Player player;
    private Match match;
    private Room room;
    @Before
    public void before() {
        room = mock(Room.class);
        match = mock(Match.class);
        Dice dice = new Dice(Colors.BLUE);
        dice.setValue(4);
        player = new Player("player", room);
        schemeCard = new KaleidoscopicDream();
        player.setSchemeCard(schemeCard);
        player.setPickedDice(dice);
        toolCard = new ToolCard("Tampone Diamantato");
    }
    @Test
    public void checkDice(){
        toolCard.useCard(player, match);
        System.out.println(player.getPickedDice().toString());
        Assert.assertEquals(3, player.getPickedDice().getValue());
        Assert.assertEquals(Colors.BLUE, player.getPickedDice().getColor());
    }
}
