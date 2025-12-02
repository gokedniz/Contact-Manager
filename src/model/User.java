package model;

public abstract class User {
    private int id;
    private String username;
    private String firstName;
    private String lastName;
    private String passwordHash;
    private Role role;

    public User(int id, String username, String firstName, String lastName, String passwordHash, Role role) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Role getRole() {
        return role;
    }

    // Abstract method to get permissions or menu options could be added here
    public abstract void showMenu();

    @Override
    public String toString() {
        return String.format("User[id=%d, username=%s, role=%s]", id, username, role);
    }
}
