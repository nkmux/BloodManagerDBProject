package bloodmanager;

import javafx.beans.property.*;

import java.util.Date;

public class Patient {
    private final IntegerProperty ssn, weight, height, phone;
    private final StringProperty fname, lname, bloodType, email, gender, address;
    private final ObjectProperty<java.sql.Date> birthDate;

    public Patient() {
        this.ssn = new SimpleIntegerProperty();
        this.weight = new SimpleIntegerProperty();
        this.height = new SimpleIntegerProperty();
        this.phone = new SimpleIntegerProperty();

        this.fname = new SimpleStringProperty();
        this.lname = new SimpleStringProperty();
        this.bloodType = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.gender = new SimpleStringProperty();
        this.address = new SimpleStringProperty();
        this.birthDate = new SimpleObjectProperty<>();
    }

    public String getBloodType() {
        return bloodType.get();
    }

    public int getHeight() {
        return height.get();
    }

    public int getPhone() {
        return phone.get();
    }

    public int getSsn() {
        return ssn.get();
    }

    public int getWeight() {
        return weight.get();
    }

    public String getAddress() {
        return address.get();
    }

    public Date getBirthDate() {
        return birthDate.get();
    }

    public String getEmail() {
        return email.get();
    }

    public String getFname() {
        return fname.get();
    }

    public String getGender() {
        return gender.get();
    }

    public String getLname() {
        return lname.get();
    }

    public void setHeight(int height) {
        this.height.set(height);
    }

    public void setBloodType(String bloodType) {
        this.bloodType.set(bloodType);
    }

    public void setPhone(int phone) {
        this.phone.set(phone);
    }

    public void setSsn(int ssn) {
        this.ssn.set(ssn);
    }

    public void setWeight(int weight) {
        this.weight.set(weight);
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate.set((java.sql.Date) birthDate);
    }

    public void setFname(String fname) {
        this.fname.set(fname);
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public void setLname(String lname) {
        this.lname.set(lname);
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public StringProperty bloodTypeProperty() {
        return bloodType;
    }

    public IntegerProperty heightProperty() {
        return height;
    }

    public StringProperty genderProperty() {
        return gender;
    }

    public ObjectProperty<java.sql.Date> birthDateProperty() {
        return birthDate;
    }

    public IntegerProperty ssnProperty() {
        return ssn;
    }

    public StringProperty addressProperty() {
        return address;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty fnameProperty() {
        return fname;
    }

    public StringProperty lnameProperty() {
        return lname;
    }

    public IntegerProperty weightProperty() {
        return weight;
    }

    public IntegerProperty phoneProperty() {
        return phone;
    }
}
