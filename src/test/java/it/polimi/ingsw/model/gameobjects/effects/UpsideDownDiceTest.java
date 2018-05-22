package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.Room;
import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
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
    private MatchMultiplayer match;
    private Room room;
    @Before
    public void before() {
        room = mock(Room.class);
        match = mock(MatchMultiplayer.class);
        Dice dice = new Dice(Colors.BLUE);
        dice.setValue(4);
        // modificato in seguito all'introduzione di Lobby
        player = new PlayerMultiplayer("player", match);
        schemeCard = new KaleidoscopicDream();
        player.setSchemeCard(schemeCard);
        player.setPickedDice(dice);
        //toolCard = new ToolCard("Tampone Diamantato");
    }
    @Test
    public void checkDice(){
        toolCard.useCard(player, match);
        Assert.assertEquals(3, player.getPickedDice().getValue());
        Assert.assertEquals(Colors.BLUE, player.getPickedDice().getColor());
        player.getPickedDice().setValue(6);
        toolCard.useCard(player, match);
        Assert.assertEquals(1, player.getPickedDice().getValue());
        Assert.assertEquals(Colors.BLUE, player.getPickedDice().getColor());
        player.getPickedDice().setValue(2);
        toolCard.useCard(player, match);
        Assert.assertEquals(5,player.getPickedDice().getValue());
        Assert.assertEquals(Colors.BLUE, player.getPickedDice().getColor());
    }
}
