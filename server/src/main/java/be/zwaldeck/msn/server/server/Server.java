package be.zwaldeck.msn.server.server;

import be.zwaldeck.msn.common.Constants;
import be.zwaldeck.msn.server.server.handler.BootHandler;
import be.zwaldeck.msn.server.server.handler.ContactHandler;
import be.zwaldeck.msn.server.server.handler.RegistrationLoginHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @Author Wout Schoovaerts
 */
@Component
public class Server {

    private final RegistrationLoginHandler registrationLoginHandler;
    private final BootHandler bootHandler;
    private final ContactHandler contactHandler;

    private boolean keepGoing;
    private ArrayList<ClientThread> clients;

    @Autowired
    public Server(RegistrationLoginHandler registrationLoginHandler,
                  BootHandler bootHandler, ContactHandler contactHandler) {
        clients = new ArrayList<>();

        this.registrationLoginHandler = registrationLoginHandler;
        this.bootHandler = bootHandler;
        this.contactHandler = contactHandler;
    }

    public void start() {
        keepGoing = true;
        try {
            ServerSocket serverSocket = new ServerSocket(Constants.PORT);
            while (keepGoing) {
                System.out.println("Server waiting for clients on port " + Constants.PORT + ".");

                Socket socket = serverSocket.accept();
                if(!keepGoing) {
                    break;
                }

                ClientThread ct = new ClientThread(this, socket,
                        registrationLoginHandler,
                        bootHandler,
                        contactHandler
                );
                clients.add(ct);
                ct.start();
            }

            serverSocket.close();
            for(ClientThread ct : clients) {
                ct.close();
            }
        }catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void stop() {
        keepGoing = false;
        // connect to myself as Client to exit statement
        // Socket socket = serverSocket.accept();
        try {
            new Socket("localhost", Constants.PORT);
        } catch (Exception e) {
            // nothing I can really do
        }
    }

    public void displayError(String error) {
        System.err.println(error);
    }

    public synchronized void remove(String id) {
        Iterator<ClientThread> ctIterator = clients.iterator();
        while (ctIterator.hasNext()) {
            ClientThread ct = ctIterator.next();
            if(ct.getClientId().equalsIgnoreCase(id)) {
                clients.remove(ct);
                return;
            }
        }
    }
}
