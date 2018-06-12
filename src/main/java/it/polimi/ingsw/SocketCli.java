package it.polimi.ingsw;

import it.polimi.ingsw.socket.ClientController;
import it.polimi.ingsw.socket.requests.ReconnectionRequest;

import java.io.Serializable;


public class SocketCli implements Serializable {

    private Cli cli;
    private transient ClientController clientController;

    public SocketCli(String username, ClientController clientController, boolean single) {
        cli = new Cli(username, null, clientController, single);
        clientController.setSocketCli(this);
        this.clientController = clientController;
        cli.printWelcome();
    }

    public void reconnect() {
        clientController.request(new ReconnectionRequest(cli.getUsername()));
    }

    public Cli getCli() {
        return cli;
    }
}