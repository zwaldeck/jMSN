package be.zwaldeck.msn.client.controller;

import javafx.fxml.FXML;
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
}
