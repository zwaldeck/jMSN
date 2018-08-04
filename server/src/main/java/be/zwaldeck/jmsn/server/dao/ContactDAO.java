package be.zwaldeck.jmsn.server.dao;

import be.zwaldeck.jmsn.server.domain.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactDAO extends JpaRepository<Contact, String> {

}
