package it.polimi.ingsw;

import it.polimi.ingsw.control.RemoteController;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface MatchObserver extends Serializable, Remote {
    void onPlayers(List<String> playersNames) throws RemoteException;

    void onYourTurn(boolean isMyTurn, String string) throws RemoteException;

    void onReserve(String string) throws RemoteException;

    void onWindowChoise(List<String> windows) throws RemoteException;

    void onAfterWindowChoise() throws RemoteException;

    void onMyWindow(String window) throws RemoteException;

    void onMyFavorTokens(int value) throws RemoteException;

    void onOtherFavorTokens(int value, String name) throws RemoteException;

    void onOtherSchemeCards(String string, String name) throws RemoteException;

    void onOtherTurn(String name) throws RemoteException;

    void onInitialization(String toolcards, String publicCards, String privateCard) throws RemoteException;

    void onPlayerExit(String name) throws RemoteException;

    void onPlayerReconnection(String name) throws RemoteException;

    void onShowTrack(String track) throws RemoteException;

    void onGameClosing() throws RemoteException;

    void onGameEnd(String winner, List<String> rankingNames, List<Integer> rankingValues) throws RemoteException;

    // todo: completare con gli altri elementi (mappe, segnalini, ...)
    void onAfterReconnection(String toolcards, String publicCards, String privateCard) throws RemoteException;
}
