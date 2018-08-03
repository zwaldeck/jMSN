package be.zwaldeck.jmsn.client.net.ip;

import java.util.Optional;

public interface IPResolver {

    Optional<String> loadIP();
}
