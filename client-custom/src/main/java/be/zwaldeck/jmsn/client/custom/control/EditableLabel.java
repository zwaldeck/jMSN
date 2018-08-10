package be.zwaldeck.jmsn.client.custom.control;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

import java.io.IOException;

public class EditableLabel extends StackPane {

    @FXML
    private Label label;

    @FXML
    private TextField textField;

    private Callback<String, Void> updateCallback;

    public EditableLabel() {
        loadFXML();
    }

    @FXML
    public void initialize() {
        label.setVisible(true);
        textField.setVisible(false);
    }

    private void loadFXML() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/custom/control/editable-label.fxml"));
        try {
            loader.setClassLoader(getClass().getClassLoader());
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
//            DialogUtils.exceptionDialog(e);
//            System.exit(-1);
            // TODO handle exceptions
        }
    }

    public void setText() {

    }

    public void setOnUpdate(Callback<String, Void> callback) {
        updateCallback = callback;
    }
}
