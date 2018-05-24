package it.polimi.ingsw;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ChooseCardHandler implements Initializable {

    @FXML
    Button card1;

    @FXML
    Button card2;

    @FXML
    Button card3;

    @FXML
    Button card4;

    @FXML
    Button play;

    private int choice;

    private String url1;
    private String url2;
    private String url3;
    private String url4;
    private String imgURL;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        choice = 0;
        //Initializing card 1
        url1 = "File:./src/main/java/it/polimi/ingsw/resources/window_pattern_card/prova-img.png";
        Image cardImg1 = new Image(url1);
        ImageView cardView1 = new ImageView(cardImg1);
        cardView1.setFitWidth(220);
        cardView1.setFitHeight(192);
        card1.setGraphic(cardView1);
        //Initializing card 2
        url2 = "File:./src/main/java/it/polimi/ingsw/resources/window_pattern_card/prova-img2.png";
        Image cardImg2 = new Image(url2);
        ImageView cardView2 = new ImageView(cardImg2);
        cardView2.setFitWidth(220);
        cardView2.setFitHeight(192);
        card2.setGraphic(cardView2);
        //Initializing card 3
        url3 = "File:./src/main/java/it/polimi/ingsw/resources/window_pattern_card/comitas.png";
        Image cardImg3 = new Image(url3);
        ImageView cardView3 = new ImageView(cardImg3);
        cardView3.setFitWidth(220);
        cardView3.setFitHeight(192);
        card3.setGraphic(cardView3);
        //Initializing card 4
        url4 = "File:./src/main/java/it/polimi/ingsw/resources/window_pattern_card/industria.png";
        Image cardImg4 = new Image(url4);
        ImageView cardView4 = new ImageView(cardImg4);
        cardView4.setFitWidth(220);
        cardView4.setFitHeight(192);
        card4.setGraphic(cardView4);


    }

    @FXML
    public void onPlayClicked(MouseEvent mouseEvent) throws Exception {
        if(choice==0){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have to choose a card", ButtonType.OK);
            alert.setTitle("CHOOSE A CARD");
            alert.setHeaderText(null);
            alert.setResizable(false);
            alert.setGraphic(null);
            alert.showAndWait();
        }
        else {
            Stage window = (Stage) play.getScene().getWindow();
            window.close();
            //Getting the image URL of the chosen window pattern card
            switch (choice){
                case 1:
                    imgURL = url1;
                    break;
                case 2:
                    imgURL = url2;
                    break;
                case 3:
                    imgURL = url3;
                    break;
                case 4:
                    imgURL = url4;
                    break;
                default: imgURL = null;
            }
            //opening Game Board window
            FXMLLoader fx = new FXMLLoader();
            fx.setLocation(new URL("File:./src/main/java/it/polimi/ingsw/resources/game-board.fxml"));
            Scene scene = new Scene(fx.load());
            GameBoardHandler handler = fx.getController();
            // initializing player's window pattern card
            handler.setWindowPatternCardImg(imgURL);
            window.setScene(scene);
            window.setTitle("Game");
            window.setResizable(false);
            window.show();
        }
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

    @FXML
    public void chosen4(MouseEvent mouseEvent) {
        choice = 4;
    }
}

