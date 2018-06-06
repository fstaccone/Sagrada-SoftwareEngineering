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

    public RmiCli(String username, RemoteController controller, boolean single) throws RemoteException {
        super();
        cli = new Cli(username, controller, null, single);
        this.controller = controller;
        reconnection = false;
        cli.printWelcome();
    }

    public void launch() {
        try {
            controller.observeMatch(cli.getUsername(), this, reconnection);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void reconnect() throws RemoteException, InterruptedException {
        reconnection = true;
        controller.reconnect(cli.getUsername());
        launch();
    }

    @Override
    public void onYourTurn(boolean yourTurn, String string) {
        cli.onYourTurn(yourTurn, string);
    }

    @Override
    public void onReserve(String string) {
        cli.onReserve(string);
    }

    @Override
    public void onWindowChoise(List<String> windows) {
        cli.onWindowChoise(windows);
    }

    @Override
    public void onAfterWindowChoise() {
        cli.onAfterWindowChoise();
    }

    @Override
    public void onMyWindow(WindowPatternCard window) {
        cli.onMyWindow(window);
    }

    @Override
    public void onMyFavorTokens(int value) {
        cli.onMyFavorTokens(value);
    }

    @Override
    public void onOtherFavorTokens(int value, String name) {
        cli.onOtherFavorTokens(value, name);
    }

    public void onOtherSchemeCards(WindowPatternCard scheme, String name) {
        cli.onOtherSchemeCards(scheme, name);
    }

    @Override
    public void onOtherTurn(String name) {
        cli.onOtherTurn(name);
    }

    @Override
    public void onInitialization(String toolcards, String publicCards, String privateCard, List<String> players) {
        cli.onInitialization(toolcards, publicCards, privateCard, players);
    }

    @Override
    public void onPlayerExit(String name) {
        cli.onPlayerExit(name);
    }

    @Override
    public void onPlayerReconnection(String name) {
        cli.onPlayerReconnection(name);
    }

    @Override
    public void onGameStarted(boolean windowChosen, List<String> names) {
        cli.onGameStarted(names);
    }

    @Override
    public void onGameClosing() {
        cli.onGameClosing();
    }

    @Override
    public void onGameEnd(String winner, List<String> rankingNames, List<Integer> rankingValues) {
        cli.onGameEnd(winner, rankingNames, rankingValues);
    }

    @Override
    public void onRoundTrack(String roundTrack) {
        cli.onRoundTrack(roundTrack);
    }

    @Override
    public void onAfterReconnection(String toolcards, String publicCards, String privateCard, String reserve, String roundTrack, int myTokens, WindowPatternCard mySchemeCard, Map<String, Integer> otherTokens, Map<String, WindowPatternCard> otherSchemeCards, boolean schemeCardChosen) {
        cli.onAfterReconnection(toolcards, publicCards, privateCard, reserve, roundTrack, myTokens, mySchemeCard, otherTokens, otherSchemeCards, schemeCardChosen);
    }
}
