package it.polimi.ingsw.view;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ViewInterface extends Remote {
    void ack(String message) throws RemoteException;
}
