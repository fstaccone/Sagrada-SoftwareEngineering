package it.polimi.ingsw.view;

import it.polimi.ingsw.Client;
import it.polimi.ingsw.control.Controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIView extends UnicastRemoteObject implements RemoteBaseView{

    // Ha senso distinguere la view singleplayer dalla multiplayer?

    private String name;
    private Controller gameController;
    private ClientController clientController;
    private ConnectionStatus status;


    public RMIView(Controller gameController, ClientController clientController) throws RemoteException {
        super();
        this.clientController = clientController;
        this.gameController = gameController;
        this.status = ConnectionStatus.CONNECTED;
    }


    // todo: Vogliamo che la RMIView contenga il client o che il client contenga la view?
}
