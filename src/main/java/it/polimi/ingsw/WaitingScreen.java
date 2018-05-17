/*package it.polimi.ingsw;

import it.polimi.ingsw.control.RemoteController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.net.URL;

public class WaitingScreen extends Application {*/

    //Stage window;

    /*private RemoteController controller;

    private Stage window;

    @Override
    public void start(Stage primaryStage) throws Exception {

        System.out.println("Ingresso in start");

        window = primaryStage;

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(new URL("File:./src/main/java/it/polimi/ingsw/resources/waiting-for-players.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Waiting for other players");
        primaryStage.setResizable(false);
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
                    event.consume();
                    onClosing();
                    System.exit(1);
                }
        );
    }

    private void onClosing(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to exit?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Exit");
        alert.setHeaderText(null);
        alert.setResizable(false);
        alert.setGraphic(null);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            window.close();
            System.exit(0);
        }
    }*/


   /* public Scene sceneInit(RemoteController controller) throws Exception{
        this.controller = controller;
        WaitingScreenHandler handler = new WaitingScreenHandler();
        controller.observeLobby(handler);
        //debug
        System.out.println("Ora WaitingScreenHandler è observer di Lobby");
        //debug
        System.out.println("Controller di scena: " + controller.toString());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(new URL("File:./src/main/java/it/polimi/ingsw/resources/waiting-for-players.fxml"));
        Parent root = fxmlLoader.load();
        return new Scene(root);
    }*/
//}
