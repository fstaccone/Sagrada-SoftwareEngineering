package it.polimi.ingsw;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.ResourceBundle;

public class WaitingScreenHandler extends UnicastRemoteObject implements Initializable, LobbyObserver {

    @FXML
    public TextArea text = new TextArea();
   // StringProperty s = new SimpleStringProperty(); //Esperimento con bind

    public void setString(String string){
        text.setText(string);
    }


    public WaitingScreenHandler() throws RemoteException {
        super();
        System.out.println("Costruttore handler ok");
        //text.textProperty().bind(s);            //Esperimento con bind
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
            //text.setText("Waiting...");
    }

    @Override
    public void onWaitingPlayers(List<String> waitingPlayers) {
        //this.waitingPlayers.setText(waitingPlayers.toString());
        System.out.println("Elenco giocatori da waitingScreen:");
        waitingPlayers.stream().forEachOrdered(System.out::println);
        System.out.println();
        //s.setValue(waitingPlayers.toString());      //Esperimento con bind
       // text.setText("Players waiting...\n" + s.getValue());          //Esperimento con bind
        text.setText("Players waiting...\n" + waitingPlayers.toString());
        System.out.println("Testo in Label:" + text.getText());
    }
}
