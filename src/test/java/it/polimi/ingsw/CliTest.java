package it.polimi.ingsw;

import it.polimi.ingsw.control.Controller;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;
import it.polimi.ingsw.model.gameobjects.windowpatterncards.AuroraSagradis;
import it.polimi.ingsw.model.gameobjects.windowpatterncards.Industria;
import it.polimi.ingsw.socket.ClientController;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

public class CliTest {

    @Test
    public void Cli() throws IOException {
        Lobby lobby = new Lobby(10, 10);
        Controller controller = new Controller(lobby);
        ClientController clientController = mock(ClientController.class);
        Cli cli = new Cli("username", controller, clientController, false);
        Assert.assertNotNull(cli);
        cli.printWelcome();
        List<String> names = new ArrayList<>();
        names.add("username");
        names.add("player2");
        cli.onGameStarted(names);
        cli.onMyFavorTokens(3);
        cli.onOtherFavorTokens(4, "player2");
        WindowPatternCard card = new AuroraSagradis();
        String[][] schemeCard = new String[4][5];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                schemeCard[i][j] = card.getWindow()[i][j].toString();
            }
        }
        cli.onOtherSchemeCards(schemeCard, "player2");
        cli.onRoundTrack("0");
        List<String> windows = new ArrayList<>();
        windows.add(new AuroraSagradis().toString());
        windows.add(new Industria().toString());
        cli.onWindowChoice(windows);
        cli.onYourTurn(true, "[1_blue,2_blue]", 1, 2);
        cli.onAfterWindowChoice();
        WindowPatternCard playerCard = new Industria();
        String[][] playerSchemeCard = new String[4][5];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                playerSchemeCard[i][j] = playerCard.getWindow()[i][j].toString();
            }
        }
        cli.onMyWindow(playerSchemeCard);
        cli.onOtherTurn("player2");

        List<String> cards = new ArrayList<>();
        cards.add("[priv1]");

        cli.onInitialization("[tool0:AAAA,tool1:BBBB,tool2:CCCC]", "[pc1,pc2,pc3]", cards, names);
        cli.onPlayerReconnection("player2");
        Assert.assertEquals("username", cli.getUsername());
        cli.onPlayerExit("player2");
        cli.onPlayerReconnection("player2");
    }

}
