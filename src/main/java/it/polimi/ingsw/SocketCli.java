package it.polimi.ingsw;

import it.polimi.ingsw.model.gameobjects.WindowPatternCard;
import it.polimi.ingsw.socket.ClientController;
import it.polimi.ingsw.socket.requests.ReconnectionRequest;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;


public class SocketCli implements Serializable, MatchObserver {

    private Cli cli;
    private String username;
    private transient ClientController clientController;

    public SocketCli(String username, ClientController clientController, boolean single) {
        this.cli = new Cli(username, null, clientController, single);
        clientController.setSocketCli(this);
        this.username = username;
        this.clientController = clientController;
        cli.printWelcome();
    }

    public void reconnect() {
        clientController.request(new ReconnectionRequest(username));
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
    public void onMyWindow(WindowPatternCard window) {
        cli.onMyWindow(window);
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
    public void onGameClosing() {
        cli.onGameClosing();
    }

    @Override
    public void onMyFavorTokens(int value) {
        cli.onMyFavorTokens(value);
    }

    @Override
    public void onOtherFavorTokens(int value, String name) {
        cli.onOtherFavorTokens(value, name);
    }

    @Override
    public void onOtherSchemeCards(WindowPatternCard scheme, String name) {
        cli.onOtherSchemeCards(scheme, name);
    }

    @Override
    public void onGameEnd(String winner, List<String> rankingNames, List<Integer> rankingValues) {
        cli.onGameEnd(winner, rankingNames, rankingValues);
    }

    @Override
    public void onAfterReconnection(String toolcards, String publicCards, String privateCard, String reserve, String roundTrack, int myTokens, WindowPatternCard mySchemeCard, Map<String, Integer> otherTokens, Map<String, WindowPatternCard> otherSchemeCards, boolean schemeCardChosen) {
        cli.onAfterReconnection(toolcards, publicCards, privateCard, reserve, roundTrack, myTokens, mySchemeCard, otherTokens, otherSchemeCards, schemeCardChosen);
    }

    @Override
    public void onRoundTrack(String roundTrack) {
        cli.onRoundTrack(roundTrack);
    }
}




