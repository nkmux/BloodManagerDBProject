package Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import bloodmanager.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class MainController implements Initializable {
    @FXML
    private Label helloLabel;
    @FXML
    private ImageView goBackButton, profileButton;
    @FXML
    private HBox buttonsHBox;

    public static User user;

    public static void setUser(User currUser) {
        user = currUser;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        switch (user.getPrivilege()) {
            case Nurse:
                break;
            case Patient:
                break;
            case Guest:
                buttonsHBox.setVisible(false);
        }
        helloLabel.setText("Hi, " + user.getName() + "!");
    }

    @FXML
    private void onBack_Click() throws IOException {
        BloodManager.setRoot("logger");
    }

    @FXML
    private void onBack_Hoover() {
        goBackButton.setOnMouseEntered(e -> goBackButton.setOpacity(.60));
        goBackButton.setOnMouseExited(e -> goBackButton.setOpacity(1));
    }

    @FXML
    private void onProfile_Click() throws IOException {
//        BloodManager.setRoot("logger");
    }

    @FXML
    private void onProfile_Hoover() {
        profileButton.setOnMouseEntered(e -> profileButton.setOpacity(.60));
        profileButton.setOnMouseExited(e -> profileButton.setOpacity(1));
    }
}

//         FOR THE INPUTTING OF DATA
//         TextInputDialog dialog = new TextInputDialog();
//         dialog.setGraphic(new ImageView());
//         dialog.setTitle("Text Input Dialog");
//         dialog.setHeaderText("Look, a Text Input Dialog");
//         dialog.setContentText("Please enter value:");
//         Optional<String> result = dialog.showAndWait();