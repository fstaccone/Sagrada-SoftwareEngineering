package it.polimi.ingsw;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class NotifiersHandler implements Runnable{
    private ClientController clientController;
    public NotifiersHandler(ClientController clientController) {
        this.clientController=clientController;
    }

    @Override
    public void run() {
        while (true) {//DA MODIFICARE TUTTO, SOLO UN TENTATIVO
            clientController.nextResponse().handleResponse(clientController);
        }
    }
}
