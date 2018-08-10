package be.zwaldeck.jmsn.client.controller;

import be.zwaldeck.jmsn.client.data.ApplicationData;
import be.zwaldeck.jmsn.client.net.ServerConnection;
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
import be.zwaldeck.jmsn.common.message.response.data.BootData;
import be.zwaldeck.jmsn.common.message.response.error.ErrorData;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.decoration.StyleClassValidationDecoration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.HashMap;

import static be.zwaldeck.jmsn.common.message.request.ServerRequestMessage.ServerRequestMessageType.LOGIN;
import static be.zwaldeck.jmsn.common.message.response.ServerResponseMessage.ServerResponseMessageType.LOGIN_FAILED;
import static be.zwaldeck.jmsn.common.message.response.ServerResponseMessage.ServerResponseMessageType.LOGIN_SUCCESS;

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

    @FXML
    private ImageView avatarIv;

    private ValidationSupport validationSupport;
    private Image loadingImg;
    private Image defaultAvatarImg;

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

        loadingImg = new Image(this.getClass().getResourceAsStream("/img/loading.gif"));
        defaultAvatarImg = new Image(this.getClass().getResourceAsStream("/img/no-profile.png"));
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
            loading();

            Task<String> publicIpFinderTask = Tasks.findPublicIp(ipFinderService);
            publicIpFinderTask.setOnFailed(event -> DialogUtils.errorDialog(translationService.getMessage("jmsn.error.something.wrong")));
            publicIpFinderTask.setOnSucceeded(result -> {
                var data = new LoginData(emailTxt.getText().toLowerCase().trim(),
                        passwordTxt.getText(), publicIpFinderTask.getValue());

                Task<Void> sendMessageTask = Tasks.sendMessageToServer(server, new ServerRequestMessage(LOGIN, data));
                sendMessageTask.setOnFailed(event1 -> DialogUtils.errorDialog(translationService.getMessage("jmsn.error.something.wrong")));
                Tasks.start(sendMessageTask);
            });
            Tasks.start(publicIpFinderTask);
        }
    }

    private void handleServerMessage(ServerResponseMessage msg) {
        Platform.runLater(() -> {
            try {
                if (msg.getType() == LOGIN_SUCCESS) {
                    ApplicationData.getInstance().setContacts(((BootData) msg.getData()).getContactList());
                    NavigationUtils.openMainWindow(stage, springContext);
                    doneLoading();
                } else if (msg.getType() == LOGIN_FAILED) {
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

                    doneLoading();
                    DialogUtils.errorDialog(message);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                DialogUtils.exceptionDialog(ex);
                System.exit(-1);
            }

        });
    }

    private void loading() {
        avatarIv.setImage(loadingImg);
        signInBtn.setDisable(true);
    }

    private void doneLoading() {
        avatarIv.setImage(defaultAvatarImg);
        signInBtn.setDisable(false);
    }
}
