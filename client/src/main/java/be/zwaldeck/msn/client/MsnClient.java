package be.zwaldeck.msn.client;

import be.zwaldeck.msn.client.controller.LoginController;
import be.zwaldeck.msn.client.util.NavigationUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * @Author Wout Schoovaerts
 */
public class MsnClient extends Application{

    public void start(Stage primaryStage) throws Exception {
        NavigationUtil.openFxmlInSameScene(primaryStage, "/login.fxml", 1200, 800);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
