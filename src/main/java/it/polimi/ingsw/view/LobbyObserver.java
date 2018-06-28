package it.polimi.ingsw.view;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface LobbyObserver extends Serializable, Remote {
    void onLastPlayer(String name) throws RemoteException;

    void onPlayerExit(String name) throws RemoteException;

    void onWaitingPlayers(List<String> waitingPlayers) throws RemoteException;

    void onMatchStarted() throws RemoteException;

    void onCheckConnection() throws RemoteException;
}
