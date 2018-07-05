package it.polimi.ingsw.view.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameBoardHandlerMulti {

    private static final Logger LOGGER = Logger.getLogger(GameBoardHandlerMulti.class.getName());
    private GameBoardHandler gameBoardHandler;
    private Gui gui;
    private Map<Integer, Label> labels = new HashMap<>();
    private List<Label> labelsList = new ArrayList<>();
    private boolean exit;

    @FXML
    Label privObjLabel;
    @FXML
    Label pubObjLabel;
    @FXML
    Label toolCardsLabel;
    @FXML
    Label toolLabel;
    @FXML
    Pane toolPane;
    @FXML
    Button useButton;
    @FXML
    Pane playerWindowPatternCard;
    @FXML
    Button tool0;
    @FXML
    Button tool1;
    @FXML
    Button tool2;
    @FXML
    Button passButton;
    @FXML
    TextArea textArea;
    @FXML
    Button quit;
    @FXML
    ImageView privateObjCard;
    @FXML
    ImageView pubObjCard1;
    @FXML
    ImageView pubObjCard2;
    @FXML
    ImageView pubObjCard3;
    @FXML
    AnchorPane gameBoard;
    @FXML
    Pane roundTrack;
    @FXML
    Pane reserve;
    @FXML
    Pane pane1;
    @FXML
    Pane pane2;
    @FXML
    Pane pane3;
    @FXML
    Pane myFavourTokensContainer;
    @FXML
    Pane favourTokensContainer1;
    @FXML
    Pane favourTokensContainer2;
    @FXML
    Pane favourTokensContainer3;
    @FXML
    Label label0;
    @FXML
    Label label1;
    @FXML
    Label label2;
    @FXML
    Label label3;

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
     * Initializes the game board scene and shows it.
     *
     * @param scene is the scene to show.
     * @param gui   is the current gui.
     */
    void init(Scene scene, Gui gui) {
        this.gui = gui;
        gameBoardHandler = new GameBoardHandler(gui.isSingle(), this, null, gameBoard, toolPane, toolLabel, useButton, null);
        gameBoardHandler.init(scene, gui);
        Platform.runLater(() -> label0.setText(gui.getUsername()));
        label1.setText(" ");
        label2.setText(" ");
        label3.setText(" ");
        useButton.setVisible(false);
        toolLabel.setVisible(false);
        toolPane.setVisible(false);
        gui.getWindowStage().setOnCloseRequest(event -> {
            if (exit) {
                gameBoardHandler.terminateGame();
            } else {
                event.consume();
                gameBoardHandler.quitClicked();
            }
        });
        quit.setOnMouseClicked(event -> {
            if (exit) {
                gameBoardHandler.terminateGame();
            } else {
                event.consume();
                gameBoardHandler.quitClicked();
            }
        });
        exit = false;

    }

    /**
     * Appends a new message to the game board text area.
     *
     * @param s is the message to append.
     */
    void appendToTextArea(String s) {
        gameBoardHandler.appendToTextArea(s);
    }

    /**
     * Shows the player's window pattern card image as the background of a Pane.
     * It then creates a grid pane where dices will be placed.
     *
     * @param imgURL is the window pattern card image url.
     */
    void setWindowPatternCardImg(String imgURL) {
        gameBoardHandler.setWindowPatternCardImg(imgURL);
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
     * Creates a list of the 3 labels of the player's opponents in the match.
     */
    void createOtherLabelsList() {
        labelsList.add(label1);
        labelsList.add(label2);
        labelsList.add(label3);
    }

    /**
     * Creates a map with 0,1,2,3 as keys and the labels as values.
     */
    void createLabelsMap() {
        labels.put(0, label0);
        labels.put(1, label1);
        labels.put(2, label2);
        labels.put(3, label3);
    }

    /**
     * Initializes the window pattern cards of the player's opponents in the match.
     *
     * @param map      is a map matching the opponent's name with his window pattern card.
     * @param namesMap is a map matching the opponent's name with the name of his window pattern card.
     */
    void initializeSchemeCards(Map<String, String[][]> map, Map<String, String> namesMap) {
        for (String name : map.keySet()) {
            for (Label label : labelsList) {
                if (label.getText().equals(name)) {
                    int a = Integer.parseInt(label.getId().substring(5, 6));
                    switch (a) {
                        case 1:
                            setOtherSchemeCards(pane1, map.get(name), namesMap.get(name));
                            break;
                        case 2:
                            setOtherSchemeCards(pane2, map.get(name), namesMap.get(name));
                            break;
                        case 3:
                            setOtherSchemeCards(pane3, map.get(name), namesMap.get(name));
                            break;
                    }

                }
            }
        }
    }

    /**
     * Sets the image of the player's opponent window pattern card as background of a Pane, then creates a GridPane where the images of the dices in the
     * player's window pattern card are placed.
     *
     * @param pane     is the Pane containing the player's window pattern card.
     * @param window   is a bi-dimensional array of string representing the player's window pattern card.
     * @param cardName is the name of the player's window pattern card.
     */
    private void setOtherSchemeCards(Pane pane, String[][] window, String cardName) {
        if (window != null) {
            String s = cardName.toLowerCase().replaceAll(" ", "_").replaceAll("'", "");
            BackgroundImage myBI = new BackgroundImage(new Image(getClass().getResourceAsStream(GameBoardHandler.WINDOW_PATTERN_CARDS_PATH + s + ".png"), 220, 192, false, true),
                    BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT);
            pane.setBackground(new Background(myBI));
        }
        if (pane.getChildren() != null) {
            Platform.runLater(() -> pane.getChildren().remove(0, pane.getChildren().size()));
        }
        GridPane schemeCard = new GridPane();
        schemeCard.setGridLinesVisible(false);
        schemeCard.setPrefSize(214, 171);
        schemeCard.setHgap(4);
        schemeCard.setVgap(4);
        schemeCard.setLayoutX(3);
        schemeCard.setLayoutY(2);
        final int numCols = 5;
        final int numRows = 4;
        for (int i = 0; i < numCols; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / numCols);
            schemeCard.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < numRows; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / numRows);
            schemeCard.getRowConstraints().add(rowConst);
        }
        Platform.runLater(() -> pane.getChildren().add(schemeCard));
        assert window != null;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                String[] parts = window[i][j].split(",");
                String temp = (parts[2].replaceAll(" ", ""));
                if (!((temp.substring(0, temp.length() - 2)).equals("null"))) {
                    String dice = parts[2].toLowerCase();
                    dice = dice.substring(2, dice.length() - 3).replace(" ", "_");
                    Image img = new Image(getClass().getResourceAsStream(GameBoardHandler.DICE_IMAGES_PATH + dice + ".png"));
                    ImageView imgView = new ImageView(img);
                    imgView.setFitWidth(40);
                    imgView.setFitHeight(40);
                    int finalI = i;
                    int finalJ = j;
                    Platform.runLater(() -> schemeCard.add(imgView, finalJ, finalI));
                }

            }
        }
    }

    /**
     * When one of the player's opponent modifies his window pattern card, its representation in the player's game board
     * scene, is modified consequently.
     *
     * @param window   is the modified window pattern card.
     * @param name     is the name of the opponent who modified his window pattern card.
     * @param cardName is the window pattern card name.
     */
    void onOtherSchemeCards(String[][] window, String name, String cardName) {

        for (Label label : labelsList) {
            if (label.getText().equals(name)) {
                int a = Integer.parseInt(label.getId().substring(5, 6));
                switch (a) {
                    case 1:
                        setOtherSchemeCards(pane1, window, cardName);
                        break;
                    case 2:
                        setOtherSchemeCards(pane2, window, cardName);
                        break;
                    case 3:
                        setOtherSchemeCards(pane3, window, cardName);
                        break;
                }
                break;
            }
        }
    }

    /**
     * Updates the reserve.
     *
     * @param dicesList is the list of dices in the reserve.
     */
    public void setReserve(List<String> dicesList) {
        gameBoardHandler.setReserve(dicesList, reserve);
    }

    /**
     * Shows the ranking of the players by their scores.
     *
     * @param winner        is the player with the highest score.
     * @param rankingNames  is the ordered list of players by their scores.
     * @param rankingValues is the ordered list of the players'scores.
     */
    void showRanking(String winner, List<String> rankingNames, List<Integer> rankingValues) {

        StringBuilder s = new StringBuilder();
        for (int i = 0; i < rankingNames.size(); i++) {
            s.append("- ");
            s.append(rankingNames.get(i));
            s.append("\t");
            s.append(rankingValues.get(i));
            s.append("\n");
        }

        if (winner.equals(gui.getUsername())) {
            s.append("Complimenti! Sei il vincitore.\n");
        } else {
            s.append(winner.toUpperCase());
            s.append(" è il vincitore!\n");
        }
        exit = true;

        gameBoardHandler.appendToTextArea(s.toString());
        disableActionsOnGameBoard();
    }

    /**
     * Appends a message to the scene text area telling the player he's the winner because he's the only player left in the game.
     * It then disables actions on board.
     */
    void onGameClosing() {
        gameBoardHandler.appendToTextArea("Congratulazioni! Sei il vincitore. Sei rimasto da solo in gioco.\n\nClicca su ESCI per terminare");
        disableActionsOnGameBoard();
        exit = true;
    }

    /**
     * Disables all actions on board except clicking the QUIT button.
     */
    public void disableActionsOnGameBoard() {
        tool0.setDisable(true);
        tool1.setDisable(true);
        tool2.setDisable(true);
        reserve.setDisable(true);
        roundTrack.setDisable(true);
        passButton.setDisable(true);
        playerWindowPatternCard.setDisable(true);
        pubObjCard1.setDisable(true);
        pubObjCard2.setDisable(true);
        pubObjCard3.setDisable(true);
        privateObjCard.setDisable(true);
    }

    /**
     * It sets the names contained in players in the right label over scheme cards clockwise starting from the position
     * of the player owner of this GUI. It maintains the playing order randomly chosen during the initialization of the match
     *
     * @param players is a list of Strings which are the names of the players invlolved in the match
     */
    void initializeLabels(List<String> players) {

        //FOR RMI CONNECTION
        if (gui.getControllerRmi() != null) {
            setLabels(players, label0);
        }
        //FOR SOCKET CONNECTION
        else {
            Platform.runLater(() -> setLabels(players, label0));
        }
    }

    /**
     * Assigns every name to the corresponding label.
     *
     * @param players is the list of players in the match.
     * @param label0  is the label containing the name of the owner of this GUI.
     */
    private void setLabels(List<String> players, Label label0) {
        AtomicInteger myPosition = new AtomicInteger();
        /* find the position of the owner of this GUI */
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).equals(gui.getUsername())) {
                myPosition.set(i);
                label0.setText(players.get(i));
                break;
            }
        }
        /* assigns the name to the right label in order to show the correct flow clockwise */
        for (int i = 1; i < players.size(); i++) {
            labels.get(i).setText(players.get((myPosition.get() + i) % players.size()));
        }
    }

    /**
     * Sets the image of a private objective card and the visual effects to be applied when the mouse enters and exits
     * the image.
     *
     * @param privateCard is the name of the private objective card.
     */
    public void setPrivateCard(String privateCard) {
        gameBoardHandler.setSinglePrivateCard(privObjLabel, privateObjCard, null, privateCard);
    }

    /**
     * Updates the number of favor tokens of the player.
     *
     * @param value is the new number of favor tokens.
     */
    void setMyFavourTokens(int value) {
        setFavourTokens(myFavourTokensContainer, value);
    }

    /**
     * Updates the number of favor tokens in the chosen Pane.
     *
     * @param pane  is the pane containing the favor tokens.
     * @param value is the new number of favor tokens.
     */
    private void setFavourTokens(Pane pane, int value) {
        if (pane.getChildren() != null) {
            Platform.runLater(() -> pane.getChildren().remove(0, pane.getChildren().size()));
        }
        GridPane myFavourTokens = new GridPane();
        myFavourTokens.setPrefSize(40, 240);
        Image img = new Image(getClass().getResourceAsStream(GameBoardHandler.FAVOR_TOKEN_PATH));
        for (int i = 0; i < value; i++) {
            ImageView imgView = new ImageView(img);
            imgView.setFitWidth(40);
            imgView.setFitHeight(40);
            int finalI = i;
            Platform.runLater(() -> myFavourTokens.add(imgView, 0, finalI));
        }
        Platform.runLater(() -> pane.getChildren().add(myFavourTokens));
    }

    /**
     * Initializes the favor tokens of all the player's opponents.
     *
     * @param map is a map with opponent's name as key and number of favor tokens as value.
     */
    void initializeFavorTokens(Map<String, Integer> map) {

        for (String name : map.keySet()) {
            for (Label label : labelsList) {
                if (label.getText().equals(name)) {
                    int a = Integer.parseInt(label.getId().substring(5, 6));
                    switch (a) {
                        case 1:
                            setFavourTokens(favourTokensContainer1, map.get(name));
                            break;
                        case 2:
                            setFavourTokens(favourTokensContainer2, map.get(name));
                            break;
                        case 3:
                            setFavourTokens(favourTokensContainer3, map.get(name));
                            break;
                    }
                    break;
                }
            }
        }
    }

    /**
     * Updates the favor tokens of a player's opponent.
     *
     * @param value is the new number of favor tokens.
     * @param name  is the opponent's name.
     */
    void onOtherFavorTokens(Integer value, String name) {

        for (Label label : labelsList) {
            if (label.getText().equals(name)) {
                int a = Integer.parseInt(label.getId().substring(5, 6));

                switch (a) {
                    case 1:
                        setFavourTokens(favourTokensContainer1, value);
                        break;
                    case 2:
                        setFavourTokens(favourTokensContainer2, value);
                        break;
                    case 3:
                        setFavourTokens(favourTokensContainer3, value);
                        break;
                }
                break;
            }
        }
    }

    /**
     * Updates the round track.
     *
     * @param track is the string representing the round track.
     */
    public void onRoundTrack(String track) {
        gameBoardHandler.onRoundTrack(track);
    }

    /**
     * Initializes the three public objective cards.
     *
     * @param publicCards is the list of the names of the three public objective cards.
     */
    public void setPublicCards(List<String> publicCards) {
        setSinglePublicCard(pubObjLabel, pubObjCard1, pubObjCard2, pubObjCard3, publicCards.get(0));
        setSinglePublicCard(pubObjLabel, pubObjCard2, pubObjCard1, pubObjCard3, publicCards.get(1));
        setSinglePublicCard(pubObjLabel, pubObjCard3, pubObjCard1, pubObjCard2, publicCards.get(2));
    }

    /**
     * Sets the image of a public objective card and the effects to apply when the mouse enters and exits the image.
     *
     * @param label      is the label pointing out where public objective cards are in the game board.
     * @param main       is the current public objective card.
     * @param other1     is one of the remaining public objective cards.
     * @param other2     is the other remaining public objective card.
     * @param publicCard is the current public objective card name.
     */
    private void setSinglePublicCard(Label label, ImageView main, ImageView other1, ImageView other2, String publicCard) {
        Image publicObjCardImg1 = new Image(getClass().getResourceAsStream(GameBoardHandler.PUBLIC_CARDS_PATH + publicCard + ".png"));
        main.setImage(publicObjCardImg1);
        main.setOnMouseEntered(event -> {
            main.setTranslateX(-10);
            main.setTranslateY(-60);
            main.setStyle("-fx-scale-x: 2.0;-fx-scale-y: 2.0");
            other1.setVisible(false);
            other2.setVisible(false);
            label.setVisible(false);
        });
        main.setOnMouseExited(event -> setTranslationCards(main, other1, other2, label));
    }

    /**
     * Updates the player's window pattern card.
     *
     * @param window is a bi-dimensional array of strings representing the player's window pattern card.
     */
    void setMyWindow(String[][] window) {
        gameBoardHandler.setMySchemeCard(playerWindowPatternCard, window);
    }

    /**
     * Sets the three tool cards in the game board scene.
     *
     * @param toolCardsList is the list of the names of the three tool cards.
     */
    void setToolCards(List<String> toolCardsList) {
        setSingleToolcard(tool0, tool1, tool2, toolCardsList.get(0));
        setSingleToolcard(tool1, tool0, tool2, toolCardsList.get(1));
        setSingleToolcard(tool2, tool0, tool1, toolCardsList.get(2));
    }

    /**
     * Sets the image of a tool card and the effects to apply when the mouse enters and exits the image.
     *
     * @param main     is the current tool card.
     * @param other1   is one of the remaining tool cards.
     * @param other2   is the other remaining tool card.
     * @param toolcard is the tool card name.
     */
    private void setSingleToolcard(Button main, Button other1, Button other2, String toolcard) {
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
            other1.setVisible(false);
            other2.setVisible(false);
            toolCardsLabel.setVisible(false);
        });
        main.setOnMouseExited(event -> setTranslationCards(main, other1, other2, toolCardsLabel));
    }

    /**
     * Sets the effect to apply when the mouse enters in a tool card image.
     *
     * @param main   is the current tool card.
     * @param other1 is one of the remaining tool cards.
     * @param other2 is the other remaining tool card.
     * @param label  is a label pointing out where tool cards are in the game board scene.
     */
    private void setTranslationCards(Node main, Node other1, Node other2, Label label) {
        main.setTranslateX(0);
        main.setTranslateY(0);
        main.setStyle("-fx-scale-x: 1.0;-fx-scale-y: 1.0");
        other1.setVisible(true);
        other2.setVisible(true);
        label.setVisible(true);
    }

    @FXML
    public void onPassButtonClicked() {
        gameBoardHandler.passButtonClicked();
    }

    void resetToolValues() {
        gameBoardHandler.resetToolValues();
    }

    void setDiceChosenOutOfRange() {
        gameBoardHandler.setDiceChosenOutOfRange();
    }

    void afterDisconnection() {
        gameBoardHandler.getWindow().setOnCloseRequest(e -> gameBoardHandler.appendToTextArea("Attendi la chiusura automatica"));
        disableActionsOnGameBoard();
        quit.setDisable(true);
        gameBoardHandler.showErrorAlert("La tua connessione è caduta, questa finestra sarà chiusa tra 10 secondi." +
                "\nPer continuare la partita esegui nuovamente il login con lo stesso username.");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "exception in a disconnected client", e);
            Thread.currentThread().interrupt();
        }
        gameBoardHandler.closeWindow();
    }
}
