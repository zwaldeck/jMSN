package be.zwaldeck.jmsn.server.service;

import be.zwaldeck.jmsn.server.domain.User;

public interface UserService {

    /**
     * @param user without encoded password
     * @return the saved user
     */
    User createUser(User user);
}
