package be.zwaldeck.jmsn.client.net.ip;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class IfConfigMeResolver implements IPResolver {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public Optional<String> loadIP() {
        try {
            log.info("Trying to get the public IP with ifconfig endpoint");
            return Optional.of(IOUtils.toString( new URL("http://ifconfig.me/ip"), StandardCharsets.UTF_8));
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
