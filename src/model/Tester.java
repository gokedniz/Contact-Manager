package model;

/**
 * Represents a Tester user in the contact management system.
 * Inherits from the User class and provides specific menu options for testers.
 * 
 * <p>Testers have limited access compared to other user roles.</p>
 * 
 * @author Group 10
 * @version 1.0
 */
public class Tester extends User {

    /**
     * Constructs a new Tester user with the specified details.
     * 
     * @param id            The unique identifier for the user.
     * @param username      The username of the user.
     * @param firstName     The first name of the user.
     * @param lastName      The last name of the user.
     * @param passwordHash  The hashed password of the user.
     */
    public Tester(int id, String username, String firstName, String lastName, String passwordHash) {
        super(id, username, firstName, lastName, passwordHash, Role.TESTER);
    }

    /**
     * Displays the menu options available to a Tester.
     * <p>Options include listing and searching contacts only.</p>
     */
    @Override
    public void showMenu() {
        System.out.println("--- Tester Menu ---");
        System.out.println("1. List Contacts");
        System.out.println("2. Search Contacts");
        System.out.println("0. Logout");
    }
}
