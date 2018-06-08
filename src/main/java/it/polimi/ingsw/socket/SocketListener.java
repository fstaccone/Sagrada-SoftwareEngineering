package it.polimi.ingsw.socket;

public class SocketListener implements Runnable {

    private ClientController clientController;

    public SocketListener(ClientController clientController) {
        this.clientController = clientController;
    }

    @Override
    public void run() {
        while (clientController != null) {
            clientController.nextResponse().handleResponse(clientController);
        }
    }
}
