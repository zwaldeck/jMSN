package be.zwaldeck.msn.client.data;

import be.zwaldeck.msn.common.messages.data.UserData;

import java.util.List;

/**
 * Singleton for holding application data
 *
 * @Author Wout Schoovaerts
 */
public class ApplicationData {

    private static ApplicationData instance;

    public static ApplicationData getInstance() {
        if(instance == null) {
            instance = new ApplicationData();
        }

        return instance;
    }

    private UserData userData;
    private List<UserData> contacts;

    private ApplicationData() {}

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public List<UserData> getContacts() {
        return contacts;
    }

    public void setContacts(List<UserData> contacts) {
        this.contacts = contacts;
    }
}
