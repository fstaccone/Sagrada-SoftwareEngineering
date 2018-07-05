package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.control.RemoteController;
import it.polimi.ingsw.view.MatchObserver;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RmiGui extends UnicastRemoteObject implements MatchObserver {

    private static final Logger LOGGER = Logger.getLogger(Gui.class.getName());

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
            LOGGER.log(Level.SEVERE, "exception in observing match", e);
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
        if (single)
            gui.onAfterWindowChoiceSingleplayer();
        else
            gui.onAfterWindowChoiceMultiplayer();
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
    public void onChoosePrivateCard() {
        gui.onChoosePrivateCard();
    }

    @Override
    public void onMyWindow(String[][] window) {
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
    public void onOtherSchemeCards(String[][] window, String name, String cardName) {
        gui.onOtherSchemeCards(window, name, cardName);
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
    public void onGameStarted(boolean windowChosen, List<String> names, int turnTime) {
        gui.onGameStarted(windowChosen, names, turnTime);
    }

    @Override
    public void onGameClosing() {
        gui.onGameClosing();
    }

    @Override
    public void onGameEnd(String winner, List<String> rankingNames, List<Integer> rankingValues) {
        gui.onGameEndMulti(winner, rankingNames, rankingValues);
    }

    @Override
    public void onAfterReconnection(String toolcards, String publicCards, List<String> privateCards, String reserve, String roundTrack, int myTokens, String[][] schemeCard, String schemeCardName, Map<String, Integer> otherTokens, Map<String, String[][]> otherSchemeCards, Map<String, String> otherSchemeCardNamesMap, boolean schemeCardChosen, Map<String, Integer> toolcardsPrices) {
        gui.onAfterReconnection(toolcards, publicCards, privateCards, reserve, roundTrack, myTokens, schemeCard, schemeCardName, otherTokens, otherSchemeCards, otherSchemeCardNamesMap, schemeCardChosen, toolcardsPrices);
    }

    @Override
    public void onRoundTrack(String track) {
        gui.onRoundTrack(track);
    }
}