package be.zwaldeck.jmsn.server.dao;

import be.zwaldeck.jmsn.server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDAO extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);
}
