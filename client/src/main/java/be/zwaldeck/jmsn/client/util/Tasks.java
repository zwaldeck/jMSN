package be.zwaldeck.jmsn.client.util;

import be.zwaldeck.jmsn.client.net.ServerConnection;
import be.zwaldeck.jmsn.common.message.request.ServerRequestMessage;
import javafx.concurrent.Task;

public final class Tasks {

    public static Task<Void> sendMessageToServer(ServerConnection connection, ServerRequestMessage srm) {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                connection.sendMessage(srm);
                return null;
            }
        };
    }

    public static void start(Task task) {
        new Thread(task).start();
    }
}
