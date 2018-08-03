package be.zwaldeck.jmsn.client.net;

import be.zwaldeck.jmsn.client.util.DialogUtils;
import be.zwaldeck.jmsn.common.Constants;
import be.zwaldeck.jmsn.common.message.request.ServerRequestMessage;
import be.zwaldeck.jmsn.common.message.response.ServerResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@Service
public class ServerConnection extends Thread {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket socket;
    private ServerMessageReceiverThread smrt;

    @PostConstruct
    public void init() {
        try {
            socket = new Socket(Constants.SERVER_IP, Constants.SERVER_PORT);
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());
            smrt = new ServerMessageReceiverThread(input);
            smrt.start();
        } catch (IOException e) {
            e.printStackTrace();
            DialogUtils.errorDialog("We could not make connection with the server.");
            System.exit(1);
        }

        log.info("Connected to central server");
    }

    public void sendMessage(ServerRequestMessage sm) {
        log.debug("Sending request to the server with type: " + sm.getType());
        try {
            output.writeObject(sm);
        } catch (IOException e) {
            DialogUtils.errorDialog("There went something wrong. Please try again.");
        }
    }

    public void disconnect() {
        try {
            input.close();
            output.close();
            socket.close();
        } catch (Exception e) {
        } finally {
            input = null;
            output = null;
            socket = null;
        }
    }

    public ServerMessageReceiverThread getServerMessageReceiverThread() {
        return this.smrt;
    }
}
