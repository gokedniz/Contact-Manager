package command;

import dao.ActivityLogDAO;
import dao.ContactDAO;
import model.Contact;

/**
 * Command for adding a new contact to the database.
 * 
 * <p>Implements the Command pattern to add contacts with undo support.
 * When executed, adds the contact and logs the action. Undo deletes the added contact.</p>
 * 
 * @author Project2-Group10
 * @version 1.0
 * @since 1.0
 */
public class AddContactCommand implements Command {
    private ContactDAO contactDAO;
    private ActivityLogDAO activityLogDAO;
    private Contact contact;
    private int userId; // The user performing the action
    private int generatedContactId;

    /**
     * Constructs an AddContactCommand with required dependencies and data.
     * 
     * @param contactDAO The DAO for contact operations.
     * @param activityLogDAO The DAO for logging activities.
     * @param contact The contact to add.
     * @param userId The ID of the user performing the action.
     */
    public AddContactCommand(ContactDAO contactDAO, ActivityLogDAO activityLogDAO, Contact contact, int userId) {
        this.contactDAO = contactDAO;
        this.activityLogDAO = activityLogDAO;
        this.contact = contact;
        this.userId = userId;
    }

    /**
     * Executes the add contact operation and logs the action.
     * 
     * <p>Adds the contact to the database, stores the generated ID, and logs
     * the action to the activity log if successful.</p>
     */
    @Override
    public void execute() {
        generatedContactId = contactDAO.addContact(contact);
        if (generatedContactId != -1) {
            contact.setId(generatedContactId); // Update contact object with new ID
            activityLogDAO.logAction(userId, "ADD",
                    "Added contact: " + contact.getFirstName() + " " + contact.getLastName());
        }
    }

    /**
     * Undoes the add contact operation by deleting the added contact.
     * 
     * <p>Removes the contact from the database if it was successfully added.
     * The undo action is not logged to avoid cluttering the activity log.</p>
     */
    @Override
    public void undo() {
        if (generatedContactId != -1) {
            contactDAO.deleteContact(generatedContactId);
        }
    }
}
