package com.cmpe343.project.model;

public class JuniorDeveloper extends User {

    public JuniorDeveloper(int id, String username, String firstName, String lastName, String passwordHash) {
        super(id, username, firstName, lastName, passwordHash, Role.JUNIOR_DEVELOPER);
    }

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
