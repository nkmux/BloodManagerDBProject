package bloodmanager;

public class User {
    private final int id;
    private final Privilege privilege;
    private final String name;
    
    public User(int id, String name, Privilege privilege) {
        this.id = id;
        this.name = name;
        this.privilege = privilege;
    }

    public int getId() {
        return id;
    }

    public Privilege getPrivilege() {
        return privilege;
    }

    public String getName() {
        return name;
    }
}
