package be.zwaldeck.jmsn.server.service.implementation;

import be.zwaldeck.jmsn.server.dao.ContactDAO;
import be.zwaldeck.jmsn.server.domain.Contact;
import be.zwaldeck.jmsn.server.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
