package util;

import dao.ContactDAO;
import model.Contact;
import java.util.List;

/**
 * Utility class for migrating gender values in the database.
 * 
 * <p>
 * Updates all existing contacts to use standard international gender codes:
 * 'M' (Male) instead of 'E' (Erkek) and 'F' (Female) instead of 'K' (Kadın).
 * </p>
 */
public class GenderMigration {

    public static void main(String[] args) {
        System.out.println("Starting Gender Migration...");
        ContactDAO dao = new ContactDAO();
        List<Contact> contacts = dao.getAllContacts();

        int count = 0;
        int updated = 0;

        for (Contact c : contacts) {
            String gender = c.getGender();
            String newGender = gender;

            if (gender != null) {
                if (gender.equalsIgnoreCase("E")) {
                    newGender = "M";
                } else if (gender.equalsIgnoreCase("K")) {
                    newGender = "F";
                }
            }

            // Only update if changed
            if (newGender != null && !newGender.equals(gender)) {
                c.setGender(newGender);
                dao.updateContact(c);
                System.out.println("Updated ID " + c.getId() + ": " + gender + " -> " + newGender);
                updated++;
            }
            count++;
        }

        System.out.println("Migration Completed.");
        System.out.println("Total Contacts Processed: " + count);
        System.out.println("Total Genders Updated: " + updated);
    }
}
