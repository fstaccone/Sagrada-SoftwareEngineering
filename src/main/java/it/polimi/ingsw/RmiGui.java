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

    public RmiGui(Stage fromLogin, String username, RemoteController controller, boolean single) throws RemoteException {
        super();
        this.gui = new Gui(fromLogin, username, controller, null, single);
        this.controller = controller;
        reconnection = false;
    }

    public List<String> getPlayers() {
        return gui.getPlayers();
    }

    public void launch() {
        try {
            controller.observeMatch(gui.getUsername(), this, reconnection);
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
    public void onPlayers(List<String> playersNames) {
        gui.onPlayers(playersNames);
    }

    @Override
    public void onYourTurn(boolean isMyTurn, String string) {
        gui.onYourTurn(isMyTurn, string);
    }

    @Override
    public void onReserve(String string) {
        gui.onReserve(string);
    }

    @Override
    public void onWindowChoise(List<String> windows) {
        gui.onWindowChoice(windows);
    }

    @Override
    public void onAfterWindowChoise() {
        gui.onAfterWindowChoice();
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
    public void onInitialization(String toolcards, String publicCards, String privateCard, List<String> players) {
        gui.onInitialization(toolcards, publicCards, privateCard, players);
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
    public void onGameStarted(boolean windowChosen) {
        gui.onGameStarted(windowChosen);
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
    public void onAfterReconnection(String toolcards, String publicCards, String privateCard, String reserve, String roundTrack, int myTokens, WindowPatternCard schemeCard, Map<String, Integer> otherTokens, Map<String, WindowPatternCard> otherSchemeCards, boolean schemeCardChosen) {
        gui.onAfterReconnection(toolcards, publicCards, privateCard, reserve, roundTrack, myTokens, schemeCard, otherTokens, otherSchemeCards, schemeCardChosen);
    }

    @Override
    public void onRoundTrack(String track) {
        gui.onRoundTrack(track);
    }
}