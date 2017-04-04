package be.zwaldeck.msn.client.controller;

import be.zwaldeck.msn.client.custom.dialog.AddContactDialog;
import be.zwaldeck.msn.client.util.DialogUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;

/**
 * @Author Wout Schoovaerts
 */
public class ContactsController extends GuiController {
    @FXML
    private ImageView profileImg;

    @FXML
    private Label nicknameLbl;

    @FXML
    private ComboBox<?> statusCb;

    @FXML
    private Label subNicknameLbl;

    @FXML
    private ListView<?> onlineLv;

    @FXML
    private ListView<?> offlineLv;

    @FXML
    void handleAddContact(ActionEvent event) {
        new AddContactDialog(this).showAndWait().ifPresent(result -> {
            if (result) {
                DialogUtils.showMessageDialog("Info", "You successfully added the contact.", Alert.AlertType.INFORMATION);
            }
        });
    }
}
