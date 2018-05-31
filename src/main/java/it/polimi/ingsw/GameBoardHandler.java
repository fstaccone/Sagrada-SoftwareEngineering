package it.polimi.ingsw;

import it.polimi.ingsw.control.RemoteController;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;

public class GameBoardHandler implements Initializable {

    private static final String PRIVATE_CARDS_PATH = "File:./src/main/java/it/polimi/ingsw/resources/private_objective_cards/";
    private static final String TOOLCARDS_PATH = "File:./src/main/java/it/polimi/ingsw/resources/toolcards/";
    private static final String DICE_IMAGES_PATH = "File:./src/main/java/it/polimi/ingsw/resources/dices/dice_";
    private static final String PUBLIC_CARDS_PATH = "File:./src/main/java/it/polimi/ingsw/resources/public_objective_cards/";
    private static final String FAVOR_TOKEN_PATH = "File:./src/main/java/it/polimi/ingsw/resources/other/favour.png";


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
    Pane pane1;
    @FXML
    Pane pane2;
    @FXML
    Pane pane3;
    @FXML
    GridPane grid1;
    @FXML
    GridPane grid2;
    @FXML
    GridPane grid3;
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

    private RemoteController controller;
    private String username;
    private Stage window;
    private RmiGui rmiGui;
    private int diceChosen;
    private List<String> reserveDices;
    private Button[][] windowPatternCard;
    private Map<String, Integer> otherFavorTokensMap;
    private Map<String, WindowPatternCard> otherSchemeCardsMap;
    private Map<Integer, Label> labels = new HashMap<>();

    public void createLabelsMap(){
        labels.put(0, label0);
        labels.put(1, label1);
        labels.put(2, label2);
        labels.put(3, label3);
    }

    private EventHandler<ActionEvent> reserveDiceSelected = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (rmiGui.isMyTurn()) {
                String s = event.getSource().toString();
                s = s.substring(21, 22);
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
                String s = event.getSource().toString().substring(14, 15);
                textArea.setText("You currently chose the toolcard " + s);
                int tool = Integer.parseInt(s);
                //USARE LA CARTA UTENSILE
            }
        }
    };

    private EventHandler<ActionEvent> windowPatternCardSlotSelected = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (rmiGui.isMyTurn()) {
                if (diceChosen != 9) {
                    Button slot = (Button) event.getSource();
                    Integer tempX = GridPane.getRowIndex(slot);
                    if (tempX == null) tempX = 0;
                    Integer tempY = GridPane.getColumnIndex(slot);
                    if (tempY == null) tempY = 0;
                    int coordinateX = tempX;
                    int coordinateY = tempY;
                    textArea.setText("Vuoi posizionare il dado: " + diceChosen + "nella posizione: " + coordinateX + "," + coordinateY);
                    try {
                        String url = DICE_IMAGES_PATH + reserveDices.get(diceChosen) + ".png";
                        if (controller.placeDice(diceChosen, coordinateX, coordinateY, username, false)) {
                            textArea.setText("Ben fatto! Il dado scelto è stato piazzato correttamente.");
                            putImage(url, coordinateX, coordinateY);
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

    public void init(Stage windowFromRmiGui, Scene sceneFromRmiGui, RemoteController controller, String username, RmiGui rmiGui) {
        diceChosen = 9;
        this.controller = controller;
        this.username = username;
        this.rmiGui = rmiGui;
        this.otherSchemeCardsMap = rmiGui.getOtherSchemeCardsMap();
        window = windowFromRmiGui;
        Platform.runLater(() -> {
            window.setScene(sceneFromRmiGui);
            window.setTitle("GameBoard");
            window.setResizable(false);
            window.show();
        });
        windowPatternCard = new Button[4][5];
        windowPatternCard[0][0] = b1;
        windowPatternCard[0][1] = b2;
        windowPatternCard[0][2] = b3;
        windowPatternCard[0][3] = b4;
        windowPatternCard[0][4] = b5;
        windowPatternCard[1][0] = b6;
        windowPatternCard[1][1] = b7;
        windowPatternCard[1][2] = b8;
        windowPatternCard[1][3] = b9;
        windowPatternCard[1][4] = b10;
        windowPatternCard[2][0] = b11;
        windowPatternCard[2][1] = b12;
        windowPatternCard[2][2] = b13;
        windowPatternCard[2][3] = b14;
        windowPatternCard[2][4] = b15;
        windowPatternCard[3][0] = b16;
        windowPatternCard[3][1] = b17;
        windowPatternCard[3][2] = b18;
        windowPatternCard[3][3] = b19;
        windowPatternCard[3][4] = b20;
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
        for (Button dicesRow[] : windowPatternCard) {
            for (Button dice : dicesRow) {
                dice.setOnAction(windowPatternCardSlotSelected);
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
        reserveDices = new ArrayList<>();
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

    public void putImage(String url, int x, int y) {
        Image diceImg = new Image(url);
        ImageView diceView = new ImageView(diceImg);
        diceView.setFitWidth(58);
        diceView.setFitHeight(54);
        Platform.runLater(() -> windowPatternCard[x][y].setGraphic(diceView));
    }

    public void deleteImage(int x, int y) {
        Platform.runLater(() -> windowPatternCard[x][y].setGraphic(null));
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
            myFavourTokensContainer.getChildren().remove(0, myFavourTokensContainer.getChildren().size());
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
        myFavourTokensContainer.getChildren().add(myFavourTokens);
    }

    // todo: chiamata ogni volta che viene notificata una nuova scheme card
    // todo: potrebbe essere utili discriminare i due casi in fase di invio, (carta settata, carta aggiornata)
    // in modo da chiamare questo metodo solo quando la carta è settata
    public void onOtherSchemeCards(WindowPatternCard window, String name) {
        // cerca tra tutte le label quella con il nome "name" e gli setta la window
    }

    public void initializeLabels(List<String> players) {
        int myPosition = 0;

        // find the position of the owner of this GUI
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).equals(username)) {
                myPosition = i;
                label0.setText(players.get(i));
                break;
            }
        }

        players.forEach(System.out::println);

        // assigns the name to the right label in order to show the correct flow clockwise
        for (int i = 1; i < players.size(); i++) {
           labels.get(i).setText(players.get((myPosition + i) % players.size()));
        }
    }
}
