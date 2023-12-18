package Controllers;

import java.io.IOException;
import java.sql.*;

import bloodmanager.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoggerController {

    @FXML
    private TextField idTextField;
    @FXML
    private PasswordField passwordField;
    @FXML 
    private Label errorLabel;

    @FXML
    private void onPatient_Click() throws IOException {
        if (idTextField.getText().isEmpty() || passwordField.getText().isEmpty()) {
        errorLabel.setText("ERROR; ENTER ALL INFORMATION.");
            return;
        }
        String userName;
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ics_db_project", "root", "Nawaf1")){
            PreparedStatement st = conn.prepareStatement("SELECT * FROM user_logon " +
                                                              "WHERE Pssn = ?");
            st.setString(1, idTextField.getText());
            ResultSet rs = st.executeQuery();
            if (!rs.next()) {
                errorLabel.setText("ERROR; NO PATIENT EXISTS WITH THIS ID.");
                return;
            }

            String pass = Password.decrypt(rs.getString(1));
            String enteredPass = passwordField.getText();
            if (!pass.equals(enteredPass)) {
                errorLabel.setText("ERROR; WRONG PASSWORD.");
                return;
            }
            st = conn.prepareStatement("SELECT Fname FROM blood_participant " +
                                            "WHERE SSN = ?");
            st.setString(1, idTextField.getText());
            rs = st.executeQuery();
            rs.next();
            userName = rs.getString("Fname");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        MainController.setUser(new User(Integer.parseInt(idTextField.getText()), userName, Privilege.Patient));
        BloodManager.setRoot("main");
    }
    
    @FXML
    private void onNurse_Click() throws IOException {
        if (idTextField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            errorLabel.setText("ERROR; ENTER ALL INFORMATION.");
            return;
        }

        String userName;
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ics_db_project", "root", "Nawaf1")){
            PreparedStatement st = conn.prepareStatement("SELECT * FROM user_logon " +
                    "WHERE Nssn = ?");
            st.setString(1, idTextField.getText());
            ResultSet rs = st.executeQuery();
            if (!rs.next()) {
                errorLabel.setText("ERROR; NO NURSE EXISTS WITH THIS ID.");
                return;
            }

            String pass = Password.decrypt(rs.getString(1));
            String enteredPass = passwordField.getText();
            if (!pass.equals(enteredPass)) {
                errorLabel.setText("ERROR; WRONG PASSWORD.");
                return;
            }
            st = conn.prepareStatement("SELECT Fname FROM nurse " +
                    "WHERE Nssn = ?");
            st.setString(1, idTextField.getText());
            rs = st.executeQuery();
            rs.next();
            userName = rs.getString("Fname");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        MainController.setUser(new User(Integer.parseInt(idTextField.getText()), userName, Privilege.Nurse));
        BloodManager.setRoot("main");
    }

    @FXML
    private void onGuest_Click() throws IOException {
        MainController.setUser(new User(0, "Guest", Privilege.Guest));
        BloodManager.setRoot("main");
        
    }
}
