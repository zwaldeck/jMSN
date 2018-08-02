package be.zwaldeck.jmsn.server.application.handler;

import be.zwaldeck.jmsn.common.Status;
import be.zwaldeck.jmsn.common.message.request.data.RegisterData;
import be.zwaldeck.jmsn.common.message.response.ServerResponseMessage;
import be.zwaldeck.jmsn.common.message.response.error.ErrorData;
import be.zwaldeck.jmsn.common.utils.PasswordUtils;
import be.zwaldeck.jmsn.server.domain.User;
import be.zwaldeck.jmsn.server.service.UserService;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static be.zwaldeck.jmsn.common.message.response.error.ErrorData.ErrorType.EMAIL_IN_USE;
import static be.zwaldeck.jmsn.common.message.response.error.ErrorData.ErrorType.PASSWORD_NOT_STRONG_ENOUGH;

@Component
public class RegisterHandler {

    private final UserService userService;

    @Autowired
    public RegisterHandler(UserService userService) {
        this.userService = userService;
    }

    public ServerResponseMessage registerUser(RegisterData data) {

        var email = data.getEmail().toLowerCase();

        if (!EmailValidator.getInstance().isValid(email)) {
            return new ServerResponseMessage(
                    ServerResponseMessage.ServerResponseMessageType.REGISTER_FAILED,
                    new ErrorData(ErrorData.ErrorType.INVALID_EMAIL, "The email is invalid")
            );
        }

        if (PasswordUtils.getPasswordStrength(data.getPassword()) < 75) {
            return new ServerResponseMessage(
                    ServerResponseMessage.ServerResponseMessageType.REGISTER_FAILED,
                    new ErrorData(PASSWORD_NOT_STRONG_ENOUGH, "The password is not strong enough")
            );
        }

        if (userService.getUserByEmail(email).isPresent()) {
            return new ServerResponseMessage(
                    ServerResponseMessage.ServerResponseMessageType.REGISTER_FAILED,
                    new ErrorData(EMAIL_IN_USE, "This email is already used")
            );
        }

        var user = new User();
        user.setEmail(email);
        user.setPassword(data.getPassword());
        user.setNickname(data.getEmail().substring(0, data.getEmail().indexOf('@')));
        user.setIp("127.0.0.1");
        user.setStatus(Status.OFFLINE);
        user.setSubNickname("");

        userService.createUser(user);

        return new ServerResponseMessage(ServerResponseMessage.ServerResponseMessageType.REGISTER_SUCCESS);
    }
}
