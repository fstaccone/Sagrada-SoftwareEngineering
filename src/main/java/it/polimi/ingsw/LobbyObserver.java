package it.polimi.ingsw;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface LobbyObserver extends Serializable, Remote{
    void onWaitingPlayers(List<String> waitingPlayers) throws RemoteException;
}
