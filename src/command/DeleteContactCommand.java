package command;

import dao.ActivityLogDAO;
import dao.ContactDAO;
import model.Contact;

public class DeleteContactCommand implements Command {
    private ContactDAO contactDAO;
    private ActivityLogDAO activityLogDAO;
    private int contactId;
    private Contact deletedContactBackup;
    private int userId;

    public DeleteContactCommand(ContactDAO contactDAO, ActivityLogDAO activityLogDAO, int contactId, int userId) {
        this.contactDAO = contactDAO;
        this.activityLogDAO = activityLogDAO;
        this.contactId = contactId;
        this.userId = userId;
    }

    @Override
    public void execute() {
        // Backup before delete
        this.deletedContactBackup = contactDAO.getContactById(contactId);

        if (deletedContactBackup != null) {
            contactDAO.deleteContact(contactId);
            activityLogDAO.logAction(userId, "DELETE", "Deleted contact ID: " + contactId);
        }
    }

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
