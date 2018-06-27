package it.polimi.ingsw;

import it.polimi.ingsw.control.RemoteController;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

public class RmiCli extends UnicastRemoteObject implements MatchObserver {

    private Cli cli;
    private transient RemoteController controller;
    private boolean reconnection;
    private boolean single;


    /**
     * Initializes the RmiCli
     * @param username         is the name of the player who is the owner of this view
     * @param controllerRmi is the client side controller used by this view (if it uses Socket connection) to contact the model
     * @param singleplayer           is a boolean used to let Cli understand if it is singleplayer or not
     */
    public RmiCli(String username, RemoteController controllerRmi, boolean singleplayer) throws RemoteException {
        super();
        cli = new Cli(username, controllerRmi, null, singleplayer);
        this.controller = controllerRmi;
        reconnection = false;
        this.single = singleplayer;
        cli.printWelcome();
    }

    /**
     * This view asks to observe the match
     */
    public void launch() {
        try {
            controller.observeMatch(cli.getUsername(), this, single, reconnection);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Request of reconnection launched to the controller
     */
    public void reconnect() throws RemoteException, InterruptedException {
        reconnection = true;
        controller.reconnect(cli.getUsername());
        launch();
    }

    /**
     * Notify propagated to Cli: there is explained the use of parameters
     */
    @Override
    public void onYourTurn(boolean yourTurn, String string, int round, int turn) {
        cli.onYourTurn(yourTurn, string, round, turn);
    }

    /**
     * Notify propagated to Cli: there is explained the use of parameters
     */
    @Override
    public void onReserve(String string) {
        cli.onReserve(string);
    }

    /**
     * Notify propagated to Cli: there is explained the use of parameters
     */
    @Override
    public void onWindowChoice(List<String> windows) {
        cli.onWindowChoice(windows);
    }

    /**
     * Notify propagated to Cli: there is explained the use of parameters
     */
    @Override
    public void onAfterWindowChoice() {
        cli.onAfterWindowChoice();
    }

    /**
     * Notify propagated to Cli: there is explained the use of parameters
     */
    @Override
    public void onMyWindow(String[][] window) {
        cli.onMyWindow(window);
    }

    /**
     * Notify propagated to Cli: there is explained the use of parameters
     */
    @Override
    public void onMyFavorTokens(int value) {
        cli.onMyFavorTokens(value);
    }

    /**
     * Notify propagated to Cli: there is explained the use of parameters
     */
    @Override
    public void onOtherFavorTokens(int value, String name) {
        cli.onOtherFavorTokens(value, name);
    }

    /**
     * Notify propagated to Cli: there is explained the use of parameters
     */
    @Override
    public void onOtherSchemeCards(String[][] scheme, String name, String cardName) {
        cli.onOtherSchemeCards(scheme, name);
    }

    /**
     * Notify propagated to Cli: there is explained the use of parameters
     */
    @Override
    public void onOtherTurn(String name) {
        cli.onOtherTurn(name);
    }

    /**
     * Notify propagated to Cli: there is explained the use of parameters
     */
    @Override
    public void onInitialization(String toolcards, String publicCards, List<String> privateCard, List<String> players) {
        cli.onInitialization(toolcards, publicCards, privateCard, players);
    }

    /**
     * Notify propagated to Cli: there is explained the use of parameters
     */
    @Override
    public void onPlayerExit(String name) {
        cli.onPlayerExit(name);
    }

    /**
     * Notify propagated to Cli: there is explained the use of parameters
     */
    @Override
    public void onPlayerReconnection(String name) {
        cli.onPlayerReconnection(name);
    }

    /**
     * Notify propagated to Cli: there is explained the use of parameters
     */
    @Override
    public void onGameStarted(boolean windowChosen, List<String> names) {
        cli.onGameStarted(names);
    }

    /**
     * Notify propagated to Cli: there is explained the use of parameters
     */
    @Override
    public void onGameClosing() {
        cli.onGameClosing();
    }

    /**
     * Notify propagated to Cli: there is explained the use of parameters
     */
    @Override
    public void onGameEnd(String winner, List<String> rankingNames, List<Integer> rankingValues) {
        cli.onGameEnd(winner, rankingNames, rankingValues);
    }

    /**
     * Notify propagated to Cli: there is explained the use of parameters
     */
    @Override
    public void onRoundTrack(String roundTrack) {
        cli.onRoundTrack(roundTrack);
    }

    /**
     * Notify propagated to Cli: there is explained the use of parameters
     */
    @Override
    public void onAfterReconnection(String toolcards, String publicCards, List<String> privateCard, String reserve, String roundTrack, int myTokens, String[][] mySchemeCard, String schemeCardName, Map<String, Integer> otherTokens, Map<String, String[][]> otherSchemeCards,Map<String, String> otherSchemeCardNamesMap, boolean schemeCardChosen, Map<String, Integer> toolCardsPrices) {
        cli.onAfterReconnection(toolcards, publicCards, privateCard, reserve, roundTrack, myTokens, mySchemeCard, otherTokens, otherSchemeCards, schemeCardChosen, toolCardsPrices);
    }

    /**
     * Notify propagated to Cli: there is explained the use of parameters
     */
    @Override
    public void onToolCardUsedByOthers(String name, int toolNumber) {
        cli.onToolCardUsedByOthers(name, toolNumber);
    }

    /**
     * Notify propagated to Cli: there is explained the use of parameters
     */
    @Override
    public void onGameEndSingle(int goal, int points) {
        cli.onGameEndSingle(goal, points);
    }

    /**
     * Notify propagated to Cli: there is explained the use of parameters
     */
    @Override
    public void onChoosePrivateCard() { cli.onChoosePrivateCard(); }

}
