package it.polimi.ingsw;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.net.URL;


public class LoginGui extends Application {

    private Stage window;
    private LoginHandler loginHandler;

    @Override
    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;
        loginHandler = new LoginHandler();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(new URL("File:./src/main/java/it/polimi/ingsw/resources/login-sagrada.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Sagrada");
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            onClosing();
        });
    }

    private void onClosing() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Vuoi veramente uscire?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Uscita");
        alert.setHeaderText(null);
        alert.setResizable(false);
        alert.setGraphic(null);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            window.close();
            System.exit(1);
        }
    }


}
