package bloodmanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.*;

public class Disease {
    public static Dialog<String> getDiseaseDialog(Patient patient) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Patient's Disease");
        dialog.setHeaderText("The diseases the patient has: ");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        ListView<String> diseases = new ListView<>();
        try {
            diseases.setItems(getDiseases(patient));
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        diseases.setPrefHeight(200);
        Label diseaseLabel = new Label("Does the patient has anymore diseases?");
        TextField diseaseField = new TextField();
        diseaseField.setPromptText("Disease");

        Button addBtn = new Button("Add");
        Button delBtn = new Button("Remove");
        HBox hBox = new HBox(4, addBtn, delBtn);
        hBox.setSpacing(150);

        addBtn.setOnAction(e -> {
            if (diseaseField.getText().isEmpty()) {
                return;
            }
            try {
                insertDisease(patient, diseaseField.getText());
                diseases.getItems().add(diseaseField.getText());
                diseaseField.setText("");
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        });

        delBtn.setOnAction(e -> {
            String str = diseases.getSelectionModel().getSelectedItem();
            if (str == null) {
                new Alert(Alert.AlertType.ERROR, "Select a disease first.").showAndWait();
                return;
            }
            try {
                deleteDisease(patient, str);
                diseases.getItems().remove(str);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        });
        VBox vBox = new VBox(2, diseases, diseaseLabel,
                diseaseField, hBox);
        vBox.setSpacing(10);
        dialogPane.setContent(vBox);
        return dialog;
    }

    public static ObservableList<String> getDiseases(Patient patient) throws SQLException {
        String sql = "SELECT * FROM `ics_db_project`.`diseases`\n" +
                      "WHERE Pssn = ?;";
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ics_db_project", "root", "Nawaf1");
        PreparedStatement st = conn.prepareStatement(sql);

        st.setInt(1, patient.getSsn());
        ResultSet rs = st.executeQuery();
        ObservableList<String> diseases = FXCollections.observableArrayList();
        while (rs.next()) {
            diseases.add(rs.getString("Disease"));
        }
        return diseases;
    }

    private static void insertDisease(Patient patient, String disease) throws  SQLException {
        String sql = "INSERT INTO `ics_db_project`.`diseases`\n" +
                "(`Pssn`, `Disease`)\n" +
                "VALUES (?, ?);\n";

        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ics_db_project", "root", "Nawaf1");
        PreparedStatement st = conn.prepareStatement(sql);

        st.setInt(1, patient.getSsn());
        st.setString(2, disease);

        st.execute();
        conn.close();
    }

    private static void deleteDisease(Patient patient, String disease) throws SQLException {
        String sql = "DELETE FROM `ics_db_project`.`diseases`\n" +
                     "WHERE Pssn = ? AND Disease = ?;";
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ics_db_project", "root", "Nawaf1");
        PreparedStatement st = conn.prepareStatement(sql);

        st.setInt(1, patient.getSsn());
        st.setString(2, disease);

        st.execute();
        conn.close();
    }

}
