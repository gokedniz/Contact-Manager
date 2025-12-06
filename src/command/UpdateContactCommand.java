package command;

import dao.ActivityLogDAO;
import dao.ContactDAO;
import model.Contact;

/**
 * Command for updating an existing contact's information.
 * 
 * <p>Implements the Command pattern with state backup. Saves the original contact
 * state before updating and logs the change. Undo reverts the contact to its
 * previous state.</p>
 * 
 * @author Group 10
 * @version 1.0
 */
public class UpdateContactCommand implements Command {
    private ContactDAO contactDAO;
    private ActivityLogDAO activityLogDAO;
    private Contact newContactState;
    private Contact oldContactState;
    private int userId;

    /**
     * Constructs an UpdateContactCommand.
     * 
     * @param contactDAO The DAO for contact database operations.
     * @param activityLogDAO The DAO for logging activities.
     * @param newContactState The contact with updated information.
     * @param userId The ID of the user performing this action (for logging).
     */
    public UpdateContactCommand(ContactDAO contactDAO, ActivityLogDAO activityLogDAO, Contact newContactState,
            int userId) {
        this.contactDAO = contactDAO;
        this.activityLogDAO = activityLogDAO;
        this.newContactState = newContactState;
        this.userId = userId;
    }

    /**
     * Executes the update contact command.
     * 
     * <p>Backs up the current contact state before updating, updates the contact,
     * and logs the modification.</p>
     */
    @Override
    public void execute() {
        // Fetch the current state before updating to save it for undo
        this.oldContactState = contactDAO.getContactById(newContactState.getId());

        if (oldContactState != null) {
            contactDAO.updateContact(newContactState);
            activityLogDAO.logAction(userId, "UPDATE", "Updated contact ID: " + newContactState.getId());
        }
    }

    /**
     * Undoes the update contact command by restoring the previous state.
     * 
     * <p>Reverts the contact to its state before the update.</p>
     */
    @Override
    public void undo() {
        if (oldContactState != null) {
            contactDAO.updateContact(oldContactState);
        }
    }
}
