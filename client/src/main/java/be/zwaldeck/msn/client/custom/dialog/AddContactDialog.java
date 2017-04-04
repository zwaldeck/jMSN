package be.zwaldeck.msn.client.custom.dialog;

import be.zwaldeck.msn.client.controller.GuiController;
import be.zwaldeck.msn.client.net.ServerConnection;
import be.zwaldeck.msn.client.util.DialogUtils;
import be.zwaldeck.msn.common.messages.ServerMessage;
import be.zwaldeck.msn.common.messages.ServerMessageType;
import be.zwaldeck.msn.common.messages.data.AddContactData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import org.apache.commons.validator.routines.EmailValidator;

import java.io.IOException;

/**
 * @Author Wout Schoovaerts
 */
public class AddContactDialog extends Dialog<Boolean> {

    @FXML
    private TextField contactTxt;

    @FXML
    private Label errorLbl;

    private GuiController controller;
    private boolean added = false;

    public AddContactDialog(GuiController controller) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/dialog/add-contact.fxml"));
        try {
            loader.setController(this);
            BorderPane root = loader.load();

            setTitle("Add contact");
            setHeaderText("Enter the person's information");
            setResizable(false);

            initializeDialogButtons();

            getDialogPane().setContent(root);

        } catch (IOException e) {
            e.printStackTrace();
            DialogUtils.showExceptionDialog(e);
        }
    }

    private void initializeDialogButtons() {
        //set buttons
        ButtonType addBtn = new ButtonType("Add contact", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(addBtn, cancelBtn);

        setResultConverter(dialogButton -> dialogButton == addBtn && added);

        getDialogPane().lookupButton(addBtn).addEventFilter(ActionEvent.ACTION, event -> {
            if (contactTxt.getText().trim().equals("")) {
                errorLbl.setText("You need to fill in all the fields");
                errorLbl.setVisible(true);
                event.consume();
                return;
            }

            if (!EmailValidator.getInstance().isValid(contactTxt.getText().trim())) {
                errorLbl.setText("Your email is not valid.");
                errorLbl.setVisible(true);
                event.consume();
                return;
            }

            //TODO loader
            ServerConnection connection = ServerConnection.getInstance(controller);
            connection.sendMessage(new ServerMessage(ServerMessageType.ADD_CONTACT, new AddContactData(contactTxt.getText().trim())));
            ServerMessage sm = connection.waitForMessageFromServer();
            if(sm.getType() == ServerMessageType.ADD_CONTACT_SUCCESS) {
                added = true;
            }
            else if(sm.getType() == ServerMessageType.ADD_CONTACT_FAILED) {
                errorLbl.setText((String) sm.getData());
                errorLbl.setVisible(true);
                event.consume();
            }
        });
    }
}
