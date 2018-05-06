package it.polimi.ingsw.view;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIView extends UnicastRemoteObject implements ViewInterface {

    public RMIView() throws RemoteException {
        super();

    }

    @Override
    public void ack(String message) throws RemoteException {

    }

}
