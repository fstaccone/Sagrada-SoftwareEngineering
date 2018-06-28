package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.control.SocketController;
import it.polimi.ingsw.socket.requests.ReconnectionRequest;
import javafx.stage.Stage;

import java.io.Serializable;

public class SocketGui implements Serializable {

    private Gui gui;
    private SocketController socketController;

    public SocketGui(Stage fromLogin, String username, SocketController socketController, boolean single) {
        this.gui = new Gui(fromLogin, username, null, socketController, single);
        this.socketController = socketController;
        socketController.setSocketGui(this);
    }

    public void reconnect() {
        socketController.request(new ReconnectionRequest(gui.getUsername()));
    }

    public Gui getGui() {
        return gui;
    }
}
