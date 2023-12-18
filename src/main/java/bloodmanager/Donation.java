package bloodmanager;

import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class Donation {

    private int driveID, bloodId;
    private Date date;
    private String location, status;

    public Donation(int bloodId, int driveID, Date date, String location, String status) {
        this.bloodId = bloodId;
        this.driveID = driveID;
        this.date = date;
        this.location = location;
        this.status = status;
    }

    public int getBloodId() {
        return bloodId;
    }

    public int getDriveID() {
        return driveID;
    }

    @Override
    public String toString() {
        return "Location: " + location + "\nDate: "
                + date + "\nStatus: " + status;
    }
}
