package model;

public class Manager extends User {

    public Manager(int id, String username, String firstName, String lastName, String passwordHash) {
        super(id, username, firstName, lastName, passwordHash, Role.MANAGER);
    }

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
