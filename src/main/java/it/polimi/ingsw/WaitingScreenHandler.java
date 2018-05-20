package it.polimi.ingsw;

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

    public WaitingScreenHandler() throws RemoteException {
        super();
        log=new TextArea();
        text=new TextArea();
    }

    @Override
    public void onPlayerExit(String name) {
        log.setText("Player " + name.toUpperCase() + " has left the room!");
    }

    @Override
    public void onLastPlayer(String name) {
        log.setText(name.toUpperCase() + " has left the room, you are the only one remaining. Timer has been canceled!");
    }

    @Override
    public void onWaitingPlayers(List<String> waitingPlayers) {
        String wPlayers=waitingPlayers.toString().toUpperCase().replaceAll("\\[","");
        wPlayers=wPlayers.replaceAll("\\]","");
        text.setText("Ordered list of waiting players (including you):\n" + wPlayers);
    }

    @Override
    public void onMatchStarted() throws RemoteException {
        loginHandler.onMatchStarted();
    }

    public void setString(String string){
        text.setText(string);
    }

    public void setLoginHandler(LoginHandler loginHandler){
        this.loginHandler=loginHandler;
    }

}
