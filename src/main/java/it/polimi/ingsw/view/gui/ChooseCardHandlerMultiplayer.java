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

    /**
     * Instantiates a new handler and initializes the four card choices buttons by disabling them until it's the player's turn.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        parent = new ChooseCardHandler(false, false);
        parent.initialize(card0, card1, card2, card3);
        exit = false;
    }

    /**
     * When the player clicks on the PLAY button the handler checks if it's the player's turn.
     * If it isn't, it shows a message in the text area. If it is, but the player did't choose any card, it shows
     * an alert message. Once the card is chosen and the PLAY button clicked, the url of the chosen window pattern card
     * and the player's choice are stored.
     */
    @FXML
    public void onPlayClicked() {
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
            if (!exit) {
                parent.onQuitClicked();
            } else {
                parent.terminateGame();
            }
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
     * Appends a new message in the scene text area.
     *
     * @param s is the new message to append.
     */
    void appendToTextArea(String s) {
        parent.appendToTextArea(textArea, s);
    }

    public void setTurn(boolean myTurn) {
        parent.setTurn(myTurn);
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
     * The scene text area shows a message displaying all the player's opponents in the current match.
     *
     * @param players is the list of the names of all players in the match.
     */
    void showOpponents(List<String> players) {
        StringBuilder otherPlayers = new StringBuilder();
        for (String s : players) {
            if (!s.equals(parent.username)) {
                otherPlayers.append(" - ");
                otherPlayers.append(s);
            }
        }
        textArea.setText("Stai giocando contro:" + otherPlayers.toString());
    }

    /**
     * Derives the private objective card image url from its name and then shows the image.
     *
     * @param privateCard is the private objective card name.
     */
    public void setPrivateCard(String privateCard) {
        Image privateObjCardImg = new Image(getClass().getResourceAsStream(ChooseCardHandler.PRIVATE_CARDS_URL + privateCard + ".png"));
        privateObjCard.setImage(privateObjCardImg);
    }

    /**
     * The game is closed when there's only one player left.
     * The text area shows a message telling the player he is the winner because he's the only player left in the game.
     * Actions on board are disabled.
     */
    void onGameClosing() {
        parent.appendToTextArea(textArea, "Congratulazioni! Sei il vincitore. Sei rimasto da solo in gioco.\n\nClicca su ESCI per terminare");
        disableActionsOnBoard();
        exit = true;
    }

    /**
     * All elements on the board are disabled except the QUIT button.
     */
    private void disableActionsOnBoard() {
        card0.setDisable(true);
        card1.setDisable(true);
        card2.setDisable(true);
        card3.setDisable(true);
        play.setDisable(true);
        privateObjCard.setDisable(true);
    }

}