package it.polimi.ingsw;


import it.polimi.ingsw.control.RemoteController;
import it.polimi.ingsw.socket.ClientController;
import it.polimi.ingsw.socket.SocketListener;
import it.polimi.ingsw.socket.requests.AddPlayerRequest;
import it.polimi.ingsw.socket.requests.CheckUsernameRequest;
import it.polimi.ingsw.socket.requests.CreateMatchRequest;
import it.polimi.ingsw.socket.requests.RemoveFromWaitingPlayersRequest;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

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
    private static final String RESOURCES = "File:./src/main/java/it/polimi/ingsw/resources/";
    private transient Socket socket = null;
    private transient ClientController controllerSocket;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rmiCheckmark.setSelected(true);
        cliCheckmark.setSelected(true);
        isRmi = true;
        isGui = true;
        isCli = false;
        singleplayer = false;
    }

    @FXML
    private void singleplayerMarked() {
        singlePlayerCheckmark.setSelected(true);
    }

    @FXML
    private void rmiMarked() {
        rmiCheckmark.setSelected(true);
        socketCheckmark.setSelected(false);
    }

    @FXML
    private void socketMarked() {
        socketCheckmark.setSelected(true);
        rmiCheckmark.setSelected(false);
    }

    @FXML
    private void guiMarked() {
        guiCheckmark.setSelected(true);
        cliCheckmark.setSelected(false);
    }

    @FXML
    private void cliMarked() {
        cliCheckmark.setSelected(true);
        guiCheckmark.setSelected(false);
    }

    @FXML
    private void playClicked() throws Exception {
        AudioClip audioClip = new AudioClip("File:./src/main/java/it/polimi/ingsw/resources/button.mp3");
        audioClip.play();
        playButton.setEffect(new DropShadow(10, 0, 0, Color.BLUE));
        readInput();

        window = (Stage) playButton.getScene().getWindow();
        if (username.equals("")) {
            showAlertWarning("Nome non valido!", "Inserisci uno username con almeno un carattere");
        } else {
            if (isCli && !singleplayer) {
                waitingRoomCli = new WaitingRoomCli(this, window, username, isRmi);
                connectionSetup(null);
            } else if (isGui && !singleplayer) {

                FXMLLoader fx = new FXMLLoader();
                fx.setLocation(new URL("File:./src/main/java/it/polimi/ingsw/resources/waiting-for-players.fxml"));
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


    private void onClosing() throws RemoteException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Sei sicuro di voler uscire?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Uscita");
        alert.setHeaderText(null);
        alert.setResizable(false);
        alert.setGraphic(null);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
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

    private void showAlertWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setResizable(false);
        alert.setContentText(message);
        alert.showAndWait();
    }

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

    private void loadAndShowGuiSingle() {
        FXMLLoader fx = new FXMLLoader();
        try {
            fx.setLocation(new URL(RESOURCES + "choose-card-single.fxml"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
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

    // the connection is established between client and lobby
    private void setupRmiConnection() throws RemoteException {

        //registry = LocateRegistry.getRegistry();

        try {
            //this.controllerRmi = (RemoteController) registry.lookup(GAME_NAME);

            controllerRmi = (RemoteController) Naming.lookup(("//" + serverAddress + "/" + GAME_NAME));

            if (isCli && !singleplayer)
                waitingRoomCli.setController(controllerRmi);
        } catch (NotBoundException e) {
            System.out.println("A client can't get the controller Rmi's reference");
            e.printStackTrace();
            //} catch (MalformedURLException e) {
            //    e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void setupSocketConnection() throws IOException {
        ObjectInputStream in;
        ObjectOutputStream out;

        try {
            socket = new Socket(serverAddress, socketPort);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            controllerSocket = new ClientController(in, out, this, singleplayer);
            if (isCli && !singleplayer) {
                waitingRoomCli.setClientController(controllerSocket);
            }
        } catch (SocketException e) {
            System.out.println("Unable to create socket connection");
        } //finally { socket.close() INOLTRE VANNO CHIUSI GLI INPUT E OUTPUT STREAM}
    }

    private void createClientRmi() {
        // to create the link between this Client and the Player in the model
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


    private void createClientSocket() {

        // to create the link between this Client and the Player in the model
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

    public void onMatchStartedSocket() {
        if (isCli) {
            new SocketCli(username, controllerSocket, singleplayer);
        } else {
            new SocketGui(window, username, controllerSocket, singleplayer);
        }
    }
}

