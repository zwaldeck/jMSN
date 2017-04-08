package be.zwaldeck.msn.server.server;

import be.zwaldeck.msn.common.Status;
import be.zwaldeck.msn.common.messages.ServerMessage;
import be.zwaldeck.msn.common.messages.ServerMessageType;
import be.zwaldeck.msn.common.messages.data.AddContactData;
import be.zwaldeck.msn.common.messages.data.LoginData;
import be.zwaldeck.msn.common.messages.data.UserData;
import be.zwaldeck.msn.server.domain.Contact;
import be.zwaldeck.msn.server.domain.User;
import be.zwaldeck.msn.server.server.handler.BootHandler;
import be.zwaldeck.msn.server.server.handler.ContactHandler;
import be.zwaldeck.msn.server.server.handler.RegistrationLoginHandler;
import be.zwaldeck.msn.server.util.DataConverter;
import be.zwaldeck.msn.server.util.NetworkUtils;
import org.mindrot.jbcrypt.BCrypt;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Author Wout Schoovaerts
 */
public class ClientThread extends Thread {

    private Server server;
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    private String id;
    private ServerMessage sm;
    private boolean keepGoing = false;

    private User user; //the user associated with this thread after login

    //handlers
    private final RegistrationLoginHandler registrationLoginHandler;
    private final BootHandler bootHandler;
    private final ContactHandler contactHandler;

    //inject all handlers in the constructor
    public ClientThread(Server server, Socket socket,
                        RegistrationLoginHandler registrationLoginHandler,
                        BootHandler bootHandler, ContactHandler contactHandler) {
        this.server = server;
        this.socket = socket;

        //handlers
        this.registrationLoginHandler = registrationLoginHandler;
        this.bootHandler = bootHandler;
        this.contactHandler = contactHandler;

        id = UUID.randomUUID().toString();

        System.out.println("Client connected with id: " + id);

        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            server.displayError("Exception creating new Input/output Streams: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        keepGoing = true;
        while (keepGoing) {
            sm = null;
            try {
                sm = (ServerMessage) input.readObject();
            } catch (IOException e) {
                if (!(e instanceof EOFException)) {
                    e.printStackTrace();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                break;
            }

            if (sm != null) {
                try {
                    handleServerMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                keepGoing = false;
            }
        }

        close();
        server.remove(id);
    }

    public void close() {
        System.out.println("Client disconnected: " + id);
        try {
            if (output != null)
                output.close();
            if (input != null)
                input.close();
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (user != null) {
            bootHandler.close(user);
        }
    }

    public String getClientId() {
        return id;
    }

    /**
     * Here we handle the server messages that can be handled by the server
     *
     * TODO split up in handler classes
     */
    private void handleServerMessage() throws IOException {
        System.out.println("Handeling message for " + id + " with type: " + sm.getType().toString());
        switch (sm.getType()) {
            case REGISTER:
                registrationLoginHandler.register(sm, output);
                break;
            case LOGIN:
                this.user = registrationLoginHandler.login(sm, output);
                break;
            case BOOT:
                bootHandler.boot(user, output);
                break;
            case ADD_CONTACT:
                contactHandler.addContact(sm, user, output);
                handleAddContact();
                break;
            case REMOVE_CONTACT:
                break;
            case UPDATE_NICKNAME:
                break;
            case UPDATE_STATUS:
                break;
            case UPDATE_PROFILE_PIC:
                break;
            case CLIENT_DISCONNECT:
                keepGoing = false;
                close();
                server.remove(id);
                break;
        }
    }

    private void handleAddContact() throws IOException {

    }


}
