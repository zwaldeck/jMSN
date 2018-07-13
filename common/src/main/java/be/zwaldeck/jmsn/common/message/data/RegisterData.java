package be.zwaldeck.jmsn.common.message.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RegisterData {
    private String email;
    private String password;
}
