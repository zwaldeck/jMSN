package be.zwaldeck.jmsn.server.service.implementation;

import be.zwaldeck.jmsn.server.dao.UserDAO;
import be.zwaldeck.jmsn.server.domain.User;
import be.zwaldeck.jmsn.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDAO userDAO, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createUser(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userDAO.saveAndFlush(user);
    }
}
