package be.zwaldeck.msn.server.server.handler;

import be.zwaldeck.msn.common.Status;
import be.zwaldeck.msn.common.messages.ServerMessage;
import be.zwaldeck.msn.common.messages.ServerMessageType;
import be.zwaldeck.msn.common.messages.data.LoginData;
import be.zwaldeck.msn.common.messages.data.RegisterData;
import be.zwaldeck.msn.server.dao.GroupDao;
import be.zwaldeck.msn.server.dao.UserDao;
import be.zwaldeck.msn.server.domain.Group;
import be.zwaldeck.msn.server.domain.User;
import be.zwaldeck.msn.server.util.DataConverter;
import be.zwaldeck.msn.server.util.NetworkUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * @Author Wout Schoovaerts
 */
@Component("registrationHandler")
public class RegistrationLoginHandler {

    private final UserDao userDao;
    private final GroupDao groupDao;

    @Autowired
    public RegistrationLoginHandler(UserDao userDao, GroupDao groupDao) {
        this.userDao = userDao;
        this.groupDao = groupDao;
    }

    public void register(ServerMessage sm, ObjectOutputStream output) throws IOException {
        //TODO Email activation shit
        String ip = NetworkUtils.getExternalIp();
        if (ip == null) {
            output.writeObject(new ServerMessage(ServerMessageType.REGISTER_FAILED, "We could not fetch your ip.\nWe need this in order to let you chat with your friends."));
            return;
        }

        RegisterData data = (RegisterData) sm.getData();
        if (userDao.getUserByEmail(data.getEmail()) != null) {
            output.writeObject(new ServerMessage(ServerMessageType.REGISTER_FAILED, "This email is already used."));
            return;
        }

        User user = new User();
        user.setEmail(data.getEmail());
        user.setName(data.getName());
        user.setPassword(data.getPassword());
        user.setIp(ip);
        user.setNickname(data.getName());
        user.setSubNickname("");
        user.setStatus(Status.OFFLINE);
        userDao.create(user);

        //creating 2 default groups (Favorites & Friends)
        Group favo = new Group();
        favo.setName("Favorites");
        favo.setUser(user);

        Group friends = new Group();
        friends.setName("Friends");
        friends.setUser(user);

        groupDao.create(favo);
        groupDao.create(friends);

        output.writeObject(new ServerMessage(ServerMessageType.REGISTER_SUCCESS, ""));
    }

    /**
     * @param sm serverMessage
     * @param output output stream
     * @return the logged in user
     * @throws IOException just catch it
     */
    public User login(ServerMessage sm, ObjectOutputStream output) throws IOException {
        LoginData data = (LoginData) sm.getData();
        User user = userDao.getUserByEmail(data.getEmail());
        if (user == null) {
            output.writeObject(new ServerMessage(ServerMessageType.LOGIN_FAILED, "We could not find a user with this email."));
            return null;
        }

        if (!BCrypt.checkpw(data.getPassword(), user.getPassword())) {
            output.writeObject(new ServerMessage(ServerMessageType.LOGIN_FAILED, "The password was not correct."));
            return null;
        }

        //update the status and ip
        String ip = NetworkUtils.getExternalIp();
        if (ip == null) {
            output.writeObject(new ServerMessage(ServerMessageType.LOGIN_FAILED, "We could not fetch your ip.\nWe need this in order to let you chat with your friends."));
            return null;
        }
        user.setIp(ip);
        user.setStatus(Status.ONLINE);
        userDao.update(user);

        output.writeObject(new ServerMessage(ServerMessageType.LOGIN_SUCCESS, DataConverter.convertUser(user)));

        return user;
    }
}
