package be.zwaldeck.jmsn.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Controller;

@Controller
public class LoginController extends GuiController {

    @FXML
    private Label errorLbl;

    @FXML
    private TextField emailTxt;

    @FXML
    private PasswordField passwordTxt;

    @FXML
    private CheckBox rememberMeCb;

    @FXML
    void onCreateAccount(ActionEvent event) {

    }

    @FXML
    void onSignIn(ActionEvent event) {

    }
}
