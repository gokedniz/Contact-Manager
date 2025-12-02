package com.cmpe343.project.model;

public class SeniorDeveloper extends User {

    public SeniorDeveloper(int id, String username, String firstName, String lastName, String passwordHash) {
        super(id, username, firstName, lastName, passwordHash, Role.SENIOR_DEVELOPER);
    }

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
