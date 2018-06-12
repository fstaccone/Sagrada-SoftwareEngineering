package it.polimi.ingsw;

import it.polimi.ingsw.socket.ClientController;
import it.polimi.ingsw.socket.requests.ReconnectionRequest;
import javafx.stage.Stage;

import java.io.Serializable;

public class SocketGui implements Serializable {

    private Gui gui;
    private ClientController clientController;

    public SocketGui(Stage fromLogin, String username, ClientController clientController, boolean single) {
        this.gui = new Gui(fromLogin, username, null, clientController, single);
        this.clientController = clientController;
        clientController.setSocketGui(this);
    }

    public void reconnect() {
        clientController.request(new ReconnectionRequest(gui.getUsername()));
    }

    public Gui getGui() {
        return gui;
    }
}
