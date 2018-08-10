package be.zwaldeck.jmsn.client.controller;

import be.zwaldeck.jmsn.client.custom.control.EditableLabel;
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

//    private AnchorPane nicknamePane;

    @FXML
    private ComboBox<Status> statusCb;

    @FXML
    private EditableLabel editableNickname;

    private Label nicknameLbl;
    private TextField nicknameTxt;

    public MainController() {
    }

    @FXML
    public void initialize() {
//        nicknameLbl = new Label("The nickname");
//        nicknameLbl.setOnMouseClicked(event -> {
//            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
//                System.out.println("Double click!!!");
//                nicknamePane.getChildren().setAll(nicknameTxt);
//            }
//        });
//        AnchorPane.setLeftAnchor(nicknameLbl, 14.0);
//        AnchorPane.setTopAnchor(nicknameLbl, 26.0);
//
//        nicknameTxt = new TextField("The nickname");
//        nicknameTxt.setOnAction(event -> {
//            System.out.println("Action");
//        });
//
//        nicknamePane.getChildren().setAll(nicknameLbl);
    }
}
