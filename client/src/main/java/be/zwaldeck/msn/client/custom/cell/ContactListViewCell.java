package be.zwaldeck.msn.client.custom.cell;

import be.zwaldeck.msn.client.util.DialogUtils;
import be.zwaldeck.msn.common.Status;
import be.zwaldeck.msn.common.messages.data.UserData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.IOException;


/**
 * @Author Wout Schoovaerts
 */
public class ContactListViewCell extends ListCell<UserData> {

    @FXML
    private ImageView statusIv;

    @FXML
    private Label nicknameLbl;

    @FXML
    private Label subNicknameLbl;

    @FXML
    private GridPane container;

    private FXMLLoader loader = null;

    @Override
    protected void updateItem(UserData item, boolean empty) {
        super.updateItem(item, empty);

        setText(null);
        if(empty || item == null) {
            setGraphic(null);
            return;
        }


        if(loader == null) {
            loader = new FXMLLoader(getClass().getResource("/cell/contact-listview-cell.fxml"));
            loader.setController(this);

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
                DialogUtils.showExceptionDialog(e);
            }
        }
        
        populateImageView(item.getStatus());
        String nickNameTxt = item.getNickname();

        if(item.getSubNickname() != null && !item.getSubNickname().trim().isEmpty()) {
            nickNameTxt += " -";
            subNicknameLbl.setText(item.getSubNickname());
        }
        else {
            subNicknameLbl.setText("");
        }

        nicknameLbl.setText(nickNameTxt);

        setGraphic(container);
    }

    private void populateImageView(Status status) {
        String image = "img/status/";
        switch (status) {
            case ONLINE:
                image += "online.png";
                break;
            case AWAY:
                image += "away.png";
                break;
            case BUSY:
                image += "busy.png";
                break;
            default:
                image += "offline.png";
        }

        statusIv.setImage(new Image(image));
    }
}
