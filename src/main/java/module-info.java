module bloodmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.mail;
    requires java.sql;

    opens Controllers to javafx.fxml;
    exports bloodmanager;
}