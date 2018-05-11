package it.polimi.ingsw;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface LobbyObserver extends Remote{
    void onWaitingPlayers(List<String> waitingPlayers) throws RemoteException;
}
