package be.zwaldeck.jmsn.common.message.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServerResponseMessage implements Serializable {

    private static final long serialVersionUID = -6858949614827727453L;
    private ServerResponseMessageType type;
    private Serializable data;

    public ServerResponseMessage(ServerResponseMessageType type) {
        this(type, null);
    }

    public enum ServerResponseMessageType {
        REGISTER_FAILED,
        REGISTER_SUCCESS,
        LOGIN_FAILED,
        LOGIN_SUCCESS,
    }
}
