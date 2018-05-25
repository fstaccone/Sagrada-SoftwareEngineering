package it.polimi.ingsw;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface MatchObserver extends Serializable, Remote {
    void onPlayers(List<String> playersNames) throws RemoteException;
    void onYourTurn( boolean isMyTurn, String string) throws RemoteException;
    void onReserve(String string) throws RemoteException;
    void onWindowChoise(List<String> windows) throws RemoteException;
    void onAfterWindowChoise() throws RemoteException;
    void onShowWindow(String window) throws RemoteException;
    void onOtherTurn(String name) throws RemoteException;
    void onShowToolCards(List<String> cards) throws RemoteException;
    void onToolCards(String string) throws RemoteException;
    void onPlayerExit(String name) throws RemoteException;
    void onPlayerReconnection(String name) throws RemoteException;
}
