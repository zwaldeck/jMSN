package be.zwaldeck.msn.client.net;

import be.zwaldeck.msn.client.controller.GuiController;
import be.zwaldeck.msn.common.Constants;
import be.zwaldeck.msn.common.messages.ServerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @Author Wout Schoovaerts
 *
 * Singleton because we only need one instance over the whole application
 */
public class ServerConnection {

    private static ServerConnection instance = null;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket socket;
    private GuiController controller;

    public static ServerConnection getInstance(GuiController controller) {
        if(instance == null) {
            instance = new ServerConnection();
            if(!instance.start()) {
                controller.displayErrorMessage("Could not connect to the server. jMSN exiting!");
                System.exit(0);
            }
        }

        instance.controller = controller;

        return instance;
    }

    private ServerConnection() {
        this.controller = null;
    }


    public boolean start() {
        try {
            socket = new Socket(Constants.SERVER_IP, Constants.PORT);
        } catch (IOException e) {
            controller.displayErrorMessage("We could not make connection with the server. Please try again.");
            return false;
        }

        try {
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());
        }catch (IOException ex) {
            controller.displayErrorMessage("There went something wrong. Please try again.");
            return false;
        }

        return true;
    }

    public void sendMessage(ServerMessage sm) {
        try {
            output.writeObject(sm);
        } catch (IOException e) {
            controller.displayErrorMessage("There went something wrong. Please try again.");
        }
    }

    public ServerMessage waitForMessageFromServer() {
        try {
            return (ServerMessage) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            controller.displayErrorMessage("Server connection was closed. Please try again later.");
            e.printStackTrace();
            return null;
        }
    }

    public ObjectInputStream getInput() {
        return input;
    }

    public ObjectOutputStream getOutput() {
        return output;
    }

    public void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
