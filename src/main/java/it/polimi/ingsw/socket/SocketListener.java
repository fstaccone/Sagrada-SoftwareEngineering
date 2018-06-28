package it.polimi.ingsw.socket;

import it.polimi.ingsw.control.SocketController;

public class SocketListener implements Runnable {

    private SocketController clientController;

    public SocketListener(SocketController socketController) {
        this.clientController = socketController;
    }

    @Override
    public void run() {
        while (clientController != null) {
            clientController.nextResponse().handleResponse(clientController);
        }
    }
}
