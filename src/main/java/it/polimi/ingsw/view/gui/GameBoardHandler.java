package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.control.RemoteController;
import it.polimi.ingsw.control.SocketController;
import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.socket.requests.*;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameBoardHandler {

    private GameBoardHandlerMulti gameBoardHandlerMulti;
    private GameBoardHandlerSingle gameBoardHandlerSingle;

    static final String CONNECTION_ERROR="Problema di connessione con il server, rieffettua il login per giocare!";
    static final String TOOLCARDS_PATH = "/images/cards/toolcards/";
    static final String DICE_IMAGES_PATH = "/images/dices/";
    static final String PUBLIC_CARDS_PATH = "/images/cards/public_objective_cards/";
    static final String FAVOR_TOKEN_PATH = "/images/other/favour.png";
    static final String WINDOW_PATTERN_CARDS_PATH = "/images/cards/window_pattern_cards/";
    private static final String PRIVATE_CARDS_PATH = "/images/cards/private_objective_cards/";
    private static final String TRANSPARENT_IMAGE_URL = "/images/other/transparent.png";
    private static final int OUT_OF_RANGE = -1;
    private static final int COLUMNS = 5;
    private static final int ROWS = 4;

    private int partialStartXForTools = GameBoardHandler.OUT_OF_RANGE;
    private int partialStartYForTools = GameBoardHandler.OUT_OF_RANGE;
    private int targetStartXForTools1 = GameBoardHandler.OUT_OF_RANGE;
    private int targetStartYForTools1 = GameBoardHandler.OUT_OF_RANGE;
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

    /* Useful for contexts */
    private ImageView sacrificeImageView;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private Button plus;
    private Button minus;
    private Button concludeButton;

    private RemoteController rmiController;
    private SocketController socketController;
    private String username;
    private Stage window;
    private Gui gui;
    private int diceChosen;
    private GridPane schemeCard = new GridPane();
    private final boolean single;

    private AnchorPane gameBoard;
    private Pane toolPane;
    private Label toolLabel;
    private Label sacrificeLabel;
    private Button useButton;

    /**
     * Constructor for GameBoardHandler.
     * @param single is true if this is a single player match, false otherwise.
     * @param gameBoardHandlerMulti is the already existing GameBoardHandlerMulti if this is a multi player match.
     * @param gameBoardHandlerSingle is the already existing GameBoardHandlerSingle if this is a single player match.
     * @param gameBoard is the AnchorPane that contains all the other elements of the scene.
     * @param toolPane is the Pane containing the context for tool card use.
     * @param toolLabel is a label contained in the toolPane.
     * @param useButton is the use button contained in the toolPane.
     * @param sacrificeLabel is a label contained in the toolPane.
     */
    GameBoardHandler(boolean single, GameBoardHandlerMulti gameBoardHandlerMulti, GameBoardHandlerSingle gameBoardHandlerSingle, AnchorPane gameBoard, Pane toolPane, Label toolLabel, Button useButton, Label sacrificeLabel) {
        this.gameBoardHandlerMulti = gameBoardHandlerMulti;
        this.gameBoardHandlerSingle = gameBoardHandlerSingle;
        this.single = single;
        this.gameBoard = gameBoard;
        this.toolPane = toolPane;
        this.toolLabel = toolLabel;
        this.useButton = useButton;
        this.sacrificeLabel = sacrificeLabel;
    }

    /**
     * Allows the player to drag a dice from the scheme card.
     * @param source is the ImageView of the dice.
     */
    private void setupSchemeCardSource(ImageView source) {
        source.setOnMouseEntered(event -> source.setCursor(Cursor.HAND));
        source.setOnDragDetected(event -> {

            partialStartXForTools = GridPane.getRowIndex(source);
            partialStartYForTools = GridPane.getColumnIndex(source);

            Dragboard db = source.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putImage(source.getImage());
            db.setContent(content);
            event.consume();
        });
    }

    /**
     * Allows the player to drag a dice from the reserve.
     * @param source is the ImageView of the dice.
     */
    private void setupReserveSource(ImageView source) {
        source.setOnMouseEntered(event -> source.setCursor(Cursor.HAND));
        source.setOnDragDetected(event -> {
            String s = event.getSource().toString();
            s = s.substring(13, 14);
            partialReserveIndexForTools = Integer.parseInt(s);
            Dragboard db = source.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putImage(source.getImage());
            db.setContent(content);
            event.consume();
        });
    }

    /**
     * Allows the player to drag a dice from the round track.
     * @param source is the ImageView of the dice.
     */
    private void setupRoundTrackSource(ImageView source) {

        source.setOnMouseEntered(event -> source.setCursor(Cursor.HAND));

        source.setOnDragDetected(event -> {
            partialRoundForTools = GridPane.getColumnIndex(source) + 1;
            partialDiceFromRoundForTools = GridPane.getRowIndex(source);
            Dragboard db = source.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putImage(source.getImage());
            db.setContent(content);
            event.consume();
        });
    }

    /**
     * Allows the player to drop a dice in the selected box in tool card context.
     * @param target is the ImageView of the selected box.
     */
    private void setupReserveTarget(ImageView target) {
        target.setOnDragOver(event -> {
            if (event.getDragboard().hasImage()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        target.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();

            target.setImage(db.getImage());
            targetReserveIndexForTools = partialReserveIndexForTools;

            event.consume();
        });
    }

    /**
     * Allows the player to drag a dice over the sacrifice dice box or drop one on it.
     * @param target is the ImageView of the sacrifice dice box.
     */
    private void setupSacrificeTarget(ImageView target) {

        target.setOnDragOver(event -> {
            if (event.getDragboard().hasImage()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        target.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            target.setImage(db.getImage());
            targetSacrificeDiceForTools = partialReserveIndexForTools;
            event.consume();
        });
    }

    private void setupSchemeCardTarget1(ImageView target) {

        target.setOnDragOver(event -> {
            if (event.getDragboard().hasImage()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        target.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            target.setImage(db.getImage());
            targetStartXForTools1 = partialStartXForTools;
            targetStartYForTools1 = partialStartYForTools;
            event.consume();
        });
    }

    private void setupSchemeCardTarget2(ImageView target) {

        target.setOnDragOver(event -> {
            if (event.getDragboard().hasImage()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        target.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            target.setImage(db.getImage());
            targetStartXForTools2 = partialStartXForTools;
            targetStartYForTools2 = partialStartYForTools;
            event.consume();
        });
    }

    private void setupRoundTrackTarget(ImageView target) {

        target.setOnDragOver(event -> {

            if (event.getDragboard().hasImage()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        target.setOnDragDropped(event -> {

            Dragboard db = event.getDragboard();
            target.setImage(db.getImage());
            targetRoundForTools = partialRoundForTools;
            targetDiceFromRoundForTools = partialDiceFromRoundForTools;
            event.consume();
        });
    }

    /**
     * Creates the context for the chosen tool card.
     * @param source is the id of the chosen tool card.
     */
    void toolSelected(String source) {
        if (gui.isMyTurn()) {
            int tool = Integer.parseInt(source);
            String name = gui.getToolCardsList().get(tool);
            name = name.replaceAll("tool", "");
            int number = Integer.parseInt(name);
            resetToolValues();
            new ToolContext(number);
        }
    }

    /**
     * Removes the username key from the multi player matches map, removes the username from the taken usernames and
     * closes the window.
     */
    void terminateGame() {
        if (rmiController != null) {
            try {
                rmiController.removeMatch(username);
                window.close();
                System.exit(0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else if (socketController != null) {
            socketController.request(new TerminateMatchRequest(username));
            window.close();
            System.exit(0);
        }
    }

    /**
     * When the player clicks on a slot of his window pattern card during his turn, checks if he has chosen a dice from
     * the reserve. If he did, the handler tries to place the dice in the selected slot.
     * The handler then appends a message to the scene text area telling the player if the dice placement was successful.
     * @param slot is the window pattern card slot clicked by the player.
     */
    private void windowPatternCardSlotSelected(ImageView slot) {
        if (gui.isMyTurn()) {
            if (diceChosen != OUT_OF_RANGE) {
                resetToolValues();

                Integer tempX = GridPane.getRowIndex(slot);
                if (tempX == null) tempX = 0;
                Integer tempY = GridPane.getColumnIndex(slot);
                if (tempY == null) tempY = 0;
                int coordinateX = tempX;
                int coordinateY = tempY;
                appendToTextArea("Vuoi posizionare il dado nella posizione: " + coordinateX + "," + coordinateY);
                if (rmiController != null) {
                    try {
                        if (rmiController.placeDice(diceChosen, coordinateX, coordinateY, username, single)) {
                            appendToTextArea("Ben fatto! Il dado scelto è stato piazzato correttamente!");
                            diceChosen = OUT_OF_RANGE;
                        } else {
                            appendToTextArea("ATTENZIONE: Hai provato a piazzare un dado dove non dovresti, o non puoi più piazzare dadi in questo turno!");
                        }
                    } catch (RemoteException e) {
                        System.exit(0);
                        e.printStackTrace();
                    }
                } else {
                    socketController.request(new PlaceDiceRequest(diceChosen, coordinateX, coordinateY, username, single));
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                    if (socketController.isDicePlaced()) {
                        socketController.setDicePlaced(false);//to reset the value
                        appendToTextArea("Ben fatto! Il dado scelto è stato piazzato correttamente!");
                        diceChosen = OUT_OF_RANGE;
                    } else {
                        appendToTextArea("ATTENZIONE: Hai provato a piazzare un dado dove non dovresti, o non puoi più piazzare dadi in questo turno!");
                    }
                }
            }

        }
    }

    /**
     * Initializes the game board scene and shows it.
     * @param scene is the scene to show.
     * @param gui is the current gui.
     */
    void init(Scene scene, Gui gui) {
        diceChosen = OUT_OF_RANGE;
        this.gui = gui;
        username = gui.getUsername();
        rmiController = gui.getControllerRmi();
        socketController = gui.getControllerSocket();
        window = gui.getWindowStage();
        Platform.runLater(() -> {
            window.setScene(scene);
            window.setTitle("Sagrada");
            window.setResizable(false);
            window.show();
        });

        if (gameBoardHandlerSingle != null) {
            sacrificeImageView = new ImageView();
            sacrificeLabel = new Label();
            sacrificeLabel.setVisible(false);
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

    /**
     * Shows the player's window pattern card image as the background of a Pane.
     * It then creates a grid pane where dices will be placed.
     * @param imgURL is the window pattern card image url.
     */
    void setWindowPatternCardImg(String imgURL) {
        BackgroundImage myBI = new BackgroundImage(new Image(getClass().getResourceAsStream(imgURL), 340, 300, false, true),
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
        schemeCard.setPrefSize(334, 263);
        Insets padding = new Insets(0, 0, 0, 5);
        schemeCard.setPadding(padding);
        schemeCard.setHgap(4);
        schemeCard.setVgap(5);
        schemeCard.setLayoutX(3);
        schemeCard.setLayoutY(5);


        for (int i = 0; i < COLUMNS; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / COLUMNS);
            schemeCard.getColumnConstraints().add(colConst);
        }

        for (int i = 0; i < ROWS; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / ROWS);
            schemeCard.getRowConstraints().add(rowConst);
        }

        Platform.runLater(() -> playerWindowPatternCard.getChildren().add(schemeCard));
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                setImageInSlot(i, j);
            }
        }
    }

    /**
     * Puts a transparent ImageView in the selected slot and sets the action to execute when it's clicked.
     * @param row is the row index of the selected slot in the GridPane.
     * @param col is the column index of the selected slot in the GridPane.
     */
    private void setImageInSlot(int row, int col) {
        Image img = new Image(getClass().getResourceAsStream(TRANSPARENT_IMAGE_URL));
        ImageView imgView = new ImageView(img);
        imgView.setFitWidth(58);
        imgView.setFitHeight(55);
        imgView.setOnMouseClicked(windowPatternCardSlotSelected);
        Platform.runLater(() -> schemeCard.add(imgView, col, row));
    }

    /**
     * Effect applied when the player clicks on a window pattern card slot.
     */
    private EventHandler<MouseEvent> windowPatternCardSlotSelected = event -> windowPatternCardSlotSelected((ImageView) event.getSource());

    /**
     * Sets the image of a private objective card and the visual effects to be applied when the mouse enters and exits
     * the image.
     * @param privObjLabel is the label in the game board pointing out where private objective cards are.
     * @param card is the ImageView of the private objective card where the image is set.
     * @param other is the ImageView of the closest private objective card to the one that is being set.
     * @param privateCard is the name of the private objective card, used to derive the image url.
     */
    void setSinglePrivateCard(Label privObjLabel, ImageView card, ImageView other, String privateCard) {
        Image privateObjCardImg = new Image(getClass().getResourceAsStream(PRIVATE_CARDS_PATH + privateCard + ".png"));
        card.setImage(privateObjCardImg);

        card.setOnMouseEntered(event -> {
            privObjLabel.setVisible(false);
            if (other != null) {
                other.setVisible(false);
            }
            card.setStyle("-fx-scale-x: 2.0;-fx-scale-y: 2.0");
            card.setTranslateX(-10);
            card.setTranslateY(-70);
        });

        card.setOnMouseExited(event -> {
            privObjLabel.setVisible(true);
            if (other != null) {
                other.setVisible(true);
            }
            card.setStyle("-fx-scale-x: 1.0;-fx-scale-y: 1.0");
            card.setTranslateX(0);
            card.setTranslateY(0);
        });
    }

    /**
     * Creates a GridPane inside the Pane passed as parameter.
     * For all the dices in dicesList a corresponding dice ImageView is placed in the GridPane and the effect to apply
     * when the ImageView is clicked is set.
     * @param dicesList is the list of all the dices in the reserve.
     * @param reserve is the Pane that contains the reserve.
     */
    void setReserve(List<String> dicesList, Pane reserve) {

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
            Image diceImg = new Image(getClass().getResourceAsStream(DICE_IMAGES_PATH + dice + ".png"));
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

    /**
     * Effect applied when the player clicks on a dice in the reserve.
     * If the player clicks during his turn, he is informed of his choice and the choice is stored.
     */
    private EventHandler<MouseEvent> reserveDiceSelected = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (gui.isMyTurn()) {
                String source = event.getSource().toString().substring(13, 14);
                int i = Integer.parseInt(source);
                source = gui.getDicesList().get(i);
                diceChosen = i;
                String[] parts = source.split("_");
                appendToTextArea("Hai scelto il dado: " + parts[0] + " " + parts[1] + ". Completa il piazzamento con un click su una casella della carta schema!");
            }
        }
    };

    /**
     * Appends a new message to the game board text area.
     * @param s is the message to append.
     */
    void appendToTextArea(String s) {
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

    /**
     * Creates a new GridPane where ImageViews with images of the dices in the window pattern cards are placed in the
     * corresponding slot.
     * In the empty slots a transparent ImageView is placed. The effect to be applied when a slot is clicked is set.
     * @param pane is the Pane containing the player's window pattern card.
     * @param window is a bi-dimensional array of strings representing the window pattern card.
     */
    void setMySchemeCard(Pane pane, String[][] window) {
        if (pane.getChildren() != null) {
            Platform.runLater(() -> pane.getChildren().remove(0, pane.getChildren().size()));
        }

        schemeCard = new GridPane();

        schemeCard.setGridLinesVisible(false);
        schemeCard.setPrefSize(334, 263);
        Insets padding = new Insets(0, 0, 0, 5);
        schemeCard.setPadding(padding);
        schemeCard.setHgap(4);
        schemeCard.setVgap(5);
        schemeCard.setLayoutX(3);
        schemeCard.setLayoutY(5);


        for (int i = 0; i < COLUMNS; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / COLUMNS);
            schemeCard.getColumnConstraints().add(colConst);
        }

        for (int i = 0; i < ROWS; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / ROWS);
            schemeCard.getRowConstraints().add(rowConst);
        }

        Platform.runLater(() -> pane.getChildren().add(schemeCard));

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                String[] parts = window[i][j].split(",");
                String temp = (parts[2].replaceAll(" ", ""));
                if ((temp.substring(0, temp.length() - 2)).equals("null")) {
                    setImageInSlot(i, j);
                } else {
                    String dice = parts[2].toLowerCase();
                    dice = dice.substring(2, dice.length() - 3).replace(" ", "_");
                    Image img = new Image(getClass().getResourceAsStream(DICE_IMAGES_PATH + dice + ".png"));
                    ImageView imgView = new ImageView(img);
                    imgView.setFitWidth(58);
                    imgView.setFitHeight(55);
                    imgView.setOnMouseClicked(windowPatternCardSlotSelected);
                    //NB
                    setupSchemeCardSource((imgView));

                    int finalI = i;
                    int finalJ = j;
                    Platform.runLater(() -> schemeCard.add(imgView, finalJ, finalI));
                }
            }
        }
    }

    /**
     * A new GridPane containing all the round track dices is created.
     * The string representing the track is parsed to obtain the dices positions and their corresponding images.
     * @param track is a string representing the round track.
     */
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
            Pattern p = Pattern.compile("\\[.*?]");
            String[] parts = track.split("\n");
            for (int i = 0; i < parts.length - 1; i = i + 2) {
                //String round = (parts[i].substring(6));
                Matcher m = p.matcher(parts[i + 1]);
                int j = 0;
                while (m.find()) {
                    String dice = (m.group(0).substring(1, m.group(0).length() - 1).toLowerCase().replaceAll(" ", "_"));
                    Image img = new Image(getClass().getResourceAsStream(DICE_IMAGES_PATH + dice + ".png"));
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

    /**
     * Restores all the attributes used in tool card context to their default values.
     */
    void resetToolValues() {
        partialReserveIndexForTools = OUT_OF_RANGE;
        targetReserveIndexForTools = OUT_OF_RANGE;
        partialDiceFromRoundForTools = OUT_OF_RANGE;
        targetDiceFromRoundForTools = OUT_OF_RANGE;
        partialStartXForTools = OUT_OF_RANGE;
        targetStartXForTools1 = OUT_OF_RANGE;
        partialStartYForTools = OUT_OF_RANGE;
        targetStartYForTools1 = OUT_OF_RANGE;
        targetStartXForTools2 = OUT_OF_RANGE;
        targetStartYForTools2 = OUT_OF_RANGE;
        partialRoundForTools = OUT_OF_RANGE;
        targetRoundForTools = OUT_OF_RANGE;
        targetSacrificeDiceForTools = OUT_OF_RANGE;

        value11 = null;
        finalCoordinateX1 = null;
        finalCoordinateY1 = null;
        finalCoordinateX2 = null;
        finalCoordinateY2 = null;
        incrOrDecr = null;
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
        if (gameBoardHandlerSingle != null) {
            sacrificeLabel.setVisible(false);
            sacrificeImageView.setVisible(false);
        }
    }

    /**
     * Creates the context relative to the selected tool card.
     */
    private class ToolContext {
        ToolContext(int i) {
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

        /**
         * Instantiates a new ImageView.
         * @param y is the LayoutY value of the position of the new ImageView.
         */
        private void setImageView1(int y) {
            imageView1 = new ImageView();
            setLayoutImageView(imageView1, y);
        }

        /**
         * Instantiates a new ImageView.
         * @param y is the LayoutY value of the position of the new ImageView.
         */
        private void setImageView2(int y) {
            imageView2 = new ImageView();
            setLayoutImageView(imageView2, y);
        }

        /**
         * Sets the ImageView image (a white box) and LayoutY value.
         * @param img is the ImageView to modify.
         * @param y is the LayoutY value.
         */
        private void setLayoutImageView(ImageView img, int y){
            img.setImage(new Image(getClass().getResourceAsStream(DICE_IMAGES_PATH + "bianco.png")));
            img.setFitWidth(70);
            img.setFitHeight(70);
            img.setLayoutX(63);
            img.setLayoutY(y);
        }

        /**
         * Creates the context for using tool card 1.
         * It shows the context pane with a box to place the dice chosen from the reserve,
         * two buttons, plus and minus, that allow the player to choose if increasing or decreasing the dice value,
         * and, if it's a single player match, also a box for the dice to sacrifice.
         * When the USE button is clicked the effect of the tool card is applied, and a message is shown in the scene text area
         * informing the player about the outcome of the application of the effect.
         */
        private void createContext1() {

            setupSacrificeImageView();

            imageView1 = null;
            toolLabel.setVisible(true);
            toolPane.setVisible(true);
            useButton.setVisible(true);
            setImageView1(250);
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

            useButton.setOnMouseClicked(event -> {
                if (targetReserveIndexForTools != GameBoardHandler.OUT_OF_RANGE && incrOrDecr != null && (incrOrDecr.equals("+") || incrOrDecr.equals("-"))) {
                    //RMI
                    if (rmiController != null) {
                        try {
                            if (gameBoardHandlerSingle != null) {
                                if (sacrificeCheck() && targetSacrificeDiceForTools != targetReserveIndexForTools) {
                                    checkBooleanSingle(rmiController.useToolCard1(targetSacrificeDiceForTools, targetReserveIndexForTools, incrOrDecr, username, true), 1);
                                }
                            } else {
                                checkBooleanMulti(rmiController.useToolCard1(OUT_OF_RANGE, targetReserveIndexForTools, incrOrDecr, username, false), 1);
                            }
                        } catch (RemoteException e) {
                            System.out.println(CONNECTION_ERROR);
                            System.exit(0);
                        }
                    }
                    //SOCKET
                    else {
                        if (gameBoardHandlerSingle != null) {
                            if (sacrificeCheck() && targetSacrificeDiceForTools != targetReserveIndexForTools) {
                                socketController.request(new UseToolCard1Request(targetSacrificeDiceForTools, targetReserveIndexForTools, incrOrDecr, username, true));
                                checkBooleanSingle(waitForToolEffectAppliedResponse(), 1);
                            }
                        } else {
                            socketController.request(new UseToolCard1Request(OUT_OF_RANGE, targetReserveIndexForTools, incrOrDecr, username, false));
                            checkBooleanMulti(waitForToolEffectAppliedResponse(), 1);
                        }

                    }
                } else {
                    appendToTextArea("Non hai scelto alcun dado dalla riserva o non hai cliccato '+' o '-'!");
                }
                resetToolValues();
            });
        }

        /**
         * Creates the context for using the tool card 2 or 3.
         * It shows the context pane with a box for the dice the player wants to move,
         * two text fields where the player puts the coordinates of the chosen dice new position, and, if it's a
         * single player match, also a box for the dice to sacrifice
         * When the USE button is clicked the effect of the tool card is applied, and a message is shown in the scene text area
         * informing the player about the outcome of the application of the effect.
         * @param n is the id of the tool card that is being used (2 or 3).
         */
        private void createContext2or3(int n) {
            setupSacrificeImageView();

            imageView1 = null;
            toolLabel.setVisible(true);
            toolPane.setVisible(true);
            useButton.setVisible(true);
            setImageView1(250);
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
            setupSchemeCardTarget1(imageView1);

            useButton.setOnMouseClicked(event -> {
                finalCoordinateX1 = tryParse(finalX1.getText());
                finalCoordinateY1 = tryParse(finalY1.getText());
                if (finalCoordinateX1 != null && finalCoordinateY1 != null && 0 <= finalCoordinateX1 && finalCoordinateX1 < 4 && 0 <= finalCoordinateY1 && finalCoordinateY1 < 5 && targetStartXForTools1 != GameBoardHandler.OUT_OF_RANGE && targetStartYForTools1 != GameBoardHandler.OUT_OF_RANGE) {
                    if (rmiController != null) {
                        try {
                            if (gameBoardHandlerSingle != null) {
                                if (sacrificeCheck()) {
                                    checkBooleanSingle(rmiController.useToolCard2or3(targetSacrificeDiceForTools, n, targetStartXForTools1, targetStartYForTools1, finalCoordinateX1, finalCoordinateY1, username, true), n);
                                }
                            } else {
                                checkBooleanMulti(rmiController.useToolCard2or3(OUT_OF_RANGE, n, targetStartXForTools1, targetStartYForTools1, finalCoordinateX1, finalCoordinateY1, username, false), n);
                            }
                        } catch (RemoteException e) {
                            System.out.println(CONNECTION_ERROR);
                            System.exit(0);
                        }
                    } else {
                        if (gameBoardHandlerSingle != null) {
                            if (sacrificeCheck()) {
                                socketController.request(new UseToolCard2or3Request(targetSacrificeDiceForTools, n, targetStartXForTools1, targetStartYForTools1, finalCoordinateX1, finalCoordinateY1, username, true));
                                checkBooleanSingle(waitForToolEffectAppliedResponse(), n);
                            }

                        } else {
                            socketController.request(new UseToolCard2or3Request(OUT_OF_RANGE, n, targetStartXForTools1, targetStartYForTools1, finalCoordinateX1, finalCoordinateY1, username, false));
                            checkBooleanMulti(waitForToolEffectAppliedResponse(), n);
                        }

                    }
                } else {
                    appendToTextArea("Non hai scelto alcun dado dalla carta schema o non hai settato correttamente le coordinate!");
                }
                resetToolValues();
            });

        }

        /**
         * Creates the context for using the tool card 4.
         * It shows the context pane with two boxes for the dices the player wants to move,
         * four text fields where the player puts the coordinates of the chosen dices new positions, and, if it's a
         * single player match, also a box for the dice to sacrifice.
         * When the USE button is clicked the effect of the tool card is applied, and a message is shown in the scene text area
         * informing the player about the outcome of the application of the effect.
         */
        private void createContext4() {
            setupSacrificeImageView();

            imageView1 = null;
            imageView2 = null;
            toolLabel.setVisible(true);
            toolPane.setVisible(true);
            useButton.setVisible(true);

            setImageView1(180);
            setImageView2(350);

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
            setupSchemeCardTarget1(imageView1);
            setupSchemeCardTarget2(imageView2);

            useButton.setOnMouseClicked(event -> {
                finalCoordinateX1 = tryParse(finalX1.getText());
                finalCoordinateY1 = tryParse(finalY1.getText());
                finalCoordinateX2 = tryParse(finalX2.getText());
                finalCoordinateY2 = tryParse(finalY2.getText());
                if (finalCoordinateX1 != null && finalCoordinateY1 != null && finalCoordinateX2 != null && finalCoordinateY2 != null && 0 <= finalCoordinateX1 && finalCoordinateX1 < 4 && 0 <= finalCoordinateY1 && finalCoordinateY1 < 5 && 0 <= finalCoordinateX2 && finalCoordinateX2 < 4 && 0 <= finalCoordinateY2 && finalCoordinateY2 < 5 && targetStartXForTools1 != GameBoardHandler.OUT_OF_RANGE && targetStartYForTools1 != GameBoardHandler.OUT_OF_RANGE && targetStartXForTools2 != GameBoardHandler.OUT_OF_RANGE && targetStartYForTools2 != GameBoardHandler.OUT_OF_RANGE) {
                    if (rmiController != null) {
                        try {
                            if (gameBoardHandlerSingle != null) {
                                if (sacrificeCheck()) {
                                    checkBooleanSingle(rmiController.useToolCard4(targetSacrificeDiceForTools, targetStartXForTools1, targetStartYForTools1, finalCoordinateX1, finalCoordinateY1, targetStartXForTools2, targetStartYForTools2, finalCoordinateX2, finalCoordinateY2, username, true), 4);
                                }
                            } else {
                                checkBooleanMulti(rmiController.useToolCard4(OUT_OF_RANGE, targetStartXForTools1, targetStartYForTools1, finalCoordinateX1, finalCoordinateY1, targetStartXForTools2, targetStartYForTools2, finalCoordinateX2, finalCoordinateY2, username, false), 4);
                            }
                        } catch (RemoteException e) {
                            System.out.println(CONNECTION_ERROR);
                            System.exit(0);
                        }
                    } else {
                        if (gameBoardHandlerSingle != null) {
                            if (sacrificeCheck()) {
                                socketController.request(new UseToolCard4Request(targetSacrificeDiceForTools, targetStartXForTools1, targetStartYForTools1, finalCoordinateX1, finalCoordinateY1, targetStartXForTools2, targetStartYForTools2, finalCoordinateX2, finalCoordinateY2, username, true));
                                checkBooleanSingle(waitForToolEffectAppliedResponse(), 4);
                            }
                        } else {
                            socketController.request(new UseToolCard4Request(OUT_OF_RANGE, targetStartXForTools1, targetStartYForTools1, finalCoordinateX1, finalCoordinateY1, targetStartXForTools2, targetStartYForTools2, finalCoordinateX2, finalCoordinateY2, username, false));
                            checkBooleanMulti(waitForToolEffectAppliedResponse(), 4);
                        }

                    }
                } else {
                    appendToTextArea("Non hai scelto alcun dado dalla carta schema o non hai settato correttamente le coordinate!");
                }
                resetToolValues();
            });
        }

        /**
         * Creates the context for using the tool card 5.
         * It shows the context pane with two boxes for the dices the player wants to switch, the upper one is for the
         * dice from the reserve, the lower one is for the dice from the round track.
         * If it's a single player match, it shows also a box for the dice to sacrifice.
         * When the USE button is clicked the effect of the tool card is applied, and a message is shown in the scene text area
         * informing the player about the outcome of the application of the effect.
         */
        private void createContext5() {
            setupSacrificeImageView();

            appendToTextArea("Stai cercando di usare la carta utensile 5." +
                    " Trascina nel riquadro in alto il dado della riserva e in basso quello del tracciato dei round ");
            imageView1 = null;
            imageView2 = null;
            toolLabel.setVisible(true);
            toolPane.setVisible(true);
            useButton.setVisible(true);
            setImageView1(180);
            setImageView2(300);
            gameBoard.getChildren().add(imageView1);
            gameBoard.getChildren().add(imageView2);

            setupReserveTarget(imageView1);
            setupRoundTrackTarget(imageView2);

            useButton.setOnMouseClicked(event -> {
                if (targetReserveIndexForTools != GameBoardHandler.OUT_OF_RANGE && targetRoundForTools != GameBoardHandler.OUT_OF_RANGE && targetDiceFromRoundForTools != GameBoardHandler.OUT_OF_RANGE) {
                    if (rmiController != null) {
                        try {
                            if (gameBoardHandlerSingle != null) {
                                if (sacrificeCheck()) {
                                    checkBooleanSingle(rmiController.useToolCard5(targetSacrificeDiceForTools, targetReserveIndexForTools, targetRoundForTools, targetDiceFromRoundForTools, username, true), 5);
                                }
                            } else {
                                checkBooleanMulti(rmiController.useToolCard5(OUT_OF_RANGE, targetReserveIndexForTools, targetRoundForTools, targetDiceFromRoundForTools, username, false), 5);

                            }
                        } catch (RemoteException e) {
                            System.out.println(CONNECTION_ERROR);
                            System.exit(0);
                        }
                    } else {
                        if (gameBoardHandlerSingle != null) {
                            if (sacrificeCheck()) {
                                socketController.request(new UseToolCard5Request(targetSacrificeDiceForTools, targetReserveIndexForTools, targetRoundForTools, targetDiceFromRoundForTools, username, true));
                                checkBooleanSingle(waitForToolEffectAppliedResponse(), 5);
                            }
                        } else {
                            socketController.request(new UseToolCard5Request(OUT_OF_RANGE, targetReserveIndexForTools, targetRoundForTools, targetDiceFromRoundForTools, username, false));
                            checkBooleanMulti(waitForToolEffectAppliedResponse(), 5);
                        }

                    }
                } else {
                    appendToTextArea("Non hai scelto alcun dado dalla riserva o dal tracciato dei round!");
                }
                resetToolValues();
            });

        }

        /**
         * Creates the context for using the tool card 6.
         * It shows the context pane with one box for the dice the player wants to re-roll.
         * If it's a single player match, it also shows a box for the dice to sacrifice.
         * When the USE button is clicked the effect of the tool card is applied, and a message is shown in the scene text area
         * informing the player about the outcome of the application of the effect.
         */
        private void createContext6() {
            setupSacrificeImageView();

            imageView1 = null;
            toolLabel.setVisible(true);
            toolPane.setVisible(true);
            useButton.setVisible(true);
            setImageView1(250);
            gameBoard.getChildren().add(imageView1);
            setupReserveTarget(imageView1);

            useButton.setOnMouseClicked(event -> {
                if (targetReserveIndexForTools != GameBoardHandler.OUT_OF_RANGE) {
                    //RMI
                    if (rmiController != null) {
                        try {
                            if (gameBoardHandlerSingle != null) {
                                if (sacrificeCheck() && targetSacrificeDiceForTools != targetReserveIndexForTools) {
                                    checkBooleanSingle(rmiController.useToolCard6(targetSacrificeDiceForTools, targetReserveIndexForTools, username, true), 6);
                                }
                            } else {
                                checkBooleanMulti(rmiController.useToolCard6(OUT_OF_RANGE, targetReserveIndexForTools, username, false), 6);
                            }
                        } catch (RemoteException e) {
                            System.out.println(CONNECTION_ERROR);
                            System.exit(0);
                        }
                    }
                    //SOCKET
                    else {
                        if (gameBoardHandlerSingle != null) {
                            if (sacrificeCheck() && targetSacrificeDiceForTools != targetReserveIndexForTools) {
                                socketController.request(new UseToolCard6Request(targetSacrificeDiceForTools, targetReserveIndexForTools, username, true));
                                checkBooleanSingle(waitForToolEffectAppliedResponse(), 6);
                            }
                        } else {
                            socketController.request(new UseToolCard6Request(OUT_OF_RANGE, targetReserveIndexForTools, username, false));
                            checkBooleanMulti(waitForToolEffectAppliedResponse(), 6);
                        }

                    }
                } else {
                    appendToTextArea("Non hai scelto alcun dado dalla riserva!");
                }
                resetToolValues();
            });
        }

        /**
         * Creates the context for using the tool cards 7 or 8.
         * It shows the context pane with the USE button.
         * If it's a single player match, it also shows a box for the dice to sacrifice.
         * When the USE button is clicked the effect of the tool card is applied, and a message is shown in the scene text area
         * informing the player about the outcome of the application of the effect.
         * @param n is the id of the tool card (7 or 8).
         */
        private void createContext7or8(int n) {
            setupSacrificeImageView();

            toolLabel.setVisible(true);
            toolPane.setVisible(true);
            useButton.setVisible(true);

            useButton.setOnMouseClicked(event -> {
                if (n == 7) {
                    if (rmiController != null) {
                        try {
                            if (diceChosen == GameBoardHandler.OUT_OF_RANGE) {
                                if (gameBoardHandlerSingle != null) {
                                    if (sacrificeCheck()) {
                                        checkBooleanSingle(rmiController.useToolCard7(targetSacrificeDiceForTools, username, true), 7);
                                    }
                                } else {
                                    checkBooleanMulti(rmiController.useToolCard7(OUT_OF_RANGE, username, false), 7);
                                }
                            } else {
                                appendToTextArea("Non puoi scegliere un dado prima di utilizzare questa carta utensile!");
                            }
                        } catch (RemoteException e) {
                            System.out.println(CONNECTION_ERROR);
                            System.exit(0);
                        }
                    } else {
                        if (diceChosen == GameBoardHandler.OUT_OF_RANGE) {
                            if (gameBoardHandlerSingle != null) {
                                if (sacrificeCheck()) {
                                    socketController.request(new UseToolCard7Request(targetSacrificeDiceForTools, username, true));
                                    checkBooleanSingle(waitForToolEffectAppliedResponse(), 7);
                                }
                            } else {
                                socketController.request(new UseToolCard7Request(OUT_OF_RANGE, username, false));
                                checkBooleanMulti(waitForToolEffectAppliedResponse(), 7);
                            }

                        } else {
                            appendToTextArea("Non puoi scegliere un dado prima di utilizzare questa carta utensile!");
                        }
                    }
                } else {
                    if (rmiController != null) {
                        try {
                            if (gameBoardHandlerSingle != null) {
                                if (sacrificeCheck()) {
                                    checkBooleanSingle(rmiController.useToolCard8(targetSacrificeDiceForTools, username, true), 8);
                                }
                            } else {
                                checkBooleanMulti(rmiController.useToolCard8(OUT_OF_RANGE, username, false), 8);

                            }
                        } catch (RemoteException e) {
                            System.out.println(CONNECTION_ERROR);
                            System.exit(0);
                        }
                    } else {
                        if (gameBoardHandlerSingle != null) {
                            if (sacrificeCheck()) {
                                socketController.request(new UseToolCard8Request(targetSacrificeDiceForTools, username, true));
                                checkBooleanSingle(waitForToolEffectAppliedResponse(), 8);
                            }
                        } else {
                            socketController.request(new UseToolCard8Request(OUT_OF_RANGE, username, false));
                            checkBooleanMulti(waitForToolEffectAppliedResponse(), 8);
                        }

                    }
                }
                resetToolValues();
            });
        }

        /**
         * Initializes the context for using the tool card 9.
         * It shows the context pane with a box for the chosen dice from the reserve and two text fields where the player
         * puts the coordinates of the dice new position.
         * If it's a single player match, it also shows a box for the dice to sacrifice.
         */
        private void initializeContext9() {
            imageView1 = null;
            toolLabel.setVisible(true);
            toolPane.setVisible(true);
            useButton.setVisible(true);
            setImageView1(250);

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
        }

        /**
         * Create the context for using the tool card 9.
         * When the USE button is clicked the effect of the tool card is applied, and a message is shown in the scene text area
         * informing the player about the outcome of the application of the effect.
         */
        private void createContext9() {
            setupSacrificeImageView();

            initializeContext9();

            useButton.setOnMouseClicked(event -> {
                finalCoordinateX1 = tryParse(finalX1.getText());
                finalCoordinateY1 = tryParse(finalY1.getText());
                if (finalCoordinateX1 != null && finalCoordinateY1 != null && 0 <= finalCoordinateX1 && finalCoordinateX1 < 4 && 0 <= finalCoordinateY1 && finalCoordinateY1 < 5 && targetReserveIndexForTools != GameBoardHandler.OUT_OF_RANGE) {
                    if (rmiController != null) {
                        try {
                            if (gameBoardHandlerSingle != null) {
                                if (sacrificeCheck()) {
                                    checkBooleanSingle(rmiController.useToolCard9(targetSacrificeDiceForTools, targetReserveIndexForTools, finalCoordinateX1, finalCoordinateY1, username, true), 9);
                                }
                            } else {
                                checkBooleanMulti(rmiController.useToolCard9(OUT_OF_RANGE, targetReserveIndexForTools, finalCoordinateX1, finalCoordinateY1, username, false), 9);
                            }
                        } catch (RemoteException e) {
                            System.out.println(CONNECTION_ERROR);
                            System.exit(0);
                        }
                    } else {
                        if (gameBoardHandlerSingle != null) {
                            if (sacrificeCheck()) {
                                socketController.request(new UseToolCard9Request(targetSacrificeDiceForTools, targetReserveIndexForTools, finalCoordinateX1, finalCoordinateY1, username, true));
                                checkBooleanSingle(waitForToolEffectAppliedResponse(), 9);
                            }
                        } else {
                            socketController.request(new UseToolCard9Request(OUT_OF_RANGE, targetReserveIndexForTools, finalCoordinateX1, finalCoordinateY1, username, false));
                            checkBooleanMulti(waitForToolEffectAppliedResponse(), 9);
                        }

                    }

                } else {
                    appendToTextArea("Non hai scelto alcun dado dalla riserva o non hai settato correttamente le coordinate!");
                }
                resetToolValues();
            });
        }

        /**
         * Create the context for using the tool card 10.
         * It shows the context pane with a box for the chosen dice to turn upside-down.
         * If it's a single player match, it also shows a box for the dice to sacrifice.
         * When the USE button is clicked the effect of the tool card is applied, and a message is shown in the scene text area
         * informing the player about the outcome of the application of the effect.
         */
        private void createContext10() {
            setupSacrificeImageView();

            imageView1 = null;
            toolLabel.setVisible(true);
            toolPane.setVisible(true);
            useButton.setVisible(true);
            setImageView1(250);
            gameBoard.getChildren().add(imageView1);
            setupReserveTarget(imageView1);

            useButton.setOnMouseClicked(event -> {
                if (targetReserveIndexForTools != GameBoardHandler.OUT_OF_RANGE) {
                    //RMI
                    if (rmiController != null) {
                        try {
                            if (gameBoardHandlerSingle != null) {
                                if (sacrificeCheck()) {
                                    checkBooleanSingle(rmiController.useToolCard10(targetSacrificeDiceForTools, targetReserveIndexForTools, username, true), 10);
                                }
                            } else {
                                checkBooleanMulti(rmiController.useToolCard10(OUT_OF_RANGE, targetReserveIndexForTools, username, false), 10);
                            }
                        } catch (RemoteException e) {
                            System.out.println(CONNECTION_ERROR);
                            System.exit(0);
                        }
                    }
                    //SOCKET
                    else {
                        if (gameBoardHandlerSingle != null) {
                            if (sacrificeCheck()) {
                                socketController.request(new UseToolCard10Request(targetSacrificeDiceForTools, targetReserveIndexForTools, username, true));
                                checkBooleanSingle(waitForToolEffectAppliedResponse(), 10);
                            }
                        } else {
                            socketController.request(new UseToolCard10Request(OUT_OF_RANGE, targetReserveIndexForTools, username, false));
                            checkBooleanMulti(waitForToolEffectAppliedResponse(), 10);
                        }

                    }

                } else {
                    appendToTextArea("Non hai scelto alcun dado dalla riserva!");
                }
                resetToolValues();
            });

        }

        /**
         * Create the first part of the context for using the tool card 11.
         * It shows the context pane with a box for the dice to put in the bag.
         * If it's a single player match, it also shows a box for the dice to sacrifice.
         * When the USE button is clicked a message is shown in the scene text area
         * informing the player about the possibility to use this tool card.
         */
        private void createContext11() {
            setupSacrificeImageView();

            imageView1 = null;
            toolLabel.setVisible(true);
            toolPane.setVisible(true);
            useButton.setVisible(true);
            setImageView1(250);
            gameBoard.getChildren().add(imageView1);
            setupReserveTarget(imageView1);

            useButton.setOnMouseClicked(event -> {
                if (targetReserveIndexForTools != GameBoardHandler.OUT_OF_RANGE) {
                    //RMI
                    if (rmiController != null) {
                        try {
                            if (gameBoardHandlerSingle != null) {
                                if (sacrificeCheck()) {
                                    if (rmiController.useToolCard11(targetSacrificeDiceForTools, targetReserveIndexForTools, username, true)) {
                                        resetToolValues();
                                        createContext11bis();
                                    } else {
                                        appendToTextArea("Carta utensile 11 non applicata, occhio ai tuoi segnalini o a come va utizzata!");
                                    }
                                }
                            } else {
                                if (rmiController.useToolCard11(OUT_OF_RANGE, targetReserveIndexForTools, username, false)) {
                                    resetToolValues();
                                    createContext11bis();
                                } else {
                                    appendToTextArea("Carta utensile 11 non applicata, occhio a non averla già utilizzata o a come va utizzata!");
                                }
                            }
                        } catch (RemoteException e) {
                            System.out.println(CONNECTION_ERROR);
                            System.exit(0);
                        }
                    }
                    //SOCKET
                    else {
                        if (gameBoardHandlerSingle != null) {
                            if (sacrificeCheck()) {
                                socketController.request(new UseToolCard11Request(targetSacrificeDiceForTools, targetReserveIndexForTools, username, true));
                            }
                        } else {
                            socketController.request(new UseToolCard11Request(OUT_OF_RANGE, targetReserveIndexForTools, username, false));
                        }
                        if (waitForToolEffectAppliedResponse()) {
                            resetToolValues();
                            createContext11bis();
                        } else {
                            appendToTextArea("Carta utensile 11 non applicata, occhio a come va utizzata!");
                        }
                    }
                } else {
                    appendToTextArea("Non hai scelto alcun dado dalla riserva!");
                }
            });
        }

        /**
         * Initializes variables used by context 12 for using the tool card 12.
         * It shows the context pane with three boxes, one for the dice from the round track and two for the dices from
         * the window pattern card, and four text fields for the coordinates of the two dices new positions.
         * If it's a single player match, it also shows a box for the dice to sacrifice.
         */
        private void initializeContext12() {
            imageView1 = null;
            imageView2 = null;
            imageView3 = null;
            toolLabel.setVisible(true);
            toolPane.setVisible(true);
            useButton.setVisible(true);
            imageView3 = new ImageView();
            imageView3.setImage(new Image(getClass().getResourceAsStream(DICE_IMAGES_PATH + "bianco.png")));
            imageView3.setFitWidth(70);
            imageView3.setFitHeight(70);
            imageView3.setLayoutX(63);
            imageView3.setLayoutY(140);
            setImageView1(240);
            setImageView2(360);

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
            setupSchemeCardTarget1(imageView1);
            setupSchemeCardTarget2(imageView2);
        }

        /**
         * Creates the context for using the tool card 12.
         * When the USE button is clicked the effect of the tool card is applied, and a message is shown in the scene text area
         * informing the player about the outcome of the application of the effect.
         */
        private void createContext12() {
            setupSacrificeImageView();

            initializeContext12();

            useButton.setOnMouseClicked(event -> {
                finalCoordinateX1 = tryParse(finalX1.getText());
                finalCoordinateY1 = tryParse(finalY1.getText());
                finalCoordinateX2 = tryParse(finalX2.getText());
                finalCoordinateY2 = tryParse(finalY2.getText());

                if (targetDiceFromRoundForTools != GameBoardHandler.OUT_OF_RANGE && targetRoundForTools != GameBoardHandler.OUT_OF_RANGE && finalCoordinateX1 != null && finalCoordinateY1 != null && finalCoordinateX2 != null && finalCoordinateY2 != null && 0 <= finalCoordinateX1 && finalCoordinateX1 < 4 && 0 <= finalCoordinateY1 && finalCoordinateY1 < 5 && 0 <= finalCoordinateX2 && finalCoordinateX2 < 4 && 0 <= finalCoordinateY2 && finalCoordinateY2 < 5 && targetStartXForTools1 != GameBoardHandler.OUT_OF_RANGE && targetStartYForTools1 != GameBoardHandler.OUT_OF_RANGE && targetStartXForTools2 != GameBoardHandler.OUT_OF_RANGE && targetStartYForTools2 != GameBoardHandler.OUT_OF_RANGE) {
                    if (rmiController != null) {
                        try {
                            if (gameBoardHandlerSingle != null) {
                                if (sacrificeCheck()) {
                                    checkBooleanSingle(rmiController.useToolCard12(targetSacrificeDiceForTools, targetRoundForTools, targetDiceFromRoundForTools, targetStartXForTools1, targetStartYForTools1, finalCoordinateX1, finalCoordinateY1, targetStartXForTools2, targetStartYForTools2, finalCoordinateX2, finalCoordinateY2, username, true), 12);
                                }
                            } else {
                                checkBooleanMulti(rmiController.useToolCard12(OUT_OF_RANGE, targetRoundForTools, targetDiceFromRoundForTools, targetStartXForTools1, targetStartYForTools1, finalCoordinateX1, finalCoordinateY1, targetStartXForTools2, targetStartYForTools2, finalCoordinateX2, finalCoordinateY2, username, false), 12);

                            }
                        } catch (RemoteException e) {
                            System.out.println(CONNECTION_ERROR);
                            System.exit(0);
                        }
                    } else {
                        if (gameBoardHandlerSingle != null) {
                            if (sacrificeCheck()) {
                                socketController.request(new UseToolCard12Request(targetSacrificeDiceForTools, targetRoundForTools, targetDiceFromRoundForTools, targetStartXForTools1, targetStartYForTools1, finalCoordinateX1, finalCoordinateY1, targetStartXForTools2, targetStartYForTools2, finalCoordinateX2, finalCoordinateY2, username, true));
                                checkBooleanSingle(waitForToolEffectAppliedResponse(), 12);
                            }
                        } else {
                            socketController.request(new UseToolCard12Request(OUT_OF_RANGE, targetRoundForTools, targetDiceFromRoundForTools, targetStartXForTools1, targetStartYForTools1, finalCoordinateX1, finalCoordinateY1, targetStartXForTools2, targetStartYForTools2, finalCoordinateX2, finalCoordinateY2, username, false));
                            checkBooleanMulti(waitForToolEffectAppliedResponse(), 12);
                        }

                    }

                } else if (targetDiceFromRoundForTools != GameBoardHandler.OUT_OF_RANGE && targetRoundForTools != GameBoardHandler.OUT_OF_RANGE && finalCoordinateX1 != null && finalCoordinateY1 != null && 0 <= finalCoordinateX1 && finalCoordinateX1 < 4 && 0 <= finalCoordinateY1 && finalCoordinateY1 < 5 && targetStartXForTools1 != GameBoardHandler.OUT_OF_RANGE && targetStartYForTools1 != GameBoardHandler.OUT_OF_RANGE && (finalCoordinateX2 == null || finalCoordinateY2 == null || 0 > finalCoordinateX2 || finalCoordinateX2 > 4 || 0 > finalCoordinateY2 || finalCoordinateY2 > 5 || targetStartXForTools2 == GameBoardHandler.OUT_OF_RANGE || targetStartYForTools2 == GameBoardHandler.OUT_OF_RANGE)) {
                    if (rmiController != null) {
                        try {
                            if (gameBoardHandlerSingle != null) {
                                if (sacrificeCheck()) {
                                    checkBooleanSingle(rmiController.useToolCard12(targetSacrificeDiceForTools, targetRoundForTools, targetDiceFromRoundForTools, targetStartXForTools1, targetStartYForTools1, finalCoordinateX1, finalCoordinateY1, -1, -1, -1, -1, username, true), 12);
                                }
                            } else {
                                checkBooleanMulti(rmiController.useToolCard12(OUT_OF_RANGE, targetRoundForTools, targetDiceFromRoundForTools, targetStartXForTools1, targetStartYForTools1, finalCoordinateX1, finalCoordinateY1, -1, -1, -1, -1, username, false), 12);
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (gameBoardHandlerSingle != null) {
                            if (sacrificeCheck()) {
                                socketController.request(new UseToolCard12Request(targetSacrificeDiceForTools, targetRoundForTools, targetDiceFromRoundForTools, targetStartXForTools1, targetStartYForTools1, finalCoordinateX1, finalCoordinateY1, -1, -1, -1, -1, username, true));
                                checkBooleanSingle(waitForToolEffectAppliedResponse(), 12);
                            }
                        } else {
                            socketController.request(new UseToolCard12Request(OUT_OF_RANGE, targetRoundForTools, targetDiceFromRoundForTools, targetStartXForTools1, targetStartYForTools1, finalCoordinateX1, finalCoordinateY1, -1, -1, -1, -1, username, false));
                            checkBooleanMulti(waitForToolEffectAppliedResponse(), 12);
                        }

                    }
                } else {
                    appendToTextArea("Non hai scelto alcun dado dalla carta schema, non hai scelto un dado dal round track o non hai settato correttamente le coordinate!");
                }
                resetToolValues();
            });
        }

        /**
         * Creates the second part of the context for using the tool card 11.
         * It shows the context pane with the image of the new dice picked from the bag (the dice is without value),
         * and three text fields, one to set the new value of the dice and the other two to set the coordinates of the
         * dice new position.
         * When the CONCLUDE button is clicked the effect of the tool card is applied, and a message is shown in the scene text area
         * informing the player about the outcome of the application of the effect.
         */
        private void createContext11bis() {
            toolLabel.setVisible(true);
            toolPane.setVisible(true);
            Colors color = null;
            if (rmiController != null) {
                try {
                    color = rmiController.askForDiceColor(username, single);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else {
                socketController.request(new DiceColorRequest(username, single));
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
                color = socketController.getDiceColor();
            }
            if (color != null) {
                appendToTextArea("Carta utensile 11 utilizzata correttamente! Il dado da te selezionato è stato inserito nel sacchetto! Ora puoi scegliere il valore del nuovo dado del colore  " + color.getDescription().toLowerCase() + " e piazzarlo! Se non concludi l'operazione ti verrà comunque addebitato il prezzo dei segnalini in quanto hai modificato lo stato della partita! Se sei in modalità partita singola non potrai più riutilizzare questa carta.");
            }
            imageView1 = new ImageView();
            imageView1.setFitWidth(70);
            imageView1.setFitHeight(70);
            imageView1.setLayoutX(63);
            imageView1.setLayoutY(250);
            assert color != null;
            imageView1.setImage(new Image(getClass().getResourceAsStream(DICE_IMAGES_PATH + color.getDescription().toLowerCase() + ".png")));
            concludeButton = new Button();
            concludeButton.setStyle("-fx-background-color: linear-gradient(lightgreen, lightseagreen)");
            concludeButton.setText("Concludi");
            if (gameBoardHandlerSingle != null) {
                concludeButton.setLayoutX(50);
                concludeButton.setLayoutY(510);
                concludeButton.setPrefHeight(40);
                concludeButton.setPrefWidth(100);
            } else {
                concludeButton.setLayoutX(63);
                concludeButton.setLayoutY(480);
            }
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

            concludeButton.setOnMouseClicked(event -> {

                value11 = tryParse(textField11.getText());
                finalCoordinateX1 = tryParse(finalX1.getText());
                finalCoordinateY1 = tryParse(finalY1.getText());

                if (value11 != null && finalCoordinateX1 != null && finalCoordinateY1 != null && value11 > 0 && value11 < 7 && 0 <= finalCoordinateX1 && finalCoordinateX1 < 4 && 0 <= finalCoordinateY1 && finalCoordinateY1 < 5) {
                    if (rmiController != null) {
                        try {
                            rmiController.setDiceValue(value11, username, single);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        try {
                            if (rmiController.placeDiceTool11(finalCoordinateX1, finalCoordinateY1, username, single)) {
                                appendToTextArea("Dado piazzato correttamente!");
                                concludeButton.setVisible(false);
                                resetToolValues();
                            } else {
                                appendToTextArea("Non puoi piazzare lì il tuo dado! Scegli altre coordinate!");
                            }
                        } catch (RemoteException e) {
                            System.out.println(CONNECTION_ERROR);
                            System.exit(0);
                        }
                    } else {
                        socketController.request(new SetDiceValueRequest(value11, username, single));
                        socketController.request(new PlaceDiceTool11Request(finalCoordinateX1, finalCoordinateY1, username, single));
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                        }
                        if (socketController.isDicePlaced()) {
                            socketController.setDicePlaced(false);//to reset the value
                            appendToTextArea("Dado piazzato correttamente!");
                            concludeButton.setVisible(false);
                            resetToolValues();
                        } else {
                            appendToTextArea("Non puoi piazzare lì il tuo dado! Scegli altre coordinate!");
                        }
                    }

                } else {
                    appendToTextArea("Non hai inserito un valore corretto oppure non hai settato correttamente le coordinate!");
                }
            });
        }

        private boolean waitForToolEffectAppliedResponse() {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }

            if (socketController.isEffectApplied()) {
                socketController.setEffectApplied(false);   //to reset the value
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

    /**
     * A message is shown in the scene text area informing the player in a multi player match about the outcome of the application of the effect.
     * @param done is true if the effect was applied successfully, false otherwise.
     * @param i is the id of the tool card.
     */
    private void checkBooleanMulti(boolean done, int i) {
        if (done) {
            appendToTextArea("Carta utensile " + i + " utilizzata correttamente!");

        } else {
            appendToTextArea("Carta utensile " + i + " non applicata, occhio ai tuoi segnalini o a come va utizzata!\n");
        }
    }

    /**
     * A message is shown in the scene text area informing the player in a single player match about the outcome of the application of the effect.
     * @param done is true if the effect was applied successfully, false otherwise.
     * @param i is the id of the tool card.
     */
    private void checkBooleanSingle(boolean done, int i) {
        if (done) {
            appendToTextArea("Carta utensile " + i + " utilizzata correttamente! Non potrai più utilizzarla per il resto della partita!");

        } else {
            appendToTextArea("Carta utensile " + i + " non applicata, occhio a non averla già utilizzata o a come va utizzata!\n");
        }
    }

    /**
     * In the tool card context Pane a white box is shown with a descriptive label.
     * In this box the player has to drop a dice from the reserve.
     */
    private void setupSacrificeImageView() {
        if (gameBoardHandlerSingle != null) {
            sacrificeLabel.setVisible(true);
            sacrificeImageView = null;
            sacrificeImageView = new ImageView();
            sacrificeImageView.setImage(new Image(getClass().getResourceAsStream(DICE_IMAGES_PATH + "bianco.png")));
            sacrificeImageView.setFitWidth(70);
            sacrificeImageView.setFitHeight(70);
            sacrificeImageView.setLayoutX(63);
            sacrificeImageView.setLayoutY(550);
            gameBoard.getChildren().add(sacrificeImageView);
            setupSacrificeTarget(sacrificeImageView);
        }
    }

    /**
     * When the PASS button is clicked all variables used in tool card context are set to their default values.
     * If the player clicks on the button during another player's turn, a message is appended in the scene text area.
     */
    void passButtonClicked() {
        diceChosen = OUT_OF_RANGE;
        resetToolValues();
        if (gui.isMyTurn()) {
            if (rmiController != null) {
                try {
                    rmiController.goThrough(username, single);
                } catch (RemoteException e) {
                    System.out.println(CONNECTION_ERROR);
                    System.exit(0);
                }
            } else {
                socketController.request(new GoThroughRequest(username, single));
            }
        } else {
            if (!single) {
                appendToTextArea("Non è il tuo turno!");
            }
        }
    }

    /**
     * When QUIT button is clicked an alert message is shown to the player.
     * If the player answers YES the window is closed and the quitting game process starts.
     */
    void quitClicked() {
        ButtonType yes = new ButtonType("SÌ", ButtonBar.ButtonData.YES);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Vuoi davvero uscire?", yes, ButtonType.NO);
        alert.setTitle("Uscita");
        alert.setHeaderText(null);
        alert.setResizable(false);
        alert.setGraphic(null);
        alert.showAndWait();
        if (alert.getResult() == yes) {
            window.close();
            if (rmiController != null) {
                try {
                    rmiController.quitGame(username, single);
                } catch (RemoteException e) {
                    System.out.println(CONNECTION_ERROR);
                    System.exit(0);
                }
            } else {
                socketController.request(new QuitGameRequest(username, single));
            }
            System.exit(0);
        }
    }

    /**
     * Checks if the player dropped a dice in the sacrifice dice box in tool card context.
     * @return true if a dice was placed correctly in the sacrifice box, false otherwise.
     */
    private boolean sacrificeCheck() {
        if (targetSacrificeDiceForTools != GameBoardHandler.OUT_OF_RANGE) {
            return true;
        } else {
            appendToTextArea("Non hai inserito alcun dado da sacrificare, o lo hai fatto in modo errato!\n");
            return false;
        }
    }

    /**
     * Sets the player's choice to the default value.
     */
    void setDiceChosenOutOfRange() {
        diceChosen = OUT_OF_RANGE;
    }
}