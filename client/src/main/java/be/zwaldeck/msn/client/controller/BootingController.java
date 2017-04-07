package be.zwaldeck.msn.client.controller;

import be.zwaldeck.msn.client.data.ApplicationData;
import be.zwaldeck.msn.client.service.BootService;
import be.zwaldeck.msn.client.util.NavigationUtil;
import be.zwaldeck.msn.common.messages.ServerMessage;
import be.zwaldeck.msn.common.messages.ServerMessageType;
import be.zwaldeck.msn.common.messages.data.UserData;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @Author Wout Schoovaerts
 */
public class BootingController extends GuiController {

    public void initialize() {
        isBooted.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                    BootService bs = new BootService(serverConnection);
                    bs.start();

                    bs.setOnFailed(event -> {
                        bs.getException().printStackTrace();
                    });

                    bs.setOnSucceeded(event -> {
                        try {
                            NavigationUtil.openFxmlInSameScene(primaryStage, "/messenger-contacts.fxml", 400, 800);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
            }
        });
    }
}
