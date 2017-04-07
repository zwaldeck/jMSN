package be.zwaldeck.msn.client.service;

import be.zwaldeck.msn.client.data.ApplicationData;
import be.zwaldeck.msn.client.net.ServerConnection;
import be.zwaldeck.msn.common.messages.ServerMessage;
import be.zwaldeck.msn.common.messages.ServerMessageType;
import be.zwaldeck.msn.common.messages.data.UserData;
import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.ArrayList;

/**
 * @Author Wout Schoovaerts
 */
public class BootService extends Service<Boolean> {

    private ServerConnection connection;

    public BootService(ServerConnection connection) {

        this.connection = connection;
    }

    @Override
    protected Task<Boolean> createTask() {
        return new Task<Boolean>() {

            @Override
            protected Boolean call() throws Exception {
                connection.sendMessage(new ServerMessage(ServerMessageType.BOOT, null));
                ServerMessage sm = connection.waitForMessageFromServer();
                if(sm.getType() == ServerMessageType.BOOT) {
                    ApplicationData.getInstance().setContacts((ArrayList<UserData>) sm.getData());
                    return true;
                }
                return false;
            }
        };
    }
}
