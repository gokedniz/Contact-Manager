package ui;

import model.Contact;
import model.Role;
import model.User;
import service.AuthenticationService;
import service.ContactService;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConsoleUI {
    private ConsoleHelper helper;
    private AuthenticationService authService;
    private ContactService contactService;
    private service.ActivityLogService activityLogService;
    private User currentUser;

    public ConsoleUI() {
        this.helper = new ConsoleHelper();
        this.authService = new AuthenticationService();
        this.contactService = new ContactService();
        this.activityLogService = new service.ActivityLogService();
    }

    public void start() {
        helper.clearScreen();
        helper.printAsciiArt();

        boolean running = true;
        while (running) {
            if (currentUser == null) {
                helper.printMenuHeader("WELCOME GUEST");
                helper.printMenuOption(1, "Login");
                helper.printMenuOption(0, "Exit Application");
                helper.printMenuFooter();

                int choice = helper.readInt("Select an option");
                if (choice == 1) {
                    showLogin();
                    // Login sonrası ekranı temizlemek için döngü başa dönecek
                    if (currentUser != null)
                        helper.clearScreen();
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
        helper.clearScreen(); // Login ekranına girince temizle
        helper.printTitle("SYSTEM LOGIN");
        String username = helper.readString("Username");
        String password = helper.readString("Password");

        User user = authService.login(username, password);
        if (user != null) {
            currentUser = user;
            helper.printSuccess("Welcome back, " + user.getFirstName() + " (" + user.getRole() + ")");
            // Burada beklemeye gerek yok, direkt ana menüye geçsin kullanıcı
        } else {
            helper.printError("Invalid username or password.");
            helper.pressEnterToContinue(); // Hatayı okuması için bekletiyorum
        }
    }

    private void showMainMenu() {
        helper.clearScreen();
        helper.printMenuHeader(currentUser.getRole() + " MENU");

        // Common Options
        helper.printMenuOption(1, "List Contacts");
        helper.printMenuOption(2, "Search Contacts");

        // Role Specific Options
        if (hasPermission(Role.SENIOR_DEVELOPER)) {
            helper.printMenuOption(3, "Add Contact");
        }
        if (hasPermission(Role.JUNIOR_DEVELOPER)) {
            helper.printMenuOption(4, "Edit Contact (Direct ID)");
        }
        if (hasPermission(Role.SENIOR_DEVELOPER)) {
            helper.printMenuOption(5, "Delete Contact (Direct ID)");
        }
        if (hasPermission(Role.MANAGER)) {
            helper.printMenuOption(6, "View Statistics");
        }

        if (hasPermission(Role.JUNIOR_DEVELOPER)) {
            helper.printMenuOption(7, "Undo Last Operation");
        }

        helper.printMenuOption(8, "Change Password");

        if (hasPermission(Role.MANAGER)) {
            helper.printMenuOption(9, "View Activity Logs");
        }

        helper.printMenuOption(0, "Logout");
        helper.printMenuFooter();

        int choice = helper.readInt("Select an option");
        handleMenuChoice(choice);
    }

    private boolean hasPermission(Role minRole) {
        return currentUser.getRole().ordinal() >= minRole.ordinal();
    }

    private void handleMenuChoice(int choice) {
        if (choice != 0)
            helper.clearScreen();

        switch (choice) {
            case 0:
                currentUser = null;
                helper.clearScreen(); // Çıkış yapınca temizle
                helper.printInfo("Logged out.");
                break;
            case 1:
                listContacts();
                // listContacts kendi içinde döngüye ve bekletmeye sahip, ekstra bekletmeye
                // gerek yok
                break;
            case 2:
                handleSearch();
                break;
            case 3:
                addContact();
                helper.pressEnterToContinue(); // Ekleme bitti, sonucu görsün kullanıcı
                break;
            case 4:
                handleEditById();
                helper.pressEnterToContinue();
                break;
            case 5:
                deleteContact();
                helper.pressEnterToContinue();
                break;
            case 6:
                showStatistics();
                // İstatistik kendi içinde bekletmeye sahip
                break;
            case 7:
                if (hasPermission(Role.JUNIOR_DEVELOPER)) {
                    contactService.undoLastAction();
                    helper.printSuccess("Undo operation executed.");
                    helper.pressEnterToContinue();
                } else {
                    helper.printError("Access Denied.");
                    helper.pressEnterToContinue();
                }
                break;
            case 8:
                handleChangePassword();
                helper.pressEnterToContinue();
                break;
            case 9:
                showActivityLogs();
                helper.pressEnterToContinue();
                break;
            default:
                helper.printError("Invalid option.");
                helper.pressEnterToContinue();
        }
    }

    private void handleEditById() {
        if (hasPermission(Role.JUNIOR_DEVELOPER)) {
            helper.printTitle("EDIT MODE (DIRECT ID)");
            int id = helper.readInt("Enter Contact ID to Edit");
            Contact c = contactService.getContactById(id);
            if (c != null)
                editContact(c);
            else
                helper.printError("Contact not found.");
        } else
            helper.printError("Access Denied.");
    }

    private void showStatistics() {
        if (!hasPermission(Role.MANAGER)) {
            helper.printError("Access Denied.");
            helper.pressEnterToContinue();
            return;
        }

        helper.printTitle("SYSTEM STATISTICS");
        Map<String, Object> stats = contactService.getStatistics();

        // Üst Kısım: Genel Sayısal Veriler (Tablo Olarak Kalsın)
        List<String[]> statData = new ArrayList<>();
        statData.add(new String[] { "Total Contacts", String.valueOf(stats.get("Total Contacts")) });
        statData.add(new String[] { "Contacts with Email", String.valueOf(stats.get("Contacts with Email")) });
        statData.add(new String[] { "Contacts with Phone", String.valueOf(stats.get("Contacts with Phone")) });

        helper.printTable(new String[] { "Metric", "Value" }, statData);

        System.out.println();
        
        // Alt Kısım: Domain Dağılımı (ARTIK GRAFİK OLACAK)
        @SuppressWarnings("unchecked")
        Map<String, Integer> domains = (Map<String, Integer>) stats.get("Email Domains");

        // Helper'daki yeni grafik çiziciyi çağırıyoruz
        helper.printHorizontalBarChart("EMAIL DOMAIN DISTRIBUTION", domains);

        helper.pressEnterToContinue(); 
    }

    private void listContacts() {
        helper.printTitle("ALL CONTACTS");
        helper.printInfo("Sort keys: [N]ame, [S]urname, [E]mail. Append '-' for Desc (e.g., N-).");
        String sortInput = helper.readString("Sort by (Enter for Default ID)");

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

        // Listeyi gösterirken ekran temizlenmişti zaten
        List<String[]> tableData = convertContactsToTableData(contacts);
        String[] headers = { "ID", "Full Name", "Phone", "Email", "Nickname" };

        helper.printTable(headers, tableData);
        interactWithResults(contacts);
    }

    private void handleSearch() {
        helper.printTitle("SEARCH CONTACTS");

        System.out.println("Select fields to search in (comma separated, e.g. 1,3):");
        System.out.println("1. First Name");
        System.out.println("2. Last Name");
        System.out.println("3. Phone");
        System.out.println("4. Email");
        System.out.println("5. All Fields (Default)");

        List<String> fields = new ArrayList<>();

        while (true) {
            String fieldInput = helper.readString("Choice");

            if (fieldInput.isEmpty()) {
                fields.clear(); // Default to all
                break;
            }

            String[] choices = fieldInput.split(",");
            boolean valid = true;
            fields.clear(); // Reset for new attempt

            for (String choice : choices) {
                String trimmed = choice.trim();
                if (!trimmed.matches("[1-5]")) {
                    valid = false;
                    break;
                }

                switch (trimmed) {
                    case "1":
                        fields.add("first_name");
                        break;
                    case "2":
                        fields.add("last_name");
                        break;
                    case "3":
                        fields.add("phone_primary");
                        break;
                    case "4":
                        fields.add("email");
                        break;
                    case "5":
                        fields.clear();
                        break; // Will default to all
                }
            }

            if (valid) {
                break;
            } else {
                helper.printError("Invalid selection. Please enter numbers 1-5 separated by commas.");
            }
        }

        String query = helper.readString("Enter search term");
        helper.clearScreen();

        List<Contact> results = contactService.searchContacts(query, fields);

        if (results.isEmpty()) {
            helper.printWarning("No contacts found matching: " + query);
            helper.pressEnterToContinue();
        } else {
            helper.printSuccess("Found " + results.size() + " matches:");
            String[] headers = { "ID", "Full Name", "Phone", "Email", "Nickname" };
            helper.printTable(headers, convertContactsToTableData(results));
            interactWithResults(results);
        }
    }

    private List<String[]> convertContactsToTableData(List<Contact> contacts) {
        List<String[]> data = new ArrayList<>();
        for (Contact c : contacts) {
            data.add(new String[] {
                    String.valueOf(c.getId()),
                    c.getFirstName() + " " + c.getLastName(),
                    c.getPhonePrimary(),
                    c.getEmail() != null ? c.getEmail() : "",
                    c.getNickname() != null ? c.getNickname() : ""
            });
        }
        return data;
    }

    private void interactWithResults(List<Contact> contacts) {
        if (contacts.isEmpty())
            return;

        while (true) {
            System.out.println();
            int id = helper.readInt("Enter Contact ID to View/Edit/Delete (0 to Back)");
            if (id == 0)
                return;

            Contact selected = contacts.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
            if (selected == null) {
                helper.printError("Contact not found in this list. Please try again.");
                continue;
            }

            // Alt menüye girince temizle
            helper.clearScreen();
            helper.printSectionHeader("SELECTED: " + selected.getFirstName().toUpperCase());

            // Kişi detaylarını tekrar hatırlatmak için mini tablo
            List<String[]> singleData = convertContactsToTableData(List.of(selected));
            helper.printTable(new String[] { "ID", "Full Name", "Phone", "Email", "Nick" }, singleData);

            helper.printMenuHeader("ACTIONS");

            helper.printMenuOption(1, "Edit Contact");
            helper.printMenuOption(2, "Delete Contact");
            helper.printMenuOption(0, "Cancel");
            helper.printMenuFooter();

            int choice = helper.readInt("Action");
            switch (choice) {
                case 1:
                    helper.clearScreen();
                    editContact(selected);
                    helper.pressEnterToContinue();
                    return; // Düzenlemeden sonra listeye geri dönmeyebilir, ana menüye atsın
                case 2:
                    if (hasPermission(Role.SENIOR_DEVELOPER)) {
                        String confirm = helper.readString("Are you sure you want to delete? (y/n)");
                        if (confirm.equalsIgnoreCase("y")) {
                            if (contactService.deleteContact(currentUser, selected.getId())) {
                                helper.printSuccess("Contact deleted.");
                            } else {
                                helper.printError("Failed to delete contact.");
                            }
                        }
                    } else {
                        helper.printError("Access Denied.");
                    }
                    helper.pressEnterToContinue();
                    return;
                case 0:
                    // İptal deyince listeye geri dönsün, ekranı temizle ve tabloyu yeniden basmak
                    // gerekir
                    // Ama basitlik adına listeyi tekrar basmıyoruz, kullanıcı 0'a basıp menüye
                    // dönecek.
                    helper.clearScreen();
                    return;
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

        helper.printTitle("EDIT CONTACT");
        helper.printInfo("Press Enter to keep current value.");

        String first = helper.readString("First Name [" + c.getFirstName() + "]");
        if (!first.isEmpty())
            c.setFirstName(first);

        String middle = helper.readString("Middle Name [" + (c.getMiddleName() != null ? c.getMiddleName() : "") + "]");
        if (!middle.isEmpty())
            c.setMiddleName(middle);

        String last = helper.readString("Last Name [" + c.getLastName() + "]");
        if (!last.isEmpty())
            c.setLastName(last);

        String nick = helper.readString("Nickname [" + (c.getNickname() != null ? c.getNickname() : "") + "]");
        if (!nick.isEmpty())
            c.setNickname(nick);

        String phone1 = helper.readString("Primary Phone [" + c.getPhonePrimary() + "]");
        if (!phone1.isEmpty())
            c.setPhonePrimary(phone1);

        String phone2 = helper
                .readString("Secondary Phone [" + (c.getPhoneSecondary() != null ? c.getPhoneSecondary() : "") + "]");
        if (!phone2.isEmpty())
            c.setPhoneSecondary(phone2);

        String email = helper.readString("Email [" + (c.getEmail() != null ? c.getEmail() : "") + "]");
        if (!email.isEmpty())
            c.setEmail(email);

        String linkedin = helper
                .readString("LinkedIn [" + (c.getLinkedinUrl() != null ? c.getLinkedinUrl() : "") + "]");
        if (!linkedin.isEmpty())
            c.setLinkedinUrl(linkedin);

        String birthStr = helper
                .readString("Birth Date [" + (c.getBirthDate() != null ? c.getBirthDate() : "") + "] (YYYY-MM-DD)");
        if (!birthStr.isEmpty()) {
            try {
                c.setBirthDate(Date.valueOf(birthStr));
            } catch (IllegalArgumentException e) {
                helper.printError("Invalid date format. Date not updated.");
            }
        }

        if (contactService.updateContact(currentUser, c)) {
            helper.printSuccess("Contact updated.");
        } else {
            helper.printError("Failed to update contact.");
        }
    }

    private void addContact() {
        if (!hasPermission(Role.SENIOR_DEVELOPER)) {
            helper.printError("Access Denied.");
            return;
        }

        helper.printTitle("ADD NEW CONTACT");

        // Verileri kullanıcıdan alıyoruz
        String first = helper.readString("First Name");
        String middle = helper.readString("Middle Name (Optional)");
        String last = helper.readString("Last Name");
        String nick = helper.readString("Nickname (Optional)");
        String phone1 = helper.readString("Primary Phone");
        String phone2 = helper.readString("Secondary Phone (Optional)");
        String email = helper.readString("Email (Optional)");
        String linkedin = helper.readString("LinkedIn (Optional)");
        Date birth = helper.readDate("Birth Date (Optional)");

        // HATA VEREN KISIM YERİNE BUNU KULLAN:
        // Tüm verileri tek seferde Constructor'a gönderiyoruz.
        // Boş girilen (Optional) alanlar için veritabanına 'null' gönderiyoruz.
        Contact contact = new Contact(
                first,
                middle.isEmpty() ? null : middle,
                last,
                nick.isEmpty() ? null : nick,
                phone1,
                phone2.isEmpty() ? null : phone2,
                email.isEmpty() ? null : email,
                linkedin.isEmpty() ? null : linkedin,
                birth);

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
        helper.printTitle("DELETE CONTACT");
        int id = helper.readInt("Enter Contact ID to delete");
        if (contactService.deleteContact(currentUser, id)) {
            helper.printSuccess("Contact deleted.");
        } else {
            helper.printError("Failed to delete contact (or access denied).");
        }
    }

    private void handleChangePassword() {
        helper.printTitle("CHANGE PASSWORD");

        String oldPass = helper.readString("Enter Old Password (0 to Cancel)");
        if (oldPass.equals("0"))
            return;

        String newPass = helper.readString("Enter New Password");
        if (newPass.isEmpty()) {
            helper.printError("Password cannot be empty.");
            return;
        }

        String confirmPass = helper.readString("Confirm New Password");
        if (!newPass.equals(confirmPass)) {
            helper.printError("Passwords do not match.");
            return;
        }

        int result = authService.changePassword(currentUser, oldPass, newPass);
        switch (result) {
            case 0:
                helper.printSuccess("Password changed successfully.");
                break;
            case 1:
                helper.printError("Incorrect old password.");
                break;
            case 2:
                helper.printError("New password cannot be the same as the old password.");
                break;
            default:
                helper.printError("Database error occurred.");
        }
    }

    private void showActivityLogs() {
        if (!hasPermission(Role.MANAGER)) {
            helper.printError("Access Denied.");
            return;
        }

        helper.printTitle("Activity Logs");

        String usernameFilter;
        while (true) {
            usernameFilter = helper.readString("Filter by Username (Enter for All)");
            if (usernameFilter.isEmpty())
                break;

            if (authService.isUserExists(usernameFilter)) {
                break;
            }
            helper.printError("User not found: " + usernameFilter);
        }

        String actionFilter;
        while (true) {
            actionFilter = helper.readString("Filter by Action [LOGIN, ADD, UPDATE, DELETE] (Enter for All)");
            if (actionFilter.isEmpty())
                break;

            String upper = actionFilter.toUpperCase();
            if (upper.equals("LOGIN") || upper.equals("ADD") || upper.equals("UPDATE") || upper.equals("DELETE")) {
                actionFilter = upper;
                break;
            }
            helper.printError("Invalid action type. Please try again.");
        }

        String sortOrder = "DESC"; // Default Newest
        while (true) {
            String sortInput = helper.readString("Show [N]ewest first or [O]ldest first? (Default: Newest)");
            if (sortInput.isEmpty() || sortInput.equalsIgnoreCase("N")) {
                sortOrder = "DESC";
                break;
            } else if (sortInput.equalsIgnoreCase("O")) {
                sortOrder = "ASC";
                break;
            }
            helper.printError("Invalid choice. Please enter 'N' or 'O'.");
        }

        List<model.ActivityLog> logs = activityLogService.getAllLogs(usernameFilter, actionFilter, sortOrder);

        if (logs.isEmpty()) {
            helper.printInfo("No activity logs found.");
        } else {
            List<String[]> tableData = new ArrayList<>();
            for (model.ActivityLog log : logs) {
                tableData.add(new String[] {
                        String.valueOf(log.getLogId()),
                        String.valueOf(log.getTimestamp()),
                        log.getUsername(),
                        String.valueOf(log.getUserId()),
                        log.getActionType(),
                        log.getDetails()
                });
            }
            String[] headers = { "ID", "Timestamp", "Username", "User ID", "Action", "Details" };
            helper.printTable(headers, tableData);
        }
    }
}
