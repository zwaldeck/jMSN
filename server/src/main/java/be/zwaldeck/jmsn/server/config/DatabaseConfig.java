package be.zwaldeck.jmsn.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DatabaseConfig {

    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    @Value("${spring.jpa.properties.hibernate.dialect}y")
    private String dialect;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean(name = "rawDatabaseSettings")
    @Profile({"fixtures"})
    public Map<String, String> rawDatabaseSettings() {
        var settings = new HashMap<String, String>();
        settings.put("connection.driver_class", driver);
        settings.put("dialect", dialect);
        settings.put("hibernate.connection.url", url);
        settings.put("hibernate.connection.username", username);
        settings.put("hibernate.connection.password", password);
        settings.put("show_sql", "true");

        return settings;
    }
}
