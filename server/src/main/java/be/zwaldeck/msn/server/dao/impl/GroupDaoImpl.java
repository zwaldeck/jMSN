package be.zwaldeck.msn.server.dao.impl;

import be.zwaldeck.msn.server.dao.GroupDao;
import be.zwaldeck.msn.server.dao.UserDao;
import be.zwaldeck.msn.server.domain.Group;
import be.zwaldeck.msn.server.domain.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * @Author Wout Schoovaerts
 */
@Repository("groupDao")
@Transactional
public class GroupDaoImpl implements GroupDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Group create(Group group) {
        em.persist(group);
        em.flush();
        return group;
    }
}
