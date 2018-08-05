package be.zwaldeck.jmsn.server.service.implementation;

import be.zwaldeck.jmsn.server.dao.ContactDAO;
import be.zwaldeck.jmsn.server.domain.Contact;
import be.zwaldeck.jmsn.server.domain.User;
import be.zwaldeck.jmsn.server.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactDAO contactDAO;

    @Autowired
    public ContactServiceImpl(ContactDAO contactDAO) {
        this.contactDAO = contactDAO;
    }

    @Override
    public Contact createContact(Contact contact) {
        return contactDAO.saveAndFlush(contact);
    }

    @Override
    public List<User> getContactsForOwner(User owner) {
        return contactDAO.findAllByOwner(owner)
                .stream()
                .map(Contact::getContact)
                .collect(Collectors.toList());
    }
}
