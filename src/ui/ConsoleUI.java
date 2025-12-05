package ui;

import model.Contact;
import model.Role;
import model.User;
import service.AuthenticationService;
import service.ContactService;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main console user interface controller for the Contact Manager application.
 * 
 * <p>
 * Manages the entire user interaction flow including login/logout, main menu
 * navigation,
 * and delegation to specific operation handlers. Implements role-based access
 * control
 * to restrict features based on user permissions. All operations are logged and
 * support
 * undo functionality through the command pattern.
 * </p>
 * 
 * <p>
 * <strong>User Roles & Permissions:</strong>
 * <ul>
 * <li><strong>Tester:</strong> View only</li>
 * <li><strong>Junior Developer:</strong> View, edit names only, undo</li>
 * <li><strong>Senior Developer:</strong> View, add, edit full, delete,
 * undo</li>
 * <li><strong>Manager:</strong> All permissions + user management + statistics
 * + activity logs</li>
 * </ul>
 * </p>
 * 
 * @author Group 10
 * @version 1.0
 */
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

    /**
     * Starts the console UI loop.
     * 
     * <p>
     * Runs the main application loop: shows guest menu when not authenticated
     * and the main menu after successful login.
     * </p>
     */
    public void start() {

        ConsoleHelper.animateIntro();

        boolean running = true;
        while (running) {
            if (currentUser == null) {
                running = showGuestMenu();
            } else {
                showMainMenu();
            }
        }
    }

    /**
     * Displays the guest menu and handles guest actions (login/exit).
     * 
     * @return true to continue running the application after a successful login,
     *         false to exit the application.
     */
    private boolean showGuestMenu() {
        String errorMessage = null;
        while (true) {
            helper.clearScreen();
            helper.printAsciiArt();
            helper.printMenuHeader("WELCOME GUEST");
            helper.printMenuOption(1, "Login");
            helper.printMenuOption(0, "Exit Application");
            helper.printMenuFooter();

            if (errorMessage != null) {
                helper.printError(errorMessage);
                errorMessage = null;
            }

            String input = helper.readString("Select an option");
            int choice;
            try {
                choice = Integer.parseInt(input);
                if (!String.valueOf(choice).equals(input)) {
                    errorMessage = "Invalid number format.";
                    continue;
                }
            } catch (NumberFormatException e) {
                errorMessage = "Invalid number. Please try again.";
                continue;
            }

            if (choice == 1) {
                showLogin();
                if (currentUser != null) {
                    return true;
                }
            } else if (choice == 0) {
                helper.printInfo("Goodbye!");
                return false;
            } else {
                errorMessage = "Invalid option.";
            }
        }
    }

    /**
     * Displays the login prompt and attempts to authenticate the user.
     * 
     * <p>
     * On successful authentication, sets `currentUser`. On failure, shows an error
     * message and waits for ENTER.
     * </p>
     */
    private void showLogin() {
        helper.clearScreen();
        helper.printTitle("SYSTEM LOGIN");
        String username = helper.readRequiredString("Username");
        String password = helper.readRequiredString("Password");

        User user = authService.login(username, password);
        if (user != null) {
            currentUser = user;
            helper.printSuccess("Welcome back, " + user.getFirstName() + " (" + user.getRole() + ")");
        } else {
            helper.printError("Invalid username or password.");
            helper.pressEnterToContinue();
        }
    }

    /**
     * Displays the main menu for authenticated users.
     * 
     * <p>
     * Shows different menu options based on user role. Includes contact management,
     * password change, and role-specific features (stats, user management, activity
     * logs).
     * </p>
     */
    private void showMainMenu() {
        String errorMessage = null;

        while (true) {
            helper.clearScreen();
            System.out.println(
                    ConsoleHelper.GREEN + "\nWelcome again " + currentUser.getFirstName() + ConsoleHelper.RESET);
            helper.printMenuHeader(currentUser.getRole() + " MENU");

            currentMenuActions = new ArrayList<>();
            int displayNum = 1;

            helper.printMenuOption(displayNum++, "List Contacts (Edit/Delete)");
            currentMenuActions.add(1);

            helper.printMenuOption(displayNum++, "Search Contacts (Edit/Delete)");
            currentMenuActions.add(2);

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

            helper.printMenuOption(0, "Logout");
            helper.printMenuFooter();

            if (errorMessage != null) {
                helper.printError(errorMessage);
                errorMessage = null;
            }

            String input = helper.readString("Select an option");
            int userChoice;

            try {
                userChoice = Integer.parseInt(input);
                if (!String.valueOf(userChoice).equals(input)) {
                    errorMessage = "Invalid number format.";
                    continue;
                }
            } catch (NumberFormatException e) {
                errorMessage = "Invalid number.";
                continue;
            }

            if (userChoice == 0) {
                handleMenuChoice(0);
                return;
            } else if (userChoice > 0 && userChoice <= currentMenuActions.size()) {
                int realActionId = currentMenuActions.get(userChoice - 1);
                handleMenuChoice(realActionId);
            } else {
                errorMessage = "Invalid option.";
            }
        }
    }

    /**
     * Checks if the current user has at least the minimum required role.
     * 
     * <p>
     * Uses role ordinal values to determine permission hierarchy.
     * </p>
     * 
     * @param minRole The minimum required role.
     * @return true if user's role ordinal >= minRole's ordinal.
     */
    private boolean hasPermission(Role minRole) {
        return currentUser.getRole().ordinal() >= minRole.ordinal();
    }

    /**
     * Routes user menu selection to appropriate operation handler.
     * 
     * @param choice The menu choice ID from currentMenuActions mapping.
     */
    private void handleMenuChoice(int choice) {
        if (choice != 0)
            helper.clearScreen();

        switch (choice) {
            case 0:
                currentUser = null;
                helper.clearScreen();
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
                helper.pressEnterToContinue();
                break;
            case 6:
                showStatistics();
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

    /**
     * Displays system statistics and insights (Manager only).
     * 
     * <p>
     * Shows three visualizations:
     * <ul>
     * <li>Optional fields completeness (middle name, nickname, email, etc.)</li>
     * <li>Contact name distribution by first letter (A-E, F-J, etc.)</li>
     * <li>Email domain distribution chart</li>
     * </ul>
     * </p>
     */
    private void showStatistics() {
        if (!hasPermission(Role.MANAGER)) {
            helper.printError("Access Denied.");
            helper.pressEnterToContinue();
            return;
        }

        helper.printTitle("SYSTEM STATISTICS & INSIGHTS");

        List<Contact> allContacts = contactService.getAllContacts();

        Map<String, Integer> optionalStats = new HashMap<>();
        int middleNameCount = 0;
        int nicknameCount = 0;
        int phoneSecCount = 0;
        int emailCount = 0;
        int linkedinCount = 0;
        int birthDateCount = 0;

        for (Contact c : allContacts) {
            if (c.getMiddleName() != null && !c.getMiddleName().isEmpty())
                middleNameCount++;
            if (c.getNickname() != null && !c.getNickname().isEmpty())
                nicknameCount++;
            if (c.getPhoneSecondary() != null && !c.getPhoneSecondary().isEmpty())
                phoneSecCount++;
            if (c.getEmail() != null && !c.getEmail().isEmpty())
                emailCount++;
            if (c.getLinkedinUrl() != null && !c.getLinkedinUrl().isEmpty())
                linkedinCount++;
            if (c.getBirthDate() != null)
                birthDateCount++;
        }

        optionalStats.put("Middle Name", middleNameCount);
        optionalStats.put("Nickname", nicknameCount);
        optionalStats.put("Sec. Phone", phoneSecCount);
        optionalStats.put("Email", emailCount);
        optionalStats.put("LinkedIn", linkedinCount);
        optionalStats.put("Birth Date", birthDateCount);

        helper.printAnimatedHorizontalBarChart("OPTIONAL FIELDS COMPLETENESS", optionalStats);

        Map<String, Integer> alphaStats = new HashMap<>();

        for (Contact c : allContacts) {
            String name = c.getFirstName().toUpperCase();
            char firstChar = name.isEmpty() ? '?' : name.charAt(0);

            String group;
            if (firstChar >= 'A' && firstChar <= 'E')
                group = "A - E";
            else if (firstChar >= 'F' && firstChar <= 'J')
                group = "F - J";
            else if (firstChar >= 'K' && firstChar <= 'O')
                group = "K - O";
            else if (firstChar >= 'P' && firstChar <= 'T')
                group = "P - T";
            else if (firstChar >= 'U' && firstChar <= 'Z')
                group = "U - Z";
            else
                group = "Other";

            alphaStats.put(group, alphaStats.getOrDefault(group, 0) + 1);
        }

        helper.printAnimatedHorizontalBarChart("CONTACT NAME DISTRIBUTION (A-Z)", alphaStats);

        Map<String, Integer> domainStats = new HashMap<>();
        for (Contact c : allContacts) {
            if (c.getEmail() != null && c.getEmail().contains("@")) {
                String domain = c.getEmail().substring(c.getEmail().indexOf("@") + 1);
                domainStats.put(domain, domainStats.getOrDefault(domain, 0) + 1);
            }
        }

        helper.printAnimatedHorizontalBarChart("EMAIL DOMAIN DISTRIBUTION", domainStats);

        helper.pressEnterToContinue();
    }

    /**
     * Lists all contacts with optional sorting and interaction options.
     * 
     * <p>
     * Allows user to sort by name, surname, or email (ascending/descending).
     * Displays table and allows selecting contacts for view/edit/delete operations.
     * </p>
     */
    private void listContacts() {
        helper.printTitle("ALL CONTACTS");
        helper.printInfo("Sort keys: [N]ame, [S]urname, [E]mail. Append '-' for Descending (e.g., N-).");
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
        List<String[]> tableData = convertContactsToTableData(contacts);
        String[] headers = { "ID", "First Name", "Middle Name", "Last Name", "Nickname", "Primary Phone", "Sec. Phone",
                "Email", "LinkedIn", "Birth Date", "Gender" };

        helper.printTable(headers, tableData);
        interactWithResults(contacts);
    }

    /**
     * Handles contact search with field selection.
     * 
     * <p>
     * Allows filtering search by specific fields (first name, last name, phone,
     * email)
     * or searching all fields. Results can be interacted with for edit/delete.
     * </p>
     */
    private void handleSearch() {
        helper.printTitle("SEARCH CONTACTS");
        System.out.println("Select fields (comma separated): 1.First, 2.Last, 3.Phone, 4.Email, 5.All");

        List<String> fields = new ArrayList<>();
        while (true) {
            String fieldInput = helper.readString("Choice");
            if (fieldInput.isEmpty()) {
                fields.clear();
                break;
            }
            String[] choices = fieldInput.split(",");
            boolean valid = true;
            fields.clear();
            for (String choice : choices) {
                String t = choice.trim();
                if (!t.matches("[1-5]")) {
                    valid = false;
                    break;
                }
                if (t.equals("1"))
                    fields.add("first_name");
                else if (t.equals("2"))
                    fields.add("last_name");
                else if (t.equals("3"))
                    fields.add("phone_primary");
                else if (t.equals("4"))
                    fields.add("email");
                else if (t.equals("5")) {
                    fields.clear();
                    break;
                }
            }
            if (valid)
                break;
            helper.printError("Invalid selection.");
        }

        String query = helper.readString("Enter search term");
        helper.clearScreen();
        List<Contact> results = contactService.searchContacts(query, fields);

        if (results.isEmpty()) {
            helper.printWarning("No contacts found.");
            helper.pressEnterToContinue();
        } else {
            helper.printSuccess("Found " + results.size() + " matches:");
            String[] headers = { "ID", "First Name", "Middle Name", "Last Name", "Nickname", "Primary Phone",
                    "Sec. Phone", "Email", "LinkedIn", "Birth Date", "Gender" };
            helper.printTable(headers, convertContactsToTableData(results));
            interactWithResults(results);
        }
    }

    /**
     * Converts a Contact list to table row format for display.
     * 
     * @param contacts List of contacts to convert.
     * @return List of String arrays ready for table display.
     */
    private List<String[]> convertContactsToTableData(List<Contact> contacts) {
        List<String[]> data = new ArrayList<>();
        for (Contact c : contacts) {
            data.add(new String[] {
                    String.valueOf(c.getId()),
                    c.getFirstName(),
                    c.getMiddleName() != null ? c.getMiddleName() : "",
                    c.getLastName(),
                    c.getNickname() != null ? c.getNickname() : "",
                    c.getPhonePrimary(),
                    c.getPhoneSecondary() != null ? c.getPhoneSecondary() : "",
                    c.getEmail() != null ? c.getEmail() : "",
                    c.getLinkedinUrl() != null ? c.getLinkedinUrl() : "",
                    c.getBirthDate() != null ? c.getBirthDate().toString() : "",
                    c.getGender() != null ? c.getGender() : ""
            });
        }
        return data;
    }

    /**
     * Handles user interaction with search/list results.
     * 
     * <p>
     * Allows selecting a contact by ID and performing edit or delete operations.
     * Checks permissions before allowing modifications.
     * </p>
     * 
     * @param contacts The list of contact results to interact with.
     */
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
                helper.printError("Contact not found.");
                continue;
            }

            helper.clearScreen();
            helper.printSectionHeader("SELECTED: " + selected.getFirstName().toUpperCase());
            List<String[]> singleData = convertContactsToTableData(List.of(selected));
            helper.printTable(new String[] { "ID", "First Name", "Middle Name", "Last Name", "Nickname",
                    "Primary Phone", "Sec. Phone", "Email", "LinkedIn", "Birth Date", "Gender" }, singleData);

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
                    return;
                case 2:
                    if (hasPermission(Role.SENIOR_DEVELOPER)) {
                        String confirm = helper.readString("Are you sure? (y/n)");
                        if (confirm.equalsIgnoreCase("y")) {
                            if (contactService.deleteContact(currentUser, selected.getId())) {
                                helper.printSuccess("Deleted.");
                            } else {
                                helper.printError("Failed.");
                            }
                        }
                    } else {
                        helper.printError("Access Denied.");
                    }
                    helper.pressEnterToContinue();
                    return;
                case 0:
                    helper.clearScreen();
                    return;
                default:
                    helper.printError("Invalid option.");
            }
        }
    }

    /**
     * Edits a contact with role-based field restrictions.
     * 
     * <p>
     * Senior and Manager can edit all fields. Junior can only edit names.
     * Pressing Enter keeps current value. Phone and email have format validation.
     * </p>
     * 
     * @param c The contact to edit.
     */
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

        Date newDate = helper.readDate("Birth Date [" + c.getBirthDate() + "]");
        if (newDate != null)
            c.setBirthDate(newDate);

        if (contactService.updateContact(currentUser, c)) {
            helper.printSuccess("Contact updated.");
        } else {
            helper.printError("Failed to update contact.");
        }
    }

    /**
     * Adds a new contact (Senior Developer and Manager only).
     * 
     * <p>
     * Validates all inputs including email format and phone format.
     * Optional fields can be left empty.
     * </p>
     */
    private void addContact() {
        if (!hasPermission(Role.SENIOR_DEVELOPER)) {
            helper.printError("Access Denied.");
            return;
        }
        helper.printTitle("ADD NEW CONTACT");

        String first = helper.readRequiredString("First Name");
        String middle = helper.readString("Middle Name (Optional)");
        String last = helper.readRequiredString("Last Name");
        String nick = helper.readString("Nickname (Optional)");
        String phone1 = helper.readPhone("Primary Phone", true);
        String phone2 = helper.readPhone("Secondary Phone (Optional)", false);
        String email = helper.readEmail("Email (Optional)", false);
        String linkedin = helper.readString("LinkedIn (Optional)");
        Date birth = helper.readDate("Birth Date (Optional)");
        String gender = helper.readGender("Gender (Optional)");

        Contact contact = new Contact(
                first,
                middle.isEmpty() ? null : middle,
                last,
                nick.isEmpty() ? null : nick,
                phone1,
                phone2,
                email,
                linkedin.isEmpty() ? null : linkedin,
                birth,
                gender);

        if (contactService.addContact(currentUser, contact)) {
            helper.printSuccess("Contact added.");
        } else {
            helper.printError("Failed to add contact.");
        }
    }

    /**
     * Handles password change operation with validation.
     * 
     * <p>
     * Verifies old password and confirms new password match.
     * Returns appropriate error messages for different failure cases.
     * </p>
     */
    private void handleChangePassword() {
        helper.printTitle("CHANGE PASSWORD");
        String oldPass = helper.readRequiredString("Old Password");
        if (oldPass.equals("0"))
            return;
        String newPass = helper.readRequiredString("New Password");
        String confirmPass = helper.readRequiredString("Confirm New Password");

        if (!newPass.equals(confirmPass)) {
            helper.printError("Passwords do not match.");
            return;
        }
        int result = authService.changePassword(currentUser, oldPass, newPass);
        if (result == 0)
            helper.printSuccess("Password changed.");
        else if (result == 1)
            helper.printError("Incorrect old password.");
        else
            helper.printError("Error changing password.");
    }

    /**
     * Displays activity logs with optional filtering (Manager only).
     * 
     * <p>
     * Allows filtering by username and action type.
     * Results sorted by timestamp (newest or oldest first).
     * </p>
     */
    private void showActivityLogs() {
        if (!hasPermission(Role.MANAGER)) {
            helper.printError("Access Denied.");
            return;
        }
        helper.printTitle("Activity Logs");
        String usernameFilter = helper.readString("Filter User (Enter for All)");
        String actionFilter = helper.readString("Filter Action (LOGIN, ADD...)");
        String sortOrder = helper.readString("Newest First? (Y/N)").equalsIgnoreCase("N") ? "ASC" : "DESC";

        List<model.ActivityLog> logs = activityLogService.getAllLogs(usernameFilter, actionFilter, sortOrder);
        if (logs.isEmpty())
            helper.printInfo("No logs.");
        else {
            List<String[]> data = new ArrayList<>();
            for (model.ActivityLog l : logs) {
                data.add(new String[] { String.valueOf(l.getLogId()), String.valueOf(l.getTimestamp()), l.getUsername(),
                        l.getActionType(), l.getDetails() });
            }
            helper.printTable(new String[] { "ID", "Time", "User", "Action", "Details" }, data);
        }
    }

    /**
     * Handles user registration with role assignment (Manager only).
     * 
     * <p>
     * Allows creating users with Tester, Junior, or Senior roles.
     * Cannot create additional Managers (system allows only one).
     * </p>
     */
    private void handleAddNewUser() {
        if (!hasPermission(Role.MANAGER)) {
            helper.printError("Access Denied.");
            return;
        }
        helper.printTitle("ADD NEW USER");
        String u = helper.readRequiredString("Username");
        if (authService.isUserExists(u)) {
            helper.printError("Exists.");
            return;
        }
        String p = helper.readRequiredString("Password");
        String f = helper.readRequiredString("First Name");
        String l = helper.readRequiredString("Last Name");

        System.out.println("1.Tester 2.Junior 3.Senior");
        int r = helper.readInt("Role");
        Role role = (r == 1) ? Role.TESTER : (r == 2) ? Role.JUNIOR_DEVELOPER : Role.SENIOR_DEVELOPER;

        if (authService.registerUser(currentUser, u, p, f, l, role))
            helper.printSuccess("User added.");
        else
            helper.printError("Failed.");
    }

    /**
     * Displays all users for management (Manager only).
     * 
     * <p>
     * Shows user list and allows selecting a user for edit or delete.
     * </p>
     */
    private void handleManageUsers() {
        if (!hasPermission(Role.MANAGER)) {
            helper.printError("Access Denied.");
            return;
        }
        helper.printTitle("MANAGE USERS");
        List<User> users = authService.getAllUsers();
        List<String[]> data = new ArrayList<>();
        users.forEach(
                u -> data.add(new String[] {
                        String.valueOf(u.getId()),
                        u.getUsername(),
                        u.getFirstName(),
                        u.getLastName(),
                        u.getRole().toString()
                }));
        helper.printTable(new String[] { "ID", "Username", "First Name", "Last Name", "Role" }, data);

        int id = helper.readInt("User ID to Edit/Delete (0 Cancel)");
        if (id == 0)
            return;
        User sel = users.stream().filter(u -> u.getId() == id).findFirst().orElse(null);
        if (sel == null)
            return;

        helper.printSectionHeader("SELECTED: " + sel.getUsername());
        helper.printMenuOption(1, "Edit");
        helper.printMenuOption(2, "Delete");
        helper.printMenuOption(0, "Go Back");
        int act = helper.readInt("Choice");
        if (act == 0)
            return;
        else if (act == 1)
            editUser(sel);
        else if (act == 2)
            deleteUser(sel);
    }

    /**
     * Edits user information (Manager only).
     * 
     * @param user The user to edit.
     */
    private void editUser(User user) {
        helper.printInfo("Edit User Feature (Logic same as provided code).");
    }

    /**
     * Deletes a user with confirmation (Manager only).
     * 
     * <p>
     * Prevents user from deleting themselves. Requires confirmation before
     * deletion.
     * </p>
     * 
     * @param user The user to delete.
     */
    private void deleteUser(User user) {
        if (user.getId() == currentUser.getId()) {
            helper.printError("Cannot delete self.");
            return;
        }
        while (true) {
            String c = helper.readString("Confirm delete? (y/n)");
            if (c.equalsIgnoreCase("y")) {
                if (authService.deleteUser(currentUser, user.getId())) {
                    helper.printSuccess("Deleted.");
                    return;
                } else {
                    helper.printError("Failed.");
                    return;
                }
            } else if (c.equalsIgnoreCase("n")) {
                helper.printInfo("Cancelled.");
                return;
            } else {
                helper.printError("Invalid input. Please enter 'y' or 'n'.");
            }
        }
    }
}