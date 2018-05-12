package it.polimi.ingsw.control;

import it.polimi.ingsw.Client;
import it.polimi.ingsw.Lobby;
import it.polimi.ingsw.LobbyObserver;
import it.polimi.ingsw.MatchObserver;
import it.polimi.ingsw.view.ViewInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteController extends Remote{

    String login(String username, ViewInterface view) throws RemoteException;
    void createMatch(String name) throws  RemoteException;
    boolean checkName(String name) throws RemoteException;
    void addPlayer(String name) throws RemoteException;
    void observeLobby(LobbyObserver lobbyObserver) throws RemoteException;
    void observeMatch(String username, MatchObserver observer) throws RemoteException;
}
