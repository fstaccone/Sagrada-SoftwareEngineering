package it.polimi.ingsw.view;


import it.polimi.ingsw.control.RemoteController;
import it.polimi.ingsw.control.SocketController;
import it.polimi.ingsw.model.gamelogic.ConnectionStatus;
import it.polimi.ingsw.socket.SocketListener;
import it.polimi.ingsw.socket.requests.AddPlayerRequest;
import it.polimi.ingsw.socket.requests.CheckUsernameRequest;
import it.polimi.ingsw.socket.requests.CreateMatchRequest;
import it.polimi.ingsw.socket.requests.RemoveFromWaitingPlayersRequest;
import it.polimi.ingsw.view.cli.RmiCli;
import it.polimi.ingsw.view.cli.SocketCli;
import it.polimi.ingsw.view.cli.WaitingRoomCli;
import it.polimi.ingsw.view.gui.RmiGui;
import it.polimi.ingsw.view.gui.SocketGui;
import it.polimi.ingsw.view.gui.WaitingScreenHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;


public class LoginHandler implements Initializable {

    private static final String GAME_NAME = "Lobby";
    private transient Socket socket = null;
    private transient SocketController controllerSocket;
    private String username;
    private Stage window;
    private transient boolean isRmi;
    private transient boolean isSocket;
    private transient boolean isGui;
    private transient boolean isCli;

    private transient boolean singleplayer;
    private transient Integer difficulty;
    private transient String serverAddress;
    private WaitingScreenHandler handler;
    private WaitingRoomCli waitingRoomCli;

    // Values to be set by file on server, how can we set these here?
    private transient int socketPort = 1100;

    private transient Registry registry;
    private transient RemoteController controllerRmi;

    @FXML
    private transient TextField difficultyInput;
    @FXML
    private transient TextField usernameInput;
    @FXML
    private transient TextField serverAddressInput;
    @FXML
    private transient CheckBox singlePlayerCheckmark;
    @FXML
    private transient CheckBox rmiCheckmark;
    @FXML
    private transient CheckBox socketCheckmark;
    @FXML
    private transient CheckBox cliCheckmark;
    @FXML
    private transient CheckBox guiCheckmark;
    @FXML
    private transient Button playButton;

    // getters
    public WaitingScreenHandler getWaitingScreenHandler() {
        return this.handler;
    }

    public boolean isCli() {
        return isCli;
    }

    public WaitingRoomCli getWaitingRoomCli() {
        return waitingRoomCli;
    }

    /**
     * When the login window is created, the default choice is rmi connection with cli.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rmiCheckmark.setSelected(true);
        cliCheckmark.setSelected(true);
        isRmi = true;
        isGui = true;
        isCli = false;
        singleplayer = false;
    }

    /**
     * Marks the single player checkBox when the player clicks on it.
     */
    @FXML
    private void singleplayerMarked() {
        if (singlePlayerCheckmark.isSelected()) {
            singlePlayerCheckmark.setSelected(true);
        } else {
            singlePlayerCheckmark.setSelected(false);
        }
    }

    /**
     * Marks the rmi checkBox when the player clicks on it. It also deselects the socket checkBox.
     */
    @FXML
    private void rmiMarked() {
        rmiCheckmark.setSelected(true);
        socketCheckmark.setSelected(false);
    }

    /**
     * Marks the socket checkBox when the player clicks on it. It also deselects the rmi checkBox.
     */
    @FXML
    private void socketMarked() {
        socketCheckmark.setSelected(true);
        rmiCheckmark.setSelected(false);
    }

    /**
     * Marks the gui checkBox when the player clicks on it. It also deselects the cli checkBox.
     */
    @FXML
    private void guiMarked() {
        guiCheckmark.setSelected(true);
        cliCheckmark.setSelected(false);
    }

    /**
     * Marks the cli checkBox when the player clicks on it. It also deselects the gui checkBox.
     */
    @FXML
    private void cliMarked() {
        cliCheckmark.setSelected(true);
        guiCheckmark.setSelected(false);
    }

