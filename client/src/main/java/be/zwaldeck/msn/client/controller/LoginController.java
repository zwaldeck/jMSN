package be.zwaldeck.msn.client.controller;

import be.zwaldeck.msn.client.data.ApplicationData;
import be.zwaldeck.msn.client.net.ServerConnection;
import be.zwaldeck.msn.client.util.NavigationUtil;
import be.zwaldeck.msn.common.messages.ServerMessage;
import be.zwaldeck.msn.common.messages.ServerMessageType;
import be.zwaldeck.msn.common.messages.data.LoginData;
import be.zwaldeck.msn.common.messages.data.UserData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import org.apache.commons.validator.routines.EmailValidator;

import java.io.IOException;

/**
 * @Author Wout Schoovaerts
 */
//TODO remember me functionality
public class LoginController extends GuiController {

    @FXML
    private TextField emailTxt;

    @FXML
    private PasswordField passwordTxt;

    @FXML
    private CheckBox rememberMeCb;

    @FXML
    private Label errorLbl;

    @FXML
    void onCreateAccount(ActionEvent event) throws IOException {
        NavigationUtil.openFxmlInSameScene(primaryStage, "/register.fxml", 220, 420);
    }

    @FXML
    void onSignIn(ActionEvent event) {
        //todo loading overlay
        errorLbl.setVisible(false);
        if (!formErrors()) {
            LoginData data = new LoginData(emailTxt.getText().trim(), passwordTxt.getText().trim());

            serverConnection.sendMessage(new ServerMessage(ServerMessageType.LOGIN, data));
            ServerMessage sm = serverConnection.waitForMessageFromServer();
            if (sm != null) {
                handleServerResponse(sm);
            }

        }
    }

    private boolean formErrors() {
        if (emailTxt.getText().trim().equals("") ||
                passwordTxt.getText().trim().equals("")) {
            errorLbl.setText("You need to fill in all the fields");
            errorLbl.setVisible(true);
            return true;
        }

        if (!EmailValidator.getInstance().isValid(emailTxt.getText().trim())) {
            errorLbl.setText("Your email is not valid.");
            errorLbl.setVisible(true);
            return true;
        }

        return false;
    }

    private void handleServerResponse(ServerMessage sm) {
        if (sm.getType() == ServerMessageType.LOGIN_SUCCESS) {
            try {
                ApplicationData.getInstance().setUserData((UserData) sm.getData());
                NavigationUtil.openFxmlInSameScene(primaryStage, "/messenger-booting.fxml", 1200, 800);
            } catch (IOException e) {
                displayErrorMessage("There went something wrong. Please try again.");
                e.printStackTrace();
            }
        } else if (sm.getType() == ServerMessageType.LOGIN_FAILED) {
            errorLbl.setText((String) sm.getData());
            errorLbl.setVisible(true);
        }
    }
}
