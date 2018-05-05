package it.polimi.ingsw.view;

import it.polimi.ingsw.control.Controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIView extends UnicastRemoteObject {

    private String name;
    Controller gameController;
    ClientController clientController;

    public RMIView(Controller gameController, ClientController clientController) throws RemoteException {
        super();
        this.clientController = clientController;
        this.gameController = gameController;
    }

}
