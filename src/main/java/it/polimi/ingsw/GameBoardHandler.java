package it.polimi.ingsw;

import it.polimi.ingsw.control.RemoteController;
import it.polimi.ingsw.model.gameobjects.Square;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;


import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameBoardHandler implements Initializable {

    private static final String PRIVATE_CARDS_PATH = "File:./src/main/java/it/polimi/ingsw/resources/private_objective_cards/";
    private static final String TOOLCARDS_PATH = "File:./src/main/java/it/polimi/ingsw/resources/toolcards/";
    private static final String DICE_IMAGES_PATH = "File:./src/main/java/it/polimi/ingsw/resources/dices/dice_";
    private static final String PUBLIC_CARDS_PATH = "File:./src/main/java/it/polimi/ingsw/resources/public_objective_cards/";
    private static final String FAVOR_TOKEN_PATH = "File:./src/main/java/it/polimi/ingsw/resources/other/favour.png";
    private static final String WINDOW_PATTERN_CARDS_PATH = "File:./src/main/java/it/polimi/ingsw/resources/window_pattern_card/";

    private int startX;
    private int startY;
    TextField finalX;
    TextField finalY;

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
   /* @FXML
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
    Button reserveDice8;*/
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


    /* Useful for context 1 */
    private ImageView imageView;
    private Button plus;
    private Button minus;


    private RemoteController controller;
    private String username;
    private Stage window;
    private RmiGui rmiGui;
    private int diceChosen;
    private List<String> reserveDices;
    private Map<String, Integer> otherFavorTokensMap;
    private Map<String, WindowPatternCard> otherSchemeCardsMap;
    private Map<Integer, Label> labels = new HashMap<>();
    private Map<Integer, Pane> favorTokensContainers = new HashMap<>();
    private GridPane schemeCard = new GridPane();

    public void createLabelsMap() {
        labels.put(0, label0);
        labels.put(1, label1);
        labels.put(2, label2);
        labels.put(3, label3);
    }

    public void createFavorTokensContainers() {
        favorTokensContainers.put(0, myFavourTokensContainer);
        favorTokensContainers.put(1, favourTokensContainer1);
        favorTokensContainers.put(2, favourTokensContainer2);
        favorTokensContainers.put(3, favourTokensContainer3);
    }

    private EventHandler<MouseEvent> reserveDiceSelected = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (rmiGui.isMyTurn()) {
                String s = event.getSource().toString();
                System.out.println(s);
                s = s.substring(13, 14);
                int i = Integer.parseInt(s);
                s = rmiGui.getDicesList().get(i);
                diceChosen = i;
                textArea.setText("Hai scelto il dado: " + s + " pos: " + i);
            }
        }
    };

    private EventHandler<ActionEvent> toolSelected = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (rmiGui.isMyTurn()) {

                System.out.println("---------------- " + event.getSource());
                String s = event.getSource().toString().substring(14, 15);

                System.out.println("Valore della toolcard " + s);

                textArea.setText("You currently chose the toolcard " + s);
                int tool = Integer.parseInt(s);
                String name = rmiGui.getToolCardsList().get(tool);
                name = name.replaceAll("tool", "");
                int number = Integer.parseInt(name);
                createToolContext(number);
            }
        }
    };


    private void createContext1() {
        useButton.setVisible(true);
        imageView = new ImageView();
        //DA SOSTITUIRE CON IMMAGINE BIANCA
        imageView.setImage(new Image(DICE_IMAGES_PATH + "1_green.png"));//SOLO UNA PROVA
        imageView.setFitWidth(70);
        imageView.setFitHeight(70);
        imageView.setLayoutX(70);
        imageView.setLayoutY(300);

        plus = new Button();
        plus.setText("+");
        plus.setLayoutX(80);
        plus.setLayoutY(350);

        minus = new Button();
        minus.setLayoutX(110);
        minus.setLayoutY(350);
        minus.setText("-");

        gameBoard.getChildren().add(imageView);
        gameBoard.getChildren().add(plus);
        gameBoard.getChildren().add(minus);

        useButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    if (controller.useToolCard1(0, "+", username, false)) { //VANNO SETTATI CORRETTAMENTE I PARAMETRI
                        plus.setVisible(false);
                        minus.setVisible(false);
                        imageView.setVisible(false);
                        useButton.setVisible(false);
                    } else {
                        textArea.setText("Errore in utilizzo toolcard 1");
                        plus.setVisible(false);
                        minus.setVisible(false);
                        imageView.setVisible(false);
                        useButton.setVisible(false);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
        });
        setupGestureTarget(imageView);
    }

    private void createContext2or3(int n) {
        useButton.setVisible(true);
        //DA SOSTITUIRE CON IMMAGINE BIANCA
        imageView = new ImageView();
        imageView.setImage(new Image(DICE_IMAGES_PATH + "1_green.png"));//SOLO UNA PROVA
        imageView.setFitWidth(70);
        imageView.setFitHeight(70);
        imageView.setLayoutX(70);
        imageView.setLayoutY(300);

        finalX = new TextField();
        finalY = new TextField();
        finalX.setMaxWidth(30);
        finalY.setMaxWidth(30);
        finalX.setLayoutX(140);
        finalX.setLayoutY(300);
        finalY.setLayoutX(170);
        finalY.setLayoutY(300);

        gameBoard.getChildren().add(imageView);
        gameBoard.getChildren().add(finalX);
        gameBoard.getChildren().add(finalY);

        useButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                int finalCoordinateX = Integer.parseInt(finalX.getText());
                int finalCoordinateY = Integer.parseInt(finalY.getText());

                try {
                    if (controller.useToolCard2or3(n, startX, startY, finalCoordinateX, finalCoordinateY, username, false)) {
                        finalX.setVisible(false);
                        finalY.setVisible(false);
                        imageView.setVisible(false);
                        useButton.setVisible(false);
                    } else {
                        textArea.setText("Errore in utilizzo toolcard " + n);
                        finalX.setVisible(false);
                        finalY.setVisible(false);
                        imageView.setVisible(false);
                        useButton.setVisible(false);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
        });
        setupGestureTarget(imageView);

    }

    private void createContext7or8(int n) {
        useButton.setVisible(true);

        useButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (n == 7) {
                    try {

                        if (controller.useToolCard7(username, false)) { //VANNO SETTATI CORRETTAMENTE I PARAMETRI

                            useButton.setVisible(false);
                        } else {
                            textArea.setText("Errore in utilizzo toolcard 7");
                            useButton.setVisible(false);
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        if (controller.useToolCard8(username, false)) { //VANNO SETTATI CORRETTAMENTE I PARAMETRI

                            useButton.setVisible(false);
                        } else {
                            textArea.setText("Errore in utilizzo toolcard 8");
                            useButton.setVisible(false);
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        setupGestureTarget(imageView);
    }


    private void setupGestureSource(ImageView source) {



        source.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                source.setCursor(Cursor.HAND);
            }
        });

        source.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {

                //todo:Valido solo se prendiamo da scheme card e non riserve, può essere gestito meglio anche per quanto riguarda source/target per assegnamento attributi;
                startX = GridPane.getRowIndex(source);
                startY = GridPane.getColumnIndex(source);

                Dragboard db = source.startDragAndDrop(TransferMode.MOVE);

                ClipboardContent content = new ClipboardContent();

                content.putImage(source.getImage());
                db.setContent(content);

                event.consume();
            }
        });
    }

    private void setupGestureTarget(ImageView target) {
        target.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {

                if (event.getDragboard().hasImage()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
                event.consume();
            }
        });

        target.setOnDragDropped(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {

                Dragboard db = event.getDragboard();

                target.setImage(db.getImage());

                event.consume();
            }
        });
    }

    private void createToolContext(int toolNumber) {
        switch (toolNumber) {

            case 1: {
                createContext1();
            }
            break;

            case 2: {
                createContext2or3(2);
            }
            break;

            case 3: {
                createContext2or3(3);
            }
            break;

            case 4: {

                createContext1();

            }
            break;

            case 5: {

                createContext1();

            }
            break;

            case 6: {

                createContext1();

            }
            break;

            case 7: {

                createContext7or8(7);

            }
            break;

            case 8: {

                createContext7or8(8);

            }
            break;

            case 9: {
                createContext1();
            }
            break;

            case 10: {
                createContext1();
            }
            break;

            case 11: {
                createContext1();
            }
            break;

            case 12: {
                createContext1();
            }
            break;

            default: {
                System.out.println("Carta non presente!");
            }
        }
    }

    private EventHandler<MouseEvent> windowPatternCardSlotSelected = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {
            if (rmiGui.isMyTurn()) {
                if (diceChosen != 9) {
                    ImageView slot = (ImageView) event.getSource();
                    Integer tempX = GridPane.getRowIndex(slot);
                    if (tempX == null) tempX = 0;
                    Integer tempY = GridPane.getColumnIndex(slot);
                    if (tempY == null) tempY = 0;
                    int coordinateX = tempX;
                    int coordinateY = tempY;
                    textArea.setText("Vuoi posizionare il dado: " + diceChosen + "nella posizione: " + coordinateX + "," + coordinateY);
                    try {
                        if (controller.placeDice(diceChosen, coordinateX, coordinateY, username, false)) {
                            textArea.setText("Ben fatto! Il dado scelto è stato piazzato correttamente.");
                            diceChosen = 9; //FIRST VALUE NEVER PRESENT IN THE RESERVE
                        } else {
                            textArea.setText("ATTENZIONE: Hai provato a piazzare un dado dove non dovresti, o non puoi più piazzare dadi in questo turno!");
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    };


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void init(Stage windowFromRmiGui, Scene sceneFromRmiGui, RemoteController controller, String
            username, RmiGui rmiGui) {
        diceChosen = 9;
        this.controller = controller;
        this.username = username;
        this.rmiGui = rmiGui;
        useButton.setVisible(false);
        this.otherSchemeCardsMap = rmiGui.getOtherSchemeCardsMap();
        window = windowFromRmiGui;
        Platform.runLater(() -> {
            window.setScene(sceneFromRmiGui);
            window.setTitle("GameBoard");
            window.setResizable(false);
            window.show();
        });
        Platform.runLater(() -> label0.setText(username));
        label1.setText(" ");
        label2.setText(" ");
        label3.setText(" ");
    }

    public void setWindowPatternCardImg(String imgURL) {
        BackgroundImage myBI = new BackgroundImage(new Image(imgURL, 340, 300, false, true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        playerWindowPatternCard.setBackground(new Background(myBI));
        //Initializing the scheme card slots

        schemeCard.setGridLinesVisible(false);
        schemeCard.setPrefSize(334, 261);
        schemeCard.setHgap(10);
        schemeCard.setVgap(14);
        schemeCard.setLayoutX(3);
        schemeCard.setLayoutY(5);
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
        Platform.runLater(() -> playerWindowPatternCard.getChildren().add(schemeCard));
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                String url = "File:./src/main/java/it/polimi/ingsw/resources/other/transparent.png";
                Image img = new Image(url);
                ImageView imgView = new ImageView(img);
                imgView.setFitWidth(58);
                imgView.setFitHeight(55);
                imgView.setOnMouseClicked(windowPatternCardSlotSelected);
                int finalI = i;
                int finalJ = j;
                Platform.runLater(() -> schemeCard.add(imgView, finalJ, finalI));
            }
        }
    }

    public void setToolCards(List<String> toolCardsList) {
        //Initializing toolCard0
        String url0 = TOOLCARDS_PATH + toolCardsList.get(0) + ".png";
        Image cardImg0 = new Image(url0);
        ImageView cardView0 = new ImageView(cardImg0);
        cardView0.setFitWidth(158);
        cardView0.setFitHeight(240);
        Platform.runLater(() -> tool0.setGraphic(cardView0));
        tool0.setOnAction(toolSelected);
        //Initializing toolCard1
        String url1 = TOOLCARDS_PATH + toolCardsList.get(1) + ".png";
        Image cardImg1 = new Image(url1);
        ImageView cardView1 = new ImageView(cardImg1);
        cardView1.setFitWidth(158);
        cardView1.setFitHeight(240);
        Platform.runLater(() -> tool1.setGraphic(cardView1));
        tool1.setOnAction(toolSelected);
        //Initializing toolCard2
        String url2 = TOOLCARDS_PATH + toolCardsList.get(2) + ".png";
        Image cardImg2 = new Image(url2);
        ImageView cardView2 = new ImageView(cardImg2);
        cardView2.setFitWidth(158);
        cardView2.setFitHeight(240);
        Platform.runLater(() -> tool2.setGraphic(cardView2));
        tool2.setOnAction(toolSelected);
    }

    public void setReserve(List<String> dicesList) {
        /*reserveDices = new ArrayList<>();
        reserveDices.addAll(dicesList);
        List<Button> reserveDices = new ArrayList<>(); // todo: come mai lo stesso nome dell'attributo?
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
        for (Button dice : reserveDices) {
            Platform.runLater(() -> dice.setGraphic(null));
            dice.setDisable(true);
            if (dicesList.size() > i && dicesList.get(i) != null) {
                String url = DICE_IMAGES_PATH + dicesList.get(i) + ".png";
                Image diceImg = new Image(url);
                ImageView diceView = new ImageView(diceImg);
                diceView.setFitWidth(70);
                diceView.setFitHeight(70);
                Platform.runLater(() -> dice.setGraphic(diceView));
                dice.setDisable(false);
                dice.setOnAction(reserveDiceSelected);
            }
            i++;
        }*/
        if (reserve.getChildren() != null) {
            Platform.runLater(() -> reserve.getChildren().remove(0, reserve.getChildren().size()));
        }
        GridPane grid = new GridPane();
        grid.setGridLinesVisible(false);
        grid.setPrefSize(276, 261);
        grid.setHgap(15);
        grid.setVgap(15);
        Insets insets = new Insets(4,4,4,4);
        grid.setPadding(insets);
        final int numCols = 3;
        final int numRows = 3;
        for (int i = 0; i < numCols; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / numCols);
            grid.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < numRows; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / numRows);
            grid.getRowConstraints().add(rowConst);
        }
        Platform.runLater(() -> reserve.getChildren().add(grid));
        int row = 0;
        int col = 0;
        int id = 0;
        for(String dice : dicesList ){
            String url = DICE_IMAGES_PATH + dice + ".png";
            Image diceImg = new Image(url);
            ImageView diceView = new ImageView(diceImg);
            diceView.setFitWidth(70);
            diceView.setFitHeight(70);
            diceView.setOnMouseClicked(reserveDiceSelected);
            diceView.setId(Integer.toString(id));
            id++;
            int finalCol = col;
            int finalRow = row;
            Platform.runLater(() -> grid.add(diceView, finalCol, finalRow));
            col++;
            if(col==3){
                col = 0;
                row++;
            }
        }

    }

    @FXML
    public void onPassButtonClicked() throws RemoteException {
        if (rmiGui.isMyTurn()) {
            controller.goThrough(username, false);
        } else {
            textArea.setText("Non è il tuo turno!");
        }
    }

    public void setTextArea(String s) {
        this.textArea.setText(s);
    }

    @FXML
    public void onQuitClicked() throws RemoteException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Vuoi davvero uscire?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Uscita");
        alert.setHeaderText(null);
        alert.setResizable(false);
        alert.setGraphic(null);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            window.close();
            controller.quitGame(username, false);
            System.exit(0);
        }
    }

    public void setPrivateCard(String privateCard) {
        Image privateObjCardImg = new Image(PRIVATE_CARDS_PATH + privateCard + ".png");
        privateObjCard.setImage(privateObjCardImg);
    }

    public void setPublicCards(List<String> publicCards) {
        Image publicObjCardImg1 = new Image(PUBLIC_CARDS_PATH + publicCards.get(0) + ".png");
        pubObjCard1.setImage(publicObjCardImg1);
        Image publicObjCardImg2 = new Image(PUBLIC_CARDS_PATH + publicCards.get(1) + ".png");
        pubObjCard2.setImage(publicObjCardImg2);
        Image publicObjCardImg3 = new Image(PUBLIC_CARDS_PATH + publicCards.get(2) + ".png");
        pubObjCard3.setImage(publicObjCardImg3);
    }

    public void setFavourTokens(int value) {
        if (myFavourTokensContainer.getChildren() != null) {
            Platform.runLater(() -> myFavourTokensContainer.getChildren().remove(0, myFavourTokensContainer.getChildren().size()));
        }

        GridPane myFavourTokens = new GridPane();
        myFavourTokens.setPrefSize(40, 240);
        Image img = new Image(FAVOR_TOKEN_PATH);
        for (int i = 0; i < value; i++) {
            ImageView imgView = new ImageView(img);
            imgView.setFitWidth(40);
            imgView.setFitHeight(40);
            int finalI = i;
            Platform.runLater(() -> myFavourTokens.add(imgView, 0, finalI));
        }
        Platform.runLater(() -> myFavourTokensContainer.getChildren().add(myFavourTokens));
    }

    //TODO: le tre funzioni riguardanti i segnalini funzionano ma il codice è abbastanza sporco, si può condensare
    public void setOtherFavorTokens(Pane pane, int value) {
        if (pane.getChildren() != null) {
            Platform.runLater(() -> pane.getChildren().remove(0, pane.getChildren().size()));
        }
        GridPane myFavourTokens = new GridPane();
        myFavourTokens.setPrefSize(40, 240);
        Image img = new Image(FAVOR_TOKEN_PATH);
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
        List<Label> labelsList = new ArrayList();
        labelsList.add(label1);
        labelsList.add(label2);
        labelsList.add(label3);
        for (String name : map.keySet()) {
            for (Label label : labelsList) {
                if (label.getText().equals(name)) {
                    int a = Integer.parseInt(label.getId().substring(5, 6));
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

                }
            }
        }
    }

    public void onOtherFavorTokens(Integer value, String name) {
        List<Label> labelsList = new ArrayList();
        labelsList.add(label1);
        labelsList.add(label2);
        labelsList.add(label3);
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

    public void setMyWindow(WindowPatternCard window) {
        setMySchemeCard(playerWindowPatternCard, window);
    }

    public void setMySchemeCard(Pane pane, WindowPatternCard window) {
        if (pane.getChildren() != null) {
            Platform.runLater(() -> pane.getChildren().remove(0, pane.getChildren().size()));
        }
        GridPane schemeCard = new GridPane();
        schemeCard.setGridLinesVisible(false);
        schemeCard.setPrefSize(334, 261);
        schemeCard.setHgap(10);
        schemeCard.setVgap(14);
        schemeCard.setLayoutX(3);
        schemeCard.setLayoutY(5);
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
        Square[][] temp = window.getWindow();
        for (int i = 0; i < window.getRows(); i++) {
            for (int j = 0; j < window.getColumns(); j++) {
                if (temp[i][j].occupiedSquare()) {
                    String dice = temp[i][j].getDice().toString().toLowerCase();
                    dice = dice.substring(1, dice.length() - 1).replace(" ", "_");
                    System.out.println("Dado " + dice + "in posizione " + i + " " + j);
                    String url = DICE_IMAGES_PATH + dice + ".png";
                    Image img = new Image(url);
                    ImageView imgView = new ImageView(img);
                    imgView.setFitWidth(58);
                    imgView.setFitHeight(55);
                    imgView.setOnMouseClicked(windowPatternCardSlotSelected);
                    //NB
                    setupGestureSource(imgView);

                    int finalI = i;
                    int finalJ = j;
                    Platform.runLater(() -> schemeCard.add(imgView, finalJ, finalI));
                } else {
                    String url = "File:./src/main/java/it/polimi/ingsw/resources/other/transparent.png";
                    Image img = new Image(url);
                    ImageView imgView = new ImageView(img);
                    imgView.setFitWidth(58);
                    imgView.setFitHeight(55);
                    imgView.setOnMouseClicked(windowPatternCardSlotSelected);
                    int finalI = i;
                    int finalJ = j;
                    Platform.runLater(() -> schemeCard.add(imgView, finalJ, finalI));
                }

            }
        }
    }

    public void setOtherSchemeCards(Pane pane, WindowPatternCard window) {
        String s = window.getName().toLowerCase().replaceAll(" ", "_").replaceAll("'", "");
        String imgURL = WINDOW_PATTERN_CARDS_PATH + s + ".png";
        BackgroundImage myBI = new BackgroundImage(new Image(imgURL, 220, 192, false, true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        pane.setBackground(new Background(myBI));
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
        Square[][] temp = window.getWindow();
        for (int i = 0; i < window.getRows(); i++) {
            for (int j = 0; j < window.getColumns(); j++) {
                if (temp[i][j].occupiedSquare()) {
                    String dice = temp[i][j].getDice().toString().toLowerCase();
                    dice = dice.substring(1, dice.length() - 1).replace(" ", "_");
                    System.out.println("Dado " + dice + "in posizione " + i + " " + j);
                    String url = DICE_IMAGES_PATH + dice + ".png";
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

    public void initializeSchemeCards(Map<String, WindowPatternCard> map) {
        List<Label> labelsList = new ArrayList();
        labelsList.add(label1);
        labelsList.add(label2);
        labelsList.add(label3);
        for (String name : map.keySet()) {
            for (Label label : labelsList) {
                if (label.getText().equals(name)) {
                    int a = Integer.parseInt(label.getId().substring(5, 6));
                    switch (a) {
                        case 1:
                            setOtherSchemeCards(pane1, map.get(name));
                            break;
                        case 2:
                            setOtherSchemeCards(pane2, map.get(name));
                            break;
                        case 3:
                            setOtherSchemeCards(pane3, map.get(name));
                            break;
                    }

                }
            }
        }
    }

    // todo: chiamata ogni volta che viene notificata una nuova scheme card
    // todo: potrebbe essere utili discriminare i due casi in fase di invio, (carta settata, carta aggiornata)
    // in modo da chiamare questo metodo solo quando la carta è settata
    public void onOtherSchemeCards(WindowPatternCard window, String name) {
        List<Label> labelsList = new ArrayList();
        labelsList.add(label1);
        labelsList.add(label2);
        labelsList.add(label3);
        for (Label label : labelsList) {
            if (label.getText().equals(name)) {
                int a = Integer.parseInt(label.getId().substring(5, 6));
                switch (a) {
                    case 1:
                        setOtherSchemeCards(pane1, window);
                        break;
                    case 2:
                        setOtherSchemeCards(pane2, window);
                        break;
                    case 3:
                        setOtherSchemeCards(pane3, window);
                        break;
                }
                break;
            }
        }
        // cerca tra tutte le label quella con il nome "name" e gli setta la window
    }

    /**
     * It sets the names contained in players in the right label over scheme cards clockwise starting from the position
     * of the player owner of this GUI. It maintains the playing order randomly chosen during the initialization of the match
     *
     * @param players is a list of Strings which are the names of the players invlolved in the match
     */
    public void initializeLabels(List<String> players) {
        int myPosition = 0;

        /* find the position of the owner of this GUI */
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).equals(username)) {
                myPosition = i;
                label0.setText(players.get(i));
                break;
            }
        }

        /* assigns the name to the right label in order to show the correct flow clockwise */
        for (int i = 1; i < players.size(); i++) {
            labels.get(i).setText(players.get((myPosition + i) % players.size()));
        }
    }

    public void initializeActions() {
        rmiGui.setDicePlaced(false);
        // todo: aggiungere altre azioni da compiere
    }

    public void onRoundTrack(String track){
        GridPane grid = new GridPane();
        grid.setGridLinesVisible(false);
        grid.setPrefSize(320, 320);
        grid.setHgap(1);
        grid.setVgap(1);
        final int numCols = 10;
        for (int i = 0; i < numCols; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / numCols);
            grid.getColumnConstraints().add(colConst);
        }
        Platform.runLater(() -> roundTrack.getChildren().add(grid));
        System.out.println(track);
        Pattern p = Pattern.compile("\\[.*?\\]");
        String[] parts = track.split("\n");
        for(int i = 0; i < parts.length-1; i = i+2){
            //String round = (parts[i].substring(6));
            Matcher m = p.matcher(parts[i+1]);
            int j = 0;
            while (m.find()){
                String dice = (m.group(0).substring(1, m.group(0).length()-1).toLowerCase().replaceAll(" ","_"));
                String url = DICE_IMAGES_PATH + dice + ".png";
                Image img = new Image(url);
                ImageView imgView = new ImageView(img);
                imgView.setFitWidth(30);
                imgView.setFitHeight(30);
                int finalI = i/2;
                int finalJ = j;
                Platform.runLater(() -> grid.add(imgView, finalI, finalJ));
                j++;
            }
        }
    }

}
