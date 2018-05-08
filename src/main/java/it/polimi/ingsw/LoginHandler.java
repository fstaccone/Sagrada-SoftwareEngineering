package it.polimi.ingsw;


import it.polimi.ingsw.control.RemoteController;
import it.polimi.ingsw.view.RMIView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;

public class LoginHandler implements Initializable, ResponseHandler{

    private String username;
    private boolean isRmi = true;
    private boolean isSocket = false;
    private boolean isGui = true;
    private boolean isCli = false;
    private boolean isSingleplayer = false;
    private int difficulty;
    private String serverAddress;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean nameAlreadyTaken=false;

    // Values to be set by file on server, how can we set these here?
    private int rmiRegistryPort = 1100;
    private int socketPort = 1101;

    Registry registry;
    RemoteController controller;

    private Client client;
    private SocketClient socketClient;

    @FXML
    private TextField usernameInput;

    @FXML
    private TextField serverAddressInput;

    @FXML
    private CheckBox rmiCheckmark;

    @FXML
    private CheckBox socketCheckmark;

    @FXML
    private CheckBox cliCheckmark;

    @FXML
    private CheckBox guiCheckmark;

    @FXML
    private CheckBox modeCheckmark;

    @FXML
    private Button playButton;

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
    private void playClicked() throws IOException, InterruptedException {
        playButton.setEffect(new DropShadow(10, 0, 0, Color.BLUE));
        //playButton.setDisable(true);
        readInput();

        connectionSetup();

        /*Stage stage = (Stage) playButton.getScene().getWindow();
        stage.close();*/

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
        showAlert(Alert.AlertType.WARNING, "Invalid username!", "Username already in use, choose another please!");
        // capire perchè viene chiusa la gui
        // aspetta finchè non viene cliccato di nuovo play
        this.username = this.usernameInput.getText();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    private void connectionSetup() throws IOException, InterruptedException {
        boolean unique = false;

        // connection establishment with the selected method
        if (isRmi) setupRmiConnection();
        else setupSocketConnection();

        // name is controlled in the model to be sure that it's unique
        while (!unique) {
            if(isRmi)
                unique = controller.checkName(this.username);
            else{
                request(new CheckUsernameRequest(this.username));
                nextResponse().handle(this);
                unique=!( this.isNameAlreadyTaken());}

                System.out.println(unique);
            if (!unique) {
                usernameInput.setText("Insert another name here:");
                //PROBABILMENTE CI VUOLE UN EVENTHANDLER
                readUsername();
                System.out.println("nuovo nome preso,al momento lo prende vuoto");
            } else System.out.println("apposto");
        }
        // view's creation and input for the model to create the Player
            if (isRmi) createAndConnectClientRmi();


    }


    // the connection is established between client and lobby
    private void setupRmiConnection() throws RemoteException {

        registry = LocateRegistry.getRegistry(rmiRegistryPort);

        try {
            this.controller = (RemoteController) registry.lookup("Lobby");
            System.out.println("Connection established");

        } catch (NotBoundException e) {
            System.out.println("A client can't get the controller's reference");
            e.printStackTrace();
        }

    }

    private void setupSocketConnection() throws IOException {

        Socket socket=null;

        try{ socket= new Socket(serverAddress, socketPort);
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in= new ObjectInputStream(socket.getInputStream());}
        finally {
          // socket.close();
        }
    }

    private void createAndConnectClientRmi() throws RemoteException {
        // to create the link between this Client and the Player in the model
        if (isSingleplayer){
            client = new Client(this.username, new RMIView(), ConnectionStatus.CONNECTED, this.controller);
            try {
                controller.createMatch(this.username);
            }
            catch (Exception e){
                e.printStackTrace();
                System.out.println("Singleplayer match can't be created!");
            }
        }
        else {
            client = new Client(this.username, new RMIView(), ConnectionStatus.CONNECTED, this.controller);
            try {
                controller.addPlayer(this.username);
            }
            catch (Exception e){
                e.printStackTrace();
                System.out.println("Player " + this.username + " can't be added to a multiplayer match!");
            }

        }
    }

    public void request(Request request) {
        try {
            out.writeObject(request);
        } catch (IOException e) {
            System.err.println("Exception on network: " + e.getMessage());
        }
    }

    public Response nextResponse() {
        try {
            return ((Response) in.readObject());
        } catch (IOException e) {
            System.err.println("Exception on network: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Wrong deserialization: " + e.getMessage());
        }

        return null;
    }

    @Override
    public void handle(NameAlreadyTakenResponse response) {
         this.nameAlreadyTaken=response.nameAlreadyTaken;
    }

    public boolean isNameAlreadyTaken() {
        return nameAlreadyTaken;
    }

    /*
    private void createSocketView() {

        // to create the link between this client and the Room in which he'll play
        if (isSingleplayer)
            // crea la view
            // ...

            // stessa cosa qui
        else controller.getLobby().createMultiplayerMatch();

    }*/
}

