package be.zwaldeck.jmsn.client.custom.control;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

import java.io.IOException;
import java.util.function.Consumer;

public class EditableLabel extends StackPane {

    @FXML
    private Label label;

    @FXML
    private TextField textField;

    private Consumer<String> updateCallback;

    private String oldValue;

    public EditableLabel() {
        loadFXML();
    }

    @FXML
    public void initialize() {
        showLabel(false);

        label.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                showTextField();
            }
        });

        textField.setOnAction(event -> {
            showLabel(false);
            if (updateCallback != null) {
                updateCallback.accept(textField.getText().trim());
            }
        });

        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
            }
        });

        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                showLabel(true);
            } else if (event.getCode() == KeyCode.TAB) {
                showLabel(false);
                if (updateCallback != null) {
                    updateCallback.accept(textField.getText().trim());
                }
            }
        });
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

    public void setText(String text) {
        label.setText(text);
    }

    public void setOnUpdate(Consumer<String> callback) {
        updateCallback = callback;
    }

    private void showLabel(boolean reset) {
        label.setVisible(true);
        textField.setVisible(false);
        label.setText(reset ? oldValue : textField.getText().trim());
        oldValue = null;
    }

    private void showTextField() {
        oldValue = label.getText();
        label.setVisible(false);
        textField.setText(label.getText());
        textField.setVisible(true);
        textField.requestFocus();
    }
}
