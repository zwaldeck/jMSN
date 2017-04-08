package be.zwaldeck.msn.server.server.handler;

import be.zwaldeck.msn.common.Status;
import be.zwaldeck.msn.common.messages.ServerMessage;
import be.zwaldeck.msn.common.messages.ServerMessageType;
import be.zwaldeck.msn.common.messages.data.UserData;
import be.zwaldeck.msn.server.dao.ContactDao;
import be.zwaldeck.msn.server.dao.UserDao;
import be.zwaldeck.msn.server.domain.User;
import be.zwaldeck.msn.server.util.DataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Wout Schoovaerts
 */
@Component("bootHandler")
public class BootHandler {

    private final UserDao userDao;
    private final ContactDao contactDao;

    @Autowired
    public BootHandler(UserDao userDao, ContactDao contactDao) {
        this.userDao = userDao;
        this.contactDao = contactDao;
    }

    /**
     * @param user logged in user
     * @param output output stream
     */
    public void boot(User user, ObjectOutputStream output) throws IOException {
        List<UserData> contacts = DataConverter.mapContactListToUserList(contactDao.getContacts(user)).stream()
                .map(DataConverter::convertUser)
                .collect(Collectors.toList());

        output.writeObject(new ServerMessage(ServerMessageType.BOOT, contacts));
    }

    public void close(User user) {
        user.setStatus(Status.OFFLINE);
        userDao.update(user);
    }
}
