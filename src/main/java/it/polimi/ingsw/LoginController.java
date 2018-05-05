package it.polimi.ingsw;

import it.polimi.ingsw.control.Controller;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private String username;
    private boolean isRmi=true;
    private boolean isSocket=false;
    private boolean isGui=true;
    private boolean isCli=false;
    private String serverAddress;
    private Controller controller;

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
    private void playClicked() {
        playButton.setEffect(new DropShadow(10,0,0,Color.BLUE));
        //playButton.setDisable(true);
        readInput();

        //SE VOGLIAMO CHE LA FINESTRA SI CHIUDA AL CLICK DEL BOTTONE
        /*Stage stage = (Stage) playButton.getScene().getWindow();
        stage.close();*/
        

    }

    @FXML
    private void glowButton() {
        playButton.setEffect(new Glow(0.3));
    }

    @FXML
    private void unGlowButton() {
        playButton.setEffect(new DropShadow(10,0,0,Color.VIOLET));
    }




    private void readInput() {
        this.username = this.usernameInput.toString();
        this.isRmi = rmiCheckmark.isSelected();
        this.isSocket=socketCheckmark.isSelected();
        this.isGui=guiCheckmark.isSelected();
        this.isCli = cliCheckmark.isSelected();
        this.serverAddress = serverAddressInput.toString();


    }

    private void setup(){
        boolean unique = false;

        // connection establishment with the selected method
        if(isRmi) setupRmiConnection();
        else setupSocketConnection();

        // name is controlled in the model to be sure that it's unique
        // checkName is a method that returns true if the username provided is not already present inside the model
        while(!unique) {
            unique = controller.checkName(this.username);
        }

        // view's creation and input for the model to create the Player

    }
}
