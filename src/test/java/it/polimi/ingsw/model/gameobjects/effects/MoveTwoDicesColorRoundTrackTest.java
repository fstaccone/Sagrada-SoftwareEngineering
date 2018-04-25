package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gameobjects.*;
import it.polimi.ingsw.model.gameobjects.windowpatterncards.KaleidoscopicDream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.LinkedList;
import java.util.List;


import static java.lang.System.setIn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MoveTwoDicesColorRoundTrackTest {
    private KaleidoscopicDream schemeCard;
    private ToolCard toolCard;
    private Player player;
    private Match match;
    private Room room;
    @Before
    public void before() {
        room = mock(Room.class);
        match = mock(Match.class);
        Board board = mock(Board.class);
        player = new PlayerMultiplayer("player", room);
        schemeCard = new KaleidoscopicDream();
        player.setSchemeCard(schemeCard);
        toolCard = new ToolCard("Taglierina Manuale");
        RoundTrack roundTrack = new RoundTrack();
        roundTrack.showRoundTrack();
        List<Dice> list0 = new LinkedList<>();
        Dice d00 = new Dice(Colors.RED);
        d00.setValue(1);
        list0.add(d00);
        List<Dice> list1 = new LinkedList<>();
        Dice d10 = new Dice(Colors.YELLOW);
        d10.setValue(5);
        list1.add(d10);
        Dice d11 = new Dice(Colors.BLUE);
        d11.setValue(2);
        list1.add(d11);
        roundTrack.putDices(list0,0);
        roundTrack.putDices(list1, 1);
        when(match.getBoard()).thenReturn(board);
        when(match.getBoard().getRoundTrack()).thenReturn(roundTrack);

        Dice dy = new Dice(Colors.YELLOW);
        dy.setValue(1);

        Dice dg = new Dice(Colors.GREEN);
        dg.setValue(5);

        Dice dr = new Dice(Colors.RED);
        dr.setValue(3);

        Dice db = new Dice(Colors.BLUE);
        db.setValue(2);

        Dice dv = new Dice(Colors.VIOLET);
        dv.setValue(2);

        player.getSchemeCard().putFirstDice(dy,0,0);
        player.getSchemeCard().putDice(dg,1,0);
        player.getSchemeCard().putDice(dr,2,0);
        player.getSchemeCard().putDice(db,3,0);
        player.getSchemeCard().putDice(dr,3,1);

        toolCard = new ToolCard("Taglierina Manuale");
        //TODO: input funziona per scegliere colore, poi si blocca quando bisogna dire quanti dadi si vogliono spostare.
        //Secondo me problema è che si apre lo stream per prendere i primi due interi nella riga 16 del relativo effetto
        //poi quando al rigo 21 si prende altro input lo stream è vuoto e test non va avanti.
        ByteArrayInputStream in = new ByteArrayInputStream("1 0 2".getBytes());
        setIn(in);
    }

    @Test
    public void checkPoints() {
        System.out.println(player.getSchemeCard().toString());
        toolCard.useCard(player, match);
        System.out.println(player.getSchemeCard().toString());
        Assert.assertEquals(Colors.RED,player.getSchemeCard().getWindow()[2][1].getDice().getColor());
        Assert.assertEquals(Colors.RED,player.getSchemeCard().getWindow()[0][2].getDice().getColor());
    }
}