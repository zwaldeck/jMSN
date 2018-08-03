package be.zwaldeck.jmsn.client.net.ip;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class IPifyResolver implements IPResolver {

    @Override
    public Optional<String> loadIP() {
        try {
            return Optional.of(IOUtils.toString( new URL("http://api.ipify.org"), StandardCharsets.UTF_8));
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
