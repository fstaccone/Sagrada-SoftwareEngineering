package it.polimi.ingsw.control;


import it.polimi.ingsw.ConnectionStatus;
import it.polimi.ingsw.LobbyObserver;
import it.polimi.ingsw.MatchObserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteController extends Remote{

    void createMatch(String name) throws  RemoteException;
    ConnectionStatus checkName(String name) throws RemoteException;
    void addPlayer(String name) throws RemoteException;
    void observeLobby(String name, LobbyObserver lobbyObserver) throws RemoteException;
    void observeMatch(String username, MatchObserver observer) throws RemoteException;
    void removePlayer(String name) throws RemoteException;
    boolean placeDice(int dice, int x, int y, String name, boolean isSingle) throws RemoteException;
    void goThrough(String name, boolean isSingle) throws RemoteException;
    void showPlayers(String name) throws  RemoteException;
    void showWindow(String name, String owner) throws RemoteException;
    void chooseWindow(String name, int index, boolean isSingle) throws RemoteException;
    void quitGame(String name, boolean isSingle) throws RemoteException;
    void reconnect(String name) throws RemoteException, InterruptedException;
    boolean useToolCard1(int diceChosen, String IncrOrDecr, String username, boolean isSingle) throws RemoteException;
    boolean useToolCard2or3(int n,int startX, int startY, int finalX, int finalY,String username, boolean isSingle) throws RemoteException;
}
