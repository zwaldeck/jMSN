package be.zwaldeck.jmsn.server.service.implementation;

import be.zwaldeck.jmsn.common.Status;
import be.zwaldeck.jmsn.server.domain.Contact;
import be.zwaldeck.jmsn.server.domain.User;
import be.zwaldeck.jmsn.server.service.ContactService;
import be.zwaldeck.jmsn.server.service.FixtureLoader;
import be.zwaldeck.jmsn.server.service.UserService;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

@Service
@Profile({"fixtures"})
public class FixtureLoaderImpl implements FixtureLoader {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final Map<String, String> rawDatabaseSettings;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final ContactService contactService;

    private final List<User> users = new ArrayList<>();

    @Autowired
    public FixtureLoaderImpl(@Qualifier("rawDatabaseSettings")Map<String, String> rawDatabaseSettings,
                             PasswordEncoder passwordEncoder, UserService userService, ContactService contactService) {
        this.rawDatabaseSettings = rawDatabaseSettings;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.contactService = contactService;
    }

    @Override
    public void loadFixtures() {
        log.info("Loading the fixtures");
        resetSchema();

        loadUsers();
        loadContacts();
    }

    private void resetSchema() {
        log.debug("Dropping and creating the schema");
        var metadataSources = new MetadataSources(
                new StandardServiceRegistryBuilder().applySettings(rawDatabaseSettings).build()
        );
        metadataSources.addAnnotatedClass(User.class);
        metadataSources.addAnnotatedClass(Contact.class);

        var export = new SchemaExport();
        export.setHaltOnError(false);
        export.setFormat(true);
        export.setDelimiter(";");
        export.execute(EnumSet.of(TargetType.DATABASE), SchemaExport.Action.BOTH, metadataSources.buildMetadata());
    }

    private void loadUsers() {
        log.debug("Loading the users");

        for (var i = 0; i < 100; i++) {
            var username = "";
            if (i == 0) {
                username = "wout";
            } else {
                username = "wout" + i;
            }

            var user = new User();
            user.setEmail(username + "@feelio.be");
            user.setPassword("testtest");
            user.setIp("127.0.0.1");
            user.setNickname(username);
            user.setStatus(Status.OFFLINE);
            user.setSubNickname("");

            users.add(userService.createUser(user));
        }
    }

    private void loadContacts() {
        log.debug("Loading contacts");

        var owner = users.get(0);

        for (var i = 1; i < 100; i++) {
            var contact = new Contact();
            contact.setOwner(owner);
            contact.setContact(users.get(i));

            contactService.createContact(contact);
        }
    }
}
