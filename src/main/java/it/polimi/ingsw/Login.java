package it.polimi.ingsw;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;


public class Login extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        LoginHandler loginHandler =new LoginHandler();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(new URL("File:./src/main/java/it/polimi/ingsw/resources/login-sagrada.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Sagrada");
        primaryStage.setResizable(false);
        primaryStage.show();

    }

}
