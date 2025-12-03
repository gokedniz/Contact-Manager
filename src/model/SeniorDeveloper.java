package model;

/**
 * Represents a Senior Developer user in the contact management system.
 * Inherits from the User class and provides specific menu options for senior developers.
 * 
 * <p>Senior Developers have more access than Junior Developers but less than Managers.</p>
 * 
 * @author Group 10
 */
public class SeniorDeveloper extends User {

    /**
     * Constructs a new Senior Developer user with the specified details.
     * 
     * @param id            The unique identifier for the user.
     * @param username      The username of the user.
     * @param firstName     The first name of the user.
     * @param lastName      The last name of the user.
     * @param passwordHash  The hashed password of the user.
     */
    public SeniorDeveloper(int id, String username, String firstName, String lastName, String passwordHash) {
        super(id, username, firstName, lastName, passwordHash, Role.SENIOR_DEVELOPER);
    }

    /**
     * Displays the menu options available to a Senior Developer.
     * <p>Options include listing, searching, adding, editing, and deleting contacts, 
     * but exclude administrative tasks like viewing statistics or undoing operations.</p>
     */
    @Override
    public void showMenu() {
        System.out.println("--- Senior Developer Menu ---");
        System.out.println("1. List Contacts");
        System.out.println("2. Search Contacts");
        System.out.println("3. Add Contact");
        System.out.println("4. Edit Contact");
        System.out.println("5. Delete Contact");
        System.out.println("0. Logout");
    }
}
