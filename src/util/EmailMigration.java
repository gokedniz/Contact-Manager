package util;

import dao.ContactDAO;
import model.Contact;
import java.util.List;

public class EmailMigration {
    public static void main(String[] args) {
        System.out.println("Starting Email Migration...");
        ContactDAO dao = new ContactDAO();
        List<Contact> contacts = dao.getAllContacts();

        int count = 0;
        int updated = 0;

        for (Contact c : contacts) {
            String oldEmail = c.getEmail();
            String localPart;

            if (oldEmail != null && oldEmail.contains("@")) {
                localPart = oldEmail.substring(0, oldEmail.indexOf("@"));
            } else {
                // Generate from name if no email or invalid
                localPart = c.getFirstName().toLowerCase().replaceAll("\\s+", "") + "." +
                        c.getLastName().toLowerCase().replaceAll("\\s+", "");
            }

            String newDomain;
            if (count < 15) {
                newDomain = "gmail.com";
            } else if (count < 25) { // 15 + 10
                newDomain = "outlook.com";
            } else if (count < 30) { // 15 + 10 + 5
                newDomain = "yandex.com";
            } else {
                newDomain = "hotmail.com";
            }

            String newEmail = localPart + "@" + newDomain;

            // Only update if changed
            if (!newEmail.equals(oldEmail)) {
                c.setEmail(newEmail);
                dao.updateContact(c);
                System.out.println("Updated ID " + c.getId() + ": " + newEmail);
                updated++;
            }
            count++;
        }

        System.out.println("Migration Completed.");
        System.out.println("Total Contacts Processed: " + count);
        System.out.println("Total Emails Updated: " + updated);
    }
}
