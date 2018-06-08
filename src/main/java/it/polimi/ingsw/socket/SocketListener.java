package it.polimi.ingsw.socket;

import it.polimi.ingsw.socket.responses.Response;

public class SocketListener implements Runnable {

    private ClientController clientController;

    public SocketListener(ClientController clientController) {
        this.clientController = clientController;
    }

    @Override
    public void run() {
        while (clientController != null) {
            Response response = clientController.nextResponse();
            if (response != null) {
                response.handleResponse(clientController);
            } else {
                System.out.println("\n Errore in ricezione\n");
            }
        }
    }
}
