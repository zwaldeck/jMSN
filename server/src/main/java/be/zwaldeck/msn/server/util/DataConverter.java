package be.zwaldeck.msn.server.util;

import be.zwaldeck.msn.common.messages.data.UserData;
import be.zwaldeck.msn.server.domain.User;

/**
 * @Author Wout Schoovaerts
 */
public class DataConverter {

    public static UserData convertUser(User user) {
        return new UserData(user.getId(), user.getNickname(), user.getStatus(), user.getIp(), user.getSubNickname());
    }
}
