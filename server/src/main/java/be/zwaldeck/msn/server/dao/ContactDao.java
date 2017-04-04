package be.zwaldeck.msn.server.dao;

import be.zwaldeck.msn.server.domain.Contact;
import be.zwaldeck.msn.server.domain.User;

/**
 * @Author Wout Schoovaerts
 */
public interface ContactDao {
    Contact create(Contact contact);
    void delete(Contact contact);
}
