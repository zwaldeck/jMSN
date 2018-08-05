package be.zwaldeck.jmsn.server.service;

import be.zwaldeck.jmsn.server.domain.Contact;
import be.zwaldeck.jmsn.server.domain.User;

import java.util.List;

public interface ContactService {

    Contact createContact(Contact contact);

    List<User> getContactsForOwner(User owner);
}
