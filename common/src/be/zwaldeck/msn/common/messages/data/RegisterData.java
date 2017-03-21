package be.zwaldeck.msn.common.messages.data;

import java.io.Serializable;

/**
 * @Author Wout Schoovaerts
 */
public class RegisterData implements Serializable{
    private String email;
    private String password;
    private String name;

    /**
     * @param email
     * @param password make sure this is already encoded!
     * @param name
     */
    public RegisterData(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }
}
