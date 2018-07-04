package it.polimi.ingsw.socket;

import it.polimi.ingsw.control.SocketController;

public class SocketListener implements Runnable {

    private SocketController clientController;
    private final String username;
    private final boolean single;

    public SocketListener(SocketController socketController, String username, boolean single) {
        this.clientController = socketController;
        this.username = username;
        this.single = single;
    }

    @Override
    public void run() {
        while (clientController != null) {
            try {
                clientController.nextResponse().handleResponse(clientController);
            } catch (Exception e) {
                System.exit(0);
            }
        }
    }
}
