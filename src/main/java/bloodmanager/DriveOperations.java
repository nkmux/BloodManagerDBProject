package bloodmanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class DriveOperations {
    public static ObservableList<Drive> getDrives() throws SQLException {
        String sql = "SELECT * " +
                "FROM ics_db_project.blood_drive;";

        ObservableList<Drive> drives = FXCollections.observableArrayList();
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ics_db_project", "root", "Nawaf1");
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            int id = rs.getInt("Drive_id");
            String location = rs.getString("Location");
            Date from = rs.getDate("Starting_date");
            Date to = rs.getDate("Ending_date");
            Drive drive = new Drive(id, from, to, location);
            drives.add(drive);
        }
        conn.close();
        return drives;
    }

    public static int insertDrive(String location, Date from, Date to) throws SQLException {
        String sql = "INSERT INTO `ics_db_project`.`blood_drive` " +
                "(`Starting_date`, " +
                "`Ending_date`, `Location`) " +
                "VALUES (?, ?, ?);";

        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ics_db_project", "root", "Nawaf1");
        PreparedStatement st = conn.prepareStatement(sql);

        st.setDate(1, from);
        st.setDate(2, to);
        st.setString(3, location);

        st.execute();

        sql = "SELECT MAX(Drive_id) FROM `ics_db_project`.`blood_drive` ";
        ResultSet rs = conn.createStatement().executeQuery(sql);
        rs.next();
        int id = rs.getInt(1);
        conn.close();
        return id;
    }

    public static boolean hasDrivesAtLocationInLastThreeMonths(String location, Date from) throws SQLException {
        String sql = "SELECT COUNT(*) FROM ics_db_project.blood_drive\n" +
                "WHERE Ending_date BETWEEN ? - interval 90 day AND ?\n" +
                "AND Location = ?;";

        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ics_db_project", "root", "Nawaf1");
        PreparedStatement st = conn.prepareStatement(sql);
        st.setDate(1, from);
        st.setDate(2, from);
        st.setString(3, location);
        ResultSet rs = st.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        boolean thereAreDrives = count > 0;
        conn.close();
        return thereAreDrives;
    }

    public static void insertNurseOrganizer(int ssn, int driveId) throws SQLException {
        String sql = "INSERT INTO `ics_db_project`.`organized_by`\n" +
                "(`Drive_id`, `Nssn`)\n" +
                "VALUES (?, ?);";

        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ics_db_project", "root", "Nawaf1");
        PreparedStatement st = conn.prepareStatement(sql);
        st.setInt(1, driveId);
        st.setInt(2, ssn);
        st.execute();
        conn.close();
    }

    public static ObservableList<Drive> getDriveInfo(Drive drive) throws SQLException {
        String sql = "SELECT bp.Blood_type, SUM(bp.Quantity) AS Total_Blood_Collected\n" +
                "FROM blood_drive bd\n" +
                "JOIN donates_in di ON bd.Drive_id = di.Drive_id\n" +
                "JOIN blood_product bp ON di.Blood_id = bp.Blood_id\n" +
                "WHERE bd.Drive_id = ?\n" +
                "GROUP BY bd.Location, bp.Blood_type;";

        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ics_db_project", "root", "Nawaf1");
        PreparedStatement st = conn.prepareStatement(sql);
        st.setInt(1, drive.getId());
        ObservableList<Drive> list = FXCollections.observableArrayList();
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            Drive drive1 = new Drive();
            drive1.setBloodTypes(rs.getString("Blood_type"));
            drive1.setTotalBloodCollected(rs.getDouble("Total_Blood_Collected"));
            list.add(drive1);
        }
        conn.close();
        return  list;
    }
}
