package be.zwaldeck.jmsn.common.message.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class ServerRequestMessage implements Serializable {

    private static final long serialVersionUID = -1383956634827977551L;
    private ServerRequestMessageType type;
    private Serializable data;

    public enum ServerRequestMessageType {
        REGISTER,
        LOGIN,
        BOOT,
        UPDATE_CONTACT,
        ADD_CONTACT,
        REMOVE_CONTACT,
        UPDATE_NICKNAME,
        UPDATE_STATUS,
        UPDATE_PROFILE_PIC,
        CLIENT_DISCONNECT
    }
}
