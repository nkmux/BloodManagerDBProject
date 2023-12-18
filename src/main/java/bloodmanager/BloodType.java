package bloodmanager;

public class BloodType {
    private BloodTypes type;
    public enum BloodTypes {
        Aplus("A+"),
        Oplus("O+"),
        Bplus("B+"),
        ABplus("AB+"),
        Aminus("A-"),
        Ominus("O-"),
        Bminus("B-"),
        ABminus("AB-");
        private final String s;

        BloodTypes(String s) {
            this.s = s;
        }

        public String getS() {
            return s;
        }
    }
    public BloodType(String type) {
        switch (type) {
            case "A+":
                this.type = BloodTypes.Aplus;
                break;
            case "B+":
                this.type = BloodTypes.Bplus;
                break;
            case "O+":
                this.type = BloodTypes.Oplus;
                break;
            case "AB+":
                this.type = BloodTypes.ABplus;
                break;
            case "A-":
                this.type = BloodTypes.Aminus;
                break;
            case "B-":
                this.type = BloodTypes.Bminus;
                break;
            case "O-":
                this.type = BloodTypes.Ominus;
                break;
            case "AB-":
                this.type = BloodTypes.ABminus;
                break;
        }
    }

    public String getType() {
        return type.name();
    }

    public static BloodTypes[] getCompatibleTypes(BloodTypes type) {
        BloodTypes[] compatibles = new BloodTypes[0];
        switch (type) {
            case Aplus:
                compatibles = new BloodTypes[]{BloodTypes.Aplus,
                                        BloodTypes.Aminus,
                                        BloodTypes.Oplus,
                                        BloodTypes.Ominus};
                break;
            case Oplus:
                compatibles = new BloodTypes[]{BloodTypes.Oplus,
                                        BloodTypes.Ominus};
                break;
            case Bplus:
                compatibles = new BloodTypes[]{BloodTypes.Bplus,
                                        BloodTypes.Bminus,
                                        BloodTypes.Oplus,
                                        BloodTypes.Ominus};
                break;
            case ABplus:
                compatibles = new BloodTypes[]{BloodTypes.Aplus,
                                        BloodTypes.Aminus,
                                        BloodTypes.Bplus,
                                        BloodTypes.Bminus,
                                        BloodTypes.Oplus,
                                        BloodTypes.Ominus,
                                        BloodTypes.ABplus,
                                        BloodTypes.ABminus};
                break;
            case Aminus:
                compatibles = new BloodTypes[]{BloodTypes.Aminus,
                                        BloodTypes.Ominus};
                break;
            case Ominus:
                compatibles = new BloodTypes[]{BloodTypes.Ominus};
                break;
            case Bminus:
                compatibles = new BloodTypes[]{BloodTypes.Bminus,
                                        BloodTypes.Ominus};
                break;
            case ABminus:
                compatibles = new BloodTypes[]{BloodTypes.ABminus,
                                        BloodTypes.Aminus,
                                        BloodTypes.Bminus,
                                        BloodTypes.Ominus};
                break;
        }
        return compatibles;
    }
}
