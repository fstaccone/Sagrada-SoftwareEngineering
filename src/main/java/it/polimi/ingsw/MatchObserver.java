package it.polimi.ingsw;

import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface MatchObserver extends Serializable, Remote {

    void onYourTurn(boolean isMyTurn, String string, int Round, int Turn) throws RemoteException;

    void onReserve(String string) throws RemoteException;

    void onWindowChoice(List<String> windows) throws RemoteException;

    void onAfterWindowChoice() throws RemoteException;

    void onMyWindow(String[][] window) throws RemoteException;

    void onMyFavorTokens(int value) throws RemoteException;

    void onOtherFavorTokens(int value, String name) throws RemoteException;

    void onOtherSchemeCards(String[][] window, String name, String cardName) throws RemoteException;

    void onOtherTurn(String name) throws RemoteException;

    void onInitialization(String toolcards, String publicCards, List<String> privateCard, List<String> players) throws RemoteException;

    void onPlayerExit(String name) throws RemoteException;

    void onPlayerReconnection(String name) throws RemoteException;

    void onGameStarted(boolean windowChosen, List<String> names) throws RemoteException;

    void onGameClosing() throws RemoteException;

    void onGameEnd(String winner, List<String> rankingNames, List<Integer> rankingValues) throws RemoteException;

    void onAfterReconnection(String toolcards, String publicCards, List<String> privateCard, String reserve, String roundTrack, int myTokens, String[][] schemeCard, String schemeCardName, Map<String, Integer> otherTokens, Map<String, String[][]> otherSchemeCards,Map<String, String> otherSchemeCardNamesMap, boolean schemeCardChosen, Map<String, Integer> toolcardsPrices) throws RemoteException;

    void onRoundTrack(String track) throws RemoteException;

    void onToolCardUsedByOthers(String name, int toolNumber) throws RemoteException;

    void onGameEndSingle(int goal, int points) throws RemoteException;

    void onChoosePrivateCard() throws RemoteException;
}
