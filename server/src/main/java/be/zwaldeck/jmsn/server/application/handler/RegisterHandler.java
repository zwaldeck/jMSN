package be.zwaldeck.jmsn.server.application.handler;

import be.zwaldeck.jmsn.common.message.request.data.RegisterData;
import be.zwaldeck.jmsn.common.message.response.ServerResponseMessage;
import org.springframework.stereotype.Component;

@Component
public class RegisterHandler {

    public ServerResponseMessage registerUser(RegisterData data) {
        return new ServerResponseMessage(ServerResponseMessage.ServerResponseMessageType.REGISTER_SUCCESS);
    }
}
