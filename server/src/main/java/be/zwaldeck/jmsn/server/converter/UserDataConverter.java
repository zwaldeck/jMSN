package be.zwaldeck.jmsn.server.converter;

import be.zwaldeck.jmsn.common.message.response.data.UserData;
import be.zwaldeck.jmsn.server.domain.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserDataConverter {

    public UserData convertUser(User user) {
        var data = new UserData();
        data.setId(user.getId());
        data.setIp(user.getIp());
        data.setNickname(user.getNickname());
        data.setSubNickname(user.getSubNickname());
        data.setStatus(user.getStatus());

        return data;
    }

    public List<UserData> convertUserList(List<User> userList) {
        return userList.stream()
                .map(this::convertUser)
                .collect(Collectors.toList());
    }
}
