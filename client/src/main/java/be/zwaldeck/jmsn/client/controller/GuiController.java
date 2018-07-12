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

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
