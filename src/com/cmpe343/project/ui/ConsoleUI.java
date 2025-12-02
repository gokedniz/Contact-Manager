package com.cmpe343.project.ui;

import com.cmpe343.project.model.Contact;
import com.cmpe343.project.model.Role;
import com.cmpe343.project.model.User;
import com.cmpe343.project.service.AuthenticationService;
import com.cmpe343.project.service.ContactService;

import java.sql.Date;
import java.util.List;

public class ConsoleUI {
    private ConsoleHelper helper;
    private AuthenticationService authService;
    private ContactService contactService;
    private User currentUser;

    public ConsoleUI() {
        this.helper = new ConsoleHelper();
        this.authService = new AuthenticationService();
        this.contactService = new ContactService();
    }

    public void start() {
        helper.printTitle("Role-Based Contact Management System");
        boolean running = true;
        while (running) {
            if (currentUser == null) {
                System.out.println("\n--- Welcome ---");
                helper.printMenuOption(1, "Login");
                helper.printMenuOption(0, "Exit Application");

                int choice = helper.readInt("Select an option");
                if (choice == 1) {
                    showLogin();
                } else if (choice == 0) {
                    running = false;
                    helper.printInfo("Goodbye!");
                } else {
                    helper.printError("Invalid option.");
                }
            } else {
                showMainMenu();
            }
        }
    }

    private void showLogin() {
        helper.printInfo("Please Login");
        String username = helper.readString("Username");
        String password = helper.readString("Password");

        User user = authService.login(username, password);
        if (user != null) {
            currentUser = user;
            helper.printSuccess("Welcome, " + user.getFirstName() + " (" + user.getRole() + ")");
        } else {
            helper.printError("Invalid username or password.");
        }
    }

    private void showMainMenu() {
        helper.printTitle(currentUser.getRole() + " Menu");

        // Common Options
        helper.printMenuOption(1, "List Contacts");
        helper.printMenuOption(2, "Search Contacts");

        // Role Specific Options
        if (hasPermission(Role.JUNIOR_DEVELOPER)) {
            helper.printMenuOption(3, "Add Contact");
            helper.printMenuOption(4, "Edit Contact (Direct ID)");
        }
        if (hasPermission(Role.SENIOR_DEVELOPER)) {
            helper.printMenuOption(5, "Delete Contact (Direct ID)");
        }
        if (hasPermission(Role.MANAGER)) {
            helper.printMenuOption(6, "View Statistics");
            helper.printMenuOption(7, "Undo Last Operation");
        }

        helper.printMenuOption(0, "Logout");

        int choice = helper.readInt("Select an option");
        handleMenuChoice(choice);
    }

    private boolean hasPermission(Role minRole) {
        return currentUser.getRole().ordinal() >= minRole.ordinal();
    }

    private void handleMenuChoice(int choice) {
        switch (choice) {
            case 0:
                currentUser = null;
                helper.printInfo("Logged out.");
                break;
            case 1:
                listContacts();
                break;
            case 2:
                handleSearch();
                break;
            case 3:
                addContact();
                break;
            case 4:
                if (hasPermission(Role.JUNIOR_DEVELOPER)) {
                    int id = helper.readInt("Enter Contact ID to Edit");
                    Contact c = contactService.getContactById(id);
                    if (c != null)
                        editContact(c);
                    else
                        helper.printError("Contact not found.");
                } else
                    helper.printError("Access Denied.");
                break;
            case 5:
                deleteContact();
                break;
            case 6:
                showStatistics();
                break;
            case 7:
                helper.printInfo("Undo feature coming soon...");
                break;
            default:
                helper.printError("Invalid option.");
        }
    }

    private void showStatistics() {
        if (!hasPermission(Role.MANAGER)) {
            helper.printError("Access Denied.");
            return;
        }

        helper.printTitle("System Statistics");
        java.util.Map<String, Object> stats = contactService.getStatistics();

        System.out.println("Total Contacts: " + stats.get("Total Contacts"));
        System.out.println("Contacts with Email: " + stats.get("Contacts with Email"));
        System.out.println("Contacts with Phone: " + stats.get("Contacts with Phone"));

        System.out.println("\nEmail Domain Distribution:");
        @SuppressWarnings("unchecked")
        java.util.Map<String, Integer> domains = (java.util.Map<String, Integer>) stats.get("Email Domains");
        if (domains.isEmpty()) {
            System.out.println("  No email data available.");
        } else {
            domains.forEach((domain, count) -> System.out.println("  - " + domain + ": " + count));
        }

        helper.readString("\nPress Enter to continue");
    }

    private void listContacts() {
        helper.printTitle("All Contacts");
        helper.printInfo("Sort: [N]ame, [S]urname, [E]mail. Append '-' for Desc (e.g., N-). [Enter] for ID.");
        String sortInput = helper.readString("Sort by");

        String sortBy = "id";
        if (sortInput.equalsIgnoreCase("n"))
            sortBy = "name_asc";
        else if (sortInput.equalsIgnoreCase("n-"))
            sortBy = "name_desc";
        else if (sortInput.equalsIgnoreCase("s"))
            sortBy = "surname_asc";
        else if (sortInput.equalsIgnoreCase("s-"))
            sortBy = "surname_desc";
        else if (sortInput.equalsIgnoreCase("e"))
            sortBy = "email_asc";
        else if (sortInput.equalsIgnoreCase("e-"))
            sortBy = "email_desc";

        List<Contact> contacts = contactService.getContactsSorted(sortBy);
        printContactList(contacts);
        interactWithResults(contacts);
    }

