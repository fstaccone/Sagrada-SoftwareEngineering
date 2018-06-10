package it.polimi.ingsw;

import it.polimi.ingsw.control.Controller;
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
        cli.showFavorTokens();
        cli.onOtherFavorTokens(4, "player2");
        cli.onOtherSchemeCards(new AuroraSagradis(), "player2");
        cli.onRoundTrack("0");
        List<String> windows = new ArrayList<>();
        windows.add(new AuroraSagradis().toString());
        windows.add(new Industria().toString());
        cli.onWindowChoice(windows);
        cli.onYourTurn(true, "[1_blue,2_blue]");
        cli.onAfterWindowChoice();
        cli.onMyWindow(new Industria());
        cli.onOtherTurn("player2");
        cli.onInitialization("[tool0:AAAA,tool1:BBBB,tool2:CCCC]", "[pc1,pc2,pc3]", "[priv1]", names);
        cli.onPlayerReconnection("player2");
        cli.showFavorTokens();
        cli.showMySchemeCard();
        cli.showPrivateCard();
        cli.showPublicCards();
        cli.showToolCards();
        Assert.assertEquals("username", cli.getUsername());
        cli.onPlayerExit("player2");
        cli.onPlayerReconnection("player2");
    }

}
