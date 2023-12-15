package Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import bloodmanager.*;
import javafx.collections.ObservableList;
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
    @FXML
    private VBox patientsVBox;
    @FXML
    private TextField searchField;
    @FXML
    private Button patientsButton, addPatient, deletePatient, updatePatient;
    @FXML
    private TableView<Patient> patientsTable;
    @FXML
    private TableColumn<Patient, Integer> ssnCol, weightCol, heightCol, phoneCol;
    @FXML
    private TableColumn<Patient, String> fnameCol, lnameCol, bloodTypeCol, genderCol, addCol, emailCol;
    @FXML
    private TableColumn<Patient, java.sql.Date> birthCol;

    public static User user;

    public static void setUser(User currUser) {
        user = currUser;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        switch (user.getPrivilege()) {
            case Nurse:
                patientsVBox.setVisible(true);
                this.initializePatientTable("*");
                break;
            case Patient:
                break;
            case Guest:
                buttonsHBox.setVisible(false);
        }
        helloLabel.setText("Hi, " + user.getName() + "!");
    }

    @FXML
    private void patientsPage_Click() {
        patientsVBox.setVisible(!patientsVBox.isVisible());
        if (patientsVBox.isVisible())
            this.initializePatientTable("*");
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
//                 FOR THE INPUTTING OF DATA
//         Dialog dialog = new TextInputDialog();
//
//         dialog.setTitle("Add a new patient.");
//         dialog.setHeaderText("Enter the patients information.");
//         dialog.setContentText("Please enter value:");
//         Optional<String> result = dialog.showAndWait();
    }

    @FXML
    private void onProfile_Hoover() {
        profileButton.setOnMouseEntered(e -> profileButton.setOpacity(.60));
        profileButton.setOnMouseExited(e -> profileButton.setOpacity(1));
    }

    private void initializePatientTable(String searchFactor) {
        ssnCol.setCellValueFactory(cellData -> cellData.getValue().ssnProperty().asObject());
        weightCol.setCellValueFactory(cellData -> cellData.getValue().weightProperty().asObject());
        heightCol.setCellValueFactory(cellData -> cellData.getValue().heightProperty().asObject());
        phoneCol.setCellValueFactory(cellData -> cellData.getValue().phoneProperty().asObject());
        fnameCol.setCellValueFactory(cellData -> cellData.getValue().fnameProperty());
        lnameCol.setCellValueFactory(cellData -> cellData.getValue().lnameProperty());
        bloodTypeCol.setCellValueFactory(cellData -> cellData.getValue().bloodTypeProperty());
        genderCol.setCellValueFactory(cellData -> cellData.getValue().genderProperty());
        addCol.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        emailCol.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        birthCol.setCellValueFactory(celldata -> celldata.getValue().birthDateProperty());
        ObservableList<Patient> patients = PatientOperations.getPatients(searchFactor);
        if (patients.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "No Patient with this ID.").showAndWait();
            return;
        }
        patientsTable.setItems(patients);

    }

    @FXML
    private void searchField_Action() {
        String searched = this.searchField.getText();
        if (searched.isEmpty()) {
            this.initializePatientTable("*");
            return;
        }
        try {
            int trail = Integer.parseInt(searched);
            this.initializePatientTable(this.searchField.getText());
            System.out.println(trail);
        } catch (NumberFormatException ex) {
            new Alert(Alert.AlertType.ERROR, "ENTER AN INTEGER!").showAndWait();
        }
    }
}

//         FOR THE INPUTTING OF DATA
//         TextInputDialog dialog = new TextInputDialog();
//         dialog.setGraphic(new ImageView());
//         dialog.setTitle("Text Input Dialog");
//         dialog.setHeaderText("Look, a Text Input Dialog");
//         dialog.setContentText("Please enter value:");
//         Optional<String> result = dialog.showAndWait();