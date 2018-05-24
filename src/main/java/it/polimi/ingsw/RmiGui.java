package it.polimi.ingsw;

import it.polimi.ingsw.control.RemoteController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class RmiGui extends UnicastRemoteObject implements MatchObserver {

    private String username;
    private RemoteController controller;
    private Stage window;
    boolean myTurn;
    private ChooseCardHandler chooseCardHandler;


    public RmiGui(Stage fromLogin, String username, RemoteController controller) throws RemoteException {
        super();
        this.username = username;
        this.controller = controller;
        this.myTurn = false;
        this.window = fromLogin;
    }

    @Override
    public void onPlayers(List<String> playersNames) throws RemoteException {

    }

    @Override
    public void onYourTurn(boolean isMyTurn, String string) throws RemoteException {

    }


    @Override
    public void onReserve(String string) throws RemoteException {

    }

    public void launch(){
        try {
            controller.observeMatch(username, this);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onShowWindow(String window) throws RemoteException {

    }

    @Override
    public void onOtherTurn(String name) throws RemoteException {
    }
    
    @Override
    public void onShowToolCards(List<String> cards) throws RemoteException {
    }

    @Override
    public void onToolCards(String string) throws RemoteException {

    }

    @Override
    public void onPlayerExit(String name) throws RemoteException {

    }

    @Override
    public void onWindowChoise(List<String> windows) throws RemoteException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            fxmlLoader.setLocation(new URL("File:./src/main/java/it/polimi/ingsw/resources/choose-card.fxml"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        chooseCardHandler = fxmlLoader.getController();
        Scene scene = new Scene(root);
        chooseCardHandler.init(window, scene, windows);
    }
}