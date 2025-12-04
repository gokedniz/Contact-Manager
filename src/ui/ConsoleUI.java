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
    private List<Integer> currentMenuActions;

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
        String username = helper.readRequiredString("Username");
        String password = helper.readRequiredString("Password");

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

        // Her seferinde listeyi sıfırla
        currentMenuActions = new ArrayList<>();
        int displayNum = 1;

        // --- 1. SEÇENEKLERİ BELİRLE VE YAZDIR ---

        // Herkes için ortak
        helper.printMenuOption(displayNum++, "List Contacts (Edit/Delete)");
        currentMenuActions.add(1); // Orijinal Action ID: 1

        helper.printMenuOption(displayNum++, "Search Contacts (Edit/Delete)");
        currentMenuActions.add(2); // Orijinal Action ID: 2

        // Role Özel Seçenekler
        if (hasPermission(Role.SENIOR_DEVELOPER)) {
            helper.printMenuOption(displayNum++, "Add Contact");
            currentMenuActions.add(3);
        }
        if (hasPermission(Role.MANAGER)) {
            helper.printMenuOption(displayNum++, "View Statistics");
            currentMenuActions.add(6);
        }
        if (hasPermission(Role.JUNIOR_DEVELOPER)) {
            helper.printMenuOption(displayNum++, "Undo Last Operation");
            currentMenuActions.add(7);
        }

        // Şifre Değiştirme (Herkes için)
        helper.printMenuOption(displayNum++, "Change Password");
        currentMenuActions.add(8);

        if (hasPermission(Role.MANAGER)) {
            helper.printMenuOption(displayNum++, "View Activity Logs");
            currentMenuActions.add(9);

            helper.printMenuOption(displayNum++, "Add New User");
            currentMenuActions.add(10);

            helper.printMenuOption(displayNum++, "Manage Users");
            currentMenuActions.add(11);
        }

        // Logout her zaman en sonda ve 0 numara olsun
        helper.printMenuOption(0, "Logout");
        helper.printMenuFooter();

        // --- 2. SEÇİMİ AL VE YÖNLENDİR ---
        int userChoice = helper.readInt("Select an option");

        if (userChoice == 0) {
            handleMenuChoice(0); // Çıkış
        } else if (userChoice > 0 && userChoice <= currentMenuActions.size()) {
            // Kullanıcının girdiği "Sıra Numarası"nı, gerçek "Action ID"ye çeviriyoruz
            int realActionId = currentMenuActions.get(userChoice - 1);
            handleMenuChoice(realActionId);
        } else {
            helper.printError("Invalid option.");
            helper.pressEnterToContinue();
        }
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
            // case 4 (Edit Direct ID) removed
            // case 5 (Delete Direct ID) removed
            case 6:
                showStatistics();
                // İstatistik kendi içinde bekletmeye sahip
                break;
            case 7:
                if (hasPermission(Role.JUNIOR_DEVELOPER)) {
                    if (contactService.undoLastAction()) {
                        helper.printSuccess("Undo operation executed.");
                    } else {
                        helper.printError("Nothing to undo.");
                    }
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
            case 10:
                handleAddNewUser();
                helper.pressEnterToContinue();
                break;
            case 11:
                handleManageUsers();
                helper.pressEnterToContinue();
                break;
            default:
                helper.printError("Invalid option.");
                helper.pressEnterToContinue();
        }
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

        // For Edit, we can't easily use readPhone(required=true) because we want to
        // allow empty to skip.
        // But if they DO enter something, it must be valid.
        // Let's use readPhone(required=false) which allows empty, but validates if not
        // empty.

        String phone1 = helper.readPhone("Primary Phone [" + c.getPhonePrimary() + "]", false);
        if (phone1 != null)
            c.setPhonePrimary(phone1);

        String phone2 = helper.readPhone(
                "Secondary Phone [" + (c.getPhoneSecondary() != null ? c.getPhoneSecondary() : "") + "]", false);
        if (phone2 != null)
            c.setPhoneSecondary(phone2);

        String email = helper.readEmail("Email [" + (c.getEmail() != null ? c.getEmail() : "") + "]", false);
        if (email != null)
            c.setEmail(email);

        String linkedin = helper
                .readString("LinkedIn [" + (c.getLinkedinUrl() != null ? c.getLinkedinUrl() : "") + "]");
        if (!linkedin.isEmpty())
            c.setLinkedinUrl(linkedin);

        String formattedDate = "";
        if (c.getBirthDate() != null) {
            java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy");
            formattedDate = c.getBirthDate().toLocalDate().format(dtf);
        }

        Date newDate = helper.readDate("Birth Date [" + formattedDate + "]");
        if (newDate != null) {
            c.setBirthDate(newDate);
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
        String first = helper.readRequiredString("First Name");
        String middle = helper.readString("Middle Name (Optional)");
        String last = helper.readRequiredString("Last Name");
        String nick = helper.readString("Nickname (Optional)");
        String phone1 = helper.readPhone("Primary Phone", true);
        String phone2 = helper.readPhone("Secondary Phone (Optional)", false);
        String email = helper.readEmail("Email (Optional)", false);
        String linkedin = helper.readString("LinkedIn (Optional)");
        Date birth = helper.readDate("Birth Date (Optional)");

        // Tüm verileri tek seferde Constructor'a gönderiyoruz.
        // Boş girilen (Optional) alanlar için veritabanına 'null' gönderiyoruz.
        Contact contact = new Contact(
                first,
                middle.isEmpty() ? null : middle,
                last,
                nick.isEmpty() ? null : nick,
                phone1,
                phone2, // readPhone returns null if optional and empty
                email, // readEmail returns null if optional and empty
                linkedin.isEmpty() ? null : linkedin,
                birth);

        if (contactService.addContact(currentUser, contact)) {
            helper.printSuccess("Contact added successfully.");
        } else {
            helper.printError("Failed to add contact.");
        }
    }

    private void handleChangePassword() {
        helper.printTitle("CHANGE PASSWORD");

        String oldPass = helper.readRequiredString("Enter Old Password (0 to Cancel)");
        if (oldPass.equals("0"))
            return;

        String newPass = helper.readRequiredString("Enter New Password");
        String confirmPass = helper.readRequiredString("Confirm New Password");

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

    private void handleAddNewUser() {
        if (!hasPermission(Role.MANAGER)) {
            helper.printError("Access Denied.");
            return;
        }

        helper.printTitle("ADD NEW USER");

        String username = helper.readRequiredString("Username");
        if (authService.isUserExists(username)) {
            helper.printError("Username already exists.");
            return;
        }

        String password = helper.readRequiredString("Password");
        String firstName = helper.readRequiredString("First Name");
        String lastName = helper.readRequiredString("Last Name");

        System.out.println("Select Role:");
        System.out.println("1. Tester");
        System.out.println("2. Junior Developer");
        System.out.println("3. Senior Developer");

        int roleChoice = helper.readInt("Role Choice");
        Role role = null;
        switch (roleChoice) {
            case 1:
                role = Role.TESTER;
                break;
            case 2:
                role = Role.JUNIOR_DEVELOPER;
                break;
            case 3:
                role = Role.SENIOR_DEVELOPER;
                break;
            default:
                helper.printError("Invalid role selection.");
                return;
        }

        if (authService.registerUser(currentUser, username, password, firstName, lastName, role)) {
            helper.printSuccess("User added successfully.");
        } else {
            helper.printError("Failed to add user.");
        }
    }

    private void handleManageUsers() {
        if (!hasPermission(Role.MANAGER)) {
            helper.printError("Access Denied.");
            return;
        }

        helper.printTitle("MANAGE USERS");
        List<User> users = authService.getAllUsers();

        if (users.isEmpty()) {
            helper.printInfo("No users found.");
            return;
        }

        List<String[]> tableData = new ArrayList<>();
        for (User u : users) {
            tableData.add(new String[] {
                    String.valueOf(u.getId()),
                    u.getUsername(),
                    u.getFirstName() + " " + u.getLastName(),
                    u.getRole().toString()
            });
        }
        helper.printTable(new String[] { "ID", "Username", "Name", "Role" }, tableData);

        int userId = helper.readInt("Enter User ID to Edit/Delete (0 to Cancel)");
        if (userId == 0)
            return;

        User selectedUser = users.stream().filter(u -> u.getId() == userId).findFirst().orElse(null);
        if (selectedUser == null) {
            helper.printError("User not found.");
            return;
        }

        helper.printSectionHeader("SELECTED: " + selectedUser.getUsername());
        helper.printMenuOption(1, "Edit User");
        helper.printMenuOption(2, "Delete User");
        helper.printMenuOption(0, "Cancel");

        int action = helper.readInt("Action");
        switch (action) {
            case 1:
                editUser(selectedUser);
                break;
            case 2:
                deleteUser(selectedUser);
                break;
            case 0:
                break;
            default:
                helper.printError("Invalid option.");
        }
    }

    private void editUser(User user) {
        helper.printTitle("EDIT USER: " + user.getUsername());
        helper.printInfo("Press Enter to keep current value.");

        String newUsername = helper.readString("Username [" + user.getUsername() + "]");
        String newFirstName = helper.readString("First Name [" + user.getFirstName() + "]");
        String newLastName = helper.readString("Last Name [" + user.getLastName() + "]");
        String newPassword = helper.readString("New Password (Leave empty to keep current)");

        System.out.println("Current Role: " + user.getRole());
        System.out.println("Select New Role (Enter to keep current):");
        System.out.println("1. Tester");
        System.out.println("2. Junior Developer");
        System.out.println("3. Senior Developer");
        System.out.println("4. Manager");

        String roleInput = helper.readString("Role Choice");
        Role newRole = user.getRole(); // Default to current

        if (!roleInput.isEmpty()) {
            try {
                int roleChoice = Integer.parseInt(roleInput);
                switch (roleChoice) {
                    case 1:
                        newRole = Role.TESTER;
                        break;
                    case 2:
                        newRole = Role.JUNIOR_DEVELOPER;
                        break;
                    case 3:
                        newRole = Role.SENIOR_DEVELOPER;
                        break;
                    case 4:
                        newRole = Role.MANAGER;
                        break;
                    default:
                        helper.printError("Invalid role selection. Keeping current role.");
                }
            } catch (NumberFormatException e) {
                helper.printError("Invalid input. Keeping current role.");
            }
        }

        // Prepare updated values
        String finalUsername = newUsername.isEmpty() ? user.getUsername() : newUsername;
        String finalFirstName = newFirstName.isEmpty() ? user.getFirstName() : newFirstName;
        String finalLastName = newLastName.isEmpty() ? user.getLastName() : newLastName;
        String finalPasswordHash = newPassword.isEmpty() ? user.getPasswordHash()
                : util.PasswordUtil.hashPassword(newPassword);

        // Create new user object based on (potentially new) role
        User updatedUser = null;
        switch (newRole) {
            case TESTER:
                updatedUser = new model.Tester(user.getId(), finalUsername, finalFirstName, finalLastName,
                        finalPasswordHash);
                break;
            case JUNIOR_DEVELOPER:
                updatedUser = new model.JuniorDeveloper(user.getId(), finalUsername, finalFirstName, finalLastName,
                        finalPasswordHash);
                break;
            case SENIOR_DEVELOPER:
                updatedUser = new model.SeniorDeveloper(user.getId(), finalUsername, finalFirstName, finalLastName,
                        finalPasswordHash);
                break;
            case MANAGER:
                updatedUser = new model.Manager(user.getId(), finalUsername, finalFirstName, finalLastName,
                        finalPasswordHash);
                break;
        }

        if (authService.updateUser(currentUser, updatedUser)) {
            helper.printSuccess("User updated successfully.");
        } else {
            helper.printError("Failed to update user.");
        }
    }

    private void deleteUser(User user) {
        if (user.getId() == currentUser.getId()) {
            helper.printError("You cannot delete yourself.");
            return;
        }

        String confirm = helper.readString("Are you sure you want to delete user " + user.getUsername() + "? (y/n)");
        if (confirm.equalsIgnoreCase("y")) {
            if (authService.deleteUser(currentUser, user.getId())) {
                helper.printSuccess("User deleted successfully.");
            } else {
                helper.printError("Failed to delete user.");
            }
        }
    }
}
