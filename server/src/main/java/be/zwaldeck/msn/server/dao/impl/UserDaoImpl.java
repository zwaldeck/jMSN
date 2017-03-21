package be.zwaldeck.msn.server.dao.impl;

import be.zwaldeck.msn.server.dao.UserDao;
import be.zwaldeck.msn.server.domain.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * @Author Wout Schoovaerts
 */
@Repository("userDao")
@Transactional
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public User create(User user) {
        em.persist(user);
        em.flush();
        return user;
    }

    @Override
    public User update(User user) {
        user = em.merge(user);
        em.flush();
        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE email = :email", User.class)
                    .setParameter("email", email).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
}
