package be.zwaldeck.msn.common.messages;

import java.io.Serializable;

/**
 * @Author Wout Schoovaerts
 *
 * This object is used to communicate to and from the server
 */
public class ServerMessage implements Serializable {

    private ServerMessageType type;
    private Object data;

    public ServerMessage(ServerMessageType type, Object data) {
        this.type = type;
        this.data = data;
    }

    public ServerMessageType getType() {
        return type;
    }

    public Object getData() {
        return data;
    }
}
