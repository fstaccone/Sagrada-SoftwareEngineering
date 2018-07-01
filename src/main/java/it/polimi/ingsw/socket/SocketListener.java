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
            try {
                clientController.nextResponse().handleResponse(clientController);
            } catch (NullPointerException e){
                System.out.println("Problema connessione server.");
                System.exit(0);
            }
        }
    }
}
