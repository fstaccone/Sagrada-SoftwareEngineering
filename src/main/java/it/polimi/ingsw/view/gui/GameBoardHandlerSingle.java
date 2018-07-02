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

    /**
     * Initializes the game board scene and shows it.
     * @param scene is the scene to show.
     * @param gui is the current gui.
     */
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

    /**
     * Initializes toolCardsMap with 0,1,2,3,4 as keys and five buttons representing tool cards as values.
     */
    private void initializeToolMap() {
        toolCardsMap.put(0, tool0);
        toolCardsMap.put(1, tool1);
        toolCardsMap.put(2, tool2);
        toolCardsMap.put(3, tool3);
        toolCardsMap.put(4, tool4);
    }

    /**
     * Appends a new message to the game board text area.
     * @param s is the message to append.
     */
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

    /**
     * Shows the player's window pattern card image as the background of a Pane.
     * It then creates a grid pane where dices will be placed.
     * @param imgURL is the window pattern card image url.
     */
    void setWindowPatternCardImg(String imgURL) {
        gameBoardHandler.setWindowPatternCardImg(imgURL);
    }

    /**
     * Shows the player's score at the end of the game and the goal to beat, telling him if he won.
     * @param goal is the score the player has to beat.
     * @param points is the player's actual score.
     */
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

    /**
     * Disables all actions on board except clicking the QUIT button.
     */
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

    /**
     * Updates the reserve.
     * @param dicesList is the list of dices in the reserve.
     */
    public void setReserve(List<String> dicesList) {
        gameBoardHandler.setReserve(dicesList, reserve);
    }

    /**
     * Sets the image of the two private objective cards and the visual effects to be applied when the mouse enters and exits
     * the images.
     * @param privateCard1 is the name of the first private objective card.
     * @param privateCard2 is the name of the second private objective card.
     */
    void setPrivateCards(String privateCard1, String privateCard2) {
        gameBoardHandler.setSinglePrivateCard(privObjLabel, privObjCard0, privObjCard1, privateCard1);
        gameBoardHandler.setSinglePrivateCard(privObjLabel, privObjCard1, privObjCard0, privateCard2);
    }

    /**
     * Updates the round track.
     * @param track is the string representing the round track.
     */
    public void onRoundTrack(String track) {
        gameBoardHandler.onRoundTrack(track);
    }

    /**
     * Initializes the two public objective cards.
     * @param publicCards is the list of the names of the two public objective cards.
     */
    public void setPublicCards(List<String> publicCards) {
        setSinglePublicCard(pubObjLabel, pubObjCard0, pubObjCard1, publicCards.get(0));
        setSinglePublicCard(pubObjLabel, pubObjCard1, pubObjCard0, publicCards.get(1));
    }

    /**
     * Sets the image of a public objective card and the effects to apply when the mouse enters and exits the image.
     * @param label is the label pointing out where public objective cards are in the game board.
     * @param main is the current public objective card.
     * @param other1 is the other public objective card.
     * @param publicCard is the current public objective card name.
     */
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

    /**
     * Updates the player's window pattern card.
     * @param window is a bi-dimensional array of strings representing the player's window pattern card.
     */
    void setMyWindow(String[][] window) {
        gameBoardHandler.setMySchemeCard(playerWindowPatternCard, window);
    }

    /**
     * Creates the context for the chosen tool card after the player clicks on it.
     */
    private EventHandler<ActionEvent> toolSelected = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            gameBoardHandler.toolSelected(event.getSource().toString().substring(14, 15));
        }
    };

    /**
     * Sets the tool cards in the game board scene.
     * @param toolCardsList is the list of the names of the tool cards.
     */
    void setToolCards(List<String> toolCardsList) {
        for (int i = 0; i < toolCardsList.size(); i++) {
            setSingleToolcard(toolCardsMap.get(i), toolCardsList.get(i));
        }
        for (int i = toolCardsList.size(); i < 5; i++) {
            toolCardsMap.get(i).setDisable(true);
        }
    }

    /**
     * Sets the image of a tool card and the effects to apply when the mouse enters and exits the image.
     * @param main is the current tool card.
     * @param toolcard is the tool card name.
     */
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

    /**
     * Disables actions on board and asks the player which of the two private objective cards he wants to use to
     * calculate his final score.
     */
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

    void afterDisconnection(){
        gameBoardHandler.getWindow().setOnCloseRequest(e -> gameBoardHandler.appendToTextArea("Attendi la chiusura automatica"));
        disableActionsOnGameBoard();
        quit.setDisable(true);
        gameBoardHandler.showErrorAlert("La tua connessione è caduta,  questa finestra sarà chiusa tra 10 secondi." +
                "\nInizia una nuova partita eseguendo un nuovo login.");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        gameBoardHandler.closeWindow();
    }
}
