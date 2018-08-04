package be.zwaldeck.jmsn.client.net.service;

import be.zwaldeck.jmsn.client.net.exception.IPResolverException;
import be.zwaldeck.jmsn.client.net.ip.*;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class IpFinderService {

    private List<IPResolver> publicResolvers = new ArrayList<>();

    @PostConstruct
    public void postConstruct() {
        publicResolvers.add(new AmazonIPResolver());
        publicResolvers.add(new ICanHazIPResolver());
        publicResolvers.add(new IfConfigMeResolver());
        publicResolvers.add(new IPifyResolver());
    }

    public String findPublicIP() throws IPResolverException {
        for (var resolver : publicResolvers) {
            var result = resolver.loadIP();
            if (result.isPresent()) {
                return result.get().trim();
            }
        }

        throw new IPResolverException();
    }

}
