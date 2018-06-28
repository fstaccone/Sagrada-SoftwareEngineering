package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.control.RemoteController;
import it.polimi.ingsw.control.SocketController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;

public class ChooseCardHandlerMultiplayer implements Initializable {

    private ChooseCardHandler parent;
    private boolean exit;

    @FXML
    Button card0;
    @FXML
    Button card1;
    @FXML
    Button card2;
    @FXML
    Button card3;
    @FXML
    Button play;
    @FXML
    TextArea textArea;
    @FXML
    Button quit;
    @FXML
    ImageView privateObjCard;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        parent = new ChooseCardHandler(false, false);
        parent.initialize(card0, card1, card2, card3);
        exit = false;
    }

    @FXML
    public void onPlayClicked() throws RemoteException {
        parent.onPlayClicked(play, textArea);
    }

    public void init(Stage windowFromGui, Scene sceneFromGui, RemoteController remoteController, SocketController socketController, String username) {
        parent.init(windowFromGui, sceneFromGui, remoteController, socketController, username);
        quit.setOnMouseClicked(event -> {
            event.consume();
            if (!exit) {
                parent.onQuitClicked();
            } else {
                parent.terminateGame();
            }
        });
    }

    public void chosen0(MouseEvent mouseEvent) {
        parent.setChoice(0);
    }

    public void chosen1(MouseEvent mouseEvent) {
        parent.setChoice(1);
    }

    public void chosen2(MouseEvent mouseEvent) {
        parent.setChoice(2);
    }

    public void chosen3(MouseEvent mouseEvent) {
        parent.setChoice(3);
    }

    public void setWindows(List<String> windows) {
        parent.setWindows(windows, card0, card1, card2, card3);
    }

    public String getImageUrl() {
        return parent.getImageUrl();
    }

    public void appendToTextArea(String s) {
        parent.appendToTextArea(textArea, s);
    }

    public void setTurn(boolean myTurn) {
        parent.setTurn(myTurn);
    }

    public void welcome() {
        parent.welcome(textArea);
    }


    public void showOpponents(List<String> players) {
        StringBuilder otherPlayers = new StringBuilder();
        for (String s : players) {
            if (!s.equals(parent.username)) {
                otherPlayers.append(" - ");
                otherPlayers.append(s);
            }
        }
        textArea.setText("Stai giocando contro:\n" + otherPlayers.toString());
    }

    public void setPrivateCard(String privateCard) {
        Image privateObjCardImg = new Image(ChooseCardHandler.PRIVATE_CARDS_URL + privateCard + ".png");
        privateObjCard.setImage(privateObjCardImg);
    }

    public void onGameClosing() {
        parent.appendToTextArea(textArea, "Congratulazioni! Sei il vincitore. Sei rimasto da solo in gioco.\n\nClicca su ESCI per terminare");
        disableActionsOnBoard();
        exit = true;
    }

    private void disableActionsOnBoard() {
        card0.setDisable(true);
        card1.setDisable(true);
        card2.setDisable(true);
        card3.setDisable(true);
        play.setDisable(true);
        privateObjCard.setDisable(true);
    }

}