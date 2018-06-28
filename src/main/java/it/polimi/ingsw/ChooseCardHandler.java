package it.polimi.ingsw;

import it.polimi.ingsw.control.RemoteController;
import it.polimi.ingsw.socket.ClientController;
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

    public ChooseCardHandler(boolean single, boolean myTurn) {
        this.single = single;
        choice = ChooseCardHandler.OUT_OF_RANGE;
        this.myTurn = myTurn;
    }

    protected static final String WINDOWS_URL = "File:./src/main/java/it/polimi/ingsw/resources/window_pattern_card/";
    protected static final String PRIVATE_CARDS_URL = "File:./src/main/java/it/polimi/ingsw/resources/private_objective_cards/";
    protected static final int OUT_OF_RANGE = 5;

    protected int choice;
    private boolean myTurn;
    protected boolean single;
    private String url0;
    private String url1;
    private String url2;
    private String url3;

    private Stage window;

    private RemoteController controllerRmi;
    private ClientController controllerSocket;

    protected String username;
    private String imgURL;

    public void initialize(Button card0, Button card1, Button card2, Button card3) {
        card0.setDisable(true);
        card1.setDisable(true);
        card2.setDisable(true);
        card3.setDisable(true);
    }

    public void onQuitClicked() {
        ButtonType yes = new ButtonType("SÌ", ButtonBar.ButtonData.YES);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Confermi di voler abbandonare la partita?", yes, ButtonType.NO);
        alert.setTitle("Abbandona");
        alert.setHeaderText(null);
        alert.setResizable(false);
        alert.setGraphic(null);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
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

    public void onPlayClicked(Button play, TextArea textArea) throws RemoteException {
        if (myTurn) {
            if (choice == OUT_OF_RANGE) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Devi scegliere una carta schema!", ButtonType.OK);
                alert.setTitle("Scelta della carta");
                alert.setHeaderText(null);
                alert.setResizable(false);
                alert.setGraphic(null);
                alert.showAndWait();
            } else {
                Stage window = (Stage) play.getScene().getWindow();

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

    //Initializing
    public void init(Stage windowFromGui, Scene sceneFromGui, RemoteController remoteController, ClientController clientController, String username) {
        this.controllerRmi = remoteController;
        this.controllerSocket = clientController;
        this.username = username;
        window = windowFromGui;
        Platform.runLater(() -> {
            window.setScene(sceneFromGui);
            window.setTitle("Scelta della carta schema");
            window.setResizable(false);
            window.show();
        });
    }

    //The window now shows the 4 window pattern cards the player have to choose between
    public void setWindows(List<String> windows, Button card0, Button card1, Button card2, Button card3) {
        card0.setDisable(false);
        card1.setDisable(false);
        card2.setDisable(false);
        card3.setDisable(false);

        ArrayList<String> imageURLs = new ArrayList<>();
        for (String s : windows) {
            BufferedReader reader = new BufferedReader(new StringReader(s));
            try {
                reader.readLine();
                s = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            s = WINDOWS_URL + s.substring(6).toLowerCase().replaceAll(" ", "_").replaceAll("'", "") + (".png");
            imageURLs.add(s);
        }

        //Initializing card 0
        url0 = imageURLs.get(0);
        Image cardImg0 = new Image(url0);
        ImageView cardView0 = new ImageView(cardImg0);
        cardView0.setFitWidth(220);
        cardView0.setFitHeight(192);
        Platform.runLater(() -> card0.setGraphic(cardView0));
        //Initializing card 1
        url1 = imageURLs.get(1);
        Image cardImg1 = new Image(url1);
        ImageView cardView1 = new ImageView(cardImg1);
        cardView1.setFitWidth(220);
        cardView1.setFitHeight(192);
        Platform.runLater(() -> card1.setGraphic(cardView1));
        //Initializing card 2
        url2 = imageURLs.get(2);
        Image cardImg2 = new Image(url2);
        ImageView cardView2 = new ImageView(cardImg2);
        cardView2.setFitWidth(220);
        cardView2.setFitHeight(192);
        Platform.runLater(() -> card2.setGraphic(cardView2));
        //Initializing card 3
        url3 = imageURLs.get(3);
        Image cardImg3 = new Image(url3);
        ImageView cardView3 = new ImageView(cardImg3);
        cardView3.setFitWidth(220);
        cardView3.setFitHeight(192);
        Platform.runLater(() -> card3.setGraphic(cardView3));
    }

    public String getImageUrl() {
        return imgURL;
    }

    public void appendToTextArea(TextArea textArea, String s) {
        s = "\n" + s;
        textArea.appendText(s);
        textArea.setScrollTop(Double.MAX_VALUE);
    }

    public void setTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

    public void setChoice(int choice) {
        this.choice = choice;
    }

    public void welcome(TextArea textArea) {
        appendToTextArea(textArea, "Benvenuto in questa nuova partita di Sagrada. Buon divertimento!");
    }

    public void terminateGame() {
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
}
