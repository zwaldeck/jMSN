package be.zwaldeck.msn.server.dao.impl;

import be.zwaldeck.msn.server.dao.ContactDao;
import be.zwaldeck.msn.server.domain.Contact;
import be.zwaldeck.msn.server.domain.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Wout Schoovaerts
 */
@Repository("contactDao")
@Transactional
public class ContactDaoImpl implements ContactDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Contact create(Contact contact) {
        em.persist(contact);
        em.flush();
        return contact;
    }

    @Override
    public List<Contact> getContacts(User owner) {
        TypedQuery<Contact> query = em.createQuery("SELECT c FROM Contact c WHERE c.owner = :owner ", Contact.class);
        query.setParameter("owner", owner);
        return query.getResultList();
    }

    @Override
    public void delete(Contact contact) {
        Contact ref = em.getReference(Contact.class, em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(contact));
        em.remove(ref);
    }
}
