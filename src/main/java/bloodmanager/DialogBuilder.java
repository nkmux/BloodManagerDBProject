package bloodmanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class DialogBuilder {
    public static Dialog<Patient> getInsertPatientDialog() {
        Dialog<Patient> dialog = new Dialog<>();
        dialog.setTitle("Add Patient");
        dialog.setHeaderText("Please specify the information of the patient:");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField ssnField = new TextField();
        ssnField.setPromptText("ID");
        ssnField.setTextFormatter(new TextFormatter<Integer>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        }));
        TextField fnameField = new TextField();
        fnameField.setPromptText("First Name");
        TextField lnameField = new TextField();
        lnameField.setPromptText("Last Name");
        ArrayList<String> bloodtypes = new ArrayList<>();
        for (BloodType.BloodTypes bloodtype: BloodType.BloodTypes.values()) {
            bloodtypes.add(bloodtype.getS());
        }
        ObservableList<String> bloodOptions =
                FXCollections.observableArrayList(bloodtypes);
        ComboBox<String> bloodComboBox = new ComboBox<>(bloodOptions);
        bloodComboBox.setPromptText("Blood Type");
        TextField weightField = new TextField();
        weightField.setPromptText("Weight (KG)");
        weightField.setTextFormatter(new TextFormatter<Integer>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        }));
        TextField heightField = new TextField();
        heightField.setPromptText("Height (cm)");
        heightField.setTextFormatter(new TextFormatter<Integer>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        }));
        ArrayList<String> genders = new ArrayList<>();
        genders.add("M"); genders.add("F");
        ObservableList<String> genderOptions =
                FXCollections.observableArrayList(genders);
        ComboBox<String> genderComboBox = new ComboBox<>(genderOptions);
        genderComboBox.setPromptText("Gender");
        DatePicker datePicker = new DatePicker(LocalDate.of(2000, 1, 1));
        TextField addressField = new TextField();
        addressField.setPromptText("Address");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone Number");
        phoneField.setTextFormatter(new TextFormatter<Integer>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        }));
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        dialogPane.setContent(new VBox(12, ssnField,fnameField, lnameField,
                                    bloodComboBox, weightField, heightField,
                                    genderComboBox, datePicker, addressField,
                                    phoneField, emailField, passwordField));

        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                try {
                    Patient patient = new Patient(Integer.parseInt(ssnField.getText()), Integer.parseInt(weightField.getText()),
                        Integer.parseInt(heightField.getText()), Integer.parseInt(phoneField.getText()),
                        fnameField.getText(), lnameField.getText(),
                        bloodComboBox.getSelectionModel().getSelectedItem(), emailField.getText(), genderComboBox.getSelectionModel().getSelectedItem(),
                        addressField.getText(),
                        Date.valueOf(datePicker.getValue()));
                    PatientOperations.insertPatient(patient, passwordField.getText());

                    new Alert(Alert.AlertType.CONFIRMATION, "A new patient has been added successfully!").showAndWait();
                } catch (SQLException ex) {
                    new Alert(Alert.AlertType.ERROR, "An error has occured, please retry with the right data types!").showAndWait();
                }
            }
            return null;
        });

        return dialog;
    }

    public static Dialog<Patient> getUpdatePatientDialog(Patient patient) {
        Dialog<Patient> dialog = new Dialog<>();
        dialog.setTitle("Update Patient Information");
        dialog.setHeaderText("Please edit the information of the patient:");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Label fnameLabel = new Label("First Name:");
        TextField fnameField = new TextField(patient.getFname());
        Label lnameLabel = new Label("Last name: ");
        TextField lnameField = new TextField(patient.getLname());
        Label bloodLabel = new Label("Blood Type: ");
        ArrayList<String> bloodtypes = new ArrayList<>();
        for (BloodType.BloodTypes bloodtype: BloodType.BloodTypes.values()) {
            bloodtypes.add(bloodtype.getS());
        }
        ObservableList<String> bloodOptions =
                FXCollections.observableArrayList(bloodtypes);
        ComboBox<String> bloodComboBox = new ComboBox<>(bloodOptions);
        bloodComboBox.getSelectionModel().select(patient.getBloodType());
        Label weightLabel = new Label("Weight (KG): ");
        TextField weightField = new TextField(patient.getWeight() + "");
        weightField.setPromptText("Weight (KG)");
        weightField.setTextFormatter(new TextFormatter<Integer>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        }));
        Label heightLabel = new Label("Height (cm): ");
        TextField heightField = new TextField(patient.getHeight() + "");
        heightField.setPromptText("Height (cm)");
        heightField.setTextFormatter(new TextFormatter<Integer>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        }));

        Label genderLabel = new Label("Gender");
        ArrayList<String> genders = new ArrayList<>();
        genders.add("M"); genders.add("F");
        ObservableList<String> genderOptions =
                FXCollections.observableArrayList(genders);
        ComboBox<String> genderComboBox = new ComboBox<>(genderOptions);
        genderComboBox.getSelectionModel().select(patient.getGender());


        Label birthLabel = new Label("Birth Date: ");
        DatePicker datePicker = new DatePicker(patient.getBirthDate().toLocalDate());

        Label addressLabel = new Label("Address: ");
        TextField addressField = new TextField(patient.getAddress());
        addressField.setPromptText("Address");

        Label phoneLabel = new Label("Phone: ");
        TextField phoneField = new TextField(patient.getPhone() + "");
        phoneField.setPromptText("Phone Number");
        phoneField.setTextFormatter(new TextFormatter<Integer>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        }));

        Label emailLabel = new Label("Email: ");
        TextField emailField = new TextField(patient.getEmail());
        emailField.setPromptText("Email");

        dialogPane.setContent(new VBox(6,fnameLabel,fnameField, lnameLabel,lnameField,
                bloodLabel,bloodComboBox, weightLabel,weightField, heightLabel,heightField,
                genderLabel,genderComboBox, birthLabel,datePicker,addressLabel, addressField,
                phoneLabel,phoneField,emailLabel, emailField));




        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                return new Patient(patient.getSsn(), Integer.parseInt(weightField.getText()),
                        Integer.parseInt(heightField.getText()), Integer.parseInt(phoneField.getText()),
                        fnameField.getText(), lnameField.getText(),
                        bloodComboBox.getSelectionModel().getSelectedItem(), emailField.getText(), genderComboBox.getSelectionModel().getSelectedItem(),
                        addressField.getText(),
                        Date.valueOf(datePicker.getValue()));
            } else {
                return patient;
            }
        });

        return dialog;
    }

    public static Dialog<Drive> getInsertDriveDialog(int ssn) {
        Dialog<Drive> dialog = new Dialog<>();
        dialog.setTitle("Initialize A Drive");
        dialog.setHeaderText("Please specify the information of the drive:");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Label locationLabel = new Label("Location: ");
        TextField locationField = new TextField();
        locationField.setPromptText("Location");
        Label fromLabel = new Label("From date:");
        DatePicker fromDate = new DatePicker(LocalDate.now());
        Label toLabel = new Label("To date:");
        DatePicker toDate = new DatePicker(LocalDate.now().plusDays(7));
        toDate.setDisable(true);
        fromDate.valueProperty().addListener((ov, oldValue, newValue) -> toDate.setValue(newValue.plusDays(14)));
        Label note = new Label("Note: A note happens only every three months\n in every location.");
        dialogPane.setContent(new VBox(6, locationLabel, locationField,
                                        fromLabel, fromDate,
                                        toLabel, toDate, note));
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                try {
                    if(validDrive(locationField.getText(), fromDate.getValue(), toDate.getValue())) {
                        int id = DriveOperations.insertDrive(locationField.getText(), Date.valueOf(fromDate.getValue()), Date.valueOf(toDate.getValue()));
                        DriveOperations.insertNurseOrganizer(ssn, id);
                        new Alert(Alert.AlertType.CONFIRMATION, "A new drive has been iniatited successfully.").showAndWait();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            return null;
        });
        return dialog;
    }

    private static boolean validDrive(String location, LocalDate from, LocalDate to) throws SQLException {
        if (from.isBefore(LocalDate.now())) {
            new Alert(Alert.AlertType.ERROR, "Choose a time after today.").showAndWait();
            return false;
        }
        if (to.isBefore(from)) {
            new Alert(Alert.AlertType.ERROR, "A drive can not end before it begins :)").showAndWait();
            return false;
        }
        if (DriveOperations.hasDrivesAtLocationInLastThreeMonths(location, Date.valueOf(from))) {
            new Alert(Alert.AlertType.ERROR, "There can not be a collection drive in this location at this time.").showAndWait();
            return false;
        }
        return true;
    }

    public static void getDriveInformation(Drive drive)  {
        Dialog<Drive> dialog = new Dialog<>();
        dialog.setTitle("Initialize A Drive");
        dialog.setHeaderText("Blood Collection Drive in " + drive.getLocation() +
                ".\nStarting: " + drive.getFrom() + "\nEnding: " + drive.getTo());
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().add(ButtonType.OK);
        TableView<Drive> tableView = new TableView<>();

        TableColumn<Drive, String> bloodTypeColumn = new TableColumn<>("Blood Type");
        bloodTypeColumn.setCellValueFactory(cellData -> cellData.getValue().bloodTypeProperty());

        TableColumn<Drive, Double> totalBloodCollectedColumn = new TableColumn<>("Total Blood Collected");
        totalBloodCollectedColumn.setCellValueFactory(cellData -> cellData.getValue().totalBloodCollectedProperty().asObject());
        totalBloodCollectedColumn.setPrefWidth(160);

        tableView.getColumns().add(bloodTypeColumn);
        tableView.getColumns().add(totalBloodCollectedColumn);
        try {
            tableView.getItems().addAll(DriveOperations.getDriveInfo(drive));
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        dialogPane.setContent(new VBox(2, tableView));
        dialogPane.setPrefHeight(350);
        dialog.showAndWait();
    }


    public static void patientHistoryDialog(User user, Patient patient) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setHeaderText("Medical History");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().setAll(ButtonType.OK);
        Label donLabel = new Label("Donations: ");
        donLabel.setFont(new Font("Arial", 14));
        ListView<Donation> donations = new ListView<>();
        donations.setPrefHeight(200);
        try {
            if (user.getPrivilege().equals(Privilege.Nurse)) {
                donations.setItems(DonationOperation.getDonations(patient.getSsn()));
                donations.setOnMouseClicked(e -> {
                    Donation selected = donations.getSelectionModel().getSelectedItem();
                    if (selected == null) return;
                    Alert alerter = new Alert(Alert.AlertType.WARNING);
                    alerter.setContentText("Do you want to remove " + patient.getFname() + "'s donation?");
                    alerter.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                    alerter.showAndWait();
                    if (alerter.getResult().equals(ButtonType.YES)) {
                        DonationOperation.deleteDonation(patient.getSsn(), selected.getDriveID(), selected.getBloodId());
                        try {
                            donations.setItems(DonationOperation.getDonations(patient.getSsn()));
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
            } else {
                donations.setItems(DonationOperation.getDonations(user.getId()));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }


        Label recLabel = new Label("Retrievals: ");
        recLabel.setFont(new Font("Arial", 14));
        ListView<RetrivalRequest> retrival = new ListView<>();
        retrival.setPrefHeight(200);
        try {
            if (user.getPrivilege().equals(Privilege.Nurse)) {
                retrival.setItems(RetrivalOperations.getTransfusions(patient.getSsn()));
                retrival.setOnMouseClicked(e -> {
                    RetrivalRequest selected = retrival.getSelectionModel().getSelectedItem();
                    if (selected == null) return;
                    Alert alerter = new Alert(Alert.AlertType.WARNING);
                    alerter.setContentText("Do you want to remove " + patient.getFname() + "'s retrieval?");
                    alerter.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                    alerter.showAndWait();
                    if (alerter.getResult().equals(ButtonType.YES)) {
                        RetrivalOperations.deleteRetrieval(patient.getSsn(), selected.getBloodId());
                        try {
                            retrival.setItems(RetrivalOperations.getTransfusions(patient.getSsn()));
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
            } else {
                retrival.setItems(RetrivalOperations.getTransfusions(user.getId()));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        dialogPane.setPrefWidth(500);
        dialogPane.setContent(new VBox(3, donLabel, donations,
                recLabel, retrival));
        dialog.showAndWait();
    }
}
