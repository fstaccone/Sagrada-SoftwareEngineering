package it.polimi.ingsw.control;


import it.polimi.ingsw.ConnectionStatus;
import it.polimi.ingsw.LobbyObserver;
import it.polimi.ingsw.MatchObserver;
import it.polimi.ingsw.model.gameobjects.Colors;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteController extends Remote {

    void createMatch(String name) throws RemoteException;

    ConnectionStatus checkName(String name) throws RemoteException;

    void addPlayer(String name) throws RemoteException;

    void observeLobby(String name, LobbyObserver lobbyObserver) throws RemoteException;

    void observeMatch(String username, MatchObserver observer, boolean reconnection) throws RemoteException;

    void removePlayer(String name) throws RemoteException;

    boolean placeDice(int dice, int x, int y, String name, boolean isSingle) throws RemoteException;

    boolean placeDiceTool11(int x, int y, String name, boolean isSingle) throws RemoteException;

    void goThrough(String name, boolean isSingle) throws RemoteException;

    void showPlayers(String name) throws RemoteException;

    void chooseWindow(String name, int index, boolean isSingle) throws RemoteException;

    void quitGame(String name, boolean isSingle) throws RemoteException;

    void reconnect(String name) throws RemoteException, InterruptedException;

    Colors askForDiceColor(String name, boolean isSingle) throws RemoteException;

    void setDiceValue(int value, String name, boolean isSingle) throws RemoteException;

    boolean useToolCard1(int diceChosen, String incrOrDecr, String username, boolean isSingle) throws RemoteException;

    boolean useToolCard2or3(int n, int startX, int startY, int finalX, int finalY, String username, boolean isSingle) throws RemoteException;

    boolean useToolCard4(int startX1, int startY1, int finalX1, int finalY1, int startX2, int startY2, int finalX2, int finalY2, String username, boolean isSingle) throws RemoteException;

    boolean useToolCard5(int diceChosen, int roundChosen, int diceChosenFromRound, String username, boolean isSingle) throws RemoteException;

    boolean useToolCard6(int diceChosen, String username, boolean isSingle) throws RemoteException;

    boolean useToolCard7(String username, boolean isSingle) throws RemoteException;

    boolean useToolCard8(String name, boolean single) throws RemoteException;

    boolean useToolCard9(int diceChosen, int finalX1, int finalY1, String username, boolean isSingle) throws RemoteException;

    boolean useToolCard10(int diceChosen, String username, boolean isSingle) throws RemoteException;

    boolean useToolCard11(int diceChosen, String username, boolean isSingle) throws RemoteException;

    boolean useToolCard12(int roundFromTrack, int diceInRound, int startX1, int startY1, int finalX1, int finalY1, int startX2, int startY2, int finalX2, int finalY2, String name, boolean isSingle) throws RemoteException;
}
