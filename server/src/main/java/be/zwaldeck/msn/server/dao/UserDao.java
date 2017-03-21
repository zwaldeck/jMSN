package be.zwaldeck.msn.server.dao;

import be.zwaldeck.msn.server.domain.User;

/**
 * @Author Wout Schoovaerts
 */
public interface UserDao {
    User create(User user);
    User getUserByEmail(String email);
    User update(User user);
}
