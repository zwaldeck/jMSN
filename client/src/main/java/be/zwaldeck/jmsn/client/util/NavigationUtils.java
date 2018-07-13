package be.zwaldeck.jmsn.client.util;

import be.zwaldeck.jmsn.client.controller.GuiController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

public class NavigationUtils {

    public static void openFxmlInSameScene(Stage stage, ApplicationContext springContext,
                                                    String fxmlFile, int width, int height) throws IOException {
        FXMLLoader loader = new FXMLLoader(NavigationUtils.class.getResource(fxmlFile));
        loader.setControllerFactory(springContext::getBean);
        Parent root = loader.load();

        // We use -1 to trigger a bit further a repaint
        Scene scene = new Scene(root, width - 1, height - 1);
        stage.setScene(scene);
        stage.setTitle("jMSN Messenger");
        stage.show();

        // resize for triggering repaint
        stage.setWidth(width);
        stage.setHeight(height);

        stage.requestFocus();
        stage.centerOnScreen();
        stage.toFront();
        root.requestLayout();

        GuiController controller = loader.getController();
        controller.setStage(stage);
    }

    public static void openLoginWindow(Stage stage, ApplicationContext springContext) throws IOException {
        openFxmlInSameScene(stage, springContext, "/login.fxml", 520,520);
    }

    public static void openRegisterWindow(Stage stage, ApplicationContext springContext) throws IOException {
        openFxmlInSameScene(stage, springContext, "/register.fxml", 300,500);
    }
}
