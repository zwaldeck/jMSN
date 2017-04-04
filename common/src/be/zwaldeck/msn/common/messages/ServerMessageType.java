package be.zwaldeck.msn.common.messages;

/**
 * @Author Wout Schoovaerts
 */
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
