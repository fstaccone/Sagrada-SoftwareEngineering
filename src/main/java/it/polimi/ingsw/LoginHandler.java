package it.polimi.ingsw;


import it.polimi.ingsw.control.RemoteController;
import it.polimi.ingsw.view.RMIView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.ResourceBundle;

public class LoginHandler extends UnicastRemoteObject implements Initializable,LobbyObserver{

    private transient Socket socket=null;
    private transient ClientController clientController;
    private transient ObjectInputStream in;
    private transient ObjectOutputStream out;
    private String username;

    private transient boolean isRmi = true;
    private transient boolean isSocket = false;
    private transient boolean isGui = true;
    private transient boolean isCli = false;
    private transient boolean isSingleplayer = false;
    private transient int difficulty;
    private transient String serverAddress;
    private String waitingPlayersString;

    // Values to be set by file on server, how can we set these here?
    private transient int rmiRegistryPort = 1100;
    private transient int socketPort = 1101;

    private transient Registry registry;
    private transient RemoteController controller;

    private transient Client client;

    @FXML
    private transient TextField usernameInput;

    @FXML
    private transient TextField serverAddressInput;

    @FXML
    private transient CheckBox rmiCheckmark;

    @FXML
    private transient CheckBox socketCheckmark;

    @FXML
    private transient CheckBox cliCheckmark;

    @FXML
    private transient CheckBox guiCheckmark;

    @FXML
    private transient CheckBox modeCheckmark;

    @FXML
    private transient Button playButton;

    public LoginHandler() throws RemoteException {
        super();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.rmiCheckmark.setSelected(true);
        this.guiCheckmark.setSelected(true);
    }
    @FXML
    private void singleplayerMarked(){
        modeCheckmark.setSelected(true);
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
        playButton.setEffect(new DropShadow(10, 0, 0, Color.BLUE));
        //playButton.setDisable(true);
        readInput();

        /*if(!isSingleplayer){
            //NON CAPISCO PERCHè NON PARTE SUBITO
            Stage stage = (Stage) playButton.getScene().getWindow();
            try {
                stage.setScene(new WaitingScreen().sceneInit());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/

        connectionSetup();

        Stage window = (Stage) playButton.getScene().getWindow();
        //stage.close();
        //window = new Stage();
        FXMLLoader fx = new FXMLLoader(getClass().getResource("waiting-for-players.fxml"));
        fx.setLocation(new URL("File:./src/main/java/it/polimi/ingsw/resources/waiting-for-players.fxml"));
        Scene waiting = new Scene(fx.load());
        window.setScene(waiting);
        window.setTitle("Waiting for players");
        window.setResizable(false);
        //CONTROLLER
        WaitingScreenHandler handler = fx.getController();
        if (isRmi) {
            controller.observeLobby(handler);
        }
        handler.setString("Players waiting...\n" + waitingPlayersString);
        window.show();
        /* Stage stage = (Stage) playButton.getScene().getWindow();
        stage.setScene(new WaitingScreen().sceneInit(controller));*/
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
        this.username = this.usernameInput.getText();
        this.isRmi = rmiCheckmark.isSelected();
        this.isSocket = socketCheckmark.isSelected();
        this.isGui = guiCheckmark.isSelected();
        this.isCli = cliCheckmark.isSelected();
        this.serverAddress = serverAddressInput.getText();
        this.isSingleplayer = modeCheckmark.isSelected();
    }

    private void readUsername() {

        // capire perchè viene chiusa la gui
        // aspetta finchè non viene cliccato di nuovo play
        //this.username = this.usernameInput.getText();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setResizable(false);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void connectionSetup() throws IOException {
        boolean unique = false;

        // connection establishment with the selected method
        if (isRmi) setupRmiConnection();
        else setupSocketConnection();

        // name is controlled in the model to be sure that it's unique
        if (!unique) {
            if(isRmi)
                unique = controller.checkName(this.username);
            else{
                clientController.request(new CheckUsernameRequest(this.username));
                clientController.nextResponse().handleResponse(clientController);
                unique=!( clientController.isNameAlreadyTaken());}

            if (!unique) {
                //usernameInput.setText("Insert another name here:");
                //PROBABILMENTE CI VUOLE UN EVENTHANDLER
                //readUsername();
                System.out.println("Invalid username");
                showAlert(Alert.AlertType.WARNING, "Invalid username!", "Username already in use, open another window and choose another one please!");
                //System.out.println("nuovo nome preso,al momento lo prende vuoto");
                socket.close();
            }
            else {
                if (isRmi) createClientRmi();
                else createClientSocket();
            }
        }
        // view's creation and input for the model to create the Player



    }


    // the connection is established between client and lobby
    private void setupRmiConnection() throws RemoteException {

        registry = LocateRegistry.getRegistry(rmiRegistryPort);

        try {
            this.controller = (RemoteController) registry.lookup("Lobby");

        } catch (NotBoundException e) {
            System.out.println("A client can't get the controller's reference");
            e.printStackTrace();
        }

    }

    private void setupSocketConnection() throws IOException {

        try{ socket= new Socket(serverAddress, socketPort);
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
            clientController = new ClientController(in,out);
        }
        catch(SocketException e){
            System.out.println("Unable to create socket connection");
        }
        finally{ /*socket.close() INOLTRE VANNO CHIUSI GLI INPUT E OUTPUT STREAM*/}
    }

    private void createClientRmi() throws RemoteException {
        // to create the link between this Client and the Player in the model
        if (isSingleplayer){
            client = new Client(this.username, new RMIView(), ConnectionStatus.CONNECTED, this.controller);
            try {
                controller.createMatch(this.username);
                if(isCli) {
                    new RmiCli(username,controller).launch();//per il momento null
                } else
                {}
            }
            catch (Exception e){
                e.printStackTrace();
                System.out.println("Singleplayer match can't be created!");
            }
        }
        else {
            client = new Client(this.username, new RMIView(), ConnectionStatus.CONNECTED, this.controller);
            try {
                controller.observeLobby(this);
                controller.addPlayer(this.username);
                new Thread(new RmiCli(username,controller)).start();
            }
            catch (Exception e){
                e.printStackTrace();
                System.out.println("Player " + this.username + " can't be added to a multiplayer match!");
            }

        }
    }



    private void createClientSocket() throws RemoteException{

        // to create the link between this Client and the Player in the model
        if (isSingleplayer){
            client = new Client(this.username, new RMIView(), ConnectionStatus.CONNECTED, this.clientController,this.controller);
            try {
                clientController.request(new CreateMatchRequest(this.username));
            }
            catch (Exception e){
                e.printStackTrace();
                System.out.println("Singleplayer match can't be created!");
            }
        }
        else {
            client = new Client(this.username, new RMIView(), ConnectionStatus.CONNECTED, this.clientController,this.controller);
            try {
                //clientController.request(new ObserveLobbyRequest(this));
                new Thread(new SocketListener(clientController)).start();
                clientController.request(new AddPlayerRequest(this.username));
                new Thread(new SocketCli(username,clientController)).start();

            }
            catch (Exception e){
                e.printStackTrace();
                System.out.println("Player " + this.username + " can't be added to a multiplayer match!");
            }

        }
    }

    @Override //QUESTA INFORMAZIONE è DA PASSARE ALLA FINESTRA WAITING PLAYERS, per ora è stampata
    public void onWaitingPlayers(List<String> waitingPlayers) {
        System.out.println("Current waiting players for the next starting match are: (disposed in order of access to the lobby)");
        waitingPlayers.forEach(System.out::println);
        System.out.println();
        waitingPlayersString = waitingPlayers.toString();
    }

}

