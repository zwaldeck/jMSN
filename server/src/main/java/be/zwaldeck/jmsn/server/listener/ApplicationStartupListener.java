package be.zwaldeck.jmsn.server.listener;

import be.zwaldeck.jmsn.server.application.Server;
import be.zwaldeck.jmsn.server.service.FixtureLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Scanner;
import java.util.stream.Stream;

@Component
public class ApplicationStartupListener implements ApplicationListener<ApplicationReadyEvent> {

    private final Server server;
    private final Environment environment;
    private final FixtureLoader fixtureLoader;

    @Autowired
    public ApplicationStartupListener(Server server, Environment environment,
                                      @Autowired(required = false) FixtureLoader fixtureLoader) {
        this.server = server;
        this.environment = environment;
        this.fixtureLoader = fixtureLoader;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {

        var hasFixtureProfile = Stream.of(environment.getActiveProfiles())
                .anyMatch(profile -> profile.equalsIgnoreCase("fixtures"));

        if (hasFixtureProfile) {
            System.out.print("Do you want to load the fixtures (Yes)/(No): ");
            Scanner scanner = new Scanner(System.in);
            var answer = scanner.nextLine();
            if (answer.equalsIgnoreCase("yes")) {
                fixtureLoader.loadFixtures();
            }
        }

        server.start();
    }
}
