package command;

import dao.ActivityLogDAO;
import dao.ContactDAO;
import model.Contact;

/**
 * Command for deleting a contact from the database.
 * 
 * <p>Implements the Command pattern with backup functionality. Before deletion,
 * the contact is backed up to enable restoration on undo. The deletion is logged.
 * Undo restores the contact with a new ID (due to auto-increment in the database).</p>
 * 
 * @author Group 10
 * @version 1.0
 */
public class DeleteContactCommand implements Command {
    private ContactDAO contactDAO;
    private ActivityLogDAO activityLogDAO;
    private int contactId;
    private Contact deletedContactBackup;
    private int userId;

    /**
     * Constructs a DeleteContactCommand.
     * 
     * @param contactDAO The DAO for contact database operations.
     * @param activityLogDAO The DAO for logging activities.
     * @param contactId The ID of the contact to delete.
     * @param userId The ID of the user performing this action (for logging).
     */
    public DeleteContactCommand(ContactDAO contactDAO, ActivityLogDAO activityLogDAO, int contactId, int userId) {
        this.contactDAO = contactDAO;
        this.activityLogDAO = activityLogDAO;
        this.contactId = contactId;
        this.userId = userId;
    }

    /**
     * Executes the delete contact command.
     * 
     * <p>Backs up the contact data before deletion, deletes it from the database,
     * and logs the deletion action.</p>
     */
    @Override
    public void execute() {
        // Backup before delete
        this.deletedContactBackup = contactDAO.getContactById(contactId);

        if (deletedContactBackup != null) {
            contactDAO.deleteContact(contactId);
            activityLogDAO.logAction(userId, "DELETE", "Deleted contact ID: " + contactId);
        }
    }

    /**
     * Undoes the delete contact command by re-adding the contact.
     * 
     * <p>Restores the contact from backup. Note: The restored contact will receive
     * a new auto-generated ID due to database auto-increment behavior.</p>
     */
    @Override
    public void undo() {
        if (deletedContactBackup != null) {
            // Re-add the contact. Note: This might generate a NEW ID depending on DB
            // auto-increment.
            // If we want to preserve the exact ID, we might need a custom query in DAO to
            // insert with ID,
            // or just accept it gets a new ID. For simplicity, we'll use addContact which
            // generates new ID.
            // Ideally, we should restore with same ID if possible, but standard INSERT
            // usually ignores ID if auto-inc.
            // Let's try to just add it back.

            // To restore strictly, we might need a method in DAO: restoreContact(Contact c)
            // that forces ID.
            // For now, let's just add it back. The user will see the contact return.
            contactDAO.addContact(deletedContactBackup);
        }
    }
}
