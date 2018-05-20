package it.polimi.ingsw;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface MatchObserver extends Serializable, Remote {
    void onPlayers(List<String> playersNames) throws RemoteException;
    void onYourTurn( boolean isMyTurn) throws RemoteException;
    void onReserve(String string) throws RemoteException;
}
