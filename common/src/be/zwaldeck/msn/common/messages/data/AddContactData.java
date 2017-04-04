package be.zwaldeck.msn.common.messages.data;

import java.io.Serializable;

/**
 * @Author Wout Schoovaerts
 */
public class AddContactData  implements Serializable{
    private String email;

    public AddContactData(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
