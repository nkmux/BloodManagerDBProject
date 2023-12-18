package bloodmanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class PatientOperations {

    public static void insertPatient(Patient patient, String password) throws SQLException {

        String sql = "INSERT INTO `ics_db_project`.`blood_participant` " +
                "(`SSN`,`Fname`,`Lname`,`Blood_type`,`Weight`,`Height`,`Gender`,`Birth_date`,`Address`,`Phone`,`Email`) " +
                        " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ics_db_project", "root", "Nawaf1");
        PreparedStatement st = conn.prepareStatement(sql);
        st.setInt(1, patient.getSsn());
        st.setString(2, patient.getFname());
        st.setString(3, patient.getLname());
        st.setString(4, patient.getBloodType());
        st.setInt(5, patient.getWeight());
        st.setInt(6, patient.getHeight());
        st.setString(7, patient.getGender());
        st.setDate(8, patient.getBirthDate());
        st.setString(9, patient.getAddress());
        st.setInt(10, patient.getPhone());
        st.setString(11, patient.getEmail());
        st.execute();


        // QUERY FOR PASSWORD
        sql = "INSERT INTO `ics_db_project`.`user_logon`\n" +
                "(`Encrypted_Password`, `Pssn`)\n" +
                "VALUES (?, ?);";
        st = conn.prepareStatement(sql);
        String encrypt = Password.encrypt(password);
        st.setString(1, encrypt);
        st.setInt(2, patient.getSsn());
        st.execute();

        conn.close();
    }

    public static void updatePatientInfo(Patient patient) throws SQLException {
        String sql = "UPDATE `ics_db_project`.`blood_participant` " +
                "SET " +
                "`Fname` = ?,`Lname` = ?, `Blood_type` = ?, " +
                "`Weight` = ?, `Height` = ?, `Gender` = ?, " +
                "`Birth_date` = ?, `Address` = ?, `Phone` = ?, " +
                "`Email` = ? " +
                "WHERE `SSN` = ?; ";

        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ics_db_project", "root", "Nawaf1");
        PreparedStatement st = conn.prepareStatement(sql);
        st.setInt(11, patient.getSsn());
        st.setString(1, patient.getFname());
        st.setString(2, patient.getLname());
        st.setString(3, patient.getBloodType());
        st.setInt(4, patient.getWeight());
        st.setInt(5, patient.getHeight());
        st.setString(6, patient.getGender());
        st.setDate(7, patient.getBirthDate());
        st.setString(8, patient.getAddress());
        st.setInt(9, patient.getPhone());
        st.setString(10, patient.getEmail());
        st.executeUpdate();
    }

    public static void deletePatient(int ssn) {
        String sql = "DELETE FROM `ics_db_project`.`blood_participant`\n" +
                "WHERE ssn = ?;";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ics_db_project", "root", "Nawaf1")) {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, ssn);
            st.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ObservableList<Patient> getPatients(String search) {
        String sql;
        if (search.equals("*"))
            sql = "SELECT * FROM `ics_db_project`.`blood_participant`";
        else
            sql = "SELECT * FROM `ics_db_project`.`blood_participant` WHERE Ssn = " + search + ";";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ics_db_project", "root", "Nawaf1")) {
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
