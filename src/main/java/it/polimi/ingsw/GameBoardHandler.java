package it.polimi.ingsw;

import it.polimi.ingsw.control.RemoteController;
import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.model.gameobjects.Square;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;
import it.polimi.ingsw.socket.ClientController;
import it.polimi.ingsw.socket.requests.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameBoardHandler implements Initializable {

    private static final String PRIVATE_CARDS_PATH = "File:./src/main/java/it/polimi/ingsw/resources/private_objective_cards/";
    private static final String TOOLCARDS_PATH = "File:./src/main/java/it/polimi/ingsw/resources/toolcards/";
    private static final String DICE_IMAGES_PATH = "File:./src/main/java/it/polimi/ingsw/resources/dices/dice_";
    private static final String PUBLIC_CARDS_PATH = "File:./src/main/java/it/polimi/ingsw/resources/public_objective_cards/";
    private static final String FAVOR_TOKEN_PATH = "File:./src/main/java/it/polimi/ingsw/resources/other/favour.png";
    private static final String WINDOW_PATTERN_CARDS_PATH = "File:./src/main/java/it/polimi/ingsw/resources/window_pattern_card/";

    private int partialStartXForTools1 = 4;
    private int partialStartYForTools1 = 4;
    private int targetStartXForTools1 = 5;
    private int targetStartYForTools1 = 5;
    private int partialStartXForTools2 = 4;
    private int partialStartYForTools2 = 4;
    private int targetStartXForTools2 = 5;
    private int targetStartYForTools2 = 5;
    private int partialRoundForTools = 0;
    private int partialDiceFromRoundForTools = 9;
    private int targetRoundForTools = 0;
    private int targetDiceFromRoundForTools = 9;
    private int partialReserveIndexForTools = 9;
    private int targetReserveIndexForTools = 9;
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


    /* Useful for contexts */
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
    private Map<Integer, Label> labels = new HashMap<>();
    private Map<Integer, Pane> favorTokensContainers = new HashMap<>();
    private GridPane schemeCard = new GridPane();

    private List<Label> labelsList = new ArrayList<>();

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

    public void createFavorTokensContainers() {
        favorTokensContainers.put(0, myFavourTokensContainer);
        favorTokensContainers.put(1, favourTokensContainer1);
        favorTokensContainers.put(2, favourTokensContainer2);
        favorTokensContainers.put(3, favourTokensContainer3);
    }

    private EventHandler<MouseEvent> reserveDiceSelected = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (gui.isMyTurn()) {
                String s = event.getSource().toString();
                System.out.println(s);
                s = s.substring(13, 14);
                int i = Integer.parseInt(s);
                s = gui.getDicesList().get(i);
                diceChosen = i;
                textArea.setText("Hai scelto il dado: " + s + " pos: " + i);
            }
        }
    };

    private EventHandler<ActionEvent> toolSelected = new EventHandler<ActionEvent>() {


        @Override
        public void handle(ActionEvent event) {
            if (gui.isMyTurn()) {

                System.out.println("---------------- " + event.getSource());
                String s = event.getSource().toString().substring(14, 15);

                System.out.println("Valore della toolcard " + s);

                textArea.setText("You currently chose the toolcard " + s);
                int tool = Integer.parseInt(s);
                String name = gui.getToolCardsList().get(tool);
                name = name.replaceAll("tool", "");
                int number = Integer.parseInt(name);


                resetToolValues();
                createToolContext(number);
            }
        }
    };


    private void createContext1() {
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
                if (targetReserveIndexForTools != 9 && incrOrDecr != null && (incrOrDecr.equals("+") || incrOrDecr.equals("-"))) {
                    //RMI
                    if (remoteController != null) {
                        try {

                            if (remoteController.useToolCard1(targetReserveIndexForTools, incrOrDecr, username, false)) {
                                textArea.setText("Carta utensile 1 utilizzata correttamente!");
                            } else {
                                textArea.setText("Carta utensile 1 non applicata, occhio ai tuoi segnalini o a come va utizzata!");
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    //SOCKET
                    else {
                        clientController.request(new UseToolCard1Request(targetReserveIndexForTools, incrOrDecr, username, false));
                        if (waitForToolEffectAppliedResponse()) {
                            textArea.setText("Carta utensile 1 utilizzata correttamente!");
                        } else {
                            textArea.setText("Carta utensile 1 non applicata, occhio ai tuoi segnalini o a come va utizzata!");
                        }
                    }

                } else {
                    textArea.setText("Non hai scelto alcun dado dalla riserva o non hai cliccato '+' o '-'!");
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
                if (finalCoordinateX1 != null && finalCoordinateY1 != null && 0 <= finalCoordinateX1 && finalCoordinateX1 < 4 && 0 <= finalCoordinateY1 && finalCoordinateY1 < 5 && targetStartXForTools1 != 4 && targetStartYForTools1 != 5) {
                    if (remoteController != null) {
                        try {
                            System.out.println(targetStartXForTools1);
                            System.out.println(targetStartYForTools1);
                            System.out.println(finalCoordinateX1);
                            System.out.println(finalCoordinateY1);

                            if (remoteController.useToolCard2or3(n, targetStartXForTools1, targetStartYForTools1, finalCoordinateX1, finalCoordinateY1, username, false)) {
                                textArea.setText("Carta utensile " + n + " utilizzata correttamente!");
                            } else {
                                textArea.setText("Carta utensile " + n + " non applicata, occhio ai tuoi segnalini o a come va utizzata!");

                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    } else {
                        clientController.request(new UseToolCard2or3Request(n, targetStartXForTools1, targetStartYForTools1, finalCoordinateX1, finalCoordinateY1, username, false));
                        if (waitForToolEffectAppliedResponse()) {
                            textArea.setText("Carta utensile " + n + " utilizzata correttamente!");
                        } else {
                            textArea.setText("Carta utensile " + n + " non applicata, occhio ai tuoi segnalini o a come va utizzata!");
                        }
                    }

                } else {
                    textArea.setText("Non hai scelto alcun dado dalla carta schema o non hai settato correttamente le coordinate!");
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
                if (finalCoordinateX1 != null && finalCoordinateY1 != null && finalCoordinateX2 != null && finalCoordinateY2 != null && 0 <= finalCoordinateX1 && finalCoordinateX1 < 4 && 0 <= finalCoordinateY1 && finalCoordinateY1 < 5 && 0 <= finalCoordinateX2 && finalCoordinateX2 < 4 && 0 <= finalCoordinateY2 && finalCoordinateY2 < 5 && targetStartXForTools1 != 4 && targetStartYForTools1 != 5 && targetStartXForTools2 != 4 && targetStartYForTools2 != 5) {
                    if (remoteController != null) {
                        try {


                            if (remoteController.useToolCard4(targetStartXForTools1, targetStartYForTools1, finalCoordinateX1, finalCoordinateY1, targetStartXForTools2, targetStartYForTools2, finalCoordinateX2, finalCoordinateY2, username, false)) {
                                textArea.setText("Carta utensile 4 utilizzata correttamente!");
                            } else {
                                textArea.setText("Carta utensile 4 non applicata, occhio ai tuoi segnalini o a come va utizzata!");

                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    } else {
                        clientController.request(new UseToolCard4Request(targetStartXForTools1, targetStartYForTools1, finalCoordinateX1, finalCoordinateY1, targetStartXForTools2, targetStartYForTools2, finalCoordinateX2, finalCoordinateY2, username, false));
                        if (waitForToolEffectAppliedResponse()) {
                            textArea.setText("Carta utensile 4 utilizzata correttamente!");
                        } else {
                            textArea.setText("Carta utensile 4 non applicata, occhio ai tuoi segnalini o a come va utizzata!");
                        }
                    }

                } else {
                    textArea.setText("Non hai scelto alcun dado dalla carta schema o non hai settato correttamente le coordinate!");
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
                if (targetReserveIndexForTools != 9 && targetRoundForTools != 0 && targetDiceFromRoundForTools != 9) {
                    if (remoteController != null) {
                        try {
                            if (remoteController.useToolCard5(targetReserveIndexForTools, targetRoundForTools, targetDiceFromRoundForTools, username, false)) {

                                textArea.setText("Carta utensile 5 utilizzata correttamente!");
                            } else {
                                textArea.setText("Carta utensile 5 non applicata, occhio ai tuoi segnalini o a come va utizzata!");
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    } else {
                        clientController.request(new UseToolCard5Request(targetReserveIndexForTools, targetRoundForTools, targetDiceFromRoundForTools, username, false));
                        if (waitForToolEffectAppliedResponse()) {
                            textArea.setText("Carta utensile 5 utilizzata correttamente!");
                        } else {
                            textArea.setText("Carta utensile 5 non applicata, occhio ai tuoi segnalini o a come va utizzata!");
                        }
                    }
                } else {
                    textArea.setText("Non hai scelto alcun dado dalla riserva o dal tracciato dei round!");
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
                if (targetReserveIndexForTools != 9) {
                    //RMI
                    if (remoteController != null) {
                        try {

                            if (remoteController.useToolCard6(targetReserveIndexForTools, username, false)) {
                                textArea.setText("Carta utensile 6 utilizzata correttamente!");
                            } else {
                                textArea.setText("Carta utensile 6 non applicata, occhio ai tuoi segnalini o a come va utizzata!");
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    //SOCKET
                    else {
                        clientController.request(new UseToolCard6Request(targetReserveIndexForTools, username, false));
                        if (waitForToolEffectAppliedResponse()) {
                            textArea.setText("Carta utensile 6 utilizzata correttamente!");
                        } else {
                            textArea.setText("Carta utensile 6 non applicata, occhio ai tuoi segnalini o a come va utizzata!");
                        }
                    }

                } else {
                    textArea.setText("Non hai scelto alcun dado dalla riserva!");
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
                            if (diceChosen == 9) {
                                if (remoteController.useToolCard7(username, false)) { //VANNO SETTATI CORRETTAMENTE I PARAMETRI
                                    textArea.appendText("Carta utensile 7 utilizzata correttamente!\n");
                                } else {
                                    textArea.appendText("Carta utensile 7 non applicata, occhio ai tuoi segnalini o a come va utizzata!\n");
                                }
                            }else{
                                textArea.appendText("Non puoi scegliere un dado prima di utilizzare questa carta utensile!\n");
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    } else {
                        clientController.request(new UseToolCard7Request(username, false));
                        if (waitForToolEffectAppliedResponse()) {
                            textArea.appendText("Carta utensile 7 utilizzata correttamente!\n");
                        } else {
                            textArea.appendText("Carta utensile 7 non applicata, occhio ai tuoi segnalini o a come va utizzata!\n");
                        }
                    }
                } else {
                    if (remoteController != null) {
                        try {
                            if (remoteController.useToolCard8(username, false)) { //VANNO SETTATI CORRETTAMENTE I PARAMETRI
                                textArea.appendText("Carta utensile 8 utilizzata correttamente!\n");
                            } else {
                                textArea.appendText("Carta utensile 8 non applicata, occhio ai tuoi segnalini o a come va utizzata!\n");
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    } else {
                        clientController.request(new UseToolCard8Request(username, false));
                        if (waitForToolEffectAppliedResponse()) {
                            textArea.appendText("Carta utensile 8 utilizzata correttamente!\n");
                        } else {
                            textArea.appendText("Carta utensile 8 non applicata, occhio ai tuoi segnalini o a come va utizzata!\n");
                        }
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
                if (finalCoordinateX1 != null && finalCoordinateY1 != null && 0 <= finalCoordinateX1 && finalCoordinateX1 < 4 && 0 <= finalCoordinateY1 && finalCoordinateY1 < 5 && targetReserveIndexForTools != 9) {
                    if (remoteController != null) {
                        try {
                            if (remoteController.useToolCard9(targetReserveIndexForTools, finalCoordinateX1, finalCoordinateY1, username, false)) {
                                textArea.setText("Carta utensile 9 utilizzata correttamente!");
                            } else {
                                textArea.setText("Carta utensile 9 non applicata, occhio ai tuoi segnalini o a come va utizzata!");

                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    } else {
                        clientController.request(new UseToolCard9Request(targetReserveIndexForTools, finalCoordinateX1, finalCoordinateY1, username, false));
                        if (waitForToolEffectAppliedResponse()) {
                            textArea.setText("Carta utensile 9 utilizzata correttamente!");
                        } else {
                            textArea.setText("Carta utensile 9 non applicata, occhio ai tuoi segnalini o a come va utizzata!");
                        }
                    }

                } else {
                    textArea.setText("Non hai scelto alcun dado dalla riserva o non hai settato correttamente le coordinate!");
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
                if (targetReserveIndexForTools != 9) {
                    //RMI
                    if (remoteController != null) {
                        try {

                            if (remoteController.useToolCard10(targetReserveIndexForTools, username, false)) {
                                textArea.setText("Carta utensile 10 utilizzata correttamente!");
                            } else {
                                textArea.setText("Carta utensile 10 non applicata, occhio ai tuoi segnalini o a come va utizzata!");
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    //SOCKET
                    else {
                        clientController.request(new UseToolCard10Request(targetReserveIndexForTools, username, false));
                        if (waitForToolEffectAppliedResponse()) {
                            textArea.setText("Carta utensile 10 utilizzata correttamente!");
                        } else {
                            textArea.setText("Carta utensile 10 non applicata, occhio ai tuoi segnalini o a come va utizzata!");
                        }
                    }

                } else {
                    textArea.setText("Non hai scelto alcun dado dalla riserva!");
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
                if (targetReserveIndexForTools != 9) {
                    //RMI
                    if (remoteController != null) {
                        try {

                            if (remoteController.useToolCard11(targetReserveIndexForTools, username, false)) {
                                resetToolValues();
                                createContext11bis();

                            } else {
                                textArea.setText("Carta utensile 11 non applicata, occhio ai tuoi segnalini o a come va utizzata!");
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    //SOCKET
                    else {
                        clientController.request(new UseToolCard11Request(targetReserveIndexForTools, username, false));
                        if (waitForToolEffectAppliedResponse()) {
                            resetToolValues();
                            createContext11bis();
                        } else {
                            textArea.setText("Carta utensile 11 non applicata, occhio ai tuoi segnalini o a come va utizzata!");
                        }
                    }
                } else {
                    textArea.setText("Non hai scelto alcun dado dalla riserva!");
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

                System.out.println(targetDiceFromRoundForTools);
                System.out.println(targetRoundForTools);
                System.out.println(finalCoordinateX1);
                System.out.println(finalCoordinateY1);
                System.out.println(finalCoordinateX2);
                System.out.println(finalCoordinateY2);
                System.out.println(targetStartXForTools1);
                System.out.println(targetStartYForTools1);
                System.out.println(targetStartXForTools2);
                System.out.println(targetStartYForTools2);


                if (targetDiceFromRoundForTools != 9 && targetRoundForTools != 0 && finalCoordinateX1 != null && finalCoordinateY1 != null && finalCoordinateX2 != null && finalCoordinateY2 != null && 0 <= finalCoordinateX1 && finalCoordinateX1 < 4 && 0 <= finalCoordinateY1 && finalCoordinateY1 < 5 && 0 <= finalCoordinateX2 && finalCoordinateX2 < 4 && 0 <= finalCoordinateY2 && finalCoordinateY2 < 5 && targetStartXForTools1 != 4 && targetStartYForTools1 != 5 && targetStartXForTools2 != 4 && targetStartYForTools2 != 5) {
                    if (remoteController != null) {
                        try {


                            if (remoteController.useToolCard12(targetRoundForTools, targetDiceFromRoundForTools, targetStartXForTools1, targetStartYForTools1, finalCoordinateX1, finalCoordinateY1, targetStartXForTools2, targetStartYForTools2, finalCoordinateX2, finalCoordinateY2, username, false)) {
                                textArea.setText("Carta utensile 12 utilizzata correttamente!");
                            } else {
                                textArea.setText("Carta utensile 12 non applicata, occhio ai tuoi segnalini o a come va utizzata!");

                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    } else {
                        clientController.request(new UseToolCard12Request(targetRoundForTools, targetDiceFromRoundForTools, targetStartXForTools1, targetStartYForTools1, finalCoordinateX1, finalCoordinateY1, targetStartXForTools2, targetStartYForTools2, finalCoordinateX2, finalCoordinateY2, username, false));
                        if (waitForToolEffectAppliedResponse()) {
                            textArea.setText("Carta utensile 12 utilizzata correttamente per entrambi i dadi!");
                        } else {
                            textArea.setText("Carta utensile 12 non applicata, occhio ai tuoi segnalini o a come va utizzata!");
                        }
                    }

                } else if (targetReserveIndexForTools != 9 && targetRoundForTools != 0 && finalCoordinateX1 != null && finalCoordinateY1 != null && 0 <= finalCoordinateX1 && finalCoordinateX1 < 4 && 0 <= finalCoordinateY1 && finalCoordinateY1 < 5 && targetStartXForTools1 == 4 && targetStartYForTools1 != 5 && (finalCoordinateX2 == null || finalCoordinateY2 == null || 0 > finalCoordinateX2 || finalCoordinateX2 < 4 || 0 > finalCoordinateY2 || finalCoordinateY2 > 5 || targetStartXForTools2 == 4 || targetStartYForTools2 == 5)) {

                    if (remoteController != null) {
                        try {


                            if (remoteController.useToolCard12(targetRoundForTools, targetDiceFromRoundForTools, targetStartXForTools1, targetStartYForTools1, finalCoordinateX1, finalCoordinateY1, -1, -1, -1, -1, username, false)) {
                                textArea.setText("Carta utensile 12 utilizzata correttamente per un solo dado!");
                            } else {
                                textArea.setText("Carta utensile 12 non applicata, occhio ai tuoi segnalini o a come va utizzata!");

                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    } else {
                        clientController.request(new UseToolCard12Request(targetRoundForTools, targetDiceFromRoundForTools, targetStartXForTools1, targetStartYForTools1, finalCoordinateX1, finalCoordinateY1, -1, -1, -1, -1, username, false));
                        if (waitForToolEffectAppliedResponse()) {
                            textArea.setText("Carta utensile 12 utilizzata correttamente!");
                        } else {
                            textArea.setText("Carta utensile 12 non applicata, occhio ai tuoi segnalini o a come va utizzata!");
                        }
                    }
                } else {
                    textArea.setText("Non hai scelto alcun dado dalla carta schema, non hai scelto un dado dal round track o non hai settato correttamente le coordinate!");
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
            }
            color = clientController.getDiceColor();
        }
        textArea.setText("Carta utensile 11 utilizzata correttamente! Il dado da te selezionato è stato inserito nel sacchetto! Ora puoi scegliere il valore del nuovo dado del colore  " + color.toString() + " e piazzarlo! Se non concludi l'operazione ti verrà comunque addebitato il prezzo dei segnalini in quanto hai modificato lo stato della partita!");
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
                                textArea.setText("Dado piazzato correttamente!");
                                concludeButton.setVisible(false);
                                resetToolValues();
                            } else {
                                textArea.setText("Non puoi piazzare lì il tuo dado! Scegli altre coordinate!");
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
                        }
                        if (clientController.isDicePlaced()) {
                            clientController.setDicePlaced(false);//to reset the value
                            textArea.setText("Dado piazzato correttamente!");
                        } else {
                            textArea.setText("Non puoi piazzare lì il tuo dado! Scegli altre coordinate!");
                        }
                    }

                } else {
                    textArea.setText("Non hai inserito un valore corretto oppure non hai settato correttamente le coordinate!");
                }
            }
        });
    }

    private void resetToolValues() {
        partialReserveIndexForTools = 9;
        targetReserveIndexForTools = 9;
        partialDiceFromRoundForTools = 9;
        targetDiceFromRoundForTools = 9;
        partialStartXForTools1 = 4;
        targetStartXForTools1 = 4;
        partialStartYForTools1 = 5;
        targetStartYForTools1 = 5;
        partialStartXForTools2 = 4;
        targetStartXForTools2 = 4;
        partialStartYForTools2 = 5;
        targetStartYForTools2 = 5;
        partialRoundForTools = 0;
        targetRoundForTools = 0;
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
    }


    private void setupSchemeCardSource(ImageView source) {

        source.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                source.setCursor(Cursor.HAND);
            }
        });

        source.setOnDragDetected(new EventHandler<MouseEvent>() {
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
                targetReserveIndexForTools = partialReserveIndexForTools;

                event.consume();
            }
        });
    }

    private void setupSchemeCardTarget(ImageView target) {
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
                targetRoundForTools = partialRoundForTools;
                targetDiceFromRoundForTools = partialDiceFromRoundForTools;

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

                createContext7or8(7);

            }
            break;

            case 8: {

                createContext7or8(8);

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

            default: {
                System.out.println("Carta non presente!");
            }
        }
    }

    private EventHandler<MouseEvent> windowPatternCardSlotSelected = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {
            if (gui.isMyTurn()) {
                if (diceChosen != 9) {
                    ImageView slot = (ImageView) event.getSource();
                    Integer tempX = GridPane.getRowIndex(slot);
                    if (tempX == null) tempX = 0;
                    Integer tempY = GridPane.getColumnIndex(slot);
                    if (tempY == null) tempY = 0;
                    int coordinateX = tempX;
                    int coordinateY = tempY;
                    textArea.setText("Vuoi posizionare il dado: " + diceChosen + "nella posizione: " + coordinateX + "," + coordinateY);
                    if (remoteController != null) {
                        try {
                            if (remoteController.placeDice(diceChosen, coordinateX, coordinateY, username, false)) {
                                textArea.setText("Ben fatto! Il dado scelto è stato piazzato correttamente.");
                                diceChosen = 9; //FIRST VALUE NEVER PRESENT IN THE RESERVE
                            } else {
                                textArea.setText("ATTENZIONE: Hai provato a piazzare un dado dove non dovresti, o non puoi più piazzare dadi in questo turno!");
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    } else {
                        clientController.request(new PlaceDiceRequest(diceChosen, coordinateX, coordinateY, username, false));
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (clientController.isDicePlaced()) {
                            clientController.setDicePlaced(false);//to reset the value
                            textArea.setText("Ben fatto! Il dado scelto è stato piazzato correttamente.");
                            diceChosen = 9;
                        } else {
                            textArea.setText("ATTENZIONE: Hai provato a piazzare un dado dove non dovresti, o non puoi più piazzare dadi in questo turno!");
                        }
                    }
                }

            }
        }
    };


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void init(Scene scene, Gui gui) {
        diceChosen = 9;
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

        Platform.runLater(() -> label0.setText(username.toUpperCase()));
        label1.setText(" ");
        label2.setText(" ");
        label3.setText(" ");
        useButton.setVisible(false);
        toolLabel.setVisible(false);
        toolPane.setVisible(false);
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
        tool0.setOnMouseEntered(event -> {
            tool0.setTranslateX(20);
            tool0.setTranslateY(-60);
            tool0.setStyle("-fx-scale-x: 1.5;-fx-scale-y: 1.5");
            tool1.setVisible(false);
            tool2.setVisible(false);
            toolCardsLabel.setVisible(false);
        });
        tool0.setOnMouseExited(event -> {
            tool0.setTranslateX(0);
            tool0.setTranslateY(0);
            tool0.setStyle("-fx-scale-x: 1.0;-fx-scale-y: 1.0");
            tool1.setVisible(true);
            tool2.setVisible(true);
            toolCardsLabel.setVisible(true);
        });
        //Initializing toolCard1
        String url1 = TOOLCARDS_PATH + toolCardsList.get(1) + ".png";
        Image cardImg1 = new Image(url1);
        ImageView cardView1 = new ImageView(cardImg1);
        cardView1.setFitWidth(158);
        cardView1.setFitHeight(240);
        Platform.runLater(() -> tool1.setGraphic(cardView1));
        tool1.setOnAction(toolSelected);
        tool1.setOnMouseEntered(event -> {
            tool1.setTranslateY(-60);
            tool1.setStyle("-fx-scale-x: 1.5;-fx-scale-y: 1.5");
            tool0.setVisible(false);
            tool2.setVisible(false);
        });
        tool1.setOnMouseExited(event -> {
            tool1.setTranslateY(0);
            tool1.setStyle("-fx-scale-x: 1.0;-fx-scale-y: 1.0");
            tool0.setVisible(true);
            tool2.setVisible(true);
        });
        //Initializing toolCard2
        String url2 = TOOLCARDS_PATH + toolCardsList.get(2) + ".png";
        Image cardImg2 = new Image(url2);
        ImageView cardView2 = new ImageView(cardImg2);
        cardView2.setFitWidth(158);
        cardView2.setFitHeight(240);
        Platform.runLater(() -> tool2.setGraphic(cardView2));
        tool2.setOnAction(toolSelected);
        tool2.setOnMouseEntered(event -> {
            tool2.setTranslateY(-60);
            tool2.setStyle("-fx-scale-x: 1.5;-fx-scale-y: 1.5");
            tool0.setVisible(false);
            tool1.setVisible(false);
            toolCardsLabel.setVisible(false);
        });
        tool2.setOnMouseExited(event -> {
            tool2.setTranslateY(0);
            tool2.setStyle("-fx-scale-x: 1.0;-fx-scale-y: 1.0");
            tool0.setVisible(true);
            tool1.setVisible(true);
            toolCardsLabel.setVisible(true);
        });
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
        Insets insets = new Insets(4, 4, 4, 4);
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

    @FXML
    public void onPassButtonClicked() {
        resetToolValues();
        if (gui.isMyTurn()) {
            if (remoteController != null) {
                try {
                    remoteController.goThrough(username, false);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else {
                clientController.request(new GoThroughRequest(username, false));
            }
        } else {
            textArea.appendText("Non è il tuo turno!\n");
        }
    }

    public void setTextArea(String s) {
        textArea.appendText(s+"\n");
    }

    @FXML
    public void onQuitClicked() {
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

    public void setPrivateCard(String privateCard) {
        Image privateObjCardImg = new Image(PRIVATE_CARDS_PATH + privateCard + ".png");
        privateObjCard.setImage(privateObjCardImg);
        privateObjCard.setOnMouseEntered(event -> {
            privObjLabel.setVisible(false);
            privateObjCard.setStyle("-fx-scale-x: 2.0;-fx-scale-y: 2.0");
            privateObjCard.setTranslateX(-10);
            privateObjCard.setTranslateY(-70);
        });
        privateObjCard.setOnMouseExited(event -> {
            privObjLabel.setVisible(true);
            privateObjCard.setStyle("-fx-scale-x: 1.0;-fx-scale-y: 1.0");
            privateObjCard.setTranslateX(0);
            privateObjCard.setTranslateY(0);

        });
    }

    public void setPublicCards(List<String> publicCards) {
        Image publicObjCardImg1 = new Image(PUBLIC_CARDS_PATH + publicCards.get(0) + ".png");
        pubObjCard1.setImage(publicObjCardImg1);
        pubObjCard1.setOnMouseEntered(event -> {
            pubObjCard1.setTranslateX(40);
            pubObjCard1.setTranslateY(-60);
            pubObjCard1.setStyle("-fx-scale-x: 2.0;-fx-scale-y: 2.0");
            pubObjCard2.setVisible(false);
            pubObjCard3.setVisible(false);
            pubObjLabel.setVisible(false);
        });
        pubObjCard1.setOnMouseExited(event -> {
            pubObjCard1.setTranslateX(0);
            pubObjCard1.setTranslateY(0);
            pubObjCard1.setStyle("-fx-scale-x: 1.0;-fx-scale-y: 1.0");
            pubObjCard2.setVisible(true);
            pubObjCard3.setVisible(true);
            pubObjLabel.setVisible(true);
        });
        Image publicObjCardImg2 = new Image(PUBLIC_CARDS_PATH + publicCards.get(1) + ".png");
        pubObjCard2.setImage(publicObjCardImg2);
        pubObjCard2.setOnMouseEntered(event -> {
            pubObjCard2.setTranslateY(-60);
            pubObjCard2.setStyle("-fx-scale-x: 2.0;-fx-scale-y: 2.0");
            pubObjCard3.setVisible(false);
            pubObjCard1.setVisible(false);
            privateObjCard.setVisible(false);
            privObjLabel.setVisible(false);
            pubObjLabel.setVisible(false);
        });
        pubObjCard2.setOnMouseExited(event -> {
            pubObjCard2.setTranslateY(0);
            pubObjCard2.setStyle("-fx-scale-x: 1.0;-fx-scale-y: 1.0");
            pubObjCard3.setVisible(true);
            pubObjCard1.setVisible(true);
            privateObjCard.setVisible(true);
            privObjLabel.setVisible(true);
            pubObjLabel.setVisible(true);
        });
        Image publicObjCardImg3 = new Image(PUBLIC_CARDS_PATH + publicCards.get(2) + ".png");
        pubObjCard3.setImage(publicObjCardImg3);
        pubObjCard3.setOnMouseEntered(event -> {
            pubObjCard3.setTranslateX(-10);
            pubObjCard3.setTranslateY(-60);
            pubObjCard3.setStyle("-fx-scale-x: 2.0;-fx-scale-y: 2.0");
            pubObjCard2.setVisible(false);
            pubObjCard1.setVisible(false);
            privateObjCard.setVisible(false);
            privObjLabel.setVisible(false);
            pubObjLabel.setVisible(false);
        });
        pubObjCard3.setOnMouseExited(event -> {
            pubObjCard3.setTranslateX(0);
            pubObjCard3.setTranslateY(0);
            pubObjCard3.setStyle("-fx-scale-x: 1.0;-fx-scale-y: 1.0");
            pubObjCard2.setVisible(true);
            pubObjCard1.setVisible(true);
            privateObjCard.setVisible(true);
            privObjLabel.setVisible(true);
            pubObjLabel.setVisible(true);
        });
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
                    setupSchemeCardSource((imgView));

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


    public void onOtherSchemeCards(WindowPatternCard window, String name) {

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
    }

    /**
     * It sets the names contained in players in the right label over scheme cards clockwise starting from the position
     * of the player owner of this GUI. It maintains the playing order randomly chosen during the initialization of the match
     *
     * @param players is a list of Strings which are the names of the players invlolved in the match
     */
    public void initializeLabels(List<String> players) {

        //FOR RMI CONNECTION
        if (remoteController != null) {
            setLabels(players);
        }
        //FOR SOCKET CONNECTION
        else {
            Platform.runLater(() -> {
                setLabels(players);
            });
        }
    }

    private void setLabels(List<String> players) {
        AtomicInteger myPosition = new AtomicInteger();
        /* find the position of the owner of this GUI */
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).equals(username)) {
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

    public void initializeActions() {
        gui.setDicePlaced(false);
        // todo: aggiungere altre azioni da compiere
    }

    public void onRoundTrack(String track) {
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

    private boolean waitForToolEffectAppliedResponse() {
        try {
            Thread.sleep(1000); //DA VERIFICARE
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("Riga 1994 GBH");
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


    public void showRanking(String winner, List<String> rankingNames, List<Integer> rankingValues) {

        StringBuilder s = new StringBuilder();
        for (int i = 0; i < rankingNames.size(); i++) {
            s.append("- " + rankingNames.get(i) + "\t" + rankingValues.get(i) + "\n");
        }

        if (winner.equals(username)) {
            s.append("\nComplimenti! Sei il vincitore.");
            Platform.runLater(() -> textArea.setText(s.toString()));
        } else {
            s.append("\n" + winner.toUpperCase() + " è il vincitore!");
            Platform.runLater(() -> textArea.setText(s.toString()));
        }
    }

    public void onGameClosing() {
        textArea.setText("Congratulations! You are the winner. You were the only one still in game.");
    }
}