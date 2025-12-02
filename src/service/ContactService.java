package service;

import dao.ContactDAO;
import model.Contact;
import model.Role;
import model.User;

import java.util.List;

public class ContactService {

    private ContactDAO contactDAO;

    public ContactService() {
        this.contactDAO = new ContactDAO();
    }

    public List<Contact> getAllContacts() {
        // Everyone can list contacts
        return contactDAO.getAllContacts();
    }

    public List<Contact> searchContacts(String query) {
        return contactDAO.searchContacts(query);
    }

    public List<Contact> getContactsSorted(String sortBy) {
        List<Contact> contacts = contactDAO.getAllContacts();
        switch (sortBy.toLowerCase()) {
            case "name_asc":
                contacts.sort((c1, c2) -> c1.getFirstName().compareToIgnoreCase(c2.getFirstName()));
                break;
            case "name_desc":
                contacts.sort((c1, c2) -> c2.getFirstName().compareToIgnoreCase(c1.getFirstName()));
                break;
            case "surname_asc":
                contacts.sort((c1, c2) -> c1.getLastName().compareToIgnoreCase(c2.getLastName()));
                break;
            case "surname_desc":
                contacts.sort((c1, c2) -> c2.getLastName().compareToIgnoreCase(c1.getLastName()));
                break;
            case "email_asc":
                contacts.sort((c1, c2) -> {
                    String e1 = c1.getEmail() == null ? "" : c1.getEmail();
                    String e2 = c2.getEmail() == null ? "" : c2.getEmail();
                    return e1.compareToIgnoreCase(e2);
                });
                break;
            case "email_desc":
                contacts.sort((c1, c2) -> {
                    String e1 = c1.getEmail() == null ? "" : c1.getEmail();
                    String e2 = c2.getEmail() == null ? "" : c2.getEmail();
                    return e2.compareToIgnoreCase(e1);
                });
                break;
            default:
                // Default sort by ID
                contacts.sort((c1, c2) -> Integer.compare(c1.getId(), c2.getId()));
        }
        return contacts;
    }

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

    public Contact getContactById(int id) {
        return contactDAO.getContactById(id);
    }

    public boolean addContact(User user, Contact contact) {
        if (user.getRole() == Role.TESTER) {
            System.out.println("ACCESS DENIED: Testers cannot add contacts.");
            return false;
        }
        contactDAO.addContact(contact);
        return true;
    }

    public boolean updateContact(User user, Contact contact) {
        if (user.getRole() == Role.TESTER) {
            System.out.println("ACCESS DENIED: Testers cannot update contacts.");
            return false;
        }

        if (user.getRole() == Role.JUNIOR_DEVELOPER) {
            // Junior can only update names.
            // We need to fetch the existing contact to preserve other fields,
            // OR we assume the UI only sends updated name fields.
            // For safety, let's fetch original and only apply name changes.
            Contact original = contactDAO.getContactById(contact.getId());
            if (original != null) {
                original.setFirstName(contact.getFirstName());
                original.setMiddleName(contact.getMiddleName());
                original.setLastName(contact.getLastName());
                original.setNickname(contact.getNickname());
                // Do not update phone, email, etc.
                contactDAO.updateContact(original);
                System.out.println("Junior Developer update applied (Names only).");
                return true;
            }
            return false;
        }

        // Senior and Manager can update everything
        contactDAO.updateContact(contact);
        return true;
    }

    public boolean deleteContact(User user, int contactId) {
        if (user.getRole() == Role.TESTER || user.getRole() == Role.JUNIOR_DEVELOPER) {
            System.out.println("ACCESS DENIED: You do not have permission to delete contacts.");
            return false;
        }
        contactDAO.deleteContact(contactId);
        return true;
    }
}
