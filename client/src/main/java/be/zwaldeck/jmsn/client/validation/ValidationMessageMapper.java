package be.zwaldeck.jmsn.client.validation;

import javafx.scene.control.Label;
import org.controlsfx.validation.ValidationMessage;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;

import java.util.Collection;
import java.util.HashMap;

public class ValidationMessageMapper {

    private final HashMap<String, Label> labelsMap;

    public ValidationMessageMapper(HashMap<String, Label> labelsMap) {
        this.labelsMap = labelsMap;
    }

    public void apply(ValidationResult validationResult) {
        clearErrors(labelsMap.values());

        validationResult.getErrors().forEach(errorMessage -> {
            var id = errorMessage.getTarget().getId();
            if (labelsMap.containsKey(id) && labelsMap.get(id).getText().equals("")) {
                Label lbl = labelsMap.get(id);
                lbl.setText(errorMessage.getText());
                lbl.setVisible(true);
            }
        });
    }

    private void clearErrors(Collection<Label> values) {
        values.forEach(lbl -> {
            lbl.setVisible(false);
            lbl.setText("");
        });
    }
}
