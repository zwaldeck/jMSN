package be.zwaldeck.msn.server.dao;

import be.zwaldeck.msn.server.domain.Contact;
import be.zwaldeck.msn.server.domain.User;

import java.util.List;

/**
 * @Author Wout Schoovaerts
 */
public interface ContactDao {
    Contact create(Contact contact);
    List<Contact> getContacts(User owner);
    void delete(Contact contact);
}
