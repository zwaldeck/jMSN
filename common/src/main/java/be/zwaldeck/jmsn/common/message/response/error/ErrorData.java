package be.zwaldeck.jmsn.common.message.response.error;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@AllArgsConstructor
@Data
public class ErrorData implements Serializable {
    private static final long serialVersionUID = -841127966976076822L;

    private ErrorType errorType;
    private String message;

    public enum ErrorType {
        INVALID_EMAIL,
        PASSWORD_NOT_STRONG_ENOUGH,
        EMAIL_IN_USE,
        USER_NOT_FOUND,
        PASSWORD_NOT_MATCHING,
        SOMETHING_WRONG
    }
}
