package be.zwaldeck.jmsn.server.application;

import be.zwaldeck.jmsn.common.message.ServerMessage;
import be.zwaldeck.jmsn.server.application.handler.MessageHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;

public class ClientThread extends Thread {

    private final Server server;
    private final Socket socket;
    private final MessageHandler messageHandler;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    private String id;
    private boolean keepGoing = false;

    public ClientThread(Server server, Socket socket, MessageHandler messageHandler) {
        this.server = server;
        this.socket = socket;
        this.messageHandler = messageHandler;
    }

    public boolean init() {
        id = UUID.randomUUID().toString();

        server.displayInfo("Client connected with id: " + id);

        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            server.displayError("Exception creating input/output streams: " + e.getMessage());
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void run() {
        keepGoing = true;

        ServerMessage sm;
        while (keepGoing) {
            sm = null;
            try {
                sm = (ServerMessage) input.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            if (sm != null && sm.getType() != ServerMessage.ServerMessageType.CLIENT_DISCONNECT) {
                messageHandler.handleMessage(sm);
            } else {
                keepGoing = false;
            }
        }

        close();
    }

    public void close() {
        keepGoing = false;
        server.displayInfo("Client disconnected with id " + id);
        try {
            if (output != null) {
                output.close();
            }

            if (input != null) {
                input.close();
            }

            if (socket != null) {
                socket.close();
            }
        } catch (IOException ignored) {
            // nothing I can handle here
        }

        // TODO: service that sets the user's status to OFFLINE
        server.remove(id);
    }

    public String getClientId() {
        return id;
    }
}
