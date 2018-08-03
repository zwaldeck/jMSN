package be.zwaldeck.jmsn.server.service;

import be.zwaldeck.jmsn.server.domain.User;

import java.util.Optional;

public interface UserService {

    /**
     * @param user without encoded password
     * @return the saved user
     */
    User createUser(User user);

    Optional<User> getUserByEmail(String email);

    User updateUser(User user);
}
