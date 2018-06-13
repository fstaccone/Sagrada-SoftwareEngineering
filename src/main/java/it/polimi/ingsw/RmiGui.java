package it.polimi.ingsw;

import it.polimi.ingsw.control.RemoteController;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

public class RmiGui extends UnicastRemoteObject implements MatchObserver {

    private Gui gui;
    private RemoteController controller;
    /* it is useful to notify the client the state of the game after the reconnection */
    private boolean reconnection;
    private boolean single;

    public RmiGui(Stage fromLogin, String username, RemoteController controller, boolean singleplayer) throws RemoteException {
        super();
        this.gui = new Gui(fromLogin, username, controller, null, singleplayer);
        this.controller = controller;
        reconnection = false;
        single = singleplayer;
    }

    public List<String> getPlayers() {
        return gui.getPlayers();
    }

    public void launch() {
        try {
            controller.observeMatch(gui.getUsername(), this, single, reconnection);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void reconnect() throws RemoteException, InterruptedException {
        reconnection = true;
        controller.reconnect(gui.getUsername());
        launch();
    }

    @Override
    public void onYourTurn(boolean isMyTurn, String string, int round, int turn) {
        gui.onYourTurn(isMyTurn, string, round, turn);
    }

    @Override
    public void onReserve(String string) {
        gui.onReserve(string);
    }

    @Override
    public void onWindowChoice(List<String> windows) {
        gui.onWindowChoice(windows);
    }

    @Override
    public void onAfterWindowChoice() {
        gui.onAfterWindowChoice();
    }

    @Override
    public void onToolCardUsedByOthers(String name, int toolNumber) {
        gui.onToolCardUsedByOthers(name, toolNumber);
    }

    @Override
    public void onGameEndSingle(int goal, int points) {
        gui.onGameEndSingle(goal, points);
    }

    @Override
    public void onMyWindow(WindowPatternCard window) {
        gui.onMyWindow(window);
    }

    @Override
    public void onMyFavorTokens(int value) {
        gui.onMyFavorTokens(value);
    }

    @Override
    public void onOtherFavorTokens(int value, String name) {
        gui.onOtherFavorTokens(value, name);
    }

    @Override
    public void onOtherSchemeCards(WindowPatternCard window, String name) {
        gui.onOtherSchemeCards(window, name);
    }

    @Override
    public void onOtherTurn(String name) {
        gui.onOtherTurn(name);
    }

    @Override
    public void onInitialization(String toolcards, String publicCards, List<String> privateCards, List<String> players) {
        gui.onInitialization(toolcards, publicCards, privateCards, players);
    }

    @Override
    public void onPlayerExit(String name) {
        gui.onPlayerExit(name);
    }

    @Override
    public void onPlayerReconnection(String name) {
        gui.onPlayerReconnection(name);
    }

    @Override
    public void onGameStarted(boolean windowChosen, List<String> names) {
        gui.onGameStarted(windowChosen, names);
    }

    @Override
    public void onGameClosing() {
        gui.onGameClosing();
    }

    @Override
    public void onGameEnd(String winner, List<String> rankingNames, List<Integer> rankingValues) {
        gui.onGameEnd(winner, rankingNames, rankingValues);
    }

    @Override
    public void onAfterReconnection(String toolcards, String publicCards, List<String> privateCards, String reserve, String roundTrack, int myTokens, WindowPatternCard schemeCard, Map<String, Integer> otherTokens, Map<String, WindowPatternCard> otherSchemeCards, boolean schemeCardChosen, Map<String, Integer> toolcardsPrices) {
        gui.onAfterReconnection(toolcards, publicCards, privateCards, reserve, roundTrack, myTokens, schemeCard, otherTokens, otherSchemeCards, schemeCardChosen, toolcardsPrices);
    }

    @Override
    public void onRoundTrack(String track) {
        gui.onRoundTrack(track);
    }
}