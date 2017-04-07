package be.zwaldeck.msn.client.controller;

import be.zwaldeck.msn.client.custom.cell.ContactListViewCell;
import be.zwaldeck.msn.client.custom.dialog.AddContactDialog;
import be.zwaldeck.msn.client.data.ApplicationData;
import be.zwaldeck.msn.client.util.DialogUtils;
import be.zwaldeck.msn.common.Status;
import be.zwaldeck.msn.common.messages.data.UserData;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Wout Schoovaerts
 */
public class ContactsController extends GuiController {
    @FXML
    private ImageView profileImg;

    @FXML
    private Label nicknameLbl;

    @FXML
    private ComboBox<Status> statusCb;

    @FXML
    private Label subNicknameLbl;

    @FXML
    private ListView<UserData> onlineLv;

    @FXML
    private ListView<UserData> offlineLv;

    private UserData user;

    @Override
    public void init(Stage stage) {
        super.init(stage);

        this.user = ApplicationData.getInstance().getUserData();

        statusCb.setItems(FXCollections.observableArrayList(Status.values()));
        statusCb.getSelectionModel().select(user.getStatus());

        List<UserData> contacts = ApplicationData.getInstance().getContacts();

        List<UserData> offLineContacts = contacts.stream()
                .filter(userData -> userData.getStatus() == Status.OFFLINE || userData.getStatus() == Status.APPEAR_OFFLINE)
                .collect(Collectors.toList());

        List<UserData> onlineContacts = contacts.stream()
                .filter(userData -> userData.getStatus() != Status.OFFLINE && userData.getStatus() != Status.APPEAR_OFFLINE)
                .collect(Collectors.toList());

        onlineLv.setItems(FXCollections.observableArrayList(onlineContacts));
        offlineLv.setItems(FXCollections.observableArrayList(offLineContacts));

        onlineLv.setCellFactory(listView -> new ContactListViewCell());
        offlineLv.setCellFactory(listView -> new ContactListViewCell());
    }

    @FXML
    void handleAddContact(ActionEvent event) {
        new AddContactDialog(this).showAndWait().ifPresent(result -> {
            if (result) {
                DialogUtils.showMessageDialog("Info", "You successfully added the contact.", Alert.AlertType.INFORMATION);
            }
        });
    }
}
