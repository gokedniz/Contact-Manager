package model;

/**
 * Abstract class representing a user in the contact management system.
 * Contains common attributes and methods for all user roles.
 * 
 * <p>
 * Subclasses must implement the showMenu method to display role-specific menu
 * options.
 * </p>
 * 
 * @author Group 10
 * @version 1.0
 */
public abstract class User {
    private int id;
    private String username;
    private String firstName;
    private String lastName;
    private String passwordHash;
    private Role role;

    /**
     * Constructs a new User with the specified details.
     * 
     * @param id           The unique identifier for the user.
     * @param username     The username of the user.
     * @param firstName    The first name of the user.
     * @param lastName     The last name of the user.
     * @param passwordHash The hashed password of the user.
     * @param role         The role of the user.
     */
    public User(int id, String username, String firstName, String lastName, String passwordHash, Role role) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    /**
     * Gets the unique identifier of the user.
     * 
     * @return The user ID.
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the username of the user.
     * 
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the first name of the user.
     * 
     * @return The first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets the last name of the user.
     * 
     * @return The last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the hashed password of the user.
     * 
     * @return The password hash.
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Gets the role of the user.
     * 
     * @return The user role.
     */
    public Role getRole() {
        return role;
    }

    // Abstract method to get permissions or menu options could be added here
    /**
     * Displays the menu options available to the user based on their role.
     * Subclasses must provide their own implementation.
     */
    public abstract void showMenu();

    /**
     * Returns a string representation of the user.
     * 
     * @return A string containing user details.
     */
    @Override
    public String toString() {
        return String.format("User[id=%d, username=%s, role=%s]", id, username, role);
    }
}
