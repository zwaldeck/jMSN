package be.zwaldeck.jmsn.client.controller;

import be.zwaldeck.jmsn.client.net.ServerConnection;
import be.zwaldeck.jmsn.client.net.exception.IPResolverException;
import be.zwaldeck.jmsn.client.net.service.IpFinderService;
import be.zwaldeck.jmsn.client.service.TranslationService;
import be.zwaldeck.jmsn.client.util.DialogUtils;
import be.zwaldeck.jmsn.client.util.NavigationUtils;
import be.zwaldeck.jmsn.client.util.Tasks;
import be.zwaldeck.jmsn.client.validation.ValidationMessageMapper;
import be.zwaldeck.jmsn.client.validation.ValidatorUtils;
import be.zwaldeck.jmsn.common.message.request.ServerRequestMessage;
import be.zwaldeck.jmsn.common.message.request.data.LoginData;
import be.zwaldeck.jmsn.common.message.response.ServerResponseMessage;
import be.zwaldeck.jmsn.common.message.response.error.ErrorData;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.decoration.StyleClassValidationDecoration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.HashMap;

import static be.zwaldeck.jmsn.common.message.request.ServerRequestMessage.ServerRequestMessageType.LOGIN;
import static be.zwaldeck.jmsn.common.message.response.ServerResponseMessage.ServerResponseMessageType.LOGIN_SUCCESS;
import static be.zwaldeck.jmsn.common.message.response.error.ErrorData.ErrorType.PASSWORD_NOT_MATCHING;
import static be.zwaldeck.jmsn.common.message.response.error.ErrorData.ErrorType.USER_NOT_FOUND;

@Controller
public class LoginController extends GuiController {

    private final ServerConnection server;
    private final TranslationService translationService;
    private final IpFinderService ipFinderService;

    @FXML
    private Label emailErrorLbl;

    @FXML
    private TextField emailTxt;

    @FXML
    private PasswordField passwordTxt;

    @FXML
    private CheckBox rememberMeCb;

    @FXML
    private Button signInBtn;
    private ValidationSupport validationSupport;

    @Autowired
    public LoginController(ServerConnection server, TranslationService translationService,
                           IpFinderService ipFinderService) {
        this.server = server;
        this.translationService = translationService;
        this.ipFinderService = ipFinderService;
    }

    @FXML
    @SuppressWarnings("unchecked")
    public void initialize() {
        validationSupport = new ValidationSupport();
        validationSupport.setValidationDecorator(new StyleClassValidationDecoration());
        validationSupport.setErrorDecorationEnabled(true);

        validationSupport.registerValidator(emailTxt, ValidatorUtils.validateEmail(
                translationService.getMessage("jmsn.error.validation.email.required"),
                translationService.getMessage("jmsn.error.validation.email.invalid")
        ));

        var validationLabelsMap = new HashMap<String, Label>();
        validationLabelsMap.put(emailTxt.getId(), emailErrorLbl);

        var validationMapper = new ValidationMessageMapper(validationLabelsMap);

        validationSupport.validationResultProperty().addListener((observable, oldValue, newValue) -> {
            validationMapper.apply(newValue);
            signInBtn.setDisable(validationSupport.isInvalid());
        });

        server.getServerMessageReceiverThread().setCallbacks(this::handleServerMessage, this::handleError);
    }

    @FXML
    void onCreateAccount(ActionEvent event) {
        try {
            NavigationUtils.openRegisterWindow(stage, springContext);
        } catch (Exception e) {
            e.printStackTrace();
            DialogUtils.exceptionDialog(e);
        }
    }

    @FXML
    void onSignIn(ActionEvent e) {
        if (!validationSupport.isInvalid()) {
            try {
                var data = new LoginData(emailTxt.getText().toLowerCase().trim(),
                        passwordTxt.getText(), ipFinderService.findPublicIP());

                Task<Void> sendMessageTask = Tasks.sendMessageToServer(server, new ServerRequestMessage(LOGIN, data));
                sendMessageTask.setOnFailed(event -> {
                    DialogUtils.errorDialog(translationService.getMessage("jmsn.error.something.wrong"));
                });
                Tasks.start(sendMessageTask);

            } catch (IPResolverException ex) {
                ex.printStackTrace();

            }
        }
    }

    private void handleServerMessage(ServerResponseMessage msg) {
        Platform.runLater(() -> {
            if (msg.getType() == LOGIN_SUCCESS) {
                System.out.println("OPEN UP THE REAL APP");
            } else {
                var errorData = (ErrorData) msg.getData();
                String message;
                switch (errorData.getErrorType()) {
                    case USER_NOT_FOUND:
                        message = translationService.getMessage("jmsn.login.error.user-not-found");
                        break;
                    case PASSWORD_NOT_MATCHING:
                        message = translationService.getMessage("jmsn.login.error.password-not-matching");
                        break;
                    default:
                        message = translationService.getMessage("jmsn.error.something.wrong");
                        break;
                }

                DialogUtils.errorDialog(message);
            }
        });
    }
}
