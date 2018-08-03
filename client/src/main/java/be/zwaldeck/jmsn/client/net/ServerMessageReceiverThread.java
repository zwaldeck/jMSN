package be.zwaldeck.jmsn.client.net;

import be.zwaldeck.jmsn.common.message.response.ServerResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ObjectInputStream;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServerMessageReceiverThread implements Runnable {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private final ObjectInputStream input;
    private Thread worker;
    private final AtomicBoolean running = new AtomicBoolean(false);

    private ReceiveMessageCallback receiveCallback;
    private ReceiveErrorCallback errorCallback;

    public ServerMessageReceiverThread(ObjectInputStream input) {
        this.input = input;
    }

    public void start() {
        worker = new Thread(this);
        running.set(true);
        worker.start();
    }

    public void stop() {
        running.set(false);
    }

    @Override
    public void run() {
        while (running.get()) {
            try {
                var srm = (ServerResponseMessage) input.readObject();
                log.debug("Received response from server with type: " + srm.getType());
                receiveCallback.messageReceived(srm);
            } catch (Exception e) {
                errorCallback.error(e);
            }
        }
    }

    public void setCallbacks(ReceiveMessageCallback receiveCallback, ReceiveErrorCallback errorCallback) {
        this.receiveCallback = receiveCallback;
        this.errorCallback = errorCallback;
    }
}
