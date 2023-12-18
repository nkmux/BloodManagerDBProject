package Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ResourceBundle;

import bloodmanager.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;

public class MainController implements Initializable {
    @FXML
    private Label helloLabel;
    @FXML
    private Button addDrive, donateButton, receiveButton, informationButton;
    @FXML
    private ImageView goBackButton, profileButton;
    @FXML
    private HBox buttonsHBox, requestsHBox;
    @FXML
    private VBox patientsVBox, drivesVBox;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Patient> patientsTable;
    @FXML
    private TableColumn<Patient, Integer> ssnCol, weightCol, heightCol, phoneCol;
    @FXML
    private TableColumn<Patient, String> fnameCol, lnameCol, bloodTypeCol, genderCol, addCol, emailCol;
    @FXML
    private TableColumn<Patient, Date> birthCol;
    @FXML
    private ListView<Drive> drivesList;
    @FXML
    private ListView<RetrivalRequest> transfusionList;
    @FXML
    private ListView<Dialog<Patient>> requestsList;
    @FXML
    private ListView<String> weekDonationList, monthDonationList, aggregatedBloodList, paymentsList;
    @FXML
    private TabPane reportsTabPane;


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
                profileButton.setVisible(false);
                addDrive.setVisible(true);
                break;
            case Patient:
                informationButton.setVisible(true);
                buttonsHBox.setVisible(false);
                addDrive.setVisible(false);
                drivesVBox.setVisible(true);
                this.initializeDriveList();
                receiveButton.setVisible(true);
                donateButton.setVisible(true);
                break;
            case Guest:
                buttonsHBox.setVisible(false);
                profileButton.setVisible(false);
                addDrive.setVisible(false);
                drivesVBox.setVisible(true);
                this.initializeDriveList();
                break;
        }
        helloLabel.setText("Hi, " + user.getName() + "!");
    }

    @FXML
    private void personalInfo_Click() {
        Patient oldPatientInfo = PatientOperations.getPatients(user.getId() + "").get(0);
        Dialog<Patient> dialog = DialogBuilder.getUpdatePatientDialog(oldPatientInfo);

        dialog.showAndWait();
        Patient updated = dialog.getResult();
        if (updated.equals(oldPatientInfo))
            return;
        new Alert(Alert.AlertType.CONFIRMATION, "Your request for update has been sent to the system.").showAndWait();
        RequestCol.reqs.add(dialog);
    }

    @FXML
    private void requestsPage_Click() {
        patientsVBox.setVisible(false);
        drivesVBox.setVisible(false);
        reportsTabPane.setVisible(false);
        requestsHBox.setVisible(!requestsHBox.isVisible());
        this.initializeTransfusionRequests();
        requestsList.setItems(RequestCol.reqs);
        requestsList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Dialog<Patient> item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Customize the displayed value based on your needs
                    setText("Update Patient: " + item.getResult().getFname());
                }
            }
        });
        requestsList.setOnMouseClicked(event -> {
            Dialog<Patient> dialog = requestsList.getSelectionModel().getSelectedItem();
            if (dialog == null) return;
            dialog.showAndWait();
            Patient newP = dialog.getResult();
            try {
                PatientOperations.updatePatientInfo(newP);
                new Alert(Alert.AlertType.CONFIRMATION, newP.getFname() + "'s Information have been updated!").showAndWait();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    private void drivesPage_Click() {
        patientsVBox.setVisible(false);
        requestsHBox.setVisible(false);
        reportsTabPane.setVisible(false);
        drivesVBox.setVisible(!drivesVBox.isVisible());
        this.initializeDriveList();
    }

    @FXML
    private void patientsPage_Click() {
        drivesVBox.setVisible(false);
        requestsHBox.setVisible(false);
        reportsTabPane.setVisible(false);
        patientsVBox.setVisible(!patientsVBox.isVisible());
        if (patientsVBox.isVisible())
            this.initializePatientTable("*");
    }

    @FXML
    private void reportsPage_Click() {
        drivesVBox.setVisible(false);
        patientsVBox.setVisible(false);
        requestsHBox.setVisible(false);
        reportsTabPane.setVisible(!reportsTabPane.isVisible());
        ReportsOperations.initializeReports(weekDonationList, monthDonationList, aggregatedBloodList, paymentsList);
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
    private void patientHistory_Click(MouseEvent event) {
        if (!user.getPrivilege().equals(Privilege.Nurse))
            DialogBuilder.patientHistoryDialog(user, null);
        Patient patient = patientsTable.getSelectionModel().getSelectedItem();
        if (patient == null) return;
        if (event.getClickCount() < 2) return;
        DialogBuilder.patientHistoryDialog(user, patient);
    }

    @FXML
    private void onProfile_Hoover() {
        profileButton.setOnMouseEntered(e -> profileButton.setOpacity(.60));
        profileButton.setOnMouseExited(e -> profileButton.setOpacity(1));
    }

    //PATIENT TABLE//
    /////////////////
    @FXML
    private void addPatient_Click() {
        Dialog<Patient> dialog = DialogBuilder.getInsertPatientDialog();
        dialog.showAndWait();
        this.initializePatientTable("*");
    }

    @FXML
    private void deletePatient_Click() {
        Patient deleted = patientsTable.getSelectionModel().getSelectedItem();
        if (deleted == null) {
            new Alert(Alert.AlertType.ERROR, "Select a patient to be deleted.").showAndWait();
            return;
        }
        Alert alerter = new Alert(Alert.AlertType.WARNING);
        alerter.setContentText("Are you sure you want to delete " + deleted.getFname() +"?");
        alerter.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        alerter.showAndWait();
        if (alerter.getResult().equals(ButtonType.YES))
            PatientOperations.deletePatient(deleted.getSsn());
        this.initializePatientTable("*");
    }

    @FXML
    private void updatePatient_Click() {
        Patient patient = patientsTable.getSelectionModel().getSelectedItem();
        if (patient == null) {
            new Alert(Alert.AlertType.ERROR, "Select a patient to update their information.").showAndWait();
            return;
        }
        Dialog<Patient> dialog = DialogBuilder.getUpdatePatientDialog(patient);

        dialog.showAndWait();
        Patient updated = dialog.getResult();
        if (updated.equals(patient))
            return;
        try {
            PatientOperations.updatePatientInfo(updated);
            new Alert(Alert.AlertType.CONFIRMATION, updated.getFname() + "'s Information have been updated!").showAndWait();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.initializePatientTable("*");
    }

    @FXML
    private void diseasePatient_Click() {
        Patient patient = patientsTable.getSelectionModel().getSelectedItem();
        if (patient == null) {
            new Alert(Alert.AlertType.ERROR, "Select a patient first.").showAndWait();
            return;
        }
        Dialog<String> dialog = Disease.getDiseaseDialog(patient);
        dialog.showAndWait();
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
        if (patients.isEmpty() && !searchFactor.equals("*")) {
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

    //DRIVE LIST//
    //////////////
    private void initializeDriveList() {
        try {
            ObservableList<Drive> drives = DriveOperations.getDrives();
            drivesList.setItems(drives);
        } catch (SQLException ex) {
            new Alert(Alert.AlertType.ERROR, "An error has occured while fetching the data.").showAndWait();
        }
    }

    @FXML
    private void addDrive_Click() {
        Dialog<Drive> dialog = DialogBuilder.getInsertDriveDialog(user.getId());
        dialog.showAndWait();
        this.initializeDriveList();
    }

    @FXML
    private void driveInfo_Click(MouseEvent event) {
        Drive drive = drivesList.getSelectionModel().getSelectedItem();
        if (event.getClickCount() <2) return;
        if (drive == null) return;
        DialogBuilder.getDriveInformation(drive);
    }

    //DONATION//
    ////////////
    @FXML
    private void donateDrive_Click(){
        Drive drive = drivesList.getSelectionModel().getSelectedItem();
        if (drive == null) {
            new Alert(Alert.AlertType.ERROR, "Select a drive to donate.").showAndWait();
            return;
        }
        if (drive.getFrom().after(Date.valueOf(LocalDate.now()))) {
            new Alert(Alert.AlertType.ERROR, "Select an ongoing drive.").showAndWait();
            return;
        }
        // Age case
        Patient patient = PatientOperations.getPatients(user.getId() + "").get(0);
        if (Period.between(LocalDate.parse(patient.getBirthDate().toString()), LocalDate.now()).getYears() < 17) {
            new Alert(Alert.AlertType.ERROR, "Sorry, you can not donate due to being underage.").showAndWait();
            return;
        }
        // Weight case
        if (patient.getWeight() < 51) {
            new Alert(Alert.AlertType.ERROR, "Sorry, you can not donate due to being underweight.").showAndWait();
            return;
        }
        // Disease case
        try {
            ObservableList<String> diseases = Disease.getDiseases(patient);
            if (!diseases.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Sorry, you can not donate due to having " + diseases.get(0) + ".").showAndWait();
                return;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        try {
            int donationsThisYear = DonationOperation.getDonationsThisYear(patient);
            if (donationsThisYear >= 2) {
                new Alert(Alert.AlertType.ERROR, "You have donated 2 times this year already.").showAndWait();
                return;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        Alert alerter = new Alert(Alert.AlertType.WARNING);
        alerter.setContentText("Are you sure you want to donate at " + drive.getLocation() + " ?");
        alerter.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        alerter.showAndWait();
        if (alerter.getResult().equals(ButtonType.NO))
            return;
        try {
            DonationOperation.makeDonation(drive, patient);
            new Alert(Alert.AlertType.CONFIRMATION, "Thank you, check your email for the reciept").showAndWait();
            Emailer.sendEmail(patient.getEmail(), "DONATION MESSAGE",
                    "Thank you for your donation at " + drive.getLocation() +
                            "!\nYou helped make the world a better place!\nRegards,\nBlood Clinic.");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    //TRANSFUSION//
    ///////////////
    private void initializeTransfusionRequests() {
        try {
            ObservableList<RetrivalRequest> requests = RetrivalOperations.getTransfusionRequests();
            transfusionList.setItems(requests);
        } catch (SQLException ex) {
            new Alert(Alert.AlertType.ERROR, "An error has occured while fetching the data.").showAndWait();
        }
    }

    @FXML
    private void approveTransfusion_Click() {
        RetrivalRequest request = transfusionList.getSelectionModel().getSelectedItem();
        if (request == null) return;
        Alert alerter = new Alert(Alert.AlertType.WARNING);
        alerter.setContentText("Do you approve " + request.getFname() + "'s reqeuest of transfusion?");
        alerter.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO, ButtonType.CLOSE);
        alerter.showAndWait();
        if (alerter.getResult().equals(ButtonType.CLOSE)) return;
        try {
            if (alerter.getResult().equals(ButtonType.YES)) {
                RetrivalOperations.handleRequest(request, user.getId(), "Completed");
                new Alert(Alert.AlertType.CONFIRMATION, request.getFname() + " has recieved the blood transfusion of type " + request.getRequestedBlood() +  ". " +
                        "More Details will be sent to them through their email.").showAndWait();
                Emailer.sendEmail(RetrivalOperations.getEmail(request.getSsn()),
                        "TRANSFUSION MESSAGE",
                        "Blood Type Recieved: " + request.getRequestedBlood() + "\nFee: 20.0 Dollars." +
                                "\nDate: " + LocalDate.now() +
                                "\nThank you so much for choosing this clinic!");
            } else if (alerter.getResult().equals(ButtonType.NO)) {
                RetrivalOperations.handleRequest(request, user.getId(), "Denied");
                new Alert(Alert.AlertType.CONFIRMATION, request.getFname() + " has been denied the blood transfusion of type " + request.getRequestedBlood() +  ". " +
                        "More Details will be sent to them through their email.").showAndWait();
                Emailer.sendEmail(RetrivalOperations.getEmail(request.getSsn()),
                        "TRANSFUSION MESSAGE",
                        "You have been denied the blood tranfusion due to shortage of blood products.\nThank you so much for choosing this clinic!");
            }

            initializeTransfusionRequests();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void recieveBlood_Click() {
        Patient patient = PatientOperations.getPatients(user.getId() + "").get(0);
        Dialog<String> dialog = RetrivalOperations.getRequestDialog(patient);
        dialog.showAndWait();
    }
}