package be.zwaldeck.msn.server;

import be.zwaldeck.msn.server.config.SpringConfig;
import be.zwaldeck.msn.server.server.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;

/**
 * @Author Wout Schoovaerts
 */
public class MsnServer {

    public MsnServer() {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        ctx.getBean("server", Server.class).start();
    }

    public static void main(String[] args) {
        new MsnServer();
    }
}
