package be.zwaldeck.jmsn.client.validation;


import be.zwaldeck.jmsn.client.util.PasswordUtils;
import javafx.scene.control.Control;
import javafx.scene.control.TextInputControl;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.Validator;

import java.util.regex.Pattern;

public class ValidatorUtils {

    public static Validator validateEmail(String empty, String invalid) {
        return Validator.combine(
                Validator.createEmptyValidator(empty, Severity.ERROR),
                Validator.createRegexValidator(invalid, Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE), Severity.ERROR)
        );
    }

    public static Validator validatePassword(String empty, String notStrongEnough) {
        return Validator.combine(
                Validator.createEmptyValidator(empty, Severity.ERROR),
                Validator.<String>createPredicateValidator(value -> PasswordUtils.getPasswordStrength(value) >= 75, notStrongEnough, Severity.ERROR)
        );
    }

    public static Validator validateControlsValueAreEqual(TextInputControl control, String msg) {
        return Validator.<String>createPredicateValidator(value -> value.equals(control.getText()), msg, Severity.ERROR);
    }
}
