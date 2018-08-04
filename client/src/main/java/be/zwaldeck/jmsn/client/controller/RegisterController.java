package be.zwaldeck.jmsn.client.controller;

import be.zwaldeck.jmsn.client.net.ServerConnection;
import be.zwaldeck.jmsn.client.service.TranslationService;
import be.zwaldeck.jmsn.client.util.DialogUtils;
import be.zwaldeck.jmsn.client.util.NavigationUtils;
import be.zwaldeck.jmsn.client.util.Tasks;
import be.zwaldeck.jmsn.client.validation.ValidationMessageMapper;
import be.zwaldeck.jmsn.client.validation.ValidatorUtils;
import be.zwaldeck.jmsn.common.message.request.ServerRequestMessage;
import be.zwaldeck.jmsn.common.message.request.data.RegisterData;
import be.zwaldeck.jmsn.common.message.response.ServerResponseMessage;
import be.zwaldeck.jmsn.common.message.response.error.ErrorData;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.decoration.StyleClassValidationDecoration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.HashMap;

import static be.zwaldeck.jmsn.common.message.request.ServerRequestMessage.ServerRequestMessageType.REGISTER;
import static be.zwaldeck.jmsn.common.message.response.ServerResponseMessage.ServerResponseMessageType.REGISTER_FAILED;
import static be.zwaldeck.jmsn.common.message.response.ServerResponseMessage.ServerResponseMessageType.REGISTER_SUCCESS;
import static be.zwaldeck.jmsn.common.message.response.error.ErrorData.ErrorType.EMAIL_IN_USE;

@Controller
public class RegisterController extends GuiController {

    private final ServerConnection server;
    private final TranslationService translationService;

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
    private Image loadingImg;
    private Image defaultAvatarImg;

    @Autowired
    public RegisterController(ServerConnection server, TranslationService translationService) {
        this.server = server;
        this.translationService = translationService;
    }

    @FXML
    @SuppressWarnings("unchecked")
    public void initialize() throws Exception {
        validationSupport = new ValidationSupport();
        validationSupport.setValidationDecorator(new StyleClassValidationDecoration());
        validationSupport.setErrorDecorationEnabled(true);
        validationSupport.registerValidator(emailTxt, ValidatorUtils.validateEmail(
                translationService.getMessage("jmsn.error.validation.email.required"),
                translationService.getMessage("jmsn.error.validation.email.invalid")
        ));
        validationSupport.registerValidator(passwordTxt, ValidatorUtils.validatePassword(
                translationService.getMessage("jmsn.register.validation.password.required"),
                translationService.getMessage("jmsn.register.validation.password.not-strong-enough"))
        );
        validationSupport.registerValidator(repeatPasswordTxt, ValidatorUtils.validateControlsValueAreEqual(passwordTxt, translationService.getMessage("jmsn.register.validation.password.repeat")));

        var validationLabelsMap = new HashMap<String, Label>();
        validationLabelsMap.put(emailTxt.getId(), emailErrorLbl);
        validationLabelsMap.put(passwordTxt.getId(), passwordErrorLbl);
        validationLabelsMap.put(repeatPasswordTxt.getId(), repeatPasswordErrorLbl);

        var validationMapper = new ValidationMessageMapper(validationLabelsMap);

        validationSupport.validationResultProperty().addListener((observable, oldValue, newValue) -> {
            validationMapper.apply(newValue);
            registerBtn.setDisable(validationSupport.isInvalid());
        });

        server.getServerMessageReceiverThread().setCallbacks(this::handleServerMessage, this::handleError);

        loadingImg = new Image(this.getClass().getResourceAsStream("/img/loading.gif"));
        defaultAvatarImg = new Image(this.getClass().getResourceAsStream("/img/no-profile.png"));
    }

    @FXML
    void onRegister(ActionEvent ae) {
        if (!validationSupport.isInvalid()) {
            loading();
            var data = new RegisterData(emailTxt.getText(), passwordTxt.getText());
            Task<Void> sendMessageTask = Tasks.sendMessageToServer(server, new ServerRequestMessage(REGISTER, data));
            sendMessageTask.setOnFailed(event -> {
                DialogUtils.errorDialog(translationService.getMessage("jmsn.error.something.wrong"));
            });
            Tasks.start(sendMessageTask);
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
        avatarImgView.setImage(loadingImg);
        registerBtn.setDisable(true);
    }

    private void doneLoading() {
        avatarImgView.setImage(defaultAvatarImg);
        registerBtn.setDisable(false);
    }

    private void handleServerMessage(ServerResponseMessage msg) {
        Platform.runLater(() -> {
            try {
                if (msg.getType() == REGISTER_SUCCESS) {
                    NavigationUtils.openLoginWindow(stage, springContext);
                    DialogUtils.infoDialog(translationService.getMessage("jmsn.register.success.header"), translationService.getMessage("jmsn.register.success.text"));
                    doneLoading();
                } else if (msg.getType() == REGISTER_FAILED){ // All checks are checked in the form, so we can just show something wrong
                    var errorData = (ErrorData) msg.getData();
                    if (errorData.getErrorType() == EMAIL_IN_USE) {
                        DialogUtils.errorDialog(translationService.getMessage("jmsn.register.error.email-in-use"));
                    } else {
                        DialogUtils.errorDialog(translationService.getMessage("jmsn.error.something.wrong"));
                    }
                    doneLoading();
                }
            } catch (IOException ex) {
                handleError(ex);
            }
        });
    }

}
