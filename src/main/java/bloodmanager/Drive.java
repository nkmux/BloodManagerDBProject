package bloodmanager;

import javafx.beans.property.*;

import java.sql.Date;

public class Drive {
    private Date from, to;
    private String location;


    private int id;
    private final StringProperty bloodTypes = new SimpleStringProperty();
    private final DoubleProperty totalBloodCollected = new SimpleDoubleProperty();

    public Drive(int id, Date from, Date to, String location) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.location = location;
    }
    public Drive() {}

    public int getId() {
        return id;
    }

    public void setBloodTypes(String bloodTypes) {
        this.bloodTypes.set(bloodTypes);
    }

    public void setTotalBloodCollected(double totalBloodCollected) {
        this.totalBloodCollected.set(totalBloodCollected);
    }

    public DoubleProperty totalBloodCollectedProperty() {
        return totalBloodCollected;
    }

    public StringProperty bloodTypeProperty() {
        return bloodTypes;
    }

    public String getLocation() {
        return location;
    }

    public Date getFrom() {
        return from;
    }

    public Date getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "Location: " + this.location + "\n" +
                "From: " + this.from + "\n" +
                "To: " + this.to;
    }
}
