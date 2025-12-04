package model;

/**
 * Represents a Junior Developer user in the contact management system.
 * Inherits from the User class and provides specific menu options for junior developers.
 * 
 * <p>Junior Developers have restricted access compared to other user roles.</p>
 * 
 * @author Group 10
 */
public class JuniorDeveloper extends User {

    /**
     * Constructs a new Junior Developer user with the specified details.
     * 
     * @param id            The unique identifier for the user.
     * @param username      The username of the user.
     * @param firstName     The first name of the user.
     * @param lastName      The last name of the user.
     * @param passwordHash  The hashed password of the user.
     */
    public JuniorDeveloper(int id, String username, String firstName, String lastName, String passwordHash) {
        super(id, username, firstName, lastName, passwordHash, Role.JUNIOR_DEVELOPER);
    }

    /**
     * Displays the menu options available to a Junior Developer.
     * <p>Options include listing, searching, and updating contacts, but exclude 
     * administrative tasks like adding or deleting users.</p>
     */
    @Override
    public void showMenu() {
        System.out.println("--- Junior Developer Menu ---");
        System.out.println("1. List Contacts");
        System.out.println("2. Search Contacts");
        System.out.println("3. Add Contact");
        System.out.println("4. Edit Contact (Name only)"); // Example restriction
        System.out.println("0. Logout");
    }
}
