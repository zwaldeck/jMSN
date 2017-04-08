package be.zwaldeck.msn.server.util;

import be.zwaldeck.msn.common.messages.data.UserData;
import be.zwaldeck.msn.server.domain.Contact;
import be.zwaldeck.msn.server.domain.User;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Wout Schoovaerts
 */
public class DataConverter {

    public static UserData convertUser(User user) {
        return new UserData(user.getId(), user.getNickname(), user.getStatus(), user.getIp(), user.getSubNickname());
    }

    public static List<User> mapContactListToUserList(List<Contact> contacts) {
        return contacts.stream()
                .map(Contact::getContact)
                .collect(Collectors.toList());
    }
}
