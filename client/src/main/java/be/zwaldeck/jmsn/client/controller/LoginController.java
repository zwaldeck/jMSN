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
import java.util.prefs.Preferences;

import static be.zwaldeck.jmsn.common.message.request.ServerRequestMessage.ServerRequestMessageType.LOGIN;
import static be.zwaldeck.jmsn.common.message.response.ServerResponseMessage.ServerResponseMessageType.LOGIN_FAILED;
import static be.zwaldeck.jmsn.common.message.response.ServerResponseMessage.ServerResponseMessageType.LOGIN_SUCCESS;

@Controller
public class LoginController extends GuiController {

    private static final String PREFS_EMAIL = "jmsn_login_email";
    private static final String PREFS_PASS = "jmsn_login_pass";
    private static final String PREFS_REMEMBER_ME = "jmsn_login_remember";


    // Injected by spring
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

    // Member vars
    private  final ValidationSupport validationSupport;
    private final Image loadingImg;
    private final Image defaultAvatarImg;
    private final Preferences loginPrefs;


    @Autowired
    public LoginController(ServerConnection server, TranslationService translationService,
                           IpFinderService ipFinderService) {
        this.server = server;
        this.translationService = translationService;
        this.ipFinderService = ipFinderService;

        validationSupport = new ValidationSupport();
        loadingImg = new Image(this.getClass().getResourceAsStream("/img/loading.gif"));
        defaultAvatarImg = new Image(this.getClass().getResourceAsStream("/img/no-profile.png"));
        loginPrefs = Preferences.userNodeForPackage(getClass());
    }

    @FXML
    @SuppressWarnings("unchecked")
    public void initialize() {

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

        handleRememberMe();
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
            doSignIn(emailTxt.getText().toLowerCase().trim(),passwordTxt.getText());
        }
    }

    private void handleServerMessage(ServerResponseMessage msg) {
        Platform.runLater(() -> {
            try {
                if (msg.getType() == LOGIN_SUCCESS) {
                    if (rememberMeCb.isSelected()) {
                        loginPrefs.put(PREFS_EMAIL, emailTxt.getText());
                        loginPrefs.put(PREFS_PASS, passwordTxt.getText());
                        loginPrefs.putBoolean(PREFS_REMEMBER_ME, true);
                    }

                    ApplicationData.getInstance().setContacts(((BootData) msg.getData()).getContactList());
                    NavigationUtils.openMainWindow(stage, springContext);
                    doneLoading();
                } else if (msg.getType() == LOGIN_FAILED) {
                    var errorData = (ErrorData) msg.getData();
                    String message;
                    switch (errorData.getErrorType()) {
                        case USER_NOT_FOUND:
                            message = translationService.getMessage("jmsn.login.error.user-not-found");
                            removePrefs();
                            break;
                        case PASSWORD_NOT_MATCHING:
                            message = translationService.getMessage("jmsn.login.error.password-not-matching");
                            removePrefs();
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

    private void doSignIn(String email, String password) {
        loading();
        Task<String> publicIpFinderTask = Tasks.findPublicIp(ipFinderService);
        publicIpFinderTask.setOnFailed(event -> DialogUtils.errorDialog(translationService.getMessage("jmsn.error.something.wrong")));
        publicIpFinderTask.setOnSucceeded(result -> {
            var data = new LoginData(email, password, publicIpFinderTask.getValue());

            Task<Void> sendMessageTask = Tasks.sendMessageToServer(server, new ServerRequestMessage(LOGIN, data));
            sendMessageTask.setOnFailed(event1 -> DialogUtils.errorDialog(translationService.getMessage("jmsn.error.something.wrong")));
            Tasks.start(sendMessageTask);
        });
        Tasks.start(publicIpFinderTask);
    }

    private void handleRememberMe() {
        rememberMeCb.setSelected(loginPrefs.getBoolean(PREFS_REMEMBER_ME, false));

        if (loginPrefs.getBoolean(PREFS_REMEMBER_ME, false)) {
            var email = loginPrefs.get(PREFS_EMAIL, "");
            var pass = loginPrefs.get(PREFS_PASS, "");
            emailTxt.setText(email);
            passwordTxt.setText(pass);
            doSignIn(email, pass);
        }
    }

    private void removePrefs() {
        if (loginPrefs.getBoolean(PREFS_REMEMBER_ME, false)) {
           loginPrefs.remove(PREFS_EMAIL);
           loginPrefs.remove(PREFS_PASS);
           loginPrefs.putBoolean(PREFS_REMEMBER_ME, false);

           emailTxt.setText("");
           passwordTxt.setText("");
           rememberMeCb.setSelected(false);
        }
    }
}
