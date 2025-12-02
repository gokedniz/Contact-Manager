package model;

public class Tester extends User {

    public Tester(int id, String username, String firstName, String lastName, String passwordHash) {
        super(id, username, firstName, lastName, passwordHash, Role.TESTER);
    }

    @Override
    public void showMenu() {
        System.out.println("--- Tester Menu ---");
        System.out.println("1. List Contacts");
        System.out.println("2. Search Contacts");
        System.out.println("0. Logout");
    }
}
