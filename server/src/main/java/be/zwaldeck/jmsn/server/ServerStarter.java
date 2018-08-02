package be.zwaldeck.jmsn.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"be.zwaldeck.jmsn.server.domain"})
@EnableJpaRepositories(basePackages = {"be.zwaldeck.jmsn.server.dao"})
public class ServerStarter {

    public static void main(String[] args) {
       SpringApplication.run(ServerStarter.class, args);
    }
}
