package it.polimi.ingsw;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.ResourceBundle;

public class WaitingScreenHandler extends UnicastRemoteObject implements LobbyObserver {


    private LoginHandler loginHandler=null;


    @FXML
    private TextArea text = new TextArea();

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
    public void onWaitingPlayers(List<String> waitingPlayers) {
        text.setText("Ordered list of waiting players (including you):\n" + waitingPlayers.toString());
    }

    @Override
    public void onMatchStarted() throws RemoteException {
        loginHandler.onMatchStarted();
    }

}
