package it.polimi.ingsw;

import it.polimi.ingsw.control.RemoteController;
import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.model.gameobjects.Square;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;
import it.polimi.ingsw.socket.ClientController;
import it.polimi.ingsw.socket.requests.*;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameBoardHandler {

    private GameBoardHandlerMulti gameBoardHandlerMulti;
    private GameBoardHandlerSingle gameBoardHandlerSingle;

    protected static final String PRIVATE_CARDS_PATH = "File:./src/main/java/it/polimi/ingsw/resources/private_objective_cards/";
    protected static final String TOOLCARDS_PATH = "File:./src/main/java/it/polimi/ingsw/resources/toolcards/";
    protected static final String DICE_IMAGES_PATH = "File:./src/main/java/it/polimi/ingsw/resources/dices/dice_";
    protected static final String PUBLIC_CARDS_PATH = "File:./src/main/java/it/polimi/ingsw/resources/public_objective_cards/";
    protected static final String FAVOR_TOKEN_PATH = "File:./src/main/java/it/polimi/ingsw/resources/other/favour.png";
    protected static final String WINDOW_PATTERN_CARDS_PATH = "File:./src/main/java/it/polimi/ingsw/resources/window_pattern_card/";
    private static final String TRANSPARENT_IMAGE_URL = "File:./src/main/java/it/polimi/ingsw/resources/other/transparent.png";
    private static final int OUT_OF_RANGE = -1;
    private static final int COLUMNS = 5;
    private static final int ROWS = 4;

    private int partialStartXForTools1 = GameBoardHandler.OUT_OF_RANGE;
    private int partialStartYForTools1 = GameBoardHandler.OUT_OF_RANGE;
    private int targetStartXForTools1 = GameBoardHandler.OUT_OF_RANGE;
    private int targetStartYForTools1 = GameBoardHandler.OUT_OF_RANGE;
    private int partialStartXForTools2 = GameBoardHandler.OUT_OF_RANGE;
    private int partialStartYForTools2 = GameBoardHandler.OUT_OF_RANGE;
    private int targetStartXForTools2 = GameBoardHandler.OUT_OF_RANGE;
    private int targetStartYForTools2 = GameBoardHandler.OUT_OF_RANGE;
    private int partialRoundForTools = GameBoardHandler.OUT_OF_RANGE;
    private int partialDiceFromRoundForTools = GameBoardHandler.OUT_OF_RANGE;
    private int targetRoundForTools = GameBoardHandler.OUT_OF_RANGE;
    private int targetDiceFromRoundForTools = GameBoardHandler.OUT_OF_RANGE;
    private int partialReserveIndexForTools = GameBoardHandler.OUT_OF_RANGE;
    private int targetReserveIndexForTools = GameBoardHandler.OUT_OF_RANGE;
    private int targetSacrificeDiceForTools = GameBoardHandler.OUT_OF_RANGE;
    private Integer finalCoordinateX1;
    private Integer finalCoordinateY1;
    private Integer finalCoordinateX2;
    private Integer finalCoordinateY2;
    private Integer value11;
    private String incrOrDecr;
    private TextField textField11;
    private TextField finalX1;
    private TextField finalY1;
    private TextField finalX2;
    private TextField finalY2;
    private int pickedDices = 0;

    /* Useful for contexts */
    private ImageView sacrificeImageView;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private Button plus;
    private Button minus;
    private Button concludeButton;

    private RemoteController remoteController;
    private ClientController clientController;
    private String username;
    private Stage window;
    private Gui gui;
    private int diceChosen;
    private List<String> reserveDices;
    private Map<String, Integer> otherFavorTokensMap;
    private Map<String, WindowPatternCard> otherSchemeCardsMap;
    private Map<Integer, Pane> favorTokensContainers = new HashMap<>();
    private GridPane schemeCard = new GridPane();
    private final boolean single;

    private AnchorPane gameBoard;
    private Pane toolPane;
    private Label toolLabel;
    private Button useButton;

    public GameBoardHandler(boolean single, GameBoardHandlerMulti gameBoardHandlerMulti, GameBoardHandlerSingle gameBoardHandlerSingle, AnchorPane gameBoard, Pane toolPane, Label toolLabel, Button useButton) {
        this.gameBoardHandlerMulti = gameBoardHandlerMulti;
        this.gameBoardHandlerSingle = gameBoardHandlerSingle;
        this.single = single;
        this.gameBoard = gameBoard;
        this.toolPane = toolPane;
        this.toolLabel = toolLabel;
        this.useButton = useButton;

    }


    private void setupSchemeCardSource(ImageView source) {
        source.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                source.setCursor(Cursor.HAND);
            }
        });
        source.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (pickedDices == 0) {
                    partialStartXForTools1 = GridPane.getRowIndex(source);
                    partialStartYForTools1 = GridPane.getColumnIndex(source);
                } else {
                    partialStartXForTools2 = GridPane.getRowIndex(source);
                    partialStartYForTools2 = GridPane.getColumnIndex(source);
                }
                Dragboard db = source.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putImage(source.getImage());
                db.setContent(content);
                event.consume();
            }
        });
    }

    private void setupReserveSource(ImageView source) {
        source.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                source.setCursor(Cursor.HAND);
            }
        });
        source.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                String s = event.getSource().toString();
                s = s.substring(13, 14);
                partialReserveIndexForTools = Integer.parseInt(s);
                Dragboard db = source.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putImage(source.getImage());
                db.setContent(content);
                event.consume();
            }
        });
    }

    private void setupRoundTrackSource(ImageView source) {

        source.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                source.setCursor(Cursor.HAND);
            }
        });

        source.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                partialRoundForTools = GridPane.getColumnIndex(source) + 1;
                partialDiceFromRoundForTools = GridPane.getRowIndex(source);
                Dragboard db = source.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putImage(source.getImage());
                db.setContent(content);
                event.consume();
            }
        });
    }

    private void setupReserveTarget(ImageView target) {

        target.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {

                if (event.getDragboard().hasImage()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
                event.consume();
            }
        });

        target.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {

                Dragboard db = event.getDragboard();

                target.setImage(db.getImage());
                targetReserveIndexForTools = partialReserveIndexForTools;

                event.consume();
            }
        });
    }

    private void setupSacrificeTarget(ImageView target) {

        target.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {

                if (event.getDragboard().hasImage()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
                event.consume();
            }
        });

        target.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                target.setImage(db.getImage());
                targetSacrificeDiceForTools = partialReserveIndexForTools;
                event.consume();
            }
        });
    }

    private void setupSchemeCardTarget(ImageView target) {

        target.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {

                if (event.getDragboard().hasImage()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
                event.consume();
            }
        });

        target.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {

                Dragboard db = event.getDragboard();
                target.setImage(db.getImage());
                if (pickedDices == 0) {
                    targetStartXForTools1 = partialStartXForTools1;
                    targetStartYForTools1 = partialStartYForTools1;
                    pickedDices = 1;
                } else {
                    targetStartXForTools2 = partialStartXForTools2;
                    targetStartYForTools2 = partialStartYForTools2;
                }
                event.consume();
            }
        });
    }

    private void setupRoundTrackTarget(ImageView target) {

        target.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {

                if (event.getDragboard().hasImage()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
                event.consume();
            }
        });

        target.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {

                Dragboard db = event.getDragboard();
                target.setImage(db.getImage());
                targetRoundForTools = partialRoundForTools;
                targetDiceFromRoundForTools = partialDiceFromRoundForTools;
                event.consume();
            }
        });
    }

    public void toolSelected(String source) {
        if (gui.isMyTurn()) {
            int tool = Integer.parseInt(source);
            String name = gui.getToolCardsList().get(tool);
            name = name.replaceAll("tool", "");
            int number = Integer.parseInt(name);

            resetToolValues();
            new ToolContext(number);
        }
    }

    public void windowPatternCardSlotSelected(ImageView slot) {
        if (gui.isMyTurn()) {
            if (diceChosen != OUT_OF_RANGE) {
                resetToolValues();
                Integer tempX = GridPane.getRowIndex(slot);
                if (tempX == null) tempX = 0;
                Integer tempY = GridPane.getColumnIndex(slot);
                if (tempY == null) tempY = 0;
                int coordinateX = tempX;
                int coordinateY = tempY;
                appendToTextArea("Vuoi posizionare il dado: " + diceChosen + "nella posizione: " + coordinateX + "," + coordinateY);
                if (remoteController != null) {
                    try {
                        if (remoteController.placeDice(diceChosen, coordinateX, coordinateY, username, single)) {
                            appendToTextArea("Ben fatto! Il dado scelto è stato piazzato correttamente!");
                            diceChosen = OUT_OF_RANGE;
                        } else {
                            appendToTextArea("ATTENZIONE: Hai provato a piazzare un dado dove non dovresti, o non puoi più piazzare dadi in questo turno!");
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    clientController.request(new PlaceDiceRequest(diceChosen, coordinateX, coordinateY, username, single));
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                    if (clientController.isDicePlaced()) {
                        clientController.setDicePlaced(false);//to reset the value
                        appendToTextArea("Ben fatto! Il dado scelto è stato piazzato correttamente!");
                        diceChosen = OUT_OF_RANGE;
                    } else {
                        appendToTextArea("ATTENZIONE: Hai provato a piazzare un dado dove non dovresti, o non puoi più piazzare dadi in questo turno!");
                    }
                }
            }

        }
    }

    public void init(Scene scene, Gui gui) {
        diceChosen = OUT_OF_RANGE;
        this.gui = gui;
        username = gui.getUsername();
        remoteController = gui.getControllerRmi();
        clientController = gui.getControllerSocket();
        otherSchemeCardsMap = gui.getOtherSchemeCardsMap();
        window = gui.getWindowStage();

        Platform.runLater(() -> {
            window.setScene(scene);
            window.setTitle("GameBoard");
            window.setResizable(true);
            window.show();
        });

        if (gameBoardHandlerSingle != null) {
            sacrificeImageView = new ImageView();
        }
        imageView1 = new ImageView();
        imageView2 = new ImageView();
        imageView3 = new ImageView();
        plus = new Button();
        minus = new Button();
        concludeButton = new Button();
        finalX1 = new TextField();
        finalY1 = new TextField();
        finalX2 = new TextField();
        finalY2 = new TextField();
        textField11 = new TextField();
    }

    public void setWindowPatternCardImg(String imgURL) {
        BackgroundImage myBI = new BackgroundImage(new Image(imgURL, 340, 300, false, true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        Pane playerWindowPatternCard;

        if (single) {
            playerWindowPatternCard = gameBoardHandlerSingle.getPlayerWindowPatternCard();
        } else {
            playerWindowPatternCard = gameBoardHandlerMulti.getPlayerWindowPatternCard();
        }

        playerWindowPatternCard.setBackground(new Background(myBI));

        //Initializing the scheme card slots
        schemeCard.setGridLinesVisible(false);
        schemeCard.setPrefSize(334, 261);
        schemeCard.setHgap(10);
        schemeCard.setVgap(14);
        schemeCard.setLayoutX(3);
        schemeCard.setLayoutY(5);

        final int numCols = COLUMNS;
        final int numRows = ROWS;

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
                setImageInSlot(i, j);
            }
        }
    }

    private void setImageInSlot(int row, int col) {
        Image img = new Image(TRANSPARENT_IMAGE_URL);
        ImageView imgView = new ImageView(img);
        imgView.setFitWidth(58);
        imgView.setFitHeight(55);
        imgView.setOnMouseClicked(windowPatternCardSlotSelected);
        int finalI = row;
        int finalJ = col;
        Platform.runLater(() -> schemeCard.add(imgView, finalJ, finalI));
    }

    private EventHandler<MouseEvent> windowPatternCardSlotSelected = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            windowPatternCardSlotSelected((ImageView) event.getSource());
        }
    };

    public void setSinglePrivateCard(Label privObjLabel, ImageView card, String privateCard) {
        Image privateObjCardImg = new Image(GameBoardHandler.PRIVATE_CARDS_PATH + privateCard + ".png");
        card.setImage(privateObjCardImg);

        card.setOnMouseEntered(event -> {
            privObjLabel.setVisible(false);
            card.setStyle("-fx-scale-x: 2.0;-fx-scale-y: 2.0");
            card.setTranslateX(-10);
            card.setTranslateY(-70);
        });

        card.setOnMouseExited(event -> {
            privObjLabel.setVisible(true);
            card.setStyle("-fx-scale-x: 1.0;-fx-scale-y: 1.0");
            card.setTranslateX(0);
            card.setTranslateY(0);
        });
    }

    public void setReserve(List<String> dicesList, Pane reserve) {

        if (reserve.getChildren() != null) {
            Platform.runLater(() -> reserve.getChildren().remove(0, reserve.getChildren().size()));
        }
        GridPane grid = new GridPane();
        grid.setGridLinesVisible(false);
        grid.setPrefSize(276, 261);
        grid.setHgap(15);
        grid.setVgap(15);
        Insets insets = new Insets(4, 0, 4, 14);
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
        for (String dice : dicesList) {
            String url = DICE_IMAGES_PATH + dice + ".png";
            Image diceImg = new Image(url);
            ImageView diceView = new ImageView(diceImg);
            diceView.setFitWidth(70);
            diceView.setFitHeight(70);
            //NB
            setupReserveSource(diceView);

            diceView.setOnMouseClicked(reserveDiceSelected);
            diceView.setId(Integer.toString(id));
            id++;
            int finalCol = col;
            int finalRow = row;
            Platform.runLater(() -> grid.add(diceView, finalCol, finalRow));
            col++;
            if (col == 3) {
                col = 0;
                row++;
            }
        }

    }

    private EventHandler<MouseEvent> reserveDiceSelected = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (gui.isMyTurn()) {
                String source = event.getSource().toString().substring(13, 14);
                int i = Integer.parseInt(source);
                source = gui.getDicesList().get(i);
                diceChosen = i;
                appendToTextArea("Hai scelto il dado: " + source + " pos: " + i);
            }
        }
    };

    public void appendToTextArea(String s) {
        TextArea textArea;
        if (single) {
            textArea = gameBoardHandlerSingle.getTextArea();
        } else {
            textArea = gameBoardHandlerMulti.getTextArea();
        }

        Platform.runLater(() -> {
            textArea.appendText(s + "\n");
            textArea.setScrollTop(Double.MAX_VALUE);
        });
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
                    String url = DICE_IMAGES_PATH + dice + ".png";
                    Image img = new Image(url);
                    ImageView imgView = new ImageView(img);
                    imgView.setFitWidth(58);
                    imgView.setFitHeight(55);
                    imgView.setOnMouseClicked(windowPatternCardSlotSelected);
                    //NB
                    setupSchemeCardSource((imgView));

                    int finalI = i;
                    int finalJ = j;
                    Platform.runLater(() -> schemeCard.add(imgView, finalJ, finalI));
                } else {
                    setImageInSlot(i, j);
                }
            }
        }
    }

    public void onRoundTrack(String track) {
        Pane roundTrack;
        if (single) {
            roundTrack = gameBoardHandlerSingle.getRoundTrack();
        } else {
            roundTrack = gameBoardHandlerMulti.getRoundTrack();
        }

        if (track != null) {
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
            Pattern p = Pattern.compile("\\[.*?\\]");
            String[] parts = track.split("\n");
            for (int i = 0; i < parts.length - 1; i = i + 2) {
                //String round = (parts[i].substring(6));
                Matcher m = p.matcher(parts[i + 1]);
                int j = 0;
                while (m.find()) {
                    String dice = (m.group(0).substring(1, m.group(0).length() - 1).toLowerCase().replaceAll(" ", "_"));
                    String url = DICE_IMAGES_PATH + dice + ".png";
                    Image img = new Image(url);
                    ImageView imgView = new ImageView(img);
                    imgView.setFitWidth(30);
                    imgView.setFitHeight(30);
                    //NB
                    int finalX = i / 2;
                    int finalY = j;
                    setupRoundTrackSource(imgView);

                    Platform.runLater(() -> grid.add(imgView, finalX, finalY));
                    j++;
                }
            }
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void resetToolValues() {
        partialReserveIndexForTools = OUT_OF_RANGE;
        targetReserveIndexForTools = OUT_OF_RANGE;
        partialDiceFromRoundForTools = OUT_OF_RANGE;
        targetDiceFromRoundForTools = OUT_OF_RANGE;
        partialStartXForTools1 = OUT_OF_RANGE;
        targetStartXForTools1 = OUT_OF_RANGE;
        partialStartYForTools1 = OUT_OF_RANGE;
        targetStartYForTools1 = OUT_OF_RANGE;
        partialStartXForTools2 = OUT_OF_RANGE;
        targetStartXForTools2 = OUT_OF_RANGE;
        partialStartYForTools2 = OUT_OF_RANGE;
        targetStartYForTools2 = OUT_OF_RANGE;
        partialRoundForTools = OUT_OF_RANGE;
        targetRoundForTools = OUT_OF_RANGE;
        value11 = null;
        finalCoordinateX1 = null;
        finalCoordinateY1 = null;
        finalCoordinateX2 = null;
        finalCoordinateY2 = null;
        incrOrDecr = null;
        pickedDices = 0;
        imageView1.setVisible(false);
        imageView2.setVisible(false);
        imageView3.setVisible(false);
        useButton.setVisible(false);
        toolLabel.setVisible(false);
        toolPane.setVisible(false);
        finalX1.setVisible(false);
        finalY1.setVisible(false);
        finalX2.setVisible(false);
        finalY2.setVisible(false);
        textField11.setVisible(false);
        plus.setVisible(false);
        minus.setVisible(false);
        concludeButton.setVisible(false);
        if(gameBoardHandlerSingle!=null) {
            sacrificeImageView.setVisible(false);
        }
    }


    private class ToolContext {
        public ToolContext(int i) {
            switch (i) {
                case 1: {
                    createContext1();
                }
                break;
                case 2: {
                    createContext2or3(i);
                }
                break;
                case 3: {
                    createContext2or3(i);
                }
                break;
                case 4: {
                    createContext4();
                }
                break;
                case 5: {
                    createContext5();
                }
                break;
                case 6: {
                    createContext6();
                }
                break;
                case 7: {
                    createContext7or8(i);
                }
                break;
                case 8: {
                    createContext7or8(i);
                }
                break;
                case 9: {
                    createContext9();
                }
                break;
                case 10: {
                    createContext10();
                }
                break;
                case 11: {
                    createContext11();
                }
                break;
                case 12: {
                    createContext12();
                }
                break;
            }
        }

        private void createContext1() {

            setupSacrificeImageView();

            imageView1 = null;
            toolLabel.setVisible(true);
            toolPane.setVisible(true);
            useButton.setVisible(true);

            imageView1 = new ImageView();
            imageView1.setImage(new Image(DICE_IMAGES_PATH + "white.png"));//SOLO UNA PROVA
            imageView1.setFitWidth(70);
            imageView1.setFitHeight(70);
            imageView1.setLayoutX(63);
            imageView1.setLayoutY(250);

            plus = new Button();
            plus.setText("+");
            plus.setStyle("-fx-background-color: linear-gradient(lightgreen, lightseagreen)");
            plus.setLayoutX(70);
            plus.setLayoutY(370);
            plus.setOnMouseClicked(event -> incrOrDecr = "+");

            minus = new Button();
            minus.setText("-");
            minus.setStyle("-fx-background-color: linear-gradient(lightgreen, lightseagreen)");
            minus.setLayoutX(100);
            minus.setLayoutY(370);
            minus.setOnMouseClicked(event -> incrOrDecr = "-");

            gameBoard.getChildren().add(imageView1);
            gameBoard.getChildren().add(plus);
            gameBoard.getChildren().add(minus);

            setupReserveTarget(imageView1);

            useButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (targetReserveIndexForTools != GameBoardHandler.OUT_OF_RANGE && incrOrDecr != null && (incrOrDecr.equals("+") || incrOrDecr.equals("-"))) {
                        //RMI
                        if (remoteController != null) {
                            try {
                                if (gameBoardHandlerSingle != null) {
                                    if (targetSacrificeDiceForTools != GameBoardHandler.OUT_OF_RANGE && targetSacrificeDiceForTools != targetReserveIndexForTools) {
                                        checkBoolean(remoteController.useToolCard1(targetSacrificeDiceForTools, targetReserveIndexForTools, incrOrDecr, username, false), 1);
                                    } else {
                                        appendToTextArea("Non hai inserito alcun dado da sacrificare, o lo hai fatto in modo errato!\n");
                                    }
                                } else {
                                    checkBoolean(remoteController.useToolCard1(-1, targetReserveIndexForTools, incrOrDecr, username, false), 1);
                                }
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                        //SOCKET
                        else {
                            if (gameBoardHandlerSingle != null) {
                                if (targetSacrificeDiceForTools != GameBoardHandler.OUT_OF_RANGE && targetSacrificeDiceForTools != targetReserveIndexForTools) {
                                    clientController.request(new UseToolCard1Request(targetSacrificeDiceForTools, targetReserveIndexForTools, incrOrDecr, username, false));
                                } else {
                                    appendToTextArea("Non hai inserito alcun dado da sacrificare, o lo hai fatto in modo errato!\n");
                                }
                            } else {
                                clientController.request(new UseToolCard1Request(-1, targetReserveIndexForTools, incrOrDecr, username, false));
                            }
                            checkBoolean(waitForToolEffectAppliedResponse(), 1);
                        }
                    } else {
                        appendToTextArea("Non hai scelto alcun dado dalla riserva o non hai cliccato '+' o '-'!");
                    }
                    resetToolValues();
                }
            });
        }

        private void createContext2or3(int n) {
            imageView1 = null;
            toolLabel.setVisible(true);
            toolPane.setVisible(true);
            useButton.setVisible(true);
            imageView1 = new ImageView();
            imageView1.setImage(new Image(DICE_IMAGES_PATH + "white.png"));
            imageView1.setFitWidth(70);
            imageView1.setFitHeight(70);
            imageView1.setLayoutX(63);
            imageView1.setLayoutY(250);

            finalX1 = new TextField();
            finalY1 = new TextField();
            finalX1.setMaxWidth(30);
            finalY1.setMaxWidth(30);
            finalX1.setLayoutX(65);
            finalX1.setLayoutY(370);
            finalY1.setLayoutX(105);
            finalY1.setLayoutY(370);

            gameBoard.getChildren().add(imageView1);
            gameBoard.getChildren().add(finalX1);
            gameBoard.getChildren().add(finalY1);
            setupSchemeCardTarget(imageView1);

            useButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {
                    finalCoordinateX1 = tryParse(finalX1.getText());
                    finalCoordinateY1 = tryParse(finalY1.getText());
                    if (finalCoordinateX1 != null && finalCoordinateY1 != null && 0 <= finalCoordinateX1 && finalCoordinateX1 < 4 && 0 <= finalCoordinateY1 && finalCoordinateY1 < 5 && targetStartXForTools1 != GameBoardHandler.OUT_OF_RANGE && targetStartYForTools1 != GameBoardHandler.OUT_OF_RANGE) {
                        if (remoteController != null) {
                            try {
                                checkBoolean(remoteController.useToolCard2or3(-1, n, targetStartXForTools1, targetStartYForTools1, finalCoordinateX1, finalCoordinateY1, username, false), n);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        } else {
                            clientController.request(new UseToolCard2or3Request(-1, n, targetStartXForTools1, targetStartYForTools1, finalCoordinateX1, finalCoordinateY1, username, false));
                            checkBoolean(waitForToolEffectAppliedResponse(), n);
                        }
                    } else {
                        appendToTextArea("Non hai scelto alcun dado dalla carta schema o non hai settato correttamente le coordinate!");
                    }
                    resetToolValues();
                }
            });

        }

        private void createContext4() {
            imageView1 = null;
            imageView2 = null;
            toolLabel.setVisible(true);
            toolPane.setVisible(true);
            useButton.setVisible(true);

            imageView1 = new ImageView();
            imageView1.setImage(new Image(DICE_IMAGES_PATH + "white.png"));
            imageView1.setFitWidth(70);
            imageView1.setFitHeight(70);
            imageView1.setLayoutX(63);
            imageView1.setLayoutY(180);
            imageView2 = new ImageView();
            imageView2.setImage(new Image(DICE_IMAGES_PATH + "white.png"));
            imageView2.setFitWidth(70);
            imageView2.setFitHeight(70);
            imageView2.setLayoutX(63);
            imageView2.setLayoutY(350);

            finalX1 = new TextField();
            finalY1 = new TextField();
            finalX1.setMaxWidth(30);
            finalY1.setMaxWidth(30);
            finalX1.setLayoutX(65);
            finalX1.setLayoutY(280);
            finalY1.setLayoutX(100);
            finalY1.setLayoutY(280);
            finalX2 = new TextField();
            finalY2 = new TextField();
            finalX2.setMaxWidth(30);
            finalY2.setMaxWidth(30);
            finalX2.setLayoutX(65);
            finalX2.setLayoutY(450);
            finalY2.setLayoutX(100);
            finalY2.setLayoutY(450);

            gameBoard.getChildren().add(imageView1);
            gameBoard.getChildren().add(imageView2);
            gameBoard.getChildren().add(finalX1);
            gameBoard.getChildren().add(finalY1);
            gameBoard.getChildren().add(finalX2);
            gameBoard.getChildren().add(finalY2);
            setupSchemeCardTarget(imageView1);
            setupSchemeCardTarget(imageView2);

            useButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {
                    finalCoordinateX1 = tryParse(finalX1.getText());
                    finalCoordinateY1 = tryParse(finalY1.getText());
                    finalCoordinateX2 = tryParse(finalX2.getText());
                    finalCoordinateY2 = tryParse(finalY2.getText());
                    if (finalCoordinateX1 != null && finalCoordinateY1 != null && finalCoordinateX2 != null && finalCoordinateY2 != null && 0 <= finalCoordinateX1 && finalCoordinateX1 < 4 && 0 <= finalCoordinateY1 && finalCoordinateY1 < 5 && 0 <= finalCoordinateX2 && finalCoordinateX2 < 4 && 0 <= finalCoordinateY2 && finalCoordinateY2 < 5 && targetStartXForTools1 != GameBoardHandler.OUT_OF_RANGE && targetStartYForTools1 != GameBoardHandler.OUT_OF_RANGE && targetStartXForTools2 != GameBoardHandler.OUT_OF_RANGE && targetStartYForTools2 != GameBoardHandler.OUT_OF_RANGE) {
                        if (remoteController != null) {
                            try {
                                checkBoolean(remoteController.useToolCard4(-1, targetStartXForTools1, targetStartYForTools1, finalCoordinateX1, finalCoordinateY1, targetStartXForTools2, targetStartYForTools2, finalCoordinateX2, finalCoordinateY2, username, false), 4);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        } else {
                            clientController.request(new UseToolCard4Request(-1, targetStartXForTools1, targetStartYForTools1, finalCoordinateX1, finalCoordinateY1, targetStartXForTools2, targetStartYForTools2, finalCoordinateX2, finalCoordinateY2, username, false));
                            checkBoolean(waitForToolEffectAppliedResponse(), 4);
                        }

                    } else {
                        appendToTextArea("Non hai scelto alcun dado dalla carta schema o non hai settato correttamente le coordinate!");
                    }
                    resetToolValues();
                }
            });
        }

        private void createContext5() {
            imageView1 = null;
            imageView2 = null;
            toolLabel.setVisible(true);
            toolPane.setVisible(true);
            useButton.setVisible(true);
            imageView1 = new ImageView();
            imageView1.setImage(new Image(DICE_IMAGES_PATH + "white.png"));
            imageView1.setFitWidth(70);
            imageView1.setFitHeight(70);
            imageView1.setLayoutX(63);
            imageView1.setLayoutY(180);

            imageView2 = new ImageView();
            imageView2.setImage(new Image(DICE_IMAGES_PATH + "white.png"));
            imageView2.setFitWidth(70);
            imageView2.setFitHeight(70);
            imageView2.setLayoutX(63);
            imageView2.setLayoutY(300);

            gameBoard.getChildren().add(imageView1);
            gameBoard.getChildren().add(imageView2);

            setupReserveTarget(imageView1);
            setupRoundTrackTarget(imageView2);

            useButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {
                    if (targetReserveIndexForTools != GameBoardHandler.OUT_OF_RANGE && targetRoundForTools != GameBoardHandler.OUT_OF_RANGE && targetDiceFromRoundForTools != GameBoardHandler.OUT_OF_RANGE) {
                        if (remoteController != null) {
                            try {
                                checkBoolean(remoteController.useToolCard5(-1, targetReserveIndexForTools, targetRoundForTools, targetDiceFromRoundForTools, username, false), 5);

                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        } else {
                            clientController.request(new UseToolCard5Request(-1, targetReserveIndexForTools, targetRoundForTools, targetDiceFromRoundForTools, username, false));
                            checkBoolean(waitForToolEffectAppliedResponse(), 5);
                        }
                    } else {
                        appendToTextArea("Non hai scelto alcun dado dalla riserva o dal tracciato dei round!");
                    }

                    resetToolValues();

                }
            });

        }

        private void createContext6() {
            imageView1 = null;
            toolLabel.setVisible(true);
            toolPane.setVisible(true);
            useButton.setVisible(true);
            imageView1 = new ImageView();
            imageView1.setImage(new Image(DICE_IMAGES_PATH + "white.png"));//SOLO UNA PROVA
            imageView1.setFitWidth(70);
            imageView1.setFitHeight(70);
            imageView1.setLayoutX(63);
            imageView1.setLayoutY(250);
            gameBoard.getChildren().add(imageView1);
            setupReserveTarget(imageView1);

            useButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (targetReserveIndexForTools != GameBoardHandler.OUT_OF_RANGE) {
                        //RMI
                        if (remoteController != null) {
                            try {
                                checkBoolean(remoteController.useToolCard6(-1, targetReserveIndexForTools, username, false), 6);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                        //SOCKET
                        else {
                            clientController.request(new UseToolCard6Request(-1, targetReserveIndexForTools, username, false));
                            checkBoolean(waitForToolEffectAppliedResponse(), 6);
                        }

                    } else {
                        appendToTextArea("Non hai scelto alcun dado dalla riserva!");
                    }
                    resetToolValues();
                }
            });
        }

        private void createContext7or8(int n) {
            toolLabel.setVisible(true);
            toolPane.setVisible(true);
            useButton.setVisible(true);

            useButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (n == 7) {
                        if (remoteController != null) {
                            try {
                                if (diceChosen == GameBoardHandler.OUT_OF_RANGE) {
                                    checkBoolean(remoteController.useToolCard7(-1, username, false), 7);
                                } else {
                                    appendToTextArea("Non puoi scegliere un dado prima di utilizzare questa carta utensile!");
                                }
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        } else {
                            clientController.request(new UseToolCard7Request(-1, username, false));
                            checkBoolean(waitForToolEffectAppliedResponse(), 7);
                        }
                    } else {
                        if (remoteController != null) {
                            try {
                                checkBoolean(remoteController.useToolCard8(-1, username, false), 8);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        } else {
                            clientController.request(new UseToolCard8Request(-1, username, false));
                            checkBoolean(waitForToolEffectAppliedResponse(), 8);

                        }
                    }
                    resetToolValues();
                }
            });
        }

        private void createContext9() {
            imageView1 = null;
            toolLabel.setVisible(true);
            toolPane.setVisible(true);
            useButton.setVisible(true);
            imageView1 = new ImageView();
            imageView1.setImage(new Image(DICE_IMAGES_PATH + "white.png"));
            imageView1.setFitWidth(70);
            imageView1.setFitHeight(70);
            imageView1.setLayoutX(63);
            imageView1.setLayoutY(250);

            finalX1 = new TextField();
            finalY1 = new TextField();
            finalX1.setMaxWidth(30);
            finalY1.setMaxWidth(30);
            finalX1.setLayoutX(65);
            finalX1.setLayoutY(370);
            finalY1.setLayoutX(105);
            finalY1.setLayoutY(370);

            gameBoard.getChildren().add(imageView1);
            gameBoard.getChildren().add(finalX1);
            gameBoard.getChildren().add(finalY1);
            setupReserveTarget(imageView1);

            useButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {
                    finalCoordinateX1 = tryParse(finalX1.getText());
                    finalCoordinateY1 = tryParse(finalY1.getText());
                    if (finalCoordinateX1 != null && finalCoordinateY1 != null && 0 <= finalCoordinateX1 && finalCoordinateX1 < 4 && 0 <= finalCoordinateY1 && finalCoordinateY1 < 5 && targetReserveIndexForTools != GameBoardHandler.OUT_OF_RANGE) {
                        if (remoteController != null) {
                            try {
                                checkBoolean(remoteController.useToolCard9(-1, targetReserveIndexForTools, finalCoordinateX1, finalCoordinateY1, username, false),9);

                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        } else {
                            clientController.request(new UseToolCard9Request(-1, targetReserveIndexForTools, finalCoordinateX1, finalCoordinateY1, username, false));
                            checkBoolean(waitForToolEffectAppliedResponse(), 9);
                        }

                    } else {
                        appendToTextArea("Non hai scelto alcun dado dalla riserva o non hai settato correttamente le coordinate!");
                    }
                    resetToolValues();
                }
            });
        }

        private void createContext10() {

            imageView1 = null;
            toolLabel.setVisible(true);
            toolPane.setVisible(true);
            useButton.setVisible(true);
            imageView1 = new ImageView();
            imageView1.setImage(new Image(DICE_IMAGES_PATH + "white.png"));
            imageView1.setFitWidth(70);
            imageView1.setFitHeight(70);
            imageView1.setLayoutX(63);
            imageView1.setLayoutY(250);
            gameBoard.getChildren().add(imageView1);
            setupReserveTarget(imageView1);

            useButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (targetReserveIndexForTools != GameBoardHandler.OUT_OF_RANGE) {
                        //RMI
                        if (remoteController != null) {
                            try {
                                checkBoolean(remoteController.useToolCard10(-1, targetReserveIndexForTools, username, false), 10);

                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                        //SOCKET
                        else {
                            clientController.request(new UseToolCard10Request(-1, targetReserveIndexForTools, username, false));
                            checkBoolean(waitForToolEffectAppliedResponse(), 10);
                        }

                    } else {
                        appendToTextArea("Non hai scelto alcun dado dalla riserva!");
                    }
                    resetToolValues();
                }
            });

        }

        private void createContext11() {
            imageView1 = null;
            toolLabel.setVisible(true);
            toolPane.setVisible(true);
            useButton.setVisible(true);
            imageView1 = new ImageView();
            imageView1.setImage(new Image(DICE_IMAGES_PATH + "white.png"));
            imageView1.setFitWidth(70);
            imageView1.setFitHeight(70);
            imageView1.setLayoutX(63);
            imageView1.setLayoutY(250);
            gameBoard.getChildren().add(imageView1);
            setupReserveTarget(imageView1);

            useButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (targetReserveIndexForTools != GameBoardHandler.OUT_OF_RANGE) {
                        //RMI
                        if (remoteController != null) {
                            try {

                                if (remoteController.useToolCard11(-1, targetReserveIndexForTools, username, false)) {
                                    resetToolValues();
                                    createContext11bis();

                                } else {
                                    appendToTextArea("Carta utensile 11 non applicata, occhio ai tuoi segnalini o a come va utizzata!");
                                }
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                        //SOCKET
                        else {
                            clientController.request(new UseToolCard11Request(-1, targetReserveIndexForTools, username, false));
                            if (waitForToolEffectAppliedResponse()) {
                                resetToolValues();
                                createContext11bis();
                            } else {
                                appendToTextArea("Carta utensile 11 non applicata, occhio ai tuoi segnalini o a come va utizzata!");
                            }
                        }
                    } else {
                        appendToTextArea("Non hai scelto alcun dado dalla riserva!");
                    }

                }
            });
        }

        private void createContext12() {
            imageView1 = null;
            imageView2 = null;
            imageView3 = null;
            toolLabel.setVisible(true);
            toolPane.setVisible(true);
            useButton.setVisible(true);
            imageView3 = new ImageView();
            imageView3.setImage(new Image(DICE_IMAGES_PATH + "white.png"));
            imageView3.setFitWidth(70);
            imageView3.setFitHeight(70);
            imageView3.setLayoutX(63);
            imageView3.setLayoutY(140);
            imageView1 = new ImageView();
            imageView1.setImage(new Image(DICE_IMAGES_PATH + "white.png"));
            imageView1.setFitWidth(70);
            imageView1.setFitHeight(70);
            imageView1.setLayoutX(63);
            imageView1.setLayoutY(240);
            imageView2 = new ImageView();
            imageView2.setImage(new Image(DICE_IMAGES_PATH + "white.png"));
            imageView2.setFitWidth(70);
            imageView2.setFitHeight(70);
            imageView2.setLayoutX(63);
            imageView2.setLayoutY(360);

            finalX1 = new TextField();
            finalY1 = new TextField();
            finalX1.setMaxWidth(30);
            finalY1.setMaxWidth(30);
            finalX1.setLayoutX(65);
            finalX1.setLayoutY(320);
            finalY1.setLayoutX(100);
            finalY1.setLayoutY(320);
            finalX2 = new TextField();
            finalY2 = new TextField();
            finalX2.setMaxWidth(30);
            finalY2.setMaxWidth(30);
            finalX2.setLayoutX(65);
            finalX2.setLayoutY(440);
            finalY2.setLayoutX(100);
            finalY2.setLayoutY(440);

            gameBoard.getChildren().add(imageView1);
            gameBoard.getChildren().add(imageView2);
            gameBoard.getChildren().add(imageView3);
            gameBoard.getChildren().add(finalX1);
            gameBoard.getChildren().add(finalY1);
            gameBoard.getChildren().add(finalX2);
            gameBoard.getChildren().add(finalY2);
            setupRoundTrackTarget(imageView3);
            setupSchemeCardTarget(imageView1);
            setupSchemeCardTarget(imageView2);


            useButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {
                    finalCoordinateX1 = tryParse(finalX1.getText());
                    finalCoordinateY1 = tryParse(finalY1.getText());
                    finalCoordinateX2 = tryParse(finalX2.getText());
                    finalCoordinateY2 = tryParse(finalY2.getText());

                    if (targetDiceFromRoundForTools != GameBoardHandler.OUT_OF_RANGE && targetRoundForTools != GameBoardHandler.OUT_OF_RANGE && finalCoordinateX1 != null && finalCoordinateY1 != null && finalCoordinateX2 != null && finalCoordinateY2 != null && 0 <= finalCoordinateX1 && finalCoordinateX1 < 4 && 0 <= finalCoordinateY1 && finalCoordinateY1 < 5 && 0 <= finalCoordinateX2 && finalCoordinateX2 < 4 && 0 <= finalCoordinateY2 && finalCoordinateY2 < 5 && targetStartXForTools1 != GameBoardHandler.OUT_OF_RANGE && targetStartYForTools1 != GameBoardHandler.OUT_OF_RANGE && targetStartXForTools2 != GameBoardHandler.OUT_OF_RANGE && targetStartYForTools2 != GameBoardHandler.OUT_OF_RANGE) {
                        if (remoteController != null) {
                            try {


                                checkBoolean (remoteController.useToolCard12(-1, targetRoundForTools, targetDiceFromRoundForTools, targetStartXForTools1, targetStartYForTools1, finalCoordinateX1, finalCoordinateY1, targetStartXForTools2, targetStartYForTools2, finalCoordinateX2, finalCoordinateY2, username, false),12);

                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        } else {
                            clientController.request(new UseToolCard12Request(-1, targetRoundForTools, targetDiceFromRoundForTools, targetStartXForTools1, targetStartYForTools1, finalCoordinateX1, finalCoordinateY1, targetStartXForTools2, targetStartYForTools2, finalCoordinateX2, finalCoordinateY2, username, false));
                            checkBoolean(waitForToolEffectAppliedResponse(),12);
                        }

                    } else if (targetDiceFromRoundForTools != GameBoardHandler.OUT_OF_RANGE && targetRoundForTools != GameBoardHandler.OUT_OF_RANGE && finalCoordinateX1 != null && finalCoordinateY1 != null && 0 <= finalCoordinateX1 && finalCoordinateX1 < 4 && 0 <= finalCoordinateY1 && finalCoordinateY1 < 5 && targetStartXForTools1 != GameBoardHandler.OUT_OF_RANGE && targetStartYForTools1 != GameBoardHandler.OUT_OF_RANGE && (finalCoordinateX2 == null || finalCoordinateY2 == null || 0 > finalCoordinateX2 || finalCoordinateX2 > 4 || 0 > finalCoordinateY2 || finalCoordinateY2 > 5 || targetStartXForTools2 == GameBoardHandler.OUT_OF_RANGE || targetStartYForTools2 == GameBoardHandler.OUT_OF_RANGE)) {
                        if (remoteController != null) {
                            try {
                                checkBoolean (remoteController.useToolCard12(-1, targetRoundForTools, targetDiceFromRoundForTools, targetStartXForTools1, targetStartYForTools1, finalCoordinateX1, finalCoordinateY1, -1, -1, -1, -1, username, false),12);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        } else {
                            clientController.request(new UseToolCard12Request(-1, targetRoundForTools, targetDiceFromRoundForTools, targetStartXForTools1, targetStartYForTools1, finalCoordinateX1, finalCoordinateY1, -1, -1, -1, -1, username, false));
                            checkBoolean (waitForToolEffectAppliedResponse(),12);
                        }
                    } else {
                        appendToTextArea("Non hai scelto alcun dado dalla carta schema, non hai scelto un dado dal round track o non hai settato correttamente le coordinate!");
                    }
                    resetToolValues();
                }
            });
        }

        private void createContext11bis() {
            toolLabel.setVisible(true);
            toolPane.setVisible(true);
            Colors color = null;
            if (remoteController != null) {
                try {
                    color = remoteController.askForDiceColor(username, false);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else {
                clientController.request(new DiceColorRequest(username, false));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
                color = clientController.getDiceColor();
            }
            if (color != null) {
                appendToTextArea("Carta utensile 11 utilizzata correttamente! Il dado da te selezionato è stato inserito nel sacchetto! Ora puoi scegliere il valore del nuovo dado del colore  " + color.toString() + " e piazzarlo! Se non concludi l'operazione ti verrà comunque addebitato il prezzo dei segnalini in quanto hai modificato lo stato della partita!");
            }
            imageView1 = new ImageView();
            imageView1.setFitWidth(70);
            imageView1.setFitHeight(70);
            imageView1.setLayoutX(63);
            imageView1.setLayoutY(250);
            imageView1.setImage(new Image(DICE_IMAGES_PATH + color + ".png"));
            concludeButton = new Button();
            concludeButton.setStyle("-fx-background-color: linear-gradient(lightgreen, lightseagreen)");
            concludeButton.setText("Concludi");
            concludeButton.setLayoutX(63);
            concludeButton.setLayoutY(480);
            finalX1 = new TextField();
            finalY1 = new TextField();
            finalX1.setMaxWidth(30);
            finalY1.setMaxWidth(30);
            finalX1.setLayoutX(63);
            finalX1.setLayoutY(430);
            finalY1.setLayoutX(103);
            finalY1.setLayoutY(430);
            textField11 = new TextField();
            textField11.setMaxWidth(30);
            textField11.setLayoutX(82);
            textField11.setLayoutY(370);
            gameBoard.getChildren().add(concludeButton);
            gameBoard.getChildren().add(imageView1);
            gameBoard.getChildren().add(textField11);
            gameBoard.getChildren().add(finalX1);
            gameBoard.getChildren().add(finalY1);

            concludeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {

                    value11 = tryParse(textField11.getText());
                    finalCoordinateX1 = tryParse(finalX1.getText());
                    finalCoordinateY1 = tryParse(finalY1.getText());

                    if (value11 != null && finalCoordinateX1 != null && finalCoordinateY1 != null && value11 > 0 && value11 < 7 && 0 <= finalCoordinateX1 && finalCoordinateX1 < 4 && 0 <= finalCoordinateY1 && finalCoordinateY1 < 5) {
                        if (remoteController != null) {
                            try {
                                remoteController.setDiceValue(value11, username, false);//POTREBBE MANCARE CONTROLLO SU VALORE DADO
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                            try {
                                if (remoteController.placeDiceTool11(finalCoordinateX1, finalCoordinateY1, username, false)) {
                                    appendToTextArea("Dado piazzato correttamente!");
                                    concludeButton.setVisible(false);
                                    resetToolValues();
                                } else {
                                    appendToTextArea("Non puoi piazzare lì il tuo dado! Scegli altre coordinate!");
                                }
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        } else {
                            clientController.request(new SetDiceValueRequest(value11, username, false));
                            clientController.request(new PlaceDiceTool11Request(finalCoordinateX1, finalCoordinateY1, username, false));
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                Thread.currentThread().interrupt();
                            }
                            if (clientController.isDicePlaced()) {
                                clientController.setDicePlaced(false);//to reset the value
                                appendToTextArea("Dado piazzato correttamente!");
                            } else {
                                appendToTextArea("Non puoi piazzare lì il tuo dado! Scegli altre coordinate!");
                            }
                        }

                    } else {
                        appendToTextArea("Non hai inserito un valore corretto oppure non hai settato correttamente le coordinate!");
                    }
                }
            });
        }

        private boolean waitForToolEffectAppliedResponse() {
            try {
                Thread.sleep(1000); // todo: controllare, ha senso ancora?
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }

            if (clientController.isEffectApplied()) {
                clientController.setEffectApplied(false);   //to reset the value
                return true;
            } else {
                return false;
            }
        }

        private Integer tryParse(String text) {

            try {
                return Integer.parseInt(text);
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }

    private void checkBoolean(boolean done, int i) {
        if (done) {
            appendToTextArea("Carta utensile " + i + " utilizzata correttamente!");
        } else {
            appendToTextArea("Carta utensile " + i + " non applicata, occhio ai tuoi segnalini o a come va utizzata!\n");
        }
    }

    private void setupSacrificeImageView() {
        if (gameBoardHandlerSingle != null) {
            sacrificeImageView = null;
            sacrificeImageView = new ImageView();
            sacrificeImageView.setImage(new Image(DICE_IMAGES_PATH + "white.png"));//SOLO UNA PROVA
            sacrificeImageView.setFitWidth(70);
            sacrificeImageView.setFitHeight(70);
            sacrificeImageView.setLayoutX(63);
            sacrificeImageView.setLayoutY(480);
            gameBoard.getChildren().add(sacrificeImageView);
            setupSacrificeTarget(sacrificeImageView);
        }
    }

    public void passButtonClicked() {
        resetToolValues();
        if (gui.isMyTurn()) {
            if (remoteController != null) {
                try {
                    remoteController.goThrough(username, single);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else {
                clientController.request(new GoThroughRequest(username, single));
            }
        } else {
            if (!single) {
                appendToTextArea("Non è il tuo turno!");
            }
        }
    }

    public void quitClicked() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Vuoi davvero uscire?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Uscita");
        alert.setHeaderText(null);
        alert.setResizable(false);
        alert.setGraphic(null);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            window.close();
            if (remoteController != null) {
                try {
                    remoteController.quitGame(username, false);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else {
                clientController.request(new QuitGameRequest(username, false));
            }
            System.exit(0);
        }
    }
}