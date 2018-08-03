package be.zwaldeck.jmsn.server.application;

import be.zwaldeck.jmsn.common.message.request.ServerRequestMessage;
import be.zwaldeck.jmsn.common.message.request.data.LoginData;
import be.zwaldeck.jmsn.common.message.request.data.RegisterData;
import be.zwaldeck.jmsn.common.message.response.ServerResponseMessage;
import be.zwaldeck.jmsn.server.application.handler.LoginHandler;
import be.zwaldeck.jmsn.server.application.handler.RegisterHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RequestMessageDispatcher {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final RegisterHandler registerHandler;
    private final LoginHandler loginHandler;

    @Autowired
    public RequestMessageDispatcher(RegisterHandler registerHandler, LoginHandler loginHandler) {
        this.registerHandler = registerHandler;
        this.loginHandler = loginHandler;
    }

    public Optional<ServerResponseMessage> dispatch(ServerRequestMessage requestMessage) {
        log.debug("Dispatching message with type: " + requestMessage.getType());
        switch (requestMessage.getType()) {
            case REGISTER:
                return Optional.of(registerHandler.registerUser((RegisterData) requestMessage.getData()));
            case LOGIN:
                return Optional.of(loginHandler.login((LoginData) requestMessage.getData()));
        }

        return Optional.empty();
    }
}
