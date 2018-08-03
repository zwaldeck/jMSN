package be.zwaldeck.jmsn.client.net;

import be.zwaldeck.jmsn.common.message.response.ServerResponseMessage;

public interface ReceiveMessageCallback {
    void messageReceived(ServerResponseMessage msg);
}
