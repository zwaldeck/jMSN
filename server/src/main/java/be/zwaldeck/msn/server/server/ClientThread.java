package be.zwaldeck.msn.server.server;

import be.zwaldeck.msn.common.Status;
import be.zwaldeck.msn.common.messages.ServerMessage;
import be.zwaldeck.msn.common.messages.ServerMessageType;
import be.zwaldeck.msn.common.messages.data.LoginData;
import be.zwaldeck.msn.common.messages.data.RegisterData;
import be.zwaldeck.msn.common.messages.data.UserData;
import be.zwaldeck.msn.server.dao.UserDao;
import be.zwaldeck.msn.server.domain.Contact;
import be.zwaldeck.msn.server.domain.User;
import be.zwaldeck.msn.server.util.DataConverter;
import be.zwaldeck.msn.server.util.NetworkUtils;
import org.mindrot.jbcrypt.BCrypt;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

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
    private UserDao userDao;
    private User user; //the user associated with this thread after login

    public ClientThread(Server server, Socket socket, UserDao userDao) {
        this.server = server;
        this.socket = socket;
        this.userDao = userDao;
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
                if(!(e instanceof EOFException)) {
                    e.printStackTrace();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                break;
            }

            if(sm != null) {
                try {
                    handleServerMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
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

        if(user != null) {
            user.setStatus(Status.OFFLINE);
            userDao.update(user);
        }
    }

    public String getClientId() {
        return id;
    }

    /**
     * Here we handle the server messages that can be handled by the server
     */
    private void handleServerMessage() throws IOException {
        System.out.println("Handeling message for " + id + " with type: " + sm.getType().toString());
        switch (sm.getType()) {
            case REGISTER:
                handleRegister();
                break;
            case LOGIN:
                handleLogin();
                break;
            case BOOT:
                handleBoot();
                break;
            case ADD_CONTACTS:
                break;
            case REMOVE_CONTACTS:
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

    private void handleRegister() throws IOException {
        //TODO Email activation shit
        String ip = NetworkUtils.getExternalIp();
        if(ip == null) {
            output.writeObject(new ServerMessage(ServerMessageType.REGISTER_FAILED, "We could not fetch your ip.\nWe need this in order to let you chat with your friends."));
            return;
        }

        RegisterData data = (RegisterData) sm.getData();
        if(userDao.getUserByEmail(data.getEmail()) != null) {
            output.writeObject(new ServerMessage(ServerMessageType.REGISTER_FAILED, "This email is already used."));
            return;
        }

        User user = new User();
        user.setEmail(data.getEmail());
        user.setName(data.getName());
        user.setPassword(data.getPassword());
        user.setIp(ip);
        user.setNickname(data.getName());
        user.setSubNickname("");
        user.setStatus(Status.OFFLINE);

        userDao.create(user);

        output.writeObject(new ServerMessage(ServerMessageType.REGISTER_SUCCESS, ""));
    }

    private void handleLogin() throws IOException {
        LoginData data = (LoginData) sm.getData();
        User user = userDao.getUserByEmail(data.getEmail());
        if(user == null) {
            output.writeObject(new ServerMessage(ServerMessageType.LOGIN_FAILED, "We could not find a user with this email."));
            return;
        }

        if(!BCrypt.checkpw(data.getPassword(), user.getPassword())) {
            output.writeObject(new ServerMessage(ServerMessageType.LOGIN_FAILED, "The password was not correct."));
            return;
        }

        //update the status and ip
        String ip = NetworkUtils.getExternalIp();
        if(ip == null) {
            output.writeObject(new ServerMessage(ServerMessageType.REGISTER_FAILED, "We could not fetch your ip.\nWe need this in order to let you chat with your friends."));
            return;
        }
        user.setIp(ip);
        user.setStatus(Status.ONLINE);
        userDao.update(user);
        this.user = user;

        output.writeObject(new ServerMessage(ServerMessageType.LOGIN_SUCCESS, DataConverter.convertUser(user)));
    }

    private void handleBoot() throws IOException {
        ArrayList<UserData> contacts = new ArrayList<>();
        for(Contact contact : user.getContacts()) {
            contacts.add(DataConverter.convertUser(contact.getContact()));
        }

        output.writeObject(new ServerMessage(ServerMessageType.BOOT, contacts));
    }
}
