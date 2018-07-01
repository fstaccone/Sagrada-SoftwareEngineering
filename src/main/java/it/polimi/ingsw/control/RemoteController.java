package it.polimi.ingsw.control;


import it.polimi.ingsw.model.gamelogic.ConnectionStatus;
import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.view.LobbyObserver;
import it.polimi.ingsw.view.MatchObserver;

import java.io.ObjectOutputStream;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteController extends Remote {

    void createMatch(String name, int difficulty, ObjectOutputStream socketOut) throws RemoteException;

    ConnectionStatus checkName(String name) throws RemoteException;

    void addPlayer(String name) throws RemoteException;

    void observeLobby(String name, LobbyObserver lobbyObserver) throws RemoteException;

    void observeMatch(String username, MatchObserver observer, boolean single, boolean reconnection) throws RemoteException;

    void removePlayer(String name) throws RemoteException;

    boolean placeDice(int dice, int x, int y, String name, boolean isSingle) throws RemoteException;

    boolean placeDiceTool11(int x, int y, String name, boolean isSingle) throws RemoteException;

    void goThrough(String name, boolean isSingle) throws RemoteException;

    void chooseWindow(String name, int index, boolean isSingle) throws RemoteException;

    void quitGame(String name, boolean isSingle) throws RemoteException;

    void reconnect(String name) throws RemoteException, InterruptedException;

    Colors askForDiceColor(String name, boolean isSingle) throws RemoteException;

    void choosePrivateCard(String username, int cardPosition) throws RemoteException;

    void setDiceValue(int value, String name, boolean isSingle) throws RemoteException;

    boolean useToolCard1(int diceToBeSacrificed, int diceChosen, String incrOrDecr, String username, boolean isSingle) throws RemoteException;

    boolean useToolCard2or3(int diceToBeSacrificed, int n, int startX, int startY, int finalX, int finalY, String username, boolean isSingle) throws RemoteException;

    boolean useToolCard4(int diceToBeSacrificed, int startX1, int startY1, int finalX1, int finalY1, int startX2, int startY2, int finalX2, int finalY2, String username, boolean isSingle) throws RemoteException;

    boolean useToolCard5(int diceToBeSacrificed, int diceChosen, int roundChosen, int diceChosenFromRound, String username, boolean isSingle) throws RemoteException;

    boolean useToolCard6(int diceToBeSacrificed, int diceChosen, String username, boolean isSingle) throws RemoteException;

    boolean useToolCard7(int diceToBeSacrificed, String username, boolean isSingle) throws RemoteException;

    boolean useToolCard8(int diceToBeSacrificed, String name, boolean single) throws RemoteException;

    boolean useToolCard9(int diceToBeSacrificed, int diceChosen, int finalX1, int finalY1, String username, boolean isSingle) throws RemoteException;

    boolean useToolCard10(int diceToBeSacrificed, int diceChosen, String username, boolean isSingle) throws RemoteException;

    boolean useToolCard11(int diceToBeSacrificed, int diceChosen, String username, boolean isSingle) throws RemoteException;

    boolean useToolCard12(int diceToBeSacrificed, int roundFromTrack, int diceInRound, int startX1, int startY1, int finalX1, int finalY1, int startX2, int startY2, int finalX2, int finalY2, String name, boolean isSingle) throws RemoteException;

    void removeMatch(String name) throws RemoteException;
}
