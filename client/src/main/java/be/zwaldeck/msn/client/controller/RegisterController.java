package be.zwaldeck.msn.client.controller;

import be.zwaldeck.msn.client.net.ServerConnection;
import be.zwaldeck.msn.client.util.NavigationUtil;
import be.zwaldeck.msn.common.messages.ServerMessage;
import be.zwaldeck.msn.common.messages.ServerMessageType;
import be.zwaldeck.msn.common.messages.data.RegisterData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.commons.validator.routines.EmailValidator;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

/**
 * @Author Wout Schoovaerts
 */
public class RegisterController extends GuiController {
    @FXML
    private TextField emailTxt;

    @FXML
    private TextField nameTxt;

    @FXML
    private PasswordField passwordTxt;

    @FXML
    private Label errorLbl;

    @FXML
    void onCreate(ActionEvent event) throws IOException {
        //todo loading overlay
        errorLbl.setVisible(false);
        if (!formErrors()) {
            String password = BCrypt.hashpw(passwordTxt.getText(), BCrypt.gensalt());
            RegisterData data = new RegisterData(emailTxt.getText().trim(), password, nameTxt.getText().trim());

            serverConnection.sendMessage(new ServerMessage(ServerMessageType.REGISTER, data));
            ServerMessage sm = serverConnection.waitForMessageFromServer();
            if (sm != null) {
                handleServerResponse(sm);
            }
        }
    }

    @FXML
    void onCancel(ActionEvent event) throws IOException {
        NavigationUtil.openFxmlInSameScene(primaryStage, "/login.fxml", 1200, 800);
    }

    private boolean formErrors() {
        if (emailTxt.getText().trim().equals("") ||
                nameTxt.getText().trim().equals("") ||
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

    private void handleServerResponse(ServerMessage sm) throws IOException {
        if (sm.getType() == ServerMessageType.REGISTER_SUCCESS) {
            NavigationUtil.openFxmlInSameScene(primaryStage, "/login.fxml", 1200, 800);
            displayInfoMessage("You are successfully registered", "Do not forget to check your email for instructions to activate your account.");
        } else if (sm.getType() == ServerMessageType.REGISTER_FAILED) {
            errorLbl.setText((String) sm.getData());
            errorLbl.setVisible(true);
        }
    }
}
