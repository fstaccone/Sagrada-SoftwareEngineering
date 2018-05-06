package it.polimi.ingsw;

import it.polimi.ingsw.control.Controller;
import it.polimi.ingsw.view.RMIView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private String username;
    private boolean isRmi = true;
    private boolean isSocket = false;
    private boolean isGui = true;
    private boolean isCli = false;
    private boolean isSinglelayer = false;
    private int difficulty;
    private String serverAddress;

    // Values to be set by file on server, how can we set these here?
    private int rmiRegistryPort = 2000;
    private int socketPort = 2001;

    // TODO: da controllare
    Controller controller;
    Registry registry;

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
    private void playClicked() throws RemoteException {
        playButton.setEffect(new DropShadow(10, 0, 0, Color.BLUE));
        //playButton.setDisable(true);
        readInput();

        setup();

        Stage stage = (Stage) playButton.getScene().getWindow();
        stage.close();
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
        this.username = this.usernameInput.toString();
        this.isRmi = rmiCheckmark.isSelected();
        this.isSocket = socketCheckmark.isSelected();
        this.isGui = guiCheckmark.isSelected();
        this.isCli = cliCheckmark.isSelected();
        this.serverAddress = serverAddressInput.toString();

        //this.isSinglelayer =
        //this.difficulty =

    }

    private void readUsername() {
        this.username = this.usernameInput.toString();
    }

    private void setup() throws RemoteException {
        boolean unique = false;

        // connection establishment with the selected method
        if (isRmi) setupRmiConnection();
        else setupSocketConnection();

        // name is controlled in the model to be sure that it's unique
        while (!unique) {
            unique = controller.checkName(this.username);
            if (!unique) {
                playButton.setTooltip(new Tooltip("username already in use"));
                readUsername();
            }
        }
        // view's creation and input for the model to create the Player
        if (isRmi) createClientRmiView();
        else createSocketView();

    }


    // the connection is established between client and lobby
    private void setupRmiConnection() throws RemoteException {

        registry = LocateRegistry.getRegistry(rmiRegistryPort);

        try {
            this.controller = (Controller) registry.lookup("Lobby");

        } catch (NotBoundException e) {
            System.out.println("A client can't get the controller's reference");
            e.printStackTrace();
        }

    }

    private void setupSocketConnection() {

    }

    private void createClientRmiView() {

        // to create the link between this client and the Room in which he'll play
        if (isSinglelayer)

            // todo: creare direttamente la partita passando come parametro il Client
            // todo: il Client sarà l'entità da cui si accede alla view e al Player all'interno del modello

            //controller.getLobby().createSingleplayerMatch( new Client(this.username, new RMIView()));

        // stessa cosa qui
        else {
            System.out.println("Match multiplayer");
            //controller.getLobby().createMultiplayerMatch();
        }
    }

    private void createSocketView() {

        // to create the link between this client and the Room in which he'll play
        if (isSinglelayer)
            // crea la view
            // ...
            // la passa come parametro al metodo che creerà le relazioni nel modello
            controller.getLobby().createSingleplayerMatch(this.username);

            // stessa cosa qui
        else controller.getLobby().createMultiplayerMatch();

    }
}

