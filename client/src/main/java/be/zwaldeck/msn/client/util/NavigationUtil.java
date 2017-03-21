package be.zwaldeck.msn.client.util;

import be.zwaldeck.msn.client.controller.AfterInit;
import be.zwaldeck.msn.client.controller.GuiController;
import be.zwaldeck.msn.client.controller.RegisterController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @Author Wout Schoovaerts
 */
public class NavigationUtil {

    public static GuiController openFxmlInSameScene(Stage stage, String fxmlFile, int width, int height) throws IOException {
        FXMLLoader loader = new FXMLLoader(NavigationUtil.class.getResource(fxmlFile));
        BorderPane root = loader.load();
        //-1 pixel because we trigger a resize for a repaint further on
        Scene scene = new Scene(root, width - 1, height - 1);
        stage.setScene(scene);
        stage.setTitle("jMSN Messenger");
        stage.show();

        //trigger resize for a repaint
        stage.setWidth(width);
        stage.setHeight(height);

        stage.requestFocus();
        stage.centerOnScreen();
        stage.toFront();
        root.requestLayout();
        GuiController controller = ((GuiController) loader.getController());
        controller.init(stage);

        return controller;
    }
}