    /**
     * When the PLAY button is clicked, the handler checks if the player actually chose a name with at least one
     * character. After that, it checks the choices made by the player (single/multi, cli/gui) and initializes the corresponding
     * window.
     *
     */
    @FXML
    private void playClicked() throws Exception {

        AudioClip audioClip = Applet.newAudioClip(getClass().getResource("/sounds/button.au"));
        audioClip.play();
        playButton.setEffect(new DropShadow(10, 0, 0, Color.BLUE));
        readInput();

        window = (Stage) playButton.getScene().getWindow();
        if (username.equals("")) {
            showAlertWarning("Nome non valido!", "Inserisci uno username con almeno un carattere");
        } else {
            if (isCli && !singleplayer) {
                waitingRoomCli = new WaitingRoomCli(this, window);
                connectionSetup(null);
            } else if (isGui && !singleplayer) {

                FXMLLoader fx = new FXMLLoader();
                fx.setLocation(getClass().getResource("/waiting-for-players.fxml"));
                Scene waiting = new Scene(fx.load());

                //CONTROLLER
                handler = fx.getController();
                handler.setLoginHandler(this);

                connectionSetup(waiting);

                window.setOnCloseRequest(event -> {
                    try {
                        event.consume();
                        onClosing();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                if (difficulty == null || difficulty < 1 || difficulty > 5) {
                    showAlertWarning("Difficoltà non valida!", "Il valore deve essere compreso tra 1 e 5");
                } else {
                    connectionSetup(null);
                }
            }
        }
    }

    /**
     * It's called after the player clicked on the QUIT button or on the X in the top-right corner of the window.
     * It shows an alert message and if the player clicks on YES it closes the login window and removes the player from
     * the game.
     *
     */
    private void onClosing() throws RemoteException {
        ButtonType yes = new ButtonType("SÌ", ButtonBar.ButtonData.YES);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Sei sicuro di voler uscire?", yes, ButtonType.NO);
        alert.setTitle("Uscita");
        alert.setHeaderText(null);
        alert.setResizable(false);
        alert.setGraphic(null);
        alert.showAndWait();
        if (alert.getResult() == yes) {
            window.close();
            if (isRmi)
                controllerRmi.removePlayer(this.username);
            else
                controllerSocket.request(new RemoveFromWaitingPlayersRequest(this.username));
            System.exit(1);
        }
    }


    @FXML
    private void glowButton() {
        playButton.setEffect(new Glow(0.3));
    }

    @FXML
    private void unGlowButton() {
        playButton.setEffect(new DropShadow(10, 0, 0, Color.VIOLET));
    }

    /**
     * Stores all the choices made by the player.
     */
    private void readInput() {
        this.username = usernameInput.getText();
        this.isRmi = rmiCheckmark.isSelected();
        this.isSocket = socketCheckmark.isSelected();
        this.isGui = guiCheckmark.isSelected();
        this.isCli = cliCheckmark.isSelected();
        this.singleplayer = singlePlayerCheckmark.isSelected();
        this.serverAddress = serverAddressInput.getText();
        this.singleplayer = singlePlayerCheckmark.isSelected();
        this.difficulty = tryParse(difficultyInput.getText());
    }

    private Integer tryParse(String text) {
        if (text != null) {
            try {
                return Integer.parseInt(text);
            } catch (NumberFormatException e) {
                return null;
            }
        } else return null;
    }

    /**
     * Shows a custom alert window. Title and message are passed as parameters.
     *
     * @param title   of the alert window.
     * @param message of the alert window.
     */
    private void showAlertWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setResizable(false);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Sets up the connection chosen by the player and checks the connection status for the chosen username.
     * If the status is CONNECTED it means the username chosen is actually being used and player has to choose a different one.
     * If the status is DISCONNECTED the player gets reconnected to the match in which he was previously playing.
     * Otherwise, the player joins a new match.
     *
     * @param waiting is the new scene (waiting room), to show only if the player chose to play with gui in a
     *                multi player match.
     */
    private void connectionSetup(Scene waiting) throws IOException, InterruptedException {
        ConnectionStatus status;

        // connection establishment with the selected method
        if (isRmi) setupRmiConnection();
        else setupSocketConnection();

        // name is controlled in the model to be sure that it's unique
        if (isRmi)
            status = controllerRmi.checkName(username);
        else {
            controllerSocket.request(new CheckUsernameRequest(username));
            controllerSocket.nextResponse().handleResponse(controllerSocket);
            status = controllerSocket.isNameAlreadyTaken();
        }

        if (status.equals(ConnectionStatus.CONNECTED)) {
            System.out.println("Invalid username");
            showAlertWarning("Username non valido!", "Nome già in uso, inseriscine un altro!");
            if (!isRmi) {
                socket.close();
            }
        } else if (status.equals(ConnectionStatus.DISCONNECTED)) {
            if (isRmi) {
                if (isCli) {
                    window.close();
                    new RmiCli(username, controllerRmi, singleplayer).reconnect();
                } else {
                    loadAndShowGuiSingle();
                    new RmiGui(window, username, controllerRmi, singleplayer).reconnect();
                }
            } else {
                new Thread(new SocketListener(controllerSocket)).start();
                if (isCli) {
                    window.close();
                    new SocketCli(username, controllerSocket, singleplayer).reconnect();
                } else {
                    new SocketGui(window, username, controllerSocket, singleplayer).reconnect();
                }
            }
        } else {
            // views' creation and input for the model to create the Player
            if (isGui && !singleplayer) {
                window.setScene(waiting);
                window.setTitle("Sala d'attesa");
                window.setResizable(false);
                window.show();
            }

            if (isRmi) createClientRmi();
            else createClientSocket();
        }
    }

    /**
     * Loads and shows the window pattern card choice scene for single player.
     */
    private void loadAndShowGuiSingle() {
        FXMLLoader fx = new FXMLLoader();
        fx.setLocation(getClass().getResource("/choose-card-single.fxml"));
        Scene chooseCard = null;
        try {
            chooseCard = new Scene(fx.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        window.setScene(chooseCard);
        window.setTitle("Scelta della carta schema");
        window.setResizable(false);
        window.show();
    }

    /**
     * The connection is established between client and lobby
     *
     */
    private void setupRmiConnection() throws RemoteException {

        //registry = LocateRegistry.getRegistry();

        try {
            //this.controllerRmi = (RemoteController) registry.lookup(GAME_NAME);

            controllerRmi = (RemoteController) Naming.lookup(("//" + serverAddress + "/" + GAME_NAME));

        } catch (NotBoundException e) {
            System.out.println("A client can't get the controller Rmi's reference");
            e.printStackTrace();
            //} catch (MalformedURLException e) {
            //    e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up the socket connection
     *
     */
    private void setupSocketConnection() throws IOException {
        ObjectInputStream in;
        ObjectOutputStream out;

        try {
            socket = new Socket(serverAddress, socketPort);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            controllerSocket = new SocketController(in, out, this, singleplayer);

        } catch (SocketException e) {
            System.out.println("Unable to create socket connection");
        }
    }

    /**
     * Creates the link between this Client and the Player in the model.
     */
    private void createClientRmi() {
        if (singleplayer) {
            try {
                controllerRmi.createMatch(this.username, difficulty, null);
                if (isCli) {
                    window.close();
                    new RmiCli(username, controllerRmi, singleplayer).launch();
                } else {

                    new RmiGui(window, username, controllerRmi, singleplayer).launch();
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Singleplayer match can't be created!");
            }
        } else {
            try {
                if (isCli) {
                    window.close();
                    controllerRmi.observeLobby(this.username, waitingRoomCli);
                } else {
                    controllerRmi.observeLobby(this.username, handler);
                }
                controllerRmi.addPlayer(this.username);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Player " + this.username + " can't be added to a multiplayer match!");
            }

        }
    }

    /**
     * Creates the link between this Client and the Player in the model.
     */
    private void createClientSocket() {

        if (singleplayer) {
            try {
                new Thread(new SocketListener(controllerSocket)).start();
                controllerSocket.request(new CreateMatchRequest(this.username, difficulty));
                if (isCli) {
                    window.close();
                    new SocketCli(username, controllerSocket, singleplayer);
                } else {
                    new SocketGui(window, username, controllerSocket, singleplayer);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Singleplayer match can't be created!");
            }
        } else {
            try {
                new Thread(new SocketListener(controllerSocket)).start();
                controllerSocket.request(new AddPlayerRequest(this.username));
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Player " + username + " can't be added to a multiplayer match!");
            }

        }
    }

    /**
     * Initializes gui or cli with rmi connection when the match starts.
     */
    public void onMatchStartedRmi() {
        if (isCli) {
            try {
                new RmiCli(username, controllerRmi, false).launch();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            try {
                new RmiGui(window, username, controllerRmi, false).launch();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Initializes gui or cli with socket connection when the match starts.
     */
    public void onMatchStartedSocket() {
        if (isCli) {
            new SocketCli(username, controllerSocket, singleplayer);
        } else {
            new SocketGui(window, username, controllerSocket, singleplayer);
        }
    }
}

