package be.zwaldeck.jmsn.client.controller;

import be.zwaldeck.jmsn.client.util.DialogUtils;
import be.zwaldeck.jmsn.client.util.NavigationUtils;
import be.zwaldeck.jmsn.client.validation.ValidationMessageMapper;
import be.zwaldeck.jmsn.client.validation.ValidatorUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.decoration.StyleClassValidationDecoration;
import org.springframework.stereotype.Controller;

import java.util.HashMap;

@Controller
public class RegisterController extends GuiController {

    @FXML
    private Label emailErrorLbl;

    @FXML
    private TextField emailTxt;

    @FXML
    private Label passwordErrorLbl;

    @FXML
    private PasswordField passwordTxt;

    @FXML
    private Label repeatPasswordErrorLbl;

    @FXML
    private PasswordField repeatPasswordTxt;

    @FXML
    private Label errorLbl;

    @FXML
    private Button registerBtn;

    private ValidationSupport validationSupport;

    @FXML
    public void initialize() {
        validationSupport = new ValidationSupport();
        validationSupport.setValidationDecorator(new StyleClassValidationDecoration());
        validationSupport.setErrorDecorationEnabled(true);
        validationSupport.registerValidator(emailTxt, ValidatorUtils.validateEmail("Please fill in your email.", "This email is invalid"));
        validationSupport.registerValidator(passwordTxt, ValidatorUtils.validatePassword("Please fill in your password", "This password is not strong enough. Use uppercase, lowercase, number and special characters"));
        validationSupport.registerValidator(repeatPasswordTxt, ValidatorUtils.validateControlsValueAreEqual(passwordTxt, "The passwords do not match"));

        var validationLabelsMap = new HashMap<String, Label>();
        validationLabelsMap.put(emailTxt.getId(), emailErrorLbl);
        validationLabelsMap.put(passwordTxt.getId(), passwordErrorLbl);
        validationLabelsMap.put(repeatPasswordTxt.getId(), repeatPasswordErrorLbl);

        var validationMapper = new ValidationMessageMapper(validationLabelsMap);

        validationSupport.validationResultProperty().addListener((observable, oldValue, newValue) -> {
            validationMapper.apply(newValue);
            registerBtn.setDisable(validationSupport.isInvalid());
        });
    }

    @FXML
    void onRegister(ActionEvent event) {
        if (!validationSupport.isInvalid()) {
            System.out.println("WE CAN REGISTER");
        }
    }

    @FXML
    void onCancel(ActionEvent event) {
        try {
            NavigationUtils.openLoginWindow(stage, springContext);
        } catch (Exception e) {
            e.printStackTrace();
            DialogUtils.exceptionDialog(e);
        }
    }

}
