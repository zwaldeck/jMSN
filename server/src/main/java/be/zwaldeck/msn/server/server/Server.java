package be.zwaldeck.msn.server.server;

import be.zwaldeck.msn.common.Constants;
import be.zwaldeck.msn.server.MsnServer;
import be.zwaldeck.msn.server.dao.ContactDao;
import be.zwaldeck.msn.server.dao.UserDao;
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

    @Autowired
    private UserDao userDao;

    @Autowired
    private ContactDao contactDao;

    private boolean keepGoing;
    private ArrayList<ClientThread> clients;

    public Server() {
        clients = new ArrayList<>();
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

                ClientThread ct = new ClientThread(this, socket, userDao, contactDao);
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
