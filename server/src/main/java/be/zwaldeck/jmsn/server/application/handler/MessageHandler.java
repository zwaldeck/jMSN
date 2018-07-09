package be.zwaldeck.jmsn.server.application.handler;

import be.zwaldeck.jmsn.common.message.ServerMessage;
import be.zwaldeck.jmsn.server.application.ClientThread;
import org.springframework.stereotype.Component;

@Component
public class MessageHandler {

    public void handleMessage(ServerMessage serverMessage) {
        switch (serverMessage.getType()) {

        }
    }
}
