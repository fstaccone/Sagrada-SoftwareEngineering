package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.LobbyObserver;
import it.polimi.ingsw.view.LoginHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class WaitingScreenHandler extends UnicastRemoteObject implements LobbyObserver {


    private LoginHandler loginHandler;

    @FXML
    private TextArea text;

    @FXML
    private TextArea log;

    /**
     * Constructor of WaitingScreenHandler, instantiates two text areas.
     * @throws RemoteException todo
     */
    public WaitingScreenHandler() throws RemoteException {
        super();
        log = new TextArea();
        text = new TextArea();
    }

    /**
     * Shows a message in the log textArea telling that a player's left the waiting room..
     * @param name is the name of the player who has left the waiting room.
     */
    @Override
    public void onPlayerExit(String name) {
        log.setText("Il giocatore " + name + " è uscito dalla sala di attesa prima dell'inizio della partita!");
    }

    /**
     * Shows a message in the log textArea telling that a player's left the waiting room and the current player is the
     * only player left.
     * @param name is the name of the player who has left the waiting room.
     */
    @Override
    public void onLastPlayer(String name) {
        log.setText(name + " è uscito dalla sala di attesa, sei l'unico rimasto. Il timer è stato cancellato!");
    }

    /**
     * Shows a message with the updated list of players currently in the waiting room.
     * @param waitingPlayers is the list of players currently in the waiting room.
     */
    @Override
    public void onWaitingPlayers(List<String> waitingPlayers) {
        String wPlayers = waitingPlayers.toString().replaceAll("\\[", "");
        wPlayers = wPlayers.replaceAll("]", "");
        text.setText("Lista ordinata dei giocatori nella sala di attesa (tu incluso):\n" + wPlayers);
    }

    @Override
    public void onMatchStarted() {
        loginHandler.onMatchStartedRmi();
    }

    @Override
    public void onCheckConnection() {
        //just to check connection before the starting of the match in case other notifies didn't happen
    }

    public void setString(String string) {
        text.setText(string);
    }

    public void setLoginHandler(LoginHandler loginHandler) {
        this.loginHandler = loginHandler;
    }

}
