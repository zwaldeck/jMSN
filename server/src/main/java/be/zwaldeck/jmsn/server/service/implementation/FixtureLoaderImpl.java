package be.zwaldeck.jmsn.server.service.implementation;

import be.zwaldeck.jmsn.common.Status;
import be.zwaldeck.jmsn.server.domain.User;
import be.zwaldeck.jmsn.server.service.FixtureLoader;
import be.zwaldeck.jmsn.server.service.UserService;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Map;

@Service
@Profile({"fixtures"})
public class FixtureLoaderImpl implements FixtureLoader {

    private final Map<String, String> rawDatabaseSettings;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Autowired
    public FixtureLoaderImpl(@Qualifier("rawDatabaseSettings")Map<String, String> rawDatabaseSettings,
                             PasswordEncoder passwordEncoder, UserService userService) {
        this.rawDatabaseSettings = rawDatabaseSettings;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Override
    public void loadFixtures() {
        resetSchema();

        loadUsers();
    }

    private void resetSchema() {
        var metadataSources = new MetadataSources(
                new StandardServiceRegistryBuilder().applySettings(rawDatabaseSettings).build()
        );
        metadataSources.addAnnotatedClass(User.class);

        var export = new SchemaExport();
        export.setHaltOnError(false);
        export.setFormat(true);
        export.setDelimiter(";");
        export.execute(EnumSet.of(TargetType.DATABASE), SchemaExport.Action.BOTH, metadataSources.buildMetadata());
    }

    private void loadUsers() {
        var user = new User();
        user.setEmail("wout@feelio.be");
        user.setPassword(passwordEncoder.encode("testtest"));
        user.setIp("127.0.0.1");
        user.setNickname("wout");
        user.setStatus(Status.OFFLINE);
        user.setSubNickname("");

        userService.createUser(user);
    }
}
