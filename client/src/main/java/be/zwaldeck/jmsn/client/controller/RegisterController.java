package be.zwaldeck.jmsn.client.controller;

import be.zwaldeck.jmsn.client.net.ServerConnection;
import be.zwaldeck.jmsn.client.util.DialogUtils;
import be.zwaldeck.jmsn.client.util.NavigationUtils;
import be.zwaldeck.jmsn.client.validation.ValidationMessageMapper;
import be.zwaldeck.jmsn.client.validation.ValidatorUtils;
import be.zwaldeck.jmsn.common.message.request.ServerRequestMessage;
import be.zwaldeck.jmsn.common.message.request.data.RegisterData;
import be.zwaldeck.jmsn.common.message.response.error.ErrorData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.decoration.StyleClassValidationDecoration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.HashMap;

import static be.zwaldeck.jmsn.common.message.response.ServerResponseMessage.ServerResponseMessageType.REGISTER_SUCCESS;
import static be.zwaldeck.jmsn.common.message.response.error.ErrorData.ErrorType.EMAIL_IN_USE;

@Controller
public class RegisterController extends GuiController {

    private final ServerConnection server;

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

    @FXML
    private Button cancelBtn;

    @FXML
    private ImageView avatarImgView;

    private ValidationSupport validationSupport;

    @Autowired
    public RegisterController(ServerConnection server) {
        this.server = server;
    }

    @FXML
    public void initialize() throws Exception {
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
    void onRegister(ActionEvent event) throws IOException {
        if (!validationSupport.isInvalid()) {
            loading();
            var data = new RegisterData(emailTxt.getText(), passwordTxt.getText());
            server.sendMessage(new ServerRequestMessage(ServerRequestMessage.ServerRequestMessageType.REGISTER, data));
            var response = server.waitForMessage();

            if (response.getType() == REGISTER_SUCCESS) {
                NavigationUtils.openLoginWindow(stage, springContext);
                DialogUtils.infoDialog("You are successfully registered", "Do not forget to check your email for instructions to activate your account.");
            } else { // All checks are checked in the form, so we can just show something wrong
                var errorData = (ErrorData) response.getData();
                if(errorData.getErrorType() == EMAIL_IN_USE) {
                    DialogUtils.errorDialog("This email is already used for another account. Please use a different email or login with this email.");
                } else {
                    DialogUtils.errorDialog("There went something wrong. Please try again.");
                }
            }
            doneLoading();
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

    private void loading() {
        registerBtn.setDisable(true);
        cancelBtn.setDisable(true);
    }

    private void doneLoading() {
        registerBtn.setDisable(false);
        cancelBtn.setDisable(false);
    }

}
