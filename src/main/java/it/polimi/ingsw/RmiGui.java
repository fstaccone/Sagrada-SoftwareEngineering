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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RmiGui extends UnicastRemoteObject implements MatchObserver {

    private String username;
    private RemoteController controller;
    private Stage windowStage;
    boolean myTurn;
    private ChooseCardHandler chooseCardHandler;
    private GameBoardHandler gameBoardHandler;
    private List<String> toolCardsList = new ArrayList<>();
    private List<String> dicesList;


    public RmiGui(Stage fromLogin, String username, RemoteController controller) throws RemoteException {
        super();
        this.username = username;
        this.controller = controller;
        this.myTurn = false;
        this.windowStage = fromLogin;
    }

    @Override
    public void onPlayers(List<String> playersNames) throws RemoteException {
        System.out.println("On players");
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
        chooseCardHandler.init(windowStage, scene, controller, username);
        chooseCardHandler.setOpponents(playersNames);
    }

    @Override
    public void onYourTurn(boolean isMyTurn, String string) throws RemoteException {
        System.out.println("On your turn");
        if (string != null)
            onReserve(string);
        this.myTurn = isMyTurn;
        if (myTurn) {
            //Solo per verifica
            String s = "Now it's your turn!";
            if (gameBoardHandler != null) gameBoardHandler.setTextArea(s);
            else chooseCardHandler.setTextArea(s);
        }
        else
            //Solo per verifica
            System.out.println("It's no more your turn!");
    }


    @Override
    public void onReserve(String string) throws RemoteException {
        dicesList = new ArrayList<>();
        System.out.println("On reserve");
        String dicesString = string.substring(1, string.length() - 1);
        List<String> temp = Pattern.compile(", ")
                .splitAsStream(dicesString)
                .collect(Collectors.toList());
        for(String s : temp){
            s = s.substring(1, s.length()-1).toLowerCase().replaceAll(" ", "_");
            dicesList.add(s);
        }
        for(String dice : dicesList) System.out.println(dice);
        if(gameBoardHandler!=null) {
            gameBoardHandler.setReserve(dicesList);
        }
    }

    public void launch(){
        System.out.println("Launch");
        try {
            controller.observeMatch(username, this);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPlayerReconnection(String name) throws RemoteException {
        System.out.println("On player reconnection");
    }

    @Override
    public void onShowTrack(String track) throws RemoteException {

    }

    @Override
    public void onShowWindow(String window) throws RemoteException {
        System.out.println("On show windowStage");
        String imgUrl = chooseCardHandler.getImageUrl();
        FXMLLoader fx = new FXMLLoader();
        try {
            fx.setLocation(new URL("File:./src/main/java/it/polimi/ingsw/resources/game-board.fxml"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Parent root = null;
        try {
            root = fx.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        gameBoardHandler = fx.getController();
        gameBoardHandler.init(windowStage, scene, controller, username, this );
        gameBoardHandler.setWindowPatternCardImg(imgUrl);
        gameBoardHandler.setToolCards(toolCardsList);
        gameBoardHandler.setReserve(dicesList);
        gameBoardHandler.setTextArea("Now it's your turn!");
    }

    @Override
    public void onOtherTurn(String name) throws RemoteException {
        System.out.println("On other turn");
        String s = "Now it's " + name + "'s turn";
        if(gameBoardHandler!=null) gameBoardHandler.setTextArea(s);
        else chooseCardHandler.setTextArea(s);
    }
    
    @Override
    public void onShowToolCards(List<String> cards) throws RemoteException {
        System.out.println("On show toolcards");
    }

    @Override
    public void onToolCards(String string) throws RemoteException {
        System.out.println("On toolcards\n" + string);
        String dicesString = string.substring(1, string.length() - 1);
        List <String> temp = Pattern.compile(", ")
                .splitAsStream(dicesString)
                .collect(Collectors.toList());
        for(String s : temp){
            s = s.split(" :", 2)[0];
            toolCardsList.add(s);
        }
    }

    @Override
    public void onPlayerExit(String name) throws RemoteException {
        System.out.println("On player exit");
    }

    @Override
    public void onWindowChoise(List<String> windows) throws RemoteException {
        System.out.println("On windowStage choise");
        chooseCardHandler.setWindows(windows);
    }

    @Override
    public void onAfterWindowChoise() throws RemoteException {

    }

    public Boolean isMyTurn(){
        return myTurn;
    }
}