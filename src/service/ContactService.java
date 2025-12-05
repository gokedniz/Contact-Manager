package service;

import dao.ContactDAO;
import model.Contact;
import model.Role;
import model.User;
import java.text.Collator;
import java.util.Locale;

import java.util.List;

/**
 * Service layer for managing contact operations with role-based access control.
 * 
 * <p>This service provides business logic for contact management, including retrieval,
 * search, sorting, and CRUD operations with permission checks based on user roles.
 * All modification operations are logged and support undo functionality through
 * the command pattern. Sorting uses Turkish locale collation for proper Turkish character handling.</p>
 * 
 * <p><strong>Permission Model:</strong>
 * <ul>
 *   <li>View: All roles can view/search contacts</li>
 *   <li>Create: Senior Developer and Manager only</li>
 *   <li>Update: Senior Developer and Manager (full), Junior Developer (names only)</li>
 *   <li>Delete: Senior Developer and Manager only</li>
 * </ul>
 * </p>
 * 
 * @author Group 10
 * @version 1.0
 */
public class ContactService {

    private ContactDAO contactDAO;
    private dao.ActivityLogDAO activityLogDAO;
    private command.CommandManager commandManager;

    public ContactService() {
        this.contactDAO = new ContactDAO();
        this.activityLogDAO = new dao.ActivityLogDAO();
        this.commandManager = command.CommandManager.getInstance();
    }

    /**
     * Retrieves all contacts from the database.
     * 
     * <p>All users can access this method. Returns complete contact list without filtering.</p>
     * 
     * @return A list of all contacts. Returns an empty list if no contacts exist.
     * 
     * @see ContactDAO#getAllContacts()
     */
    public List<Contact> getAllContacts() {
        // Everyone can list contacts
        return contactDAO.getAllContacts();
    }

    /**
     * Searches for contacts using default search fields.
     * 
     * <p>Delegates to {@link #searchContacts(String, List)} with null fields parameter
     * to use default fields (first_name, last_name, phone_primary, email).</p>
     * 
     * @param query The search query string. Used for LIKE pattern matching.
     * @return A list of matching contacts, ordered by name.
     * 
     * @see ContactDAO#searchContacts(String)
     */
    public List<Contact> searchContacts(String query) {
        return contactDAO.searchContacts(query);
    }

    /**
     * Searches for contacts in specific fields.
     * 
     * @param query The search query string. Used for LIKE pattern matching.
     * @param fields List of field names to search in. If null, uses default fields.
     * @return A list of matching contacts.
     * 
     * @see ContactDAO#searchContacts(String, List)
     */
    public List<Contact> searchContacts(String query, List<String> fields) {
        return contactDAO.searchContacts(query, fields);
    }

    /**
     * Retrieves all contacts sorted by specified criteria using Turkish locale collation.
     * 
     * <p>Supports sorting by first name, last name, or email in ascending or descending order.
     * Uses Turkish (tr_TR) locale for proper handling of Turkish characters (ç, ğ, ı, ö, ş, ü).
     * Default sort is by contact ID ascending if sortBy parameter is unrecognized.</p>
     * 
     * @param sortBy Sorting criteria: "name_asc", "name_desc", "surname_asc", "surname_desc",
     *               "email_asc", "email_desc". Case-insensitive. Defaults to ID sort if unrecognized.
     * @return A list of all contacts sorted according to the specified criteria.
     * 
     * @see #getAllContacts()
     */
    public List<Contact> getContactsSorted(String sortBy) {
        List<Contact> contacts = contactDAO.getAllContacts();

        Collator trCollator = Collator.getInstance(new Locale("tr", "TR"));
        trCollator.setStrength(Collator.PRIMARY);

        switch (sortBy.toLowerCase()) {
            case "name_asc":
                // compareToIgnoreCase YERİNE trCollator.compare kullanıyoruz
                contacts.sort((c1, c2) -> trCollator.compare(c1.getFirstName(), c2.getFirstName()));
                break;
            case "name_desc":
                contacts.sort((c1, c2) -> trCollator.compare(c2.getFirstName(), c1.getFirstName()));
                break;
            case "surname_asc":
                contacts.sort((c1, c2) -> trCollator.compare(c1.getLastName(), c2.getLastName()));
                break;
            case "surname_desc":
                contacts.sort((c1, c2) -> trCollator.compare(c2.getLastName(), c1.getLastName()));
                break;
            case "email_asc":
                contacts.sort((c1, c2) -> {
                    String e1 = c1.getEmail() == null ? "" : c1.getEmail();
                    String e2 = c2.getEmail() == null ? "" : c2.getEmail();
                    return trCollator.compare(e1, e2);
                });
                break;
            case "email_desc":
                contacts.sort((c1, c2) -> {
                    String e1 = c1.getEmail() == null ? "" : c1.getEmail();
                    String e2 = c2.getEmail() == null ? "" : c2.getEmail();
                    return trCollator.compare(e2, e1);
                });
                break;
            default:
                // Default sort by ID
                contacts.sort((c1, c2) -> Integer.compare(c1.getId(), c2.getId()));
        }
        return contacts;
    }

    /**
     * Generates statistics about contacts in the database.
     * 
     * <p>Returns a map containing:
     * <ul>
     *   <li>"Total Contacts": Total number of contacts</li>
     *   <li>"Contacts with Email": Count of contacts with email addresses</li>
     *   <li>"Contacts with Phone": Count of contacts with phone numbers</li>
     *   <li>"Email Domains": Map of email domain frequencies</li>
     * </ul>
     * </p>
     * 
     * @return A Map containing statistics about contacts.
     */
    public java.util.Map<String, Object> getStatistics() {
        List<Contact> all = contactDAO.getAllContacts();
        java.util.Map<String, Object> stats = new java.util.HashMap<>();

        stats.put("Total Contacts", all.size());

        long withEmail = all.stream().filter(c -> c.getEmail() != null && !c.getEmail().isEmpty()).count();
        stats.put("Contacts with Email", withEmail);

        long withPhone = all.stream().filter(c -> c.getPhonePrimary() != null && !c.getPhonePrimary().isEmpty())
                .count();
        stats.put("Contacts with Phone", withPhone);

        // Domain Distribution
        java.util.Map<String, Integer> domains = new java.util.HashMap<>();
        for (Contact c : all) {
            if (c.getEmail() != null && c.getEmail().contains("@")) {
                String domain = c.getEmail().substring(c.getEmail().indexOf("@") + 1);
                domains.put(domain, domains.getOrDefault(domain, 0) + 1);
            }
        }
        stats.put("Email Domains", domains);

        return stats;
    }

    /**
     * Retrieves a contact by its ID.
     * 
     * @param id The contact ID.
     * @return The Contact object if found, or null if not found.
     * 
     * @see ContactDAO#getContactById(int)
     */
    public Contact getContactById(int id) {
        return contactDAO.getContactById(id);
    }

    /**
     * Adds a new contact (Senior Developer and Manager only).
     * 
     * <p>Testers cannot add contacts. Operation is logged and can be undone.
     * If user lacks permission, message is printed and false is returned.</p>
     * 
     * @param user The user performing the action. Used for permission and activity logging.
     * @param contact The contact to add.
     * @return true if add succeeds, false if denied or fails.
     */
    public boolean addContact(User user, Contact contact) {
        if (user.getRole() == Role.TESTER) {
            System.out.println("ACCESS DENIED: Testers cannot add contacts.");
            return false;
        }
        commandManager.executeCommand(new command.AddContactCommand(contactDAO, activityLogDAO, contact, user.getId()));
        return true;
    }

    /**
     * Updates an existing contact with role-based restrictions.
     * 
     * <p>Permission restrictions:
     * <ul>
     *   <li>Tester: Denied</li>
     *   <li>Junior Developer: Can only update names (first, middle, last, nickname)</li>
     *   <li>Senior Developer & Manager: Full update allowed</li>
     * </ul>
     * Operation is logged and can be undone.</p>
     * 
     * @param user The user performing the update.
     * @param contact The contact with updated information.
     * @return true if update succeeds, false if denied or fails.
     */
    public boolean updateContact(User user, Contact contact) {
        if (user.getRole() == Role.TESTER) {
            System.out.println("ACCESS DENIED: Testers cannot update contacts.");
            return false;
        }

        if (user.getRole() == Role.JUNIOR_DEVELOPER) {
            // Junior can only update names.
            Contact original = contactDAO.getContactById(contact.getId());
            if (original != null) {
                original.setFirstName(contact.getFirstName());
                original.setMiddleName(contact.getMiddleName());
                original.setLastName(contact.getLastName());
                original.setNickname(contact.getNickname());
                // Do not update phone, email, etc.

                commandManager.executeCommand(
                        new command.UpdateContactCommand(contactDAO, activityLogDAO, original, user.getId()));
                System.out.println("Junior Developer update applied (Names only).");
                return true;
            }
            return false;
        }

        // Senior and Manager can update everything
        commandManager
                .executeCommand(new command.UpdateContactCommand(contactDAO, activityLogDAO, contact, user.getId()));
        return true;
    }

    /**
     * Deletes a contact (Senior Developer and Manager only).
     * 
     * <p>Testers and Junior Developers cannot delete contacts. Operation is logged and can be undone.</p>
     * 
     * @param user The user performing the deletion.
     * @param contactId The ID of the contact to delete.
     * @return true if deletion succeeds, false if denied or fails.
     */
    public boolean deleteContact(User user, int contactId) {
        if (user.getRole() == Role.TESTER || user.getRole() == Role.JUNIOR_DEVELOPER) {
            System.out.println("ACCESS DENIED: You do not have permission to delete contacts.");
            return false;
        }
        commandManager
                .executeCommand(new command.DeleteContactCommand(contactDAO, activityLogDAO, contactId, user.getId()));
        return true;
    }

    /**
     * Undoes the last contact operation.
     * 
     * @return true if undo succeeds, false if no operations to undo.
     * 
     * @see command.CommandManager#undo()
     */
    public boolean undoLastAction() {
        return commandManager.undo();
    }
}
