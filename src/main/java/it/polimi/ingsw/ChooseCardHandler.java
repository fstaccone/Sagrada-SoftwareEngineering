package it.polimi.ingsw;

import it.polimi.ingsw.control.RemoteController;
import it.polimi.ingsw.socket.ClientController;
import it.polimi.ingsw.socket.requests.ChooseWindowRequest;
import it.polimi.ingsw.socket.requests.QuitGameRequest;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ChooseCardHandler implements Initializable {

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
    TextArea opponents;

    @FXML
    ImageView privateObjCard;

    @FXML
    Button quit;

    private int choice;

    private String url0;
    private String url1;
    private String url2;
    private String url3;
    private Stage window;
    private RemoteController remoteController;
    private ClientController clientController;
    private String username;
    private String imgURL;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        choice = 5;
        card0.setDisable(true);
        card1.setDisable(true);
        card2.setDisable(true);
        card3.setDisable(true);
    }

    @FXML
    public void onQuitClicked() throws RemoteException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to exit?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Exit");
        alert.setHeaderText(null);
        alert.setResizable(false);
        alert.setGraphic(null);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            window.close();
            if (remoteController != null)
                remoteController.quitGame(username, false);
            else
                clientController.request(new QuitGameRequest(username, false));
            System.exit(0);
        }
    }

    @FXML
    public void onPlayClicked() throws Exception {
        if (choice == 5) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have to choose a card", ButtonType.OK);
            alert.setTitle("CHOOSE A CARD");
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
            if (remoteController != null)
                remoteController.chooseWindow(username, choice, false);
            else
                clientController.request(new ChooseWindowRequest(username, choice, false));
        }
    }

    @FXML
    public void chosen0(MouseEvent mouseEvent) {
        choice = 0;
    }

    @FXML
    public void chosen1(MouseEvent mouseEvent) {
        choice = 1;
    }

    @FXML
    public void chosen2(MouseEvent mouseEvent) {
        choice = 2;
    }

    @FXML
    public void chosen3(MouseEvent mouseEvent) {
        choice = 3;
    }

    //Initializing
    public void init(Stage windowFromGui, Scene sceneFromGui, RemoteController remoteController, ClientController clientController, String username) {
        this.remoteController = remoteController;
        this.clientController = clientController;
        this.username = username;
        window = windowFromGui;
        Platform.runLater(() -> {
            window.setScene(sceneFromGui);
            window.setTitle("Game");
            window.setResizable(false);
            window.show();
        });
    }

    //The window now shows the 4 window pattern cards the player have to choose between
    public void setWindows(List<String> windows) {
        card0.setDisable(false);
        card1.setDisable(false);
        card2.setDisable(false);
        card3.setDisable(false);
        int i = 0;
        ArrayList<String> imageURLs = new ArrayList<>();
        for (String s : windows) {
            BufferedReader reader = new BufferedReader(new StringReader(s));
            try {
                reader.readLine();
                s = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(i + s);
            i++;
            //Now I get a substring with the name of the window pattern card
            String genericURL = "File:./src/main/java/it/polimi/ingsw/resources/window_pattern_card/";
            s = genericURL + s.substring(6).toLowerCase().replaceAll(" ", "_").replaceAll("'", "") + (".png");
            //Questo stampa l'url da cui prenderemo l'immagine, per ora serve a controllare se qualche è scritto male
            System.out.println(s);
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

    public void setOpponents(List<String> players) {
        StringBuilder otherPlayers = new StringBuilder();
        for (String s : players) {
            if (!s.equals(username)) {
                otherPlayers.append(" - ");
                otherPlayers.append(s);
            }
        }
        opponents.setText("Your match starts now! You are playing SAGRADA against:" + otherPlayers.toString());
    }

    public void setTextArea(String s) {
        s = "\n" + s;
        opponents.appendText(s);
    }

    public void setPrivateCard(String privateCard) {
        Image privateObjCardImg = new Image("File:./src/main/java/it/polimi/ingsw/resources/private_objective_cards/" + privateCard + ".png");
        privateObjCard.setImage(privateObjCardImg);
    }

}