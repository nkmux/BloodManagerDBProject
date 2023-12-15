package bloodmanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class PatientOperations {

    public static void insertPatient(int ssn, String fname, String lname, String bloodType,
                                     int weight, int height, String gender, Date birthDate, int phone, String email, String password) throws SQLException {

        String sql = "INSERT INTO `ics_db_project`.`blood_participant` " +
                "(`SSN`,`Fname`,`Lname`,`Blood_type`,`Weight`,`Height`,`Gender`,`Birth_date`,`Address`,`Phone`,`Email`) " +
                        " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ics_db_project", "root", "Nawaf1");) {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, ssn);
            st.setString(2, fname);
            st.setString(3, lname);
            st.setString(4, bloodType);
            st.setInt(5, weight);
            st.setInt(6, height);
            st.setString(7, gender);
            st.setDate(8, birthDate);
            st.setInt(9, phone);
            st.setString(10, email);

            st.executeQuery();


            // QUERY FOR PASSWORD
            sql = "INSERT INTO `ics_db_project`.`user_logon` " +
                    "(`Encrypted_Password`,`Pssn`) VALUES(?,?);";
            st = conn.prepareStatement(sql);
            st.setString(1, Password.encrypt(password));
            st.setInt(2, ssn);
            st.executeQuery();
        } catch (SQLException ex) {
            ex.fillInStackTrace();
        }
    }

    public static void updatePatientInfo() {

    }

    public static void deletePatient(int ssn) {
        String sql = "DELETE FROM `ics_db_project`.`blood_participant`\n" +
                "WHERE ssn = ?;";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ics_db_project", "root", "Nawaf1");) {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, ssn);
            st.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ObservableList<Patient> getPatients(String search) {
        String sql = "";
        if (search.equals("*"))
            sql = "SELECT * FROM `ics_db_project`.`blood_participant`";
        else
            sql = "SELECT * FROM `ics_db_project`.`blood_participant` WHERE Ssn = " + search + ";";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ics_db_project", "root", "Nawaf1");) {
            PreparedStatement st = conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            ObservableList<Patient> patientsList = FXCollections.observableArrayList();
            while (rs.next()) {
                Patient patient = new Patient();

                patient.setSsn(rs.getInt("Ssn"));
                patient.setFname(rs.getString("Fname"));
                patient.setLname(rs.getString("Lname"));
                patient.setBloodType(rs.getString("Blood_Type"));
                patient.setWeight(rs.getInt("Weight"));
                patient.setHeight(rs.getInt("Height"));
                patient.setGender(rs.getString("Gender"));
                patient.setBirthDate(rs.getDate("Birth_date"));
                patient.setAddress(rs.getString("Address"));
                patient.setPhone(rs.getInt("Phone"));
                patient.setEmail(rs.getString("Email"));

                patientsList.add(patient);
            }
            return  patientsList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
