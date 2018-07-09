package be.zwaldeck.jmsn.common.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class ServerMessage implements Serializable {

    private ServerMessageType type;
    private Serializable data;


    public enum ServerMessageType {
        REGISTER,
        REGISTER_FAILED,
        REGISTER_SUCCESS,
        LOGIN,
        LOGIN_FAILED,
        LOGIN_SUCCESS,
        BOOT,
        UPDATE_CONTACT,
        ADD_CONTACT,
        ADD_CONTACT_SUCCESS,
        ADD_CONTACT_FAILED,
        REMOVE_CONTACT,
        UPDATE_NICKNAME,
        UPDATE_STATUS,
        UPDATE_PROFILE_PIC,
        CLIENT_DISCONNECT
    }
}
