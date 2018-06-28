package it.polimi.ingsw;

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
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class GameBoardHandlerMulti {

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

    public Pane getPlayerWindowPatternCard() {
        return playerWindowPatternCard;
    }

    public void init(Scene scene, Gui gui) {
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

    public void appendToTextArea(String s) {
        gameBoardHandler.appendToTextArea(s);
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

    private EventHandler<ActionEvent> toolSelected = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            gameBoardHandler.toolSelected(event.getSource().toString().substring(14, 15));
        }
    };

    public void createOtherLabelsList() {
        labelsList.add(label1);
        labelsList.add(label2);
        labelsList.add(label3);
    }

    public void createLabelsMap() {
        labels.put(0, label0);
        labels.put(1, label1);
        labels.put(2, label2);
        labels.put(3, label3);
    }

    public void initializeSchemeCards(Map<String, String[][]> map, Map<String, String> namesMap) {
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

    private void setOtherSchemeCards(Pane pane, String[][] window, String cardName) {
        if (window != null) {//DA CONTROLLARE
            String s = cardName.toLowerCase().replaceAll(" ", "_").replaceAll("'", "");
            String imgURL = GameBoardHandler.WINDOW_PATTERN_CARDS_PATH + s + ".png";
            BackgroundImage myBI = new BackgroundImage(new Image(imgURL, 220, 192, false, true),
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
                    String url = GameBoardHandler.DICE_IMAGES_PATH + dice + ".png";
                    Image img = new Image(url);
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

    public void onOtherSchemeCards(String[][] window, String name, String cardName) {

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

    public void setReserve(List<String> dicesList) {
        gameBoardHandler.setReserve(dicesList, reserve);
    }

    public void showRanking(String winner, List<String> rankingNames, List<Integer> rankingValues) {

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

    public void onGameClosing() {
        gameBoardHandler.appendToTextArea("Congratulazioni! Sei il vincitore. Sei rimasto da solo in gioco.\n\nClicca su ESCI per terminare");
        disableActionsOnGameBoard();
        exit = true;
    }


    private void disableActionsOnGameBoard() {
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
    public void initializeLabels(List<String> players) {

        //FOR RMI CONNECTION
        if (gui.getControllerRmi() != null) {
            setLabels(players, label0);
        }
        //FOR SOCKET CONNECTION
        else {
            Platform.runLater(() -> setLabels(players, label0));
        }
    }

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

    public void setPrivateCard(String privateCard) {
        gameBoardHandler.setSinglePrivateCard(privObjLabel, privateObjCard, null, privateCard);
    }

    public void setFavourTokens(int value) {
        if (myFavourTokensContainer.getChildren() != null) {
            Platform.runLater(() -> myFavourTokensContainer.getChildren().remove(0, myFavourTokensContainer.getChildren().size()));
        }

        GridPane myFavourTokens = new GridPane();
        myFavourTokens.setPrefSize(40, 240);
        Image img = new Image(GameBoardHandler.FAVOR_TOKEN_PATH);
        for (int i = 0; i < value; i++) {
            ImageView imgView = new ImageView(img);
            imgView.setFitWidth(40);
            imgView.setFitHeight(40);
            int finalI = i;
            Platform.runLater(() -> myFavourTokens.add(imgView, 0, finalI));
        }
        Platform.runLater(() -> myFavourTokensContainer.getChildren().add(myFavourTokens));
    }

    private void setOtherFavorTokens(Pane pane, int value) {
        if (pane.getChildren() != null) {
            Platform.runLater(() -> pane.getChildren().remove(0, pane.getChildren().size()));
        }
        GridPane myFavourTokens = new GridPane();
        myFavourTokens.setPrefSize(40, 240);
        Image img = new Image(GameBoardHandler.FAVOR_TOKEN_PATH);
        for (int i = 0; i < value; i++) {
            ImageView imgView = new ImageView(img);
            imgView.setFitWidth(40);
            imgView.setFitHeight(40);
            int finalI = i;
            Platform.runLater(() -> myFavourTokens.add(imgView, 0, finalI));
        }
        Platform.runLater(() -> pane.getChildren().add(myFavourTokens));
    }

    public void initializeFavorTokens(Map<String, Integer> map) {

        for (String name : map.keySet()) {
            for (Label label : labelsList) {
                System.out.println(label.getText());
                if (label.getText().equals(name)) {
                    int a = Integer.parseInt(label.getId().substring(5, 6));
                    System.out.println(a);
                    switch (a) {
                        case 1:
                            setOtherFavorTokens(favourTokensContainer1, map.get(name));
                            break;
                        case 2:
                            setOtherFavorTokens(favourTokensContainer2, map.get(name));
                            break;
                        case 3:
                            setOtherFavorTokens(favourTokensContainer3, map.get(name));
                            break;
                    }
                    break;
                }
            }
        }
    }

    public void onOtherFavorTokens(Integer value, String name) {

        for (Label label : labelsList) {
            if (label.getText().equals(name)) {
                int a = Integer.parseInt(label.getId().substring(5, 6));

                switch (a) {
                    case 1:
                        setOtherFavorTokens(favourTokensContainer1, value);
                        break;
                    case 2:
                        setOtherFavorTokens(favourTokensContainer2, value);
                        break;
                    case 3:
                        setOtherFavorTokens(favourTokensContainer3, value);
                        break;
                }
                break;
            }
        }
    }

    public void onRoundTrack(String track) {
        gameBoardHandler.onRoundTrack(track);
    }

    public void setPublicCards(List<String> publicCards) {
        setSinglePublicCard(pubObjLabel, pubObjCard1, pubObjCard2, pubObjCard3, publicCards.get(0));
        setSinglePublicCard(pubObjLabel, pubObjCard2, pubObjCard1, pubObjCard3, publicCards.get(1));
        setSinglePublicCard(pubObjLabel, pubObjCard3, pubObjCard1, pubObjCard2, publicCards.get(2));
    }

    private void setSinglePublicCard(Label label, ImageView main, ImageView other1, ImageView other2, String publicCard) {
        Image publicObjCardImg1 = new Image(GameBoardHandler.PUBLIC_CARDS_PATH + publicCard + ".png");
        main.setImage(publicObjCardImg1);
        main.setOnMouseEntered(event -> {
            main.setTranslateX(-10);
            main.setTranslateY(-60);
            main.setStyle("-fx-scale-x: 2.0;-fx-scale-y: 2.0");
            other1.setVisible(false);
            other2.setVisible(false);
            label.setVisible(false);
        });
        main.setOnMouseExited(event -> {
            main.setTranslateX(0);
            main.setTranslateY(0);
            main.setStyle("-fx-scale-x: 1.0;-fx-scale-y: 1.0");
            other1.setVisible(true);
            other2.setVisible(true);
            label.setVisible(true);
        });
    }

    public void setMyWindow(String[][] window) {
        gameBoardHandler.setMySchemeCard(playerWindowPatternCard, window);
    }

    public void setToolCards(List<String> toolCardsList) {
        setSingleToolcard(tool0, tool1, tool2, toolCardsList.get(0));
        setSingleToolcard(tool1, tool0, tool2, toolCardsList.get(1));
        setSingleToolcard(tool2, tool0, tool1, toolCardsList.get(2));
    }

    private void setSingleToolcard(Button main, Button other1, Button other2, String toolcard) {
        String url = GameBoardHandler.TOOLCARDS_PATH + toolcard + ".png";
        Image cardImg = new Image(url);

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
        main.setOnMouseExited(event -> {
            main.setTranslateX(0);
            main.setTranslateY(0);
            main.setStyle("-fx-scale-x: 1.0;-fx-scale-y: 1.0");
            other1.setVisible(true);
            other2.setVisible(true);
            toolCardsLabel.setVisible(true);
        });
    }

    @FXML
    public void onPassButtonClicked() {
        gameBoardHandler.passButtonClicked();
    }

    public void resetToolValues() {
        gameBoardHandler.resetToolValues();
    }
}
