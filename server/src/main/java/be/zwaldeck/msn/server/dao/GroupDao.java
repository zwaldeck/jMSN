package be.zwaldeck.msn.server.dao;

import be.zwaldeck.msn.server.domain.Group;
import be.zwaldeck.msn.server.domain.User;

/**
 * @Author Wout Schoovaerts
 */
public interface GroupDao {
    Group create(Group group);
}
