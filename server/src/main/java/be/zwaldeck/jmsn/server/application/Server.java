package be.zwaldeck.jmsn.server.application;

import be.zwaldeck.jmsn.common.Constants;
import be.zwaldeck.jmsn.server.application.handler.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

@Component
public class Server {

    private boolean keepGoing;
    private ArrayList<ClientThread> clients = new ArrayList<>();

    private final MessageHandler messageHandler;

    @Autowired
    public Server(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public void start() {
        keepGoing = true;
        try {
            ServerSocket serverSocket = new ServerSocket(Constants.SERVER_PORT);
            while (keepGoing) {
                System.out.println("Server is waiting for clients on port " + Constants.SERVER_PORT);

                Socket socket = serverSocket.accept();
                if (!keepGoing) {
                    break;
                }

                createClient(socket);
            }

            serverSocket.close();
            clients.forEach(ClientThread::close);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void stop() {
        keepGoing = false;
        // connect to myself as Client to exit statement
        // Socket socket = serverSocket.accept();
        try {
            new Socket("localhost", Constants.SERVER_PORT);
        } catch (Exception e) {
            // nothing I can really do
        }
    }

    public void displayError(String error) {
        System.err.println(error);
    }

    public void displayInfo(String msg) {
        System.out.println(msg);
    }

    public synchronized void remove(String id) {
        clients.removeIf(client -> client.getClientId().equalsIgnoreCase(id));
    }

    private void createClient(Socket clientSocket) {
        var client = new ClientThread(this, clientSocket, messageHandler);

        if (client.init()) {
            clients.add(client);
            client.start();
        } else {
            // TODO: Send message to client that there went something wrong
        }
    }
}
