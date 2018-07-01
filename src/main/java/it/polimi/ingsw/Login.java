package it.polimi.ingsw;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;


public class Login extends Application {
    private Stage window;

    @Override
    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/login-sagrada.fxml"));
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
        ButtonType yes = new ButtonType("SÌ", ButtonBar.ButtonData.YES);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Vuoi veramente uscire?", yes, ButtonType.NO);
        alert.setTitle("Uscita");
        alert.setHeaderText(null);
        alert.setResizable(false);
        alert.setGraphic(null);
        alert.showAndWait();
        if (alert.getResult() == yes) {
            window.close();
            System.exit(1);
        }
    }


}
