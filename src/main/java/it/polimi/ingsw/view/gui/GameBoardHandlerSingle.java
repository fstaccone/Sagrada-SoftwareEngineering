package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.socket.requests.PrivateCardChosenRequest;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameBoardHandlerSingle {
    private Gui gui;
    private GameBoardHandler gameBoardHandler;
    private Map<Integer, Button> toolCardsMap = new HashMap<>();

    @FXML
    Label sacrificeLabel;
    @FXML
    Label privObjLabel;
    @FXML
    Label pubObjLabel;
    @FXML
    Label toolCardsLabel;
    @FXML
    Pane toolPane;
    @FXML
    Pane toolPane1;
    @FXML
    Pane toolPane2;
    @FXML
    Button useButton;
    @FXML
    Label toolLabel;
    @FXML
    Pane playerWindowPatternCard;
    @FXML
    Button tool0;
    @FXML
    Button tool1;
    @FXML
    Button tool2;
    @FXML
    Button tool3;
    @FXML
    Button tool4;
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
    @FXML
    Pane forPrivateCardChoice;
    @FXML
    Button leftCard;
    @FXML
    Button rightCard;

    void init(Scene scene, Gui gui) {
        this.gui = gui;
        gameBoardHandler = new GameBoardHandler(gui.isSingle(), null, this, gameBoard, toolPane, toolLabel, useButton, sacrificeLabel);
        gameBoardHandler.init(scene, gui);
        initializeToolMap();
        Platform.runLater(() -> {
            useButton.setVisible(false);
            toolLabel.setVisible(false);
            toolPane.setVisible(false);
            gui.getWindowStage().setOnCloseRequest(event -> {
                event.consume();
                gameBoardHandler.quitClicked();
            });
            forPrivateCardChoice.setVisible(false);
            quit.setOnMouseClicked(event -> {
                event.consume();
                gameBoardHandler.quitClicked();
            });
        });
    }

    private void initializeToolMap() {
        toolCardsMap.put(0, tool0);
        toolCardsMap.put(1, tool1);
        toolCardsMap.put(2, tool2);
        toolCardsMap.put(3, tool3);
        toolCardsMap.put(4, tool4);
    }

    void appendToTextArea(String s) {
        gameBoardHandler.appendToTextArea(s);
    }

    public Pane getRoundTrack() {
        return roundTrack;
    }

    public TextArea getTextArea() {
        return textArea;
    }

    Pane getPlayerWindowPatternCard() {
        return playerWindowPatternCard;
    }

    void setWindowPatternCardImg(String imgURL) {
        gameBoardHandler.setWindowPatternCardImg(imgURL);
    }

    void showResultForSingle(int goal, int points) {

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
        forPrivateCardChoice.setVisible(false);
        privObjCard0.setDisable(true);
        privObjCard1.setDisable(true);
    }

    private void disableActionsOnGameBoard() {
        reserve.setDisable(true);
        playerWindowPatternCard.setDisable(true);
        tool0.setDisable(true);
        tool1.setDisable(true);
        tool2.setDisable(true);
        tool3.setDisable(true);
        tool4.setDisable(true);
        passButton.setDisable(true);
        pubObjCard0.setDisable(true);
        pubObjCard1.setDisable(true);
    }

    public void setReserve(List<String> dicesList) {
        gameBoardHandler.setReserve(dicesList, reserve);
    }

    void setPrivateCards(String privateCard1, String privateCard2) {
        gameBoardHandler.setSinglePrivateCard(privObjLabel, privObjCard0, privObjCard1, privateCard1);
        gameBoardHandler.setSinglePrivateCard(privObjLabel, privObjCard1, privObjCard0, privateCard2);
    }

    public void onRoundTrack(String track) {
        gameBoardHandler.onRoundTrack(track);
    }

    public void setPublicCards(List<String> publicCards) {
        setSinglePublicCard(pubObjLabel, pubObjCard0, pubObjCard1, publicCards.get(0));
        setSinglePublicCard(pubObjLabel, pubObjCard1, pubObjCard0, publicCards.get(1));
    }

    private void setSinglePublicCard(Label label, ImageView main, ImageView other1, String publicCard) {
        Image publicObjCardImg1 = new Image(getClass().getResourceAsStream(GameBoardHandler.PUBLIC_CARDS_PATH + publicCard + ".png"));
        main.setImage(publicObjCardImg1);
        main.setOnMouseEntered(event -> {
            main.setTranslateX(-10);
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

    void setMyWindow(String[][] window) {
        gameBoardHandler.setMySchemeCard(playerWindowPatternCard, window);
    }

    private EventHandler<ActionEvent> toolSelected = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            gameBoardHandler.toolSelected(event.getSource().toString().substring(14, 15));
        }
    };

    void setToolCards(List<String> toolCardsList) {
        for (int i = 0; i < toolCardsList.size(); i++) {
            setSingleToolcard(toolCardsMap.get(i), toolCardsList.get(i));
        }
        for (int i = toolCardsList.size(); i < 5; i++) {
            toolCardsMap.get(i).setDisable(true);
        }
    }

    private void setSingleToolcard(Button main, String toolcard) {
        Image cardImg = new Image(getClass().getResourceAsStream(GameBoardHandler.TOOLCARDS_PATH + toolcard + ".png"));

        ImageView cardView = new ImageView(cardImg);
        cardView.setFitWidth(158);
        cardView.setFitHeight(240);
        Platform.runLater(() -> main.setGraphic(cardView));
        main.setOnAction(toolSelected);
        main.setOnMouseEntered(event -> {
            main.setTranslateX(20);
            main.setTranslateY(-60);
            main.setStyle("-fx-scale-x: 1.5;-fx-scale-y: 1.5");
            for (int i : toolCardsMap.keySet()) {
                if (toolCardsMap.get(i) != null && toolCardsMap.get(i) != main) {
                    toolCardsMap.get(i).setVisible(false);
                }
            }
            toolCardsLabel.setVisible(false);
        });

        main.setOnMouseExited(event -> {
            main.setTranslateX(0);
            main.setTranslateY(0);
            main.setStyle("-fx-scale-x: 1.0;-fx-scale-y: 1.0");
            for (int i : toolCardsMap.keySet()) {
                if (toolCardsMap.get(i) != null && toolCardsMap.get(i) != main) {
                    toolCardsMap.get(i).setVisible(true);
                }
            }
            toolCardsLabel.setVisible(true);
        });
    }

    void choosePrivateCard() {
        disableActionsOnGameBoard();
        gameBoardHandler.appendToTextArea("Sei giunto al termine della partita, nel riquadro a destra scegli" +
                "quale carta obiettivo utilizzare per il calcolo del punteggio");
        Platform.runLater(() -> forPrivateCardChoice.setVisible(true));

        leftCard.setOnMouseClicked(event -> {
            if (gui.getControllerRmi() != null) {
                try {
                    gui.getControllerRmi().choosePrivateCard(gui.getUsername(), 0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else if (gui.getControllerSocket() != null) {
                gui.getControllerSocket().request(new PrivateCardChosenRequest(gui.getUsername(), 0));
            }
        });

        rightCard.setOnMouseClicked(event -> {
            if (gui.getControllerRmi() != null) {
                try {
                    gui.getControllerRmi().choosePrivateCard(gui.getUsername(), 1);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else if (gui.getControllerSocket() != null) {
                gui.getControllerSocket().request(new PrivateCardChosenRequest(gui.getUsername(), 1));
            }
        });
    }

    @FXML
    public void onPassButtonClicked() {
        gameBoardHandler.passButtonClicked();
    }

    void resetToolValues() {
        gameBoardHandler.resetToolValues();
    }

    void setDiceChosenOutOfRange(){
        gameBoardHandler.setDiceChosenOutOfRange();
    }
}
