package it.polimi.ingsw;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class WaitingScreenHandler extends UnicastRemoteObject implements LobbyObserver {


    private LoginHandler loginHandler=null;


    @FXML
    private TextArea text = new TextArea();

    @FXML
    private TextArea log = new TextArea();

    public void setString(String string){
        text.setText(string);
    }

    public void setLoginHandler(LoginHandler loginHandler){
        this.loginHandler=loginHandler;
    }

    public WaitingScreenHandler() throws RemoteException {
        super();
    }

    @Override
    public void onPlayerExit(String name) throws RemoteException {
        log.setText("Player " + name.toUpperCase() + " has left the room!");
    }

    @Override
    public void onLastPlayer(String name) throws RemoteException {
        log.setText(name.toUpperCase() + " has left the room, you are the only one remaining. Timer has been canceled!");
    }

    @Override
    public void onWaitingPlayers(List<String> waitingPlayers) {
        text.setText("Ordered list of waiting players (including you):\n" + waitingPlayers.toString());
        log.setText("");
    }

    @Override
    public void onMatchStarted() throws RemoteException {
        loginHandler.onMatchStarted();
    }

}
