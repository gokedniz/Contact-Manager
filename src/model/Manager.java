package model;

/**
 * Represents a Manager user in the contact management system.
 * Inherits from the User class and provides specific menu options for managers.
 * 
 * <p>Managers have elevated access compared to other user roles.</p>
 * 
 * @author Group 10
 */
public class Manager extends User {

    /**
     * Constructs a new Manager user with the specified details.
     * 
     * @param id            The unique identifier for the user.
     * @param username      The username of the user.
     * @param firstName     The first name of the user.
     * @param lastName      The last name of the user.
     * @param passwordHash  The hashed password of the user.
     */
    public Manager(int id, String username, String firstName, String lastName, String passwordHash) {
        super(id, username, firstName, lastName, passwordHash, Role.MANAGER);
    }

    /**
     * Displays the menu options available to a Manager.
     * <p>Options include full access to contact management features, 
     * including adding, editing, deleting contacts, viewing statistics, 
     * and undoing operations.</p>
     */
    @Override
    public void showMenu() {
        System.out.println("--- Manager Menu ---");
        System.out.println("1. List Contacts");
        System.out.println("2. Search Contacts");
        System.out.println("3. Add Contact");
        System.out.println("4. Edit Contact");
        System.out.println("5. Delete Contact");
        System.out.println("6. View Statistics");
        System.out.println("7. Undo Last Operation");
        System.out.println("0. Logout");
    }
}
