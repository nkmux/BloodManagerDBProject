package bloodmanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class RetrivalOperations {

    public static Dialog<String> getRequestDialog(Patient patient) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Receive A Blood Transfusion");
        dialog.setHeaderText("Are you sure you want to request a blood transfusion?" +
                "\nNote: The fee for a blood transfusion is 20.0$.");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Label urgencyLabel = new Label("How urgent is the transfusion?");

        ObservableList<String> urgencyLevels = FXCollections.observableArrayList();
        urgencyLevels.addAll(new String[]{"Low", "Medium", "High"});
        ComboBox<String> urgency = new ComboBox<>();
        urgency.setItems(urgencyLevels);
        dialogPane.setContent(new VBox(2, urgencyLabel, urgency));
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                try {
                    String selectedUrgency = urgency.getSelectionModel().getSelectedItem();
                    if (selectedUrgency == null) {
                        new Alert(Alert.AlertType.ERROR, "Please select the urgency of the transfusion.").showAndWait();
                        return null;
                    }
                    if (!insertRetrivalRequest(patient, selectedUrgency)) {
                        new Alert(Alert.AlertType.INFORMATION, "Sadly, the clinic does not have any compatible blood types for you at this moment.").showAndWait();
                        return null;
                    }
                    new Alert(Alert.AlertType.CONFIRMATION, "Your request has been sent to the clinic, please keep checking your email for updates.").showAndWait();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            return null;
        });
        return  dialog;
    }

    private static boolean insertRetrivalRequest(Patient patient, String urgency) throws SQLException {
        String sql = "INSERT INTO `ics_db_project`.`recipient`\n" +
                "(`Pssn`, `Urgency_level`)\n" +
                "VALUES (?, ?) ON DUPLICATE KEY UPDATE\n" +
                "Urgency_level = ?;";

        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ics_db_project", "root", "Nawaf1");
        PreparedStatement st = conn.prepareStatement(sql);
        st.setInt(1,patient.getSsn());
        st.setString(2, urgency);
        st.setString(3, urgency);

        st.execute();

        ArrayList<String> types = getCompatibleTypesList(patient);
        StringBuilder questionMarks = new StringBuilder();

        for(String ignored : types)
            questionMarks.append("?").append(", ");

        questionMarks.replace(questionMarks.lastIndexOf(","), questionMarks.length(), "");
        sql = "SELECT * FROM `ics_db_project`.`blood_product`\n" +
                "WHERE DATE(NOW()) < DATE(Expiration_date)\n" +
                "AND Blood_id NOT IN (\n" +
                    "\tSELECT Blood_id from recieves\n" +
                ")\n" +
                "AND Blood_type IN (" + questionMarks + ");";


        st = conn.prepareStatement(sql);
        for (int i = 1; i <= types.size(); i++)
            st.setString(i, String.valueOf(types.get(i - 1)));
        ResultSet rs = st.executeQuery();
        if (!rs.next()) {
            return false;
        }

        int bloodId = rs.getInt("Blood_id");
        sql = "INSERT INTO `ics_db_project`.`recieves`\n" +
                "(`Blood_id`, `Nssn`,\n" +
                "`Rssn`, \n" +
                "`Status_of_Retrieval`, `Fee`)\n" +
                "VALUES (?, ?, ?, ?, ?);";
        final int FEE = 20;
        st = conn.prepareStatement(sql);
        st.setInt(1, bloodId);
        st.setInt(2, 2001);
        st.setInt(3, patient.getSsn());
        st.setString(4, "Pending");
        st.setInt(5, FEE);
        st.execute();

        conn.close();
        return true;
    }

    private static ArrayList<String> getCompatibleTypesList(Patient patient) {
        String enumConstant = patient.getBloodType();
        if (enumConstant.endsWith("-")) {
            enumConstant = enumConstant.replace("-", "minus");
        } else {
            enumConstant = enumConstant.replace("+", "plus");
        }
        BloodType.BloodTypes[] compatibleBloodTypes = BloodType.getCompatibleTypes(BloodType.BloodTypes.valueOf(enumConstant));
        ArrayList<String> strs = new ArrayList<>();
        for(BloodType.BloodTypes type: compatibleBloodTypes) {
            strs.add(type.getS());
        }
        return strs;
    }

    public static ObservableList<RetrivalRequest> getTransfusionRequests() throws SQLException {
        String fname, requestedBlood, bloodOfPatient, urgency;
        int ssn;

        String sql = "SELECT bp.Fname, bp.SSN,\n" +
                "    r.Urgency_level, bp.Blood_type AS Participant_Blood_type,\n" +
                "    bprod.Blood_type AS Product_Blood_type\n" +
                "FROM blood_participant bp\n" +
                "JOIN recipient r ON bp.SSN = r.Pssn\n" +
                "JOIN recieves rec ON r.Pssn = rec.Rssn\n" +
                "JOIN blood_product bprod ON rec.Blood_id = bprod.Blood_id\n" +
                "WHERE rec.Status_of_Retrieval = 'Pending';";

        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ics_db_project", "root", "Nawaf1");
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        ObservableList<RetrivalRequest> retrivals = FXCollections.observableArrayList();
        while (rs.next()) {
            ssn = rs.getInt("SSN");
            fname = rs.getString("Fname");
            requestedBlood = rs.getString("Product_Blood_type");
            bloodOfPatient = rs.getString("Participant_Blood_type");
            urgency = rs.getString("Urgency_level");
            RetrivalRequest ret = new RetrivalRequest(ssn, fname, requestedBlood, bloodOfPatient, urgency);
            retrivals.add(ret);
        }
        return retrivals;
    }

    public static void handleRequest(RetrivalRequest request, int nurseID, String status) throws SQLException {
        String sql = "UPDATE `ics_db_project`.`recieves`\n" +
                "SET\n" +
                "`Nssn` = ?,\n" +
                "`Date_of_Retrieval` = ?,\n" +
                "`Status_of_Retrieval` = ?\n" +
                "WHERE `Rssn` = ?;";

        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ics_db_project", "root", "Nawaf1");
        PreparedStatement st = conn.prepareStatement(sql);
        st.setInt(1, nurseID);
        st.setDate(2, Date.valueOf(LocalDate.now()));
        st.setString(3, status);
        st.setInt(4, request.getSsn());

        st.execute();
        conn.close();
    }

    public static String getEmail(int ssn) throws SQLException {
        String sql = "SELECT Email FROM blood_participant\n" +
                     "WHERE Ssn = ?";

        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ics_db_project", "root", "Nawaf1");
        PreparedStatement st = conn.prepareStatement(sql);

        st.setInt(1, ssn);

        ResultSet rs = st.executeQuery();
        rs.next();
        return rs.getString("Email");
    }

    public static ObservableList<RetrivalRequest> getTransfusions(int ssn) throws  SQLException {
        String sql = "SELECT n.Fname AS Nurse_FirstName, r.Blood_Id, r.Status_of_Retrieval, r.Fee, bp.Blood_type\n" +
                    "FROM recieves r\n" +
                    "JOIN nurse n ON r.Nssn = n.Nssn\n" +
                    "JOIN blood_product bp ON r.Blood_id = bp.Blood_id\n" +
                    "WHERE r.Rssn = ?;";

        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ics_db_project", "root", "Nawaf1");

        PreparedStatement st = conn.prepareStatement(sql);
        st.setInt(1, ssn);
        ObservableList<RetrivalRequest> retrivalRequests = FXCollections.observableArrayList();
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            int bloodId = rs.getInt("Blood_Id");
            String nurse = rs.getString("Nurse_FirstName");
            String status = rs.getString("Status_of_Retrieval");
            int fee = rs.getInt("Fee");
            String type = rs.getString("Blood_type");
            RetrivalRequest req = new RetrivalRequest(bloodId, nurse, status, fee, type);
            retrivalRequests.add(req);
        }
        conn.close();
        return retrivalRequests;
    }

    public static void deleteRetrieval(int ssn, int bloodId) {
        String sql = "DELETE FROM `ics_db_project`.`recieves`\n" +
                "WHERE Rssn = ? AND Blood_id = ?;";
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ics_db_project", "root", "Nawaf1");

            PreparedStatement st = conn.prepareStatement(sql);

            st.setInt(1, ssn);
            st.setInt(2, bloodId);
            st.execute();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
