package be.zwaldeck.jmsn.server.dao;

import be.zwaldeck.jmsn.server.domain.Contact;
import be.zwaldeck.jmsn.server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactDAO extends JpaRepository<Contact, String> {

    List<Contact> findAllByOwner(User owner);
}
