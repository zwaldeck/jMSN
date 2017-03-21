package be.zwaldeck.msn.common.messages.data;

import java.io.Serializable;

/**
 * @Author Wout Schoovaerts
 */
public class LoginData implements Serializable{
    private String email;
    private String password;

    public LoginData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
