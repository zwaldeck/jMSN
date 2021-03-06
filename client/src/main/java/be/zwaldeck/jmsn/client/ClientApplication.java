package be.zwaldeck.jmsn.client;

import be.zwaldeck.jmsn.client.util.NavigationUtils;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;

import java.util.Locale;

@SpringBootApplication
public class ClientApplication extends Application {

    private ConfigurableApplicationContext springContext;

    @Override
    public void init() throws Exception {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        springContext = SpringApplication.run(ClientApplication.class, getParameters().getRaw().toArray(new String[0]));
        NavigationUtils.openLoginWindow(primaryStage, springContext);
    }

    @Override
    public void stop() throws Exception {
        springContext.close();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
