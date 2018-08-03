package be.zwaldeck.jmsn.client.controller;

import be.zwaldeck.jmsn.client.util.DialogUtils;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.io.EOFException;

public abstract class GuiController {

    @Autowired
    protected ApplicationContext springContext;

    protected Stage stage;

    protected void handleError(Exception ex) {
        Platform.runLater(() -> {
            if (ex instanceof EOFException) {
                ex.printStackTrace();
                System.exit(9);
            } else {
                DialogUtils.exceptionDialog(ex);
            }
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
