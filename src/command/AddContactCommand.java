package command;

import dao.ActivityLogDAO;
import dao.ContactDAO;
import model.Contact;

/**
 * Command for adding a new contact to the database.
 * 
 * <p>Implements the Command pattern to support undo functionality. When executed,
 * adds a contact to the database, updates the contact object with the generated ID,
 * and logs the action. When undone, deletes the contact by its ID.</p>
 * 
 * @author Group 10
 * @version 1.0
 */
public class AddContactCommand implements Command {
    private ContactDAO contactDAO;
    private ActivityLogDAO activityLogDAO;
    private Contact contact;
    private int userId;
    private int generatedContactId;

    /**
     * Constructs an AddContactCommand.
     * 
     * @param contactDAO The DAO for contact database operations.
     * @param activityLogDAO The DAO for logging activities.
     * @param contact The contact object to add.
     * @param userId The ID of the user performing this action (for logging).
     */
    public AddContactCommand(ContactDAO contactDAO, ActivityLogDAO activityLogDAO, Contact contact, int userId) {
        this.contactDAO = contactDAO;
        this.activityLogDAO = activityLogDAO;
        this.contact = contact;
        this.userId = userId;
    }

    /**
     * Executes the add contact command.
     * 
     * <p>Inserts the contact into the database, updates the contact object with
     * the generated ID, and logs the action.</p>
     */
    @Override
    public void execute() {
        generatedContactId = contactDAO.addContact(contact);
        if (generatedContactId != -1) {
            contact.setId(generatedContactId);
            activityLogDAO.logAction(userId, "ADD",
                    "Added contact: " + contact.getFirstName() + " " + contact.getLastName());
        }
    }

    /**
     * Undoes the add contact command by deleting the contact.
     * 
     * <p>Deletes the contact if it was successfully added (ID != -1).</p>
     */
    @Override
    public void undo() {
        if (generatedContactId != -1) {
            contactDAO.deleteContact(generatedContactId);
            // Optionally log the undo action, but usually undo is silent or logged as undo
            // activityLogDAO.logAction(userId, "UNDO_ADD", "Undid add contact: " +
            // generatedContactId);
        }
    }
}
