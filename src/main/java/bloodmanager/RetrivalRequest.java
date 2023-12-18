package bloodmanager;

public class RetrivalRequest {
    private String fname, requestedBlood, bloodOfPatient, urgency, nurse, status;
    private int ssn, fee, bloodId;
    public RetrivalRequest(int ssn, String fname, String requestedBlood, String bloodOfPatient, String urgency) {
        this.ssn = ssn;
        this.fname = fname;
        this.requestedBlood = requestedBlood;
        this.bloodOfPatient = bloodOfPatient;
        this.urgency = urgency;
    }

    public RetrivalRequest(int bloodId, String nurse, String status, int fee, String requestedBlood) {
        this.bloodId = bloodId;
        this.nurse = nurse;
        this.status = status;
        this.fee = fee;
        this.requestedBlood = requestedBlood;
    }

    public int getSsn() {
        return ssn;
    }

    public int getBloodId() {
        return bloodId;
    }

    public String getFname() {
        return fname;
    }

    public String getRequestedBlood() {
        return requestedBlood;
    }

    @Override
    public String toString() {
        if (fname != null) {
            String[] titles = {"From:", "ID:", "BloodType:", "Given Type:", "Urgency:"};
            return String.format("%-30s %10s", titles[0], fname) +
                    "\n" +
                    String.format("%-30s %10s", titles[1], ssn) +
                    "\n" +
                    String.format("%-30s %10s", titles[2], bloodOfPatient) +
                    "\n" +
                    String.format("%-30s %10s", titles[3], requestedBlood) +
                    "\n" +
                    String.format("%-100s %10s", titles[4], urgency);
        }
        else {
            String[] titles = {"Nurse:", "Status:", "BloodType:", "Given Type:", "Fee:"};
            return String.format("%-30s %10s", titles[0], nurse) +
                    "\n" +
                    String.format("%-30s %10s", titles[1], status) +
                    "\n" +
                    String.format("%-30s %10s", titles[2], requestedBlood) +
                    "\n" +
                    String.format("%-30s %10s", titles[3], fee);
        }
    }
}
