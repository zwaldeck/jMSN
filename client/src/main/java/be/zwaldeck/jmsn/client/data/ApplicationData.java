package be.zwaldeck.jmsn.client.data;

import be.zwaldeck.jmsn.common.message.response.data.UserData;
import lombok.Data;

import java.util.List;

@Data
public class ApplicationData {

    // Singleton stuff

    private static ApplicationData instance = new ApplicationData();

    public static ApplicationData getInstance() {
        return instance;
    }

    private ApplicationData() {
    }

    private List<UserData> contacts;
    private UserData user;

}
