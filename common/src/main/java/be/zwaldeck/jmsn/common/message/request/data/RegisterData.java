package be.zwaldeck.jmsn.common.message.request.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class RegisterData implements Serializable  {

    private static final long serialVersionUID = -1869107944143895032L;

    private String email;
    private String password;
}
