package be.zwaldeck.msn.client.controller;

import be.zwaldeck.msn.client.net.ServerConnection;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.dialog.ProgressDialog;

import java.util.Observable;

/**
 * @Author Wout Schoovaerts
 */
public abstract class GuiController {

    protected ServerConnection serverConnection;
    protected Stage primaryStage;

    protected BooleanBinding isBooted = Bindings.createBooleanBinding(() -> serverConnection != null);


    public void init(Stage stage) {
        this.primaryStage = stage;
        serverConnection = ServerConnection.getInstance(this);
        isBooted.invalidate();
    }

    public void displayErrorMessage(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error occured!");
        alert.setContentText(error);

        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));

        alert.showAndWait();
    }

    public void displayInfoMessage(String headerTxt, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(headerTxt);
        alert.setContentText(text);

        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));

        alert.showAndWait();
    }
}