    private void handleSearch() {
        helper.printTitle("Search Contacts");
        String query = helper.readString("Enter search term (Name, Phone, Email)");
        List<Contact> results = contactService.searchContacts(query);

        if (results.isEmpty()) {
            helper.printInfo("No contacts found matching: " + query);
        } else {
            helper.printSuccess("Found " + results.size() + " matches:");
            printContactList(results);
            interactWithResults(results);
        }
    }

    private void interactWithResults(List<Contact> contacts) {
        if (contacts.isEmpty())
            return;

        System.out.println("\n---------------------------------------------------------------");

        while (true) {
            int id = helper.readInt("Enter Contact ID to View/Edit/Delete (0 to Back)");
            if (id == 0)
                return;

            Contact selected = contacts.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
            if (selected == null) {
                helper.printError("Contact not found in this list. Please try again.");
                continue;
            }

            helper.printTitle("Selected: " + selected.getFirstName() + " " + selected.getLastName());
            helper.printMenuOption(1, "Edit Contact");
            helper.printMenuOption(2, "Delete Contact");
            helper.printMenuOption(0, "Cancel");

            int choice = helper.readInt("Action");
            switch (choice) {
                case 1:
                    editContact(selected);
                    return;
                case 2:
                    if (hasPermission(Role.SENIOR_DEVELOPER)) {
                        if (contactService.deleteContact(currentUser, selected.getId())) {
                            helper.printSuccess("Contact deleted.");
                        } else {
                            helper.printError("Failed to delete contact.");
                        }
                    } else {
                        helper.printError("Access Denied: Only Senior Developers and Managers can delete.");
                    }
                    return;
                case 0:
                    break;
                default:
                    helper.printError("Invalid option.");
            }
        }
    }

    private void editContact(Contact c) {
        if (!hasPermission(Role.JUNIOR_DEVELOPER)) {
            helper.printError("Access Denied.");
            return;
        }

        helper.printInfo("Editing Contact (Press Enter to keep current value)");

        String first = helper.readString("First Name [" + c.getFirstName() + "]");
        if (!first.isEmpty())
            c.setFirstName(first);

        String middle = helper.readString("Middle Name [" + (c.getMiddleName() == null ? "" : c.getMiddleName()) + "]");
        if (!middle.isEmpty())
            c.setMiddleName(middle);

        String last = helper.readString("Last Name [" + c.getLastName() + "]");
        if (!last.isEmpty())
            c.setLastName(last);

        String nick = helper.readString("Nickname [" + (c.getNickname() == null ? "" : c.getNickname()) + "]");
        if (!nick.isEmpty())
            c.setNickname(nick);

        if (hasPermission(Role.SENIOR_DEVELOPER)) {
            String phone1 = helper.readString("Phone 1 [" + c.getPhonePrimary() + "]");
            if (!phone1.isEmpty())
                c.setPhonePrimary(phone1);

            String phone2 = helper
                    .readString("Phone 2 [" + (c.getPhoneSecondary() == null ? "" : c.getPhoneSecondary()) + "]");
            if (!phone2.isEmpty())
                c.setPhoneSecondary(phone2);

            String email = helper.readString("Email [" + (c.getEmail() == null ? "" : c.getEmail()) + "]");
            if (!email.isEmpty())
                c.setEmail(email);

            String linkedin = helper
                    .readString("LinkedIn [" + (c.getLinkedinUrl() == null ? "" : c.getLinkedinUrl()) + "]");
            if (!linkedin.isEmpty())
                c.setLinkedinUrl(linkedin);
        } else {
            helper.printInfo("Note: You can only edit name fields as Junior Developer.");
        }

        if (contactService.updateContact(currentUser, c)) {
            helper.printSuccess("Contact updated.");
        } else {
            helper.printError("Failed to update contact.");
        }
    }

    private void printContactList(List<Contact> contacts) {
        if (contacts.isEmpty()) {
            helper.printInfo("No contacts found.");
        } else {
            System.out.printf("%-5s %-20s %-15s %-20s%n", "ID", "Name", "Phone", "Email");
            System.out.println("---------------------------------------------------------------");
            for (Contact c : contacts) {
                System.out.printf("%-5d %-20s %-15s %-20s%n",
                        c.getId(),
                        c.getFirstName() + " " + c.getLastName(),
                        c.getPhonePrimary(),
                        c.getEmail() != null ? c.getEmail() : "N/A");
            }
        }
    }

    private void addContact() {
        if (!hasPermission(Role.JUNIOR_DEVELOPER)) {
            helper.printError("Access Denied.");
            return;
        }

        helper.printTitle("Add New Contact");
        String first = helper.readString("First Name");
        String middle = helper.readString("Middle Name (Optional)");
        String last = helper.readString("Last Name");
        String nick = helper.readString("Nickname (Optional)");
        String phone1 = helper.readString("Primary Phone");
        String phone2 = helper.readString("Secondary Phone (Optional)");
        String email = helper.readString("Email (Optional)");
        String linkedin = helper.readString("LinkedIn (Optional)");
        Date birth = helper.readDate("Birth Date (Optional)");

        Contact contact = new Contact(first, middle.isEmpty() ? null : middle, last,
                nick.isEmpty() ? null : nick, phone1,
                phone2.isEmpty() ? null : phone2,
                email.isEmpty() ? null : email,
                linkedin.isEmpty() ? null : linkedin, birth);

        if (contactService.addContact(currentUser, contact)) {
            helper.printSuccess("Contact added successfully.");
        } else {
            helper.printError("Failed to add contact.");
        }
    }

    private void deleteContact() {
        if (!hasPermission(Role.SENIOR_DEVELOPER)) {
            helper.printError("Access Denied.");
            return;
        }
        int id = helper.readInt("Enter Contact ID to delete");
        if (contactService.deleteContact(currentUser, id)) {
            helper.printSuccess("Contact deleted.");
        } else {
            helper.printError("Failed to delete contact (or access denied).");
        }
    }
}
