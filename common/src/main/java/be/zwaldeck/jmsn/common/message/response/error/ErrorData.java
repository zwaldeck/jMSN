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

        SOMETHING_WRONG
    }
}
