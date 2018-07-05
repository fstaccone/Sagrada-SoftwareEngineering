package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.control.SocketController;
import it.polimi.ingsw.socket.requests.ReconnectionRequest;
import javafx.stage.Stage;

import java.io.Serializable;

public class SocketGui implements Serializable {

    private Gui gui;
    private SocketController socketController;

    /**
     * Constructor for SocketGui.
     *
     * @param fromLogin        is the Stage where the login scene was showed.
     * @param username         is the name of the GUI owner.
     * @param socketController is a controller with socket connection.
     * @param single           is true if the GUI refers to a single player match, false otherwise.
     */
    public SocketGui(Stage fromLogin, String username, SocketController socketController, boolean single) {
        this.gui = new Gui(fromLogin, username, null, socketController, single);
        this.socketController = socketController;
        socketController.setSocketGui(this);
    }

    /**
     * Sends a ReconnectionRequest.
     */
    public void reconnect() {
        socketController.request(new ReconnectionRequest(gui.getUsername()));
    }

    public Gui getGui() {
        return gui;
    }
}
