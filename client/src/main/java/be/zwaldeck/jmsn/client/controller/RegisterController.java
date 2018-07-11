package be.zwaldeck.jmsn.client.controller;

import be.zwaldeck.jmsn.client.util.NavigationUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class RegisterController extends GuiController {

    @FXML
    private Label errorLbl;

    @FXML
    private TextField emailTxt;

    @FXML
    private PasswordField passwordTxt;

    @FXML
    private PasswordField repeatPasswordTxt;

    @FXML
    void onRegister(ActionEvent event) {

    }

    @FXML
    void onCancel(ActionEvent event) {
        try {
            NavigationUtils.openLoginWindow(stage, springContext);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO Handle exceptions
        }
    }

}
