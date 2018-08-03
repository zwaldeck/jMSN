package be.zwaldeck.jmsn.server.application;

import be.zwaldeck.jmsn.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

@Component
public class Server {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private boolean keepGoing;
    private ArrayList<ClientThread> clients = new ArrayList<>();

    private final RequestMessageDispatcher requestMessageDispatcher;

    @Autowired
    public Server(RequestMessageDispatcher requestMessageDispatcher) {
        this.requestMessageDispatcher = requestMessageDispatcher;
    }

    public void start() {
        keepGoing = true;
        try {
            ServerSocket serverSocket = new ServerSocket(Constants.SERVER_PORT);
            while (keepGoing) {
                log.info("Server is waiting for clients on port " + Constants.SERVER_PORT);

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

        log.info("Server is closed");
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
        log.info(msg);
    }

    public synchronized void remove(String id) {
        log.debug("Removing client with id: " + id);
        clients.removeIf(client -> client.getClientId().equalsIgnoreCase(id));
    }

    private void createClient(Socket clientSocket) {
        var client = new ClientThread(this, clientSocket, requestMessageDispatcher);

        if (client.init()) {
            clients.add(client);
            client.start();
        } else {
            // TODO: Send message to client that there went something wrong
        }
    }
}
