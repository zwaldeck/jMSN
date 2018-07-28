package be.zwaldeck.jmsn.server.listener;

import be.zwaldeck.jmsn.server.application.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartupListener implements ApplicationListener<ApplicationReadyEvent> {

    private final Server server;

    @Autowired
    public ApplicationStartupListener(Server server) {
        this.server = server;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        server.start();
    }
}
