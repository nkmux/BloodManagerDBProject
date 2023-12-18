package bloodmanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import java.sql.*;

public class ReportsOperations {

    public static void initializeReports(ListView<String> weekDonationList, ListView<String> monthDonationList,
                                         ListView<String> aggregatedBloodList, ListView<String> paymentsList) {
        try {
            weekDonationList.setItems(getLastWeekDonations());

            monthDonationList.setItems(getLastMonthDonations());

            aggregatedBloodList.setItems(getAggregatedBloodTypes());

            paymentsList.setItems(getCompletePayments());
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static ObservableList<String> getCompletePayments() throws SQLException {
        String sql = "SELECT `Blood_id`, `Rssn`, `Date_of_Retrieval`, `Fee`\n" +
                    "FROM `recieves`\n" +
                    "WHERE `Status_of_Retrieval` = 'Completed';";
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ics_db_project", "root", "Nawaf1");
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        ObservableList<String> payments = FXCollections.observableArrayList();
        String[] titles = {"Blood ID: ", "Ssn of Buyer:", "Date of Payment", "Total Payment: "};
        while (rs.next()) {
            String payment = "";
            int id = rs.getInt("Blood_id");
            int rssn = rs.getInt("Rssn");
            Date date = rs.getDate("Date_of_Retrieval");
            int fee = rs.getInt("Fee");
            payment = String.format("%-30s %10s", titles[0], id) +
                    "\n" +
                    String.format("%-30s %10s", titles[1], rssn) +
                    "\n" +
                    String.format("%-30s %10s", titles[2], date) +
                    "\n" +
                    String.format("%-30s %10s", titles[3], fee) +
                    "$\n";
            payments.add(payment);
        }
        conn.close();
        return payments;
    }

    private static ObservableList<String> getAggregatedBloodTypes() throws SQLException {
        String sql = "SELECT bp.Blood_type, SUM(bp.Quantity) AS `Total_Quantity`\n" +
                    "FROM`blood_product` bp\n" +
                    "LEFT JOIN `recieves` r ON bp.Blood_id = r.Blood_id\n" +
                    "WHERE r.Blood_id IS NULL\n" +
                    "GROUP BY bp.Blood_type;";

        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ics_db_project", "root", "Nawaf1");
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        ObservableList<String> types = FXCollections.observableArrayList();
        String[] titles = {"Blood Type: ", "Total Quantity"};
        while (rs.next()) {
            String type = "";
            String bloodType = rs.getString("Blood_type");
            double quantity = rs .getDouble("Total_Quantity");
            type = String.format("%-30s %10s", titles[0], bloodType) +
                    "\n" +
                    String.format("%-30s %10s", titles[1], quantity);
            types.add(type);
        }
        conn.close();
        return types;
    }

    private static ObservableList<String> getLastMonthDonations() throws SQLException {
        String sql = "SELECT di.`Blood_id`, di.`Date_of_Donation`, di.`Status_of_Donation`, bp.`Blood_type`, bp.`Quantity`\n" +
                "FROM `donates_in` di\n" +
                "JOIN `blood_product` bp ON di.`Blood_id` = bp.`Blood_id`\n" +
                "WHERE di.`Date_of_Donation` >= CURDATE() - INTERVAL 1 MONTH\n" +
                "ORDER BY di.`Date_of_Donation` DESC;";

        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ics_db_project", "root", "Nawaf1");
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        ObservableList<String> donations = FXCollections.observableArrayList();
        String[] titles = {"Blood ID:", "Date:", "Status:", "Blood Type:", "Quantity:"};
        while (rs.next()) {
            String donation = "";
            int id = rs.getInt("Blood_id");
            Date date = rs.getDate("Date_of_Donation");
            String status = rs.getString("Status_of_Donation");
            String type = rs.getString("Blood_type");
            int quantity = rs .getInt("Quantity");
            donation = String.format("%-30s %10s", titles[0], id) +
                    "\n" +
                    String.format("%-30s %10s", titles[1], date) +
                    "\n" +
                    String.format("%-30s %10s", titles[2], status) +
                    "\n" +
                    String.format("%-30s %10s", titles[3], type) +
                    "\n" +
                    String.format("%-30s %10s", titles[4], quantity);
            donations.add(donation);
        }
        conn.close();
        return donations;
    }

    private static ObservableList<String> getLastWeekDonations() throws SQLException {
        String sql = "SELECT di.`Blood_id`, di.`Date_of_Donation`, di.`Status_of_Donation`, bp.`Blood_type`, bp.`Quantity`\n" +
                    "FROM `donates_in` di\n" +
                    "JOIN `blood_product` bp ON di.`Blood_id` = bp.`Blood_id`\n" +
                    "WHERE di.`Date_of_Donation` >= CURDATE() - INTERVAL 1 WEEK\n" +
                    "ORDER BY di.`Date_of_Donation` DESC;";

        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ics_db_project", "root", "Nawaf1");
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        ObservableList<String> donations = FXCollections.observableArrayList();
        String[] titles = {"Blood ID:", "Date:", "Status:", "Blood Type:", "Quantity:"};
        while (rs.next()) {
            String donation = "";
            int id = rs.getInt("Blood_id");
            Date date = rs.getDate("Date_of_Donation");
            String status = rs.getString("Status_of_Donation");
            String type = rs.getString("Blood_type");
            int quantity = rs .getInt("Quantity");
            donation = String.format("%-30s %10s", titles[0], id) +
                    "\n" +
                    String.format("%-30s %10s", titles[1], date) +
                    "\n" +
                    String.format("%-30s %10s", titles[2], status) +
                    "\n" +
                    String.format("%-30s %10s", titles[3], type) +
                    "\n" +
                    String.format("%-30s %10s", titles[4], quantity);
            donations.add(donation);
        }
        conn.close();
        return donations;
    }
}
