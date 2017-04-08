package be.zwaldeck.msn.server.server.handler;

import be.zwaldeck.msn.common.messages.ServerMessage;
import be.zwaldeck.msn.common.messages.ServerMessageType;
import be.zwaldeck.msn.common.messages.data.AddContactData;
import be.zwaldeck.msn.server.dao.ContactDao;
import be.zwaldeck.msn.server.dao.UserDao;
import be.zwaldeck.msn.server.domain.Contact;
import be.zwaldeck.msn.server.domain.User;
import be.zwaldeck.msn.server.util.DataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * @Author Wout Schoovaerts
 */
@Component("contactHelper")
public class ContactHandler {

    private final UserDao userDao;
    private final ContactDao contactDao;

    @Autowired
    public ContactHandler(UserDao userDao, ContactDao contactDao) {
        this.userDao = userDao;
        this.contactDao = contactDao;
    }

    public void addContact(ServerMessage sm, User loggedInUser, ObjectOutputStream output) throws IOException {
        AddContactData contactData = (AddContactData) sm.getData();
        User userToAdd = userDao.getUserByEmail(contactData.getEmail());
        if (userToAdd == null) {
            output.writeObject(new ServerMessage(ServerMessageType.ADD_CONTACT_FAILED, "We could not find that user."));
            return;
        }

        for (User u : DataConverter.mapContactListToUserList(contactDao.getContacts(loggedInUser))) {
            if (u.getId().intValue() == userToAdd.getId().intValue()) {
                output.writeObject(new ServerMessage(ServerMessageType.ADD_CONTACT_FAILED, "You already added this user to your contacts."));
                return;
            }
        }

        Contact contact = new Contact();
        contact.setOwner(loggedInUser);
        contact.setContact(userToAdd);

        contactDao.create(contact);

        output.writeObject(new ServerMessage(ServerMessageType.ADD_CONTACT_SUCCESS, null));
    }
}
