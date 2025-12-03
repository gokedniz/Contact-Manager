package command;

import dao.ActivityLogDAO;
import dao.ContactDAO;
import model.Contact;

public class AddContactCommand implements Command {
    private ContactDAO contactDAO;
    private ActivityLogDAO activityLogDAO;
    private Contact contact;
    private int userId; // The user performing the action
    private int generatedContactId;

    public AddContactCommand(ContactDAO contactDAO, ActivityLogDAO activityLogDAO, Contact contact, int userId) {
        this.contactDAO = contactDAO;
        this.activityLogDAO = activityLogDAO;
        this.contact = contact;
        this.userId = userId;
    }

    @Override
    public void execute() {
        generatedContactId = contactDAO.addContact(contact);
        if (generatedContactId != -1) {
            contact.setId(generatedContactId); // Update contact object with new ID
            activityLogDAO.logAction(userId, "ADD",
                    "Added contact: " + contact.getFirstName() + " " + contact.getLastName());
        }
    }

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
