package it.polimi.ingsw;

import it.polimi.ingsw.model.gameobjects.WindowPatternCard;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.util.List;

public class GameBoardHandlerSingle {
    private Gui gui;
    private GameBoardHandler gameBoardHandler;

    @FXML
    Label privObjLabel;
    @FXML
    Label pubObjLabel;
    @FXML
    Label toolCardsLabel;
    @FXML
    Pane toolPane;
    @FXML
    Button useButton;
    @FXML
    Label toolLabel;
    @FXML
    Pane playerWindowPatternCard;
    @FXML
    Button toolCard0;
    @FXML
    Button toolCard1;
    @FXML
    Button toolCard2;
    @FXML
    Button toolCard3;
    @FXML
    Button toolCard4;
    @FXML
    Button passButton;
    @FXML
    TextArea textArea;
    @FXML
    Button quit;
    @FXML
    ImageView privObjCard0;
    @FXML
    ImageView privObjCard1;
    @FXML
    ImageView pubObjCard0;
    @FXML
    ImageView pubObjCard1;
    @FXML
    AnchorPane gameBoard;
    @FXML
    Pane roundTrack;
    @FXML
    Pane reserve;
    @FXML
    Label playerName;

    public void init(Scene scene, Gui gui) {
        this.gui = gui;
        gameBoardHandler = new GameBoardHandler(gui.isSingle(), null, this);
        gameBoardHandler.init(scene, gui);
        Platform.runLater(() -> {
            useButton.setVisible(false);
            toolLabel.setVisible(false);
            toolPane.setVisible(false);
        });
    }

    public void appendToTextArea(String s){
        gameBoardHandler.appendToTextArea(s);
    }

    public Pane getRoundTrack() {
        return roundTrack;
    }

    public TextArea getTextArea() {
        return textArea;
    }

    public Pane getPlayerWindowPatternCard() {
        return playerWindowPatternCard;
    }

    /**
     * the boolean dicePlaced is used to control if a dice has been placed during a turn.
     * This method initialize dicePlaced to false. If the value is false, the player can place a dice
     */
    public void initializeActions() {
        gui.setDicePlaced(false);
        // todo: aggiungere altre azioni da compiere
    }

    public void setWindowPatternCardImg(String imgURL) {
        gameBoardHandler.setWindowPatternCardImg(imgURL);
    }

    private EventHandler<MouseEvent> windowPatternCardSlotSelected = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            gameBoardHandler.windowPatternCardSlotSelected((ImageView) event.getSource());
        }
    };

    private EventHandler<MouseEvent> reserveDiceSelected = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            gameBoardHandler.reserveDiceSelected(event.getSource().toString());
        }
    };

    public void showResultForSingle(int goal, int points) {

        StringBuilder s = new StringBuilder();
        s.append("Obiettivo da battere: \t");
        s.append(goal);
        s.append("\nPunteggio ottenuto: \t");
        s.append(points);

        if (points > goal) {
            s.append("\n\nComplimenti, hai vinto!");
        } else {
            s.append("\n\nNon hai vinto!");
        }
        gameBoardHandler.appendToTextArea(s.toString());
        disableActionsOnGameBoard();
    }

    private void disableActionsOnGameBoard() {

    }

    public void setReserve(List<String> dicesList) {
        gameBoardHandler.setReserve(dicesList, reserve);
    }

    public void setPrivateCards(String privateCard1, String privateCard2) {
        gameBoardHandler.setSinglePrivateCard(privObjLabel, privObjCard0, privateCard1);
        gameBoardHandler.setSinglePrivateCard(privObjLabel, privObjCard1, privateCard2);
    }

    public void onRoundTrack(String track) {
        gameBoardHandler.onRoundTrack(track);
    }

    public void setPublicCards(List<String> publicCards) {
        setSinglePublicCard(pubObjLabel, pubObjCard0, pubObjCard1, publicCards.get(0));
        setSinglePublicCard(pubObjLabel, pubObjCard1, pubObjCard0, publicCards.get(1));
    }

    private void setSinglePublicCard(Label label, ImageView main, ImageView other1, String publicCard) {
        Image publicObjCardImg1 = new Image(GameBoardHandler.PUBLIC_CARDS_PATH + publicCard + ".png");
        main.setImage(publicObjCardImg1);
        main.setOnMouseEntered(event -> {
            main.setTranslateX(40);
            main.setTranslateY(-60);
            main.setStyle("-fx-scale-x: 2.0;-fx-scale-y: 2.0");
            other1.setVisible(false);
            label.setVisible(false);
        });
        main.setOnMouseExited(event -> {
            main.setTranslateX(0);
            main.setTranslateY(0);
            main.setStyle("-fx-scale-x: 1.0;-fx-scale-y: 1.0");
            other1.setVisible(true);
            label.setVisible(true);
        });
    }

    public void setMyWindow(WindowPatternCard window) {
        gameBoardHandler.setMySchemeCard(playerWindowPatternCard, window);
    }

    private EventHandler<ActionEvent> toolSelected = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            gameBoardHandler.toolSelected(event.getSource().toString().substring(14, 15));
        }
    };

    // todo: evitare di ripetere codice qui è più complicato del previsto
    /*
    public void setToolCards(List<String> toolCardsList) {
        for (String toolCard : toolCardsList) {
            setSingleToolcard(toolLabel, tool0, tool1, tool2, toolCardsList.get(0));
            setSingleToolcard(toolLabel, tool1, tool0, tool2, toolCardsList.get(1));
            setSingleToolcard(toolLabel, tool2, tool0, tool1, toolCardsList.get(2));
        }
    }

    private void setSingleToolcard(Label label, Button main, Button other1, Button other2, String toolcard) {
        String url = GameBoardHandler.TOOLCARDS_PATH + toolcard + ".png";
        Image cardImg = new Image(url);

        ImageView cardView = new ImageView(cardImg);
        cardView.setFitWidth(158);
        cardView.setFitHeight(240);
        Platform.runLater(() -> main.setGraphic(cardView));
        main.setOnAction(toolSelected);
        main.setOnMouseEntered(event -> {
            main.setTranslateY(-60);
            main.setStyle("-fx-scale-x: 1.5;-fx-scale-y: 1.5");
            other1.setVisible(false);
            other2.setVisible(false);
            label.setVisible(false);
        });
        main.setOnMouseExited(event -> {
            main.setTranslateY(0);
            main.setStyle("-fx-scale-x: 1.0;-fx-scale-y: 1.0");
            other1.setVisible(true);
            other2.setVisible(true);
            label.setVisible(true);
        });
    }
    */
}
