package be.zwaldeck.jmsn.server.application;

import be.zwaldeck.jmsn.common.message.request.ServerRequestMessage;
import be.zwaldeck.jmsn.common.message.response.ServerResponseMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;

public class ClientThread extends Thread {

    private final Server server;
    private final Socket socket;
    private final RequestMessageDispatcher requestMessageDispatcher;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    private String id;
    private boolean keepGoing = false;

    public ClientThread(Server server, Socket socket, RequestMessageDispatcher requestMessageDispatcher) {
        this.server = server;
        this.socket = socket;
        this.requestMessageDispatcher = requestMessageDispatcher;
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

        ServerRequestMessage sm;
        while (keepGoing) {
            sm = receiveMessage();

            if (sm != null && sm.getType() != ServerRequestMessage.ServerRequestMessageType.CLIENT_DISCONNECT) {
                requestMessageDispatcher
                        .dispatch(sm)
                        .ifPresent(this::sendMessage);
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

    private ServerRequestMessage receiveMessage() {
        try {
            return (ServerRequestMessage) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void sendMessage(ServerResponseMessage message) {
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
