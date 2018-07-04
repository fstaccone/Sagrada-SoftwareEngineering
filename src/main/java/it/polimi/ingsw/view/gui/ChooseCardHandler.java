package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.control.RemoteController;
import it.polimi.ingsw.control.SocketController;
import it.polimi.ingsw.socket.requests.ChooseWindowRequest;
import it.polimi.ingsw.socket.requests.QuitGameRequest;
import it.polimi.ingsw.socket.requests.TerminateMatchRequest;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ChooseCardHandler {

    private static final String WINDOWS_URL = "/images/cards/window_pattern_cards/";
    private static final int OUT_OF_RANGE = 5;
    static final String PRIVATE_CARDS_URL = "/images/cards/private_objective_cards/";

    private int choice;
    private boolean myTurn;
    protected boolean single;
    private String url0;
    private String url1;
    private String url2;
    private String url3;

    private Stage window;

    private RemoteController controllerRmi;
    private SocketController controllerSocket;

    protected String username;
    private String imgURL;

    /**
     * Constructor for ChooseCardHandler. The window pattern card choice value is initialized to a default value.
     *
     * @param single is true is this is a single player match, false otherwise.
     * @param myTurn is true if it's the player's turn, false otherwise.
     */
    ChooseCardHandler(boolean single, boolean myTurn) {
        this.single = single;
        choice = ChooseCardHandler.OUT_OF_RANGE;
        this.myTurn = myTurn;
    }

    /**
     * Initializes the four card choices buttons by disabling them until it's the player's turn.
     *
     * @param card0 first option.
     * @param card1 second option.
     * @param card2 third option.
     * @param card3 fourth option.
     */
    void initialize(Button card0, Button card1, Button card2, Button card3) {
        card0.setDisable(true);
        card1.setDisable(true);
        card2.setDisable(true);
        card3.setDisable(true);
    }

    public Stage getWindow() {
        return window;
    }

    /**
     * Shows an alert message when the player clicks on QUIT button or on the X in the top-right corner.
     * If the player answers by clicking YES the handler closes the window and starts the quitting game process.
     */
    void onQuitClicked() {
        ButtonType yes = new ButtonType("SÌ", ButtonBar.ButtonData.YES);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Confermi di voler abbandonare la partita?", yes, ButtonType.NO);
        alert.setTitle("Abbandona");
        alert.setHeaderText(null);
        alert.setResizable(false);
        alert.setGraphic(null);
        alert.showAndWait();
        if (alert.getResult() == yes) {
            window.close();
            if (controllerRmi != null) {
                try {
                    controllerRmi.quitGame(username, single);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else
                controllerSocket.request(new QuitGameRequest(username, single));
            System.exit(0);
        }
    }

    /**
     * When the player clicks on the PLAY button the handler checks if it's the player's turn.
     * If it isn't, it shows a message in the text area. If it is, but the player did't choose any card, it shows
     * an alert message. Once the card is chosen and the PLAY button clicked, the url of the chosen window pattern card
     * and the player's choice are stored.
     *
     * @param textArea in the choose card scene where messages are shown
     * @throws RemoteException todo
     */
    void onPlayClicked(TextArea textArea) throws RemoteException {
        if (myTurn) {
            if (choice == OUT_OF_RANGE) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Devi scegliere una carta schema!", ButtonType.OK);
                alert.setTitle("Scelta della carta");
                alert.setHeaderText(null);
                alert.setResizable(false);
                alert.setGraphic(null);
                alert.showAndWait();
            } else {
                //Getting the image URL of the chosen window pattern card
                switch (choice) {
                    case 0:
                        imgURL = url0;
                        break;
                    case 1:
                        imgURL = url1;
                        break;
                    case 2:
                        imgURL = url2;
                        break;
                    case 3:
                        imgURL = url3;
                        break;
                    default:
                        imgURL = null;
                }
                if (controllerRmi != null)
                    controllerRmi.chooseWindow(username, choice, single);
                else
                    controllerSocket.request(new ChooseWindowRequest(username, choice, single));
            }
        } else {
            appendToTextArea(textArea, "Aspetta il tuo turno per scegliere la carta!");
        }
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
        this.controllerRmi = remoteController;
        this.controllerSocket = socketController;
        this.username = username;
        window = windowFromGui;
        Platform.runLater(() -> {
            window.setScene(sceneFromGui);
            window.setTitle("Scelta della carta schema");
            window.setResizable(false);
            window.show();
        });
        windowFromGui.setOnCloseRequest(event -> {
            event.consume();
            onQuitClicked();
        });
    }

    /**
     * The window now shows the four window pattern cards the player have to choose between.
     *
     * @param windows is a list of the names of the four window pattern cards proposed.
     * @param card0   button corresponding to the first option.
     * @param card1   button corresponding to the second option.
     * @param card2   button corresponding to the third option.
     * @param card3   button corresponding to the fourth option.
     */
    void setWindows(List<String> windows, Button card0, Button card1, Button card2, Button card3) {
        card0.setDisable(false);
        card1.setDisable(false);
        card2.setDisable(false);
        card3.setDisable(false);

        ArrayList<String> imageURLs = new ArrayList<>();
        for (String s : windows) {
            BufferedReader reader = new BufferedReader(new StringReader(s));
            try {
                s = reader.readLine(); //ignored line
                s = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            s = WINDOWS_URL + s.substring(6).toLowerCase().replaceAll(" ", "_").replaceAll("'", "") + (".png");
            imageURLs.add(s);
        }

        //Initializing card 0
        url0 = imageURLs.get(0);
        Image cardImg0 = new Image(getClass().getResourceAsStream(url0));
        ImageView cardView0 = new ImageView(cardImg0);
        cardView0.setFitWidth(220);
        cardView0.setFitHeight(192);
        Platform.runLater(() -> card0.setGraphic(cardView0));
        //Initializing card 1
        url1 = imageURLs.get(1);
        Image cardImg1 = new Image(getClass().getResourceAsStream(url1));
        ImageView cardView1 = new ImageView(cardImg1);
        cardView1.setFitWidth(220);
        cardView1.setFitHeight(192);
        Platform.runLater(() -> card1.setGraphic(cardView1));
        //Initializing card 2
        url2 = imageURLs.get(2);
        Image cardImg2 = new Image(getClass().getResourceAsStream(url2));
        ImageView cardView2 = new ImageView(cardImg2);
        cardView2.setFitWidth(220);
        cardView2.setFitHeight(192);
        Platform.runLater(() -> card2.setGraphic(cardView2));
        //Initializing card 3
        url3 = imageURLs.get(3);
        Image cardImg3 = new Image(getClass().getResourceAsStream(url3));
        ImageView cardView3 = new ImageView(cardImg3);
        cardView3.setFitWidth(220);
        cardView3.setFitHeight(192);
        Platform.runLater(() -> card3.setGraphic(cardView3));
    }

    String getImageUrl() {
        return imgURL;
    }

    /**
     * Appends a new message in the scene text area.
     *
     * @param textArea is the scene text area.
     * @param s        is the new message to append.
     */
    void appendToTextArea(TextArea textArea, String s) {
        s = "\n" + s;
        textArea.appendText(s);
        textArea.setScrollTop(Double.MAX_VALUE);
    }

    public void setTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

    void setChoice(int choice) {
        this.choice = choice;
    }

    /**
     * Appends a welcome message to the text area showing also the time limit for each turn.
     *
     * @param textArea is the scene text area.
     * @param turnTime is the time limit for each turn.
     */
    void welcome(TextArea textArea, int turnTime) {
        appendToTextArea(textArea, "Benvenuto in questa nuova partita di Sagrada. Buon divertimento!");
        appendToTextArea(textArea, "Hai a disposizione " + turnTime / 1000 + " secondi ad ogni turno per giocare!");
    }

    /**
     * Closes the window and removes the player from the match.
     */
    void terminateGame() {
        window.close();
        if (controllerRmi != null) {
            try {
                controllerRmi.removeMatch(username);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else if (controllerSocket != null) {
            controllerSocket.request(new TerminateMatchRequest(username));
        }
        System.exit(0);
    }

    /**
     * Shows an error alert window. Title and message are passed as parameters.
     *
     * @param message of the alert window.
     */
    void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Caduta di connessione");
        alert.setHeaderText(null);
        alert.setResizable(false);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * close the window and terminate the process
     */
    void closeWindow() {
        window.close();
        System.exit(0);
    }
}
