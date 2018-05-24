package it.polimi.ingsw;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ResourceBundle;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setWindowPatternCardImg(String imgURL){
        BackgroundImage myBI= new BackgroundImage(new Image(imgURL,340,300,false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        playerWindowPatternCard.setBackground(new Background(myBI));
    }
}
