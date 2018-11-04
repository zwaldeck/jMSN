package be.zwaldeck.jmsn.client.controller;

import be.zwaldeck.jmsn.client.custom.control.EditableLabel;
import be.zwaldeck.jmsn.client.data.ApplicationData;
import be.zwaldeck.jmsn.common.Status;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import org.springframework.stereotype.Controller;

@Controller
public class MainController extends GuiController {

    @FXML
    private ImageView profileImg;

    @FXML
    private ComboBox<Status> statusCb;

    @FXML
    private EditableLabel editableNickname;

    public MainController() {
    }

    @FXML
    public void initialize() {
        editableNickname.setText(ApplicationData.getInstance().getUser().getNickname());

        editableNickname.setOnUpdate(value -> {
            System.out.println("Update: " + value);
        });
    }
}
