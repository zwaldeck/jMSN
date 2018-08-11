package be.zwaldeck.jmsn.server.application.handler;

import be.zwaldeck.jmsn.common.message.request.data.LoginData;
import be.zwaldeck.jmsn.common.message.response.ServerResponseMessage;
import be.zwaldeck.jmsn.common.message.response.data.BootData;
import be.zwaldeck.jmsn.common.message.response.error.ErrorData;
import be.zwaldeck.jmsn.server.converter.UserDataConverter;
import be.zwaldeck.jmsn.server.service.ContactService;
import be.zwaldeck.jmsn.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import static be.zwaldeck.jmsn.common.message.response.ServerResponseMessage.ServerResponseMessageType.LOGIN_FAILED;
import static be.zwaldeck.jmsn.common.message.response.ServerResponseMessage.ServerResponseMessageType.LOGIN_SUCCESS;
import static be.zwaldeck.jmsn.common.message.response.error.ErrorData.ErrorType.PASSWORD_NOT_MATCHING;
import static be.zwaldeck.jmsn.common.message.response.error.ErrorData.ErrorType.USER_NOT_FOUND;

@Controller
public class LoginHandler {

    private final UserService userService;
    private final ContactService contactService;
    private final UserDataConverter userConverter;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginHandler(UserService userService, ContactService contactService, UserDataConverter userConverter,
                        PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.contactService = contactService;
        this.userConverter = userConverter;
        this.passwordEncoder = passwordEncoder;
    }

    public ServerResponseMessage login(LoginData data) {
        final var response = new ServerResponseMessage();

        userService.getUserByEmail(data.getEmail()).ifPresentOrElse(user -> {
            if (!passwordEncoder.matches(data.getPassword(), user.getPassword())) {
                response.setType(LOGIN_FAILED);
                response.setData(new ErrorData(PASSWORD_NOT_MATCHING, "The password is not correct"));
                return;
            }

            user.setIp(data.getIp());
            user = userService.updateUser(user);
            var contacts = contactService.getContactsForOwner(user);

            var bootData = new BootData();
            bootData.setContactList(userConverter.convertUserList(contacts));
            bootData.setUser(userConverter.convertUser(user));

            response.setType(LOGIN_SUCCESS);
            response.setData(bootData);
        }, () -> {
            response.setType(LOGIN_FAILED);
            response.setData(new ErrorData(USER_NOT_FOUND, "User not found"));
        });

        return response;
    }
}
