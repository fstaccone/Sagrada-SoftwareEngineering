package it.polimi.ingsw.control;

import it.polimi.ingsw.view.RemoteBaseView;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteController extends Remote{

    public String login(String username, RemoteBaseView view) throws RemoteException;
}
