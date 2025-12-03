package command;

import dao.ActivityLogDAO;
import dao.ContactDAO;
import model.Contact;

public class UpdateContactCommand implements Command {
    private ContactDAO contactDAO;
    private ActivityLogDAO activityLogDAO;
    private Contact newContactState;
    private Contact oldContactState;
    private int userId;

    public UpdateContactCommand(ContactDAO contactDAO, ActivityLogDAO activityLogDAO, Contact newContactState,
            int userId) {
        this.contactDAO = contactDAO;
        this.activityLogDAO = activityLogDAO;
        this.newContactState = newContactState;
        this.userId = userId;
    }

    @Override
    public void execute() {
        // Fetch the current state before updating to save it for undo
        this.oldContactState = contactDAO.getContactById(newContactState.getId());

        if (oldContactState != null) {
            contactDAO.updateContact(newContactState);
            activityLogDAO.logAction(userId, "UPDATE", "Updated contact ID: " + newContactState.getId());
        }
    }

    @Override
    public void undo() {
        if (oldContactState != null) {
            contactDAO.updateContact(oldContactState);
        }
    }
}
