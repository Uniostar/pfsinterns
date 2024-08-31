
public class Patron {
    private int id;
    private String name;
    private String email;

    public Patron(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    // Additional constructor for cases without an ID (e.g., when adding a new patron)
    public Patron(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
