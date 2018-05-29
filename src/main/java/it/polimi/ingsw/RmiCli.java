package it.polimi.ingsw;

import it.polimi.ingsw.control.RemoteController;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class RmiCli extends UnicastRemoteObject implements MatchObserver {

    private Cli cli;
    private String username;
    private transient RemoteController controller;

    public RmiCli(String username, RemoteController controller, boolean single) throws RemoteException {
        super();
        this.cli = new Cli(username, controller, null, single);
        this.username = username;
        this.controller = controller;
        cli.printWelcome();
    }

    public void launch() throws RemoteException {
        controller.observeMatch(username, this);
    }

    public void reconnect() throws RemoteException, InterruptedException {
        controller.reconnect(username);
        launch();
    }

    @Override
    public void onPlayers(List<String> playersNames) {
        cli.onPlayers(playersNames);
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
    public void onMyWindow(String window) {
        cli.onMyWindow(window);
    }

    @Override
    public void onMyFavorTokens(int value){
        cli.onMyFavorTokens(value);
    }

    @Override
    public void onOtherFavorTokens(int value, String name){
        cli.onOtherFavorTokens(value,name);
    }

    public void onOtherSchemeCards(String scheme, String name){
        cli.onOtherSchemeCards(scheme,name);
    }

    @Override
    public void onOtherTurn(String name) {
        cli.onOtherTurn(name);
    }

    @Override
    public void onInitialization(String toolcards, String publicCards, String privateCard, boolean windowChosen) {
        cli.onInitialization(toolcards, publicCards, privateCard, windowChosen);
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
    public void onShowTrack(String track) {
        cli.onShowTrack(track);
    }

    @Override
    public void onGameClosing() { cli.onGameClosing(); }

    @Override
    public void onGameEnd(String winner, List<String> rankingNames, List<Integer> rankingValues) {
        cli.onGameEnd(winner, rankingNames, rankingValues);
    }
}
