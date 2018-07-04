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

public class ChooseCardHandlerSingle implements Initializable {

    private ChooseCardHandler parent;

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
    ImageView privateObjCard0;
    @FXML
    ImageView privateObjCard1;

    /**
     * Instantiates a new handler and initializes the four card choices buttons by disabling them until it's the player's turn.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        parent = new ChooseCardHandler(true, true);
        parent.initialize(card0, card1, card2, card3);
    }

    /**
     * When the player clicks on the PLAY button the handler checks if the player chose a card.
     * If the player did't choose any card, it shows an alert message.
     * Once the card is chosen and the PLAY button clicked, the url of the chosen window pattern card
     * and the player's choice are stored.
     *
     * @throws RemoteException todo
     */
    @FXML
    public void onPlayClicked() throws RemoteException {
        parent.onPlayClicked(textArea);
    }

    /**
     * Initializes the choose card scene.
     *
     * @param windowFromGui    is the Stage where the new scene has to be shown.
     * @param sceneFromGui     is the new choose card scene.
     * @param remoteController is the rmi controller.
     * @param socketController is the socket controller.
     * @param username         is the player's username.
     */
    void init(Stage windowFromGui, Scene sceneFromGui, RemoteController remoteController, SocketController socketController, String username) {
        parent.init(windowFromGui, sceneFromGui, remoteController, socketController, username);
        quit.setOnMouseClicked(event -> {
            event.consume();
            parent.onQuitClicked();
        });
    }

    /**
     * When the player clicks on the first card option, it is set as the player's choice.
     *
     * @param mouseEvent is the click on the card.
     */
    public void chosen0(MouseEvent mouseEvent) {
        parent.setChoice(0);
    }

    /**
     * When the player clicks on the second card option, it is set as the player's choice.
     *
     * @param mouseEvent is the click on the card.
     */
    public void chosen1(MouseEvent mouseEvent) {
        parent.setChoice(1);
    }

    /**
     * When the player clicks on the third card option, it is set as the player's choice.
     *
     * @param mouseEvent is the click on the card.
     */
    public void chosen2(MouseEvent mouseEvent) {
        parent.setChoice(2);
    }

    /**
     * When the player clicks on the fourth card option, it is set as the player's choice.
     *
     * @param mouseEvent is the click on the card.
     */
    public void chosen3(MouseEvent mouseEvent) {
        parent.setChoice(3);
    }

    /**
     * The window now shows the four window pattern cards the player have to choose between.
     *
     * @param windows is a list of the names of the four window pattern cards proposed.
     */
    public void setWindows(List<String> windows) {
        parent.setWindows(windows, card0, card1, card2, card3);
    }

    String getImageUrl() {
        return parent.getImageUrl();
    }

    /**
     * Appends a welcome message to the text area showing also the time limit for each turn.
     *
     * @param turnTime is the time limit for each turn.
     */
    void welcome(int turnTime) {
        parent.welcome(textArea, turnTime);
    }

    /**
     * Derives the two private objective cards image urls from their names and then shows the images.
     *
     * @param privateCard0 is the first private objective card name.
     * @param privateCard1 is the second private objective card name.
     */
    void setPrivateCard(String privateCard0, String privateCard1) {
        Image image0 = new Image(getClass().getResourceAsStream(ChooseCardHandler.PRIVATE_CARDS_URL + privateCard0 + ".png"));
        privateObjCard0.setImage(image0);
        Image image1 = new Image(getClass().getResourceAsStream(ChooseCardHandler.PRIVATE_CARDS_URL + privateCard1 + ".png"));
        privateObjCard1.setImage(image1);
    }

    void afterDisconnection() {
        parent.getWindow().setOnCloseRequest(e -> parent.appendToTextArea(textArea, "Aspetta la chiusura automatica"));
        play.setDisable(true);
        quit.setDisable(true);
        parent.showErrorAlert("La tua connessione è caduta,  questa finestra sarà chiusa tra 10 secondi." +
                "\nInizia una nuova partita eseguendo un nuovo login.");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        parent.closeWindow();
    }

}