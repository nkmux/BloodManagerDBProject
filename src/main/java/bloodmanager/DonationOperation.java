package bloodmanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class DonationOperation {
    public static void makeDonation(Drive drive, Patient patient) throws SQLException {
        String sql = "INSERT INTO `ics_db_project`.`blood_product`\n" +
                "(`Blood_type`, `Expiration_date`, `Quantity`)\n" +
                "VALUES (?, ?, ?);";

        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ics_db_project", "root", "Nawaf1");
        PreparedStatement st = conn.prepareStatement(sql);
        st.setString(1, patient.getBloodType());
        LocalDate expiryDate = LocalDate.now().plusDays(47);
        st.setDate(2, Date.valueOf(expiryDate));
        st.setDouble(3, 0.5);

        st.execute();

        sql = "SELECT MAX(Drive_id) FROM `ics_db_project`.`blood_drive` ";
        ResultSet rs = conn.createStatement().executeQuery(sql);
        rs.next();
        int driveId = rs.getInt(1);

        sql = "SELECT MAX(Blood_id) FROM `ics_db_project`.`blood_product` ";
        rs = conn.createStatement().executeQuery(sql);
        rs.next();
        int bloodId = rs.getInt(1);

        sql = "INSERT INTO `ics_db_project`.`donor`\n" +
                "(`Pssn`, `Current_Year`) VALUES (?, ?)\n" +
                "ON DUPLICATE KEY UPDATE\n" +
                "No_of_Donations = No_of_Donations + 1;";
        st = conn.prepareStatement(sql);
        st.setInt(1, patient.getSsn());
        st.setInt(2, LocalDate.now().getYear());
        st.execute();

        sql = "INSERT INTO `ics_db_project`.`donates_in`\n" +
                "(`Drive_id`, `Blood_id`, `Dssn`, `Date_of_Donation`, `Status_of_Donation`)\n" +
                "VALUES(?, ?, ?, ?, ?);";
        st = conn.prepareStatement(sql);
        st.setInt(1, driveId);
        st.setInt(2, bloodId);
        st.setInt(3, patient.getSsn());
        st.setDate(4, Date.valueOf(LocalDate.now()));
        st.setString(5, "Accepted");
        st.execute();
        conn.close();
    }

    public static int getDonationsThisYear(Patient patient) throws SQLException {
        String sql = "SELECT * FROM ics_db_project.donor\n" +
                "WHERE Pssn = ? AND Current_Year = ?;";

        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ics_db_project", "root", "Nawaf1");
        PreparedStatement st = conn.prepareStatement(sql);
        st.setInt(1, patient.getSsn());
        st.setInt(2, LocalDate.now().getYear());
        ResultSet rs = st.executeQuery();
        int no_of_donations = 0;
        if (rs.next()) {
            no_of_donations = rs.getInt("No_of_Donations");
        }
        conn.close();
        return no_of_donations;

    }

    public static ObservableList<Donation> getDonations(int ssn) throws SQLException {
        String sql = "SELECT di.Drive_id, bd.Location, di.Blood_id, di.Status_of_Donation, di.Date_of_Donation\n" +
                     "FROM donates_in di\n" +
                     "JOIN blood_drive bd ON di.Drive_id = bd.Drive_id\n" +
                     "JOIN blood_product bp ON di.Blood_id = bp.Blood_id\n" +
                     "WHERE di.Dssn = ?;";

        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ics_db_project", "root", "Nawaf1");
        PreparedStatement st = conn.prepareStatement(sql);
        st.setInt(1, ssn);
        ResultSet rs = st.executeQuery();
        ObservableList<Donation> donations = FXCollections.observableArrayList();
        while (rs.next()) {
            int bloodId = rs.getInt("Blood_id");
            int driveId = rs.getInt("Drive_id");
            String location = rs.getString("Location");
            Date date = rs.getDate("Date_of_Donation");
            String status = rs.getString("Status_of_Donation");
            Donation donation = new Donation(bloodId, driveId, date, location, status);
            donations.add(donation);
        }
        conn.close();
        return donations;
    }

    public static void deleteDonation(int ssn, int driveID, int bloodId)  {
        String sql = "DELETE FROM `ics_db_project`.`donates_in`\n" +
                "WHERE Drive_id = ? AND Dssn = ? AND Blood_id = ?;";
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ics_db_project", "root", "Nawaf1");

            PreparedStatement st = conn.prepareStatement(sql);

            st.setInt(1, driveID);
            st.setInt(2, ssn);
            st.setInt(3, bloodId);
            st.execute();

            sql = "UPDATE `ics_db_project`.`donor`\n" +
                    "SET `No_of_Donations` = No_of_Donations - 1\n" +
                    "WHERE `Pssn` = ?;";

            st = conn.prepareStatement(sql);
            st.setInt(1, ssn);
            conn.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
