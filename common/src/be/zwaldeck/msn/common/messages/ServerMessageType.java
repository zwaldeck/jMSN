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
    UPDATE_CONTACTS,
    ADD_CONTACTS,
    REMOVE_CONTACTS,
    UPDATE_NICKNAME,
    UPDATE_STATUS,
    UPDATE_PROFILE_PIC,
    CLIENT_DISCONNECT
}
