package bloodmanager;

import javafx.beans.property.*;

import java.util.Date;

public class Patient {
    private final IntegerProperty ssn = new SimpleIntegerProperty();
    private final IntegerProperty weight = new SimpleIntegerProperty();
    private final IntegerProperty height = new SimpleIntegerProperty();
    private final IntegerProperty phone = new SimpleIntegerProperty();
    private final StringProperty fname = new SimpleStringProperty();
    private final StringProperty lname = new SimpleStringProperty();
    private final StringProperty bloodType = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty gender = new SimpleStringProperty();
    private final StringProperty address= new SimpleStringProperty();
    private final ObjectProperty<java.sql.Date> birthDate = new SimpleObjectProperty<>();

    public Patient() {}

    public Patient(int ssn, int weight, int height, int phone,
                   String fname, String lname, String bloodType,
                   String email, String gender, String addres, Date birthDate) {
        this.ssn.set(ssn);
        this.weight.set(weight);
        this.height.set(height);
        this.phone.set(phone);

        this.fname.set(fname);
        this.lname.set(lname);
        this.bloodType.set(bloodType);
        this.email.set(email);
        this.gender.set(gender);
        this.address.set(addres);
        this.birthDate.set((java.sql.Date) birthDate);
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

    public java.sql.Date getBirthDate() {
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

    @Override
    public boolean equals(Object obj) {
        Patient other = new Patient();
        if (obj instanceof Patient)
            other = (Patient) obj;
        if (this.getSsn() != other.getSsn())
            return false;
        if (this.getWeight() != other.getWeight())
            return false;
        if (this.getHeight() != other.getHeight())
            return false;
        if (this.getPhone() != other.getPhone())
            return false;
        if (!this.getBirthDate().equals(other.getBirthDate()))
            return false;
        if (!this.getGender().equals(other.getGender()))
            return false;
        if (!this.getBloodType().equals(other.getBloodType()))
            return false;
        if (!this.getEmail().equals(other.getEmail()))
            return false;
        if (!this.getFname().equals(other.getFname()))
            return false;
        return this.getLname().equals(other.getLname());
    }
}
