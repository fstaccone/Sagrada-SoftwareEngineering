package it.polimi.ingsw;

import it.polimi.ingsw.control.RemoteController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GameBoardHandler implements Initializable {

    @FXML
    Button b1;
    @FXML
    Button b2;
    @FXML
    Button b3;
    @FXML
    Button b4;
    @FXML
    Button b5;
    @FXML
    Button b6;
    @FXML
    Button b7;
    @FXML
    Button b8;
    @FXML
    Button b9;
    @FXML
    Button b10;
    @FXML
    Button b11;
    @FXML
    Button b12;
    @FXML
    Button b13;
    @FXML
    Button b14;
    @FXML
    Button b15;
    @FXML
    Button b16;
    @FXML
    Button b17;
    @FXML
    Button b18;
    @FXML
    Button b19;
    @FXML
    Button b20;
    @FXML
    Pane playerWindowPatternCard;
    @FXML
    Button cartaObPub1;
    @FXML
    Button tool0;
    @FXML
    Button tool1;
    @FXML
    Button tool2;
    @FXML
    Button passButton;
    @FXML
    Button reserveDice0;
    @FXML
    Button reserveDice1;
    @FXML
    Button reserveDice2;
    @FXML
    Button reserveDice3;
    @FXML
    Button reserveDice4;
    @FXML
    Button reserveDice5;
    @FXML
    Button reserveDice6;
    @FXML
    Button reserveDice7;
    @FXML
    Button reserveDice8;

    private RemoteController controller;
    private String username;
    private Stage window;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void init(Stage windowFromRmiGui, Scene sceneFromRmiGui, RemoteController controller, String username) {
        this.controller = controller;
        this.username = username;
        window = windowFromRmiGui;
        Platform.runLater(() -> {
            window.setScene(sceneFromRmiGui);
            window.setTitle("GameBoard");
            window.setResizable(false);
            window.show();
        });
    }

    public void setWindowPatternCardImg(String imgURL){
        BackgroundImage myBI= new BackgroundImage(new Image(imgURL,340,300,false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        playerWindowPatternCard.setBackground(new Background(myBI));
    }

    public void setToolCards(List<String> toolCardsList){
        String genericURL = "File:./src/main/java/it/polimi/ingsw/resources/toolcards/";
        //Initializing toolCard0
        String url0 = genericURL + toolCardsList.get(0) + ".png";
        Image cardImg0 = new Image(url0);
        ImageView cardView0 = new ImageView(cardImg0);
        cardView0.setFitWidth(158);
        cardView0.setFitHeight(240);
        Platform.runLater(()->tool0.setGraphic(cardView0));
        //Initializing toolCard1
        String url1 = genericURL + toolCardsList.get(1) + ".png";
        Image cardImg1 = new Image(url1);
        ImageView cardView1 = new ImageView(cardImg1);
        cardView1.setFitWidth(158);
        cardView1.setFitHeight(240);
        Platform.runLater(()->tool1.setGraphic(cardView1));
        //Initializing toolCard2
        String url2 = genericURL + toolCardsList.get(2) + ".png";
        Image cardImg2 = new Image(url2);
        ImageView cardView2 = new ImageView(cardImg2);
        cardView2.setFitWidth(158);
        cardView2.setFitHeight(240);
        Platform.runLater(()->tool2.setGraphic(cardView2));
    }

    public void setReserve(List<String> dicesList){
        String genericURL = "File:./src/main/java/it/polimi/ingsw/resources/dices/dice_";
        List<Button> reserveDices = new ArrayList<>();
        reserveDices.add(reserveDice0);
        reserveDices.add(reserveDice1);
        reserveDices.add(reserveDice2);
        reserveDices.add(reserveDice3);
        reserveDices.add(reserveDice4);
        reserveDices.add(reserveDice5);
        reserveDices.add(reserveDice6);
        reserveDices.add(reserveDice7);
        reserveDices.add(reserveDice8);
        int i = 0;
        for(Button dice : reserveDices){
            Platform.runLater(()-> dice.setGraphic(null));
            if(dicesList.size() > i && dicesList.get(i)!=null){
                String url = genericURL + dicesList.get(i) + ".png";
                Image diceImg = new Image(url);
                ImageView diceView = new ImageView(diceImg);
                diceView.setFitWidth(70);
                diceView.setFitHeight(70);
                Platform.runLater(()->dice.setGraphic(diceView));
            }
            i++;
        }

    }

    @FXML
    public void onPassButtonClicked() throws RemoteException {
        controller.goThrough(username, false);
    }

}
