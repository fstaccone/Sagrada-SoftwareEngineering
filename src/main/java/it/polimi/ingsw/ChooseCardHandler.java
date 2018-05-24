package it.polimi.ingsw;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
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

    private int choice;

    private String url0;
    private String url1;
    private String url2;
    private String url3;
    private String imgURL;
    private Stage window;
    final String genericURL = "File:./src/main/java/it/polimi/ingsw/resources/window_pattern_card/";

    @Override
    public void initialize(URL location, ResourceBundle resources){
        choice = 0;
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
                    imgURL = url0;
                    break;
                case 2:
                    imgURL = url1;
                    break;
                case 3:
                    imgURL = url2;
                    break;
                case 4:
                    imgURL = url3;
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
            //TODO: VA AGGIUNTO IL MESSAGGIO CON LA SCELTA DEL GIOCATORE DA RESTITUIRE AL SERVER
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

    //Initializing
    public void init(Stage windowFromRmiGui, Scene sceneFromRmiGui, List<String> windows){
        window = windowFromRmiGui;
        Platform.runLater(()->{
            window.setScene(sceneFromRmiGui);
            window.setTitle("Game");
            window.setResizable(false);
            window.show();
        });
        int i = 0;
        ArrayList<String> imageURLs = new ArrayList<>();
        for(String s : windows){
            //TODO: si può usare libreria java.io?
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
            s = genericURL + s.substring(6).toLowerCase().replaceAll(" ","_").replaceAll("'","") + (".png");
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
        Platform.runLater(()->card0.setGraphic(cardView0));
        //Initializing card 1
        url1 = imageURLs.get(1);
        Image cardImg1 = new Image(url1);
        ImageView cardView1 = new ImageView(cardImg1);
        cardView1.setFitWidth(220);
        cardView1.setFitHeight(192);
        Platform.runLater(()->card1.setGraphic(cardView1));
        //Initializing card 2
        url2 = imageURLs.get(2);
        Image cardImg2 = new Image(url2);
        ImageView cardView2 = new ImageView(cardImg2);
        cardView2.setFitWidth(220);
        cardView2.setFitHeight(192);
        Platform.runLater(()->card2.setGraphic(cardView2));
        //Initializing card 3
        url3 = imageURLs.get(3);
        Image cardImg3 = new Image(url3);
        ImageView cardView3 = new ImageView(cardImg3);
        cardView3.setFitWidth(220);
        cardView3.setFitHeight(192);
        Platform.runLater(()->card3.setGraphic(cardView3));
    }
}

