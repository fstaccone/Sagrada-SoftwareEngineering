package it.polimi.ingsw;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface MatchObserver extends Remote {
    void onPlayers(List<String> playersNames) throws RemoteException;
}
