package it.polimi.ingsw.control;


import it.polimi.ingsw.LobbyObserver;
import it.polimi.ingsw.MatchObserver;
import it.polimi.ingsw.model.gameobjects.Dice;
import it.polimi.ingsw.view.ViewInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteController extends Remote{

    // todo: login serve ancora?
    String login(String username, ViewInterface view) throws RemoteException;
    void createMatch(String name) throws  RemoteException;
    boolean checkName(String name) throws RemoteException;
    void addPlayer(String name) throws RemoteException;
    void observeLobby(String name, LobbyObserver lobbyObserver) throws RemoteException;
    void observeMatch(String username, MatchObserver observer) throws RemoteException;
    void removePlayer(String name) throws RemoteException;
    boolean placeDice(int dice, int x, int y, String name, boolean isSingle) throws RemoteException;
    void goThrough(String name, boolean isSingle) throws RemoteException;
    void showPlayers(String name) throws  RemoteException;
    void showWindow(String name, String owner) throws RemoteException;
    void chooseWindow(String name, int index, boolean isSingle) throws RemoteException;
    void showToolcards(String name, boolean isSingle) throws RemoteException;
}
