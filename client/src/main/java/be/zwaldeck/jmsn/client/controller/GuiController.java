package be.zwaldeck.jmsn.client.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public abstract class GuiController {

    @Autowired
    protected ApplicationContext springContext;

    protected Stage stage;

    public void displayErrorMessage(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error occured!");
        alert.setContentText(error);

        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label) node).setMinHeight(Region.USE_PREF_SIZE));

        alert.showAndWait();
    }

    public void displayInfoMessage(String headerTxt, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(headerTxt);
        alert.setContentText(text);

        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label) node).setMinHeight(Region.USE_PREF_SIZE));

        alert.showAndWait();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
