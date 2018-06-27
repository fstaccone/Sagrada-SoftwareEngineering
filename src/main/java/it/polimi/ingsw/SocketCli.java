package it.polimi.ingsw;

import it.polimi.ingsw.socket.ClientController;
import it.polimi.ingsw.socket.requests.ReconnectionRequest;

import java.io.Serializable;


public class SocketCli implements Serializable {

    private Cli cli;
    private transient ClientController clientController;

    /**
     * Initializes the SocketCli
     * @param username         is the name of the player who is the owner of this view
     * @param controllerSocket is the client side controller used by this view (if it uses Socket connection) to contact the model
     * @param single           is a boolean used to let Cli understand if it is singleplayer or not
     */
    public SocketCli(String username, ClientController controllerSocket, boolean single) {
        cli = new Cli(username, null, controllerSocket, single);
        controllerSocket.setSocketCli(this);
        this.clientController = controllerSocket;
        cli.printWelcome();
    }

    public Cli getCli() {
        return cli;
    }

    /**
     * Request of reconnection launched to the controller
     */
    public void reconnect() {
        clientController.request(new ReconnectionRequest(cli.getUsername()));
    }

}