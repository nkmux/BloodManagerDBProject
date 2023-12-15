package bloodmanager;

public class User {
    private Privilege privilege;
    private String name;
    
    public User(String name, Privilege privilege) {
        this.name = name;
        this.privilege = privilege;
    }

    public void setPrivilege(Privilege priv) {
        this.privilege = priv;
    }

    public Privilege getPrivilege() {
        return privilege;
    }

    public String getName() {
        return name;
    }
}
