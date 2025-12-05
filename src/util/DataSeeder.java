package util;

import dao.ContactDAO;
import model.Contact;
import java.util.Random;

public class DataSeeder {
    public static void main(String[] args) {
        System.out.println("Starting Data Seeding...");
        ContactDAO dao = new ContactDAO();
        Random random = new Random();

        String[] firstNames = { "Ahmet", "Mehmet", "Ayse", "Fatma", "Ali", "Veli", "Zeynep", "Mustafa", "Can", "Elif",
                "Burak", "Cem", "Deniz", "Ege", "Selin" };
        String[] lastNames = { "Yilmaz", "Kaya", "Demir", "Celik", "Sahin", "Yildiz", "Ozturk", "Aydin", "Ozdemir",
                "Arslan", "Dogan", "Kilic", "Aslan", "Kara", "Cetin" };

        String[] domains = {
                "gmail.com", "outlook.com", "yandex.com", "hotmail.com", "yahoo.com",
                "protonmail.com", "icloud.com", "zoho.com", "aol.com", "gmx.com"
        };

        // Specific distribution requested: "total 10 types of domains"
        // We will cycle through them to ensure variety.

        for (int i = 0; i < 50; i++) {
            String firstName = firstNames[random.nextInt(firstNames.length)];
            String lastName = lastNames[random.nextInt(lastNames.length)];

            // simple randomization for variety
            String phone = "5" + (10 + random.nextInt(90)) + " " + (100 + random.nextInt(900)) + " "
                    + (10 + random.nextInt(90)) + " " + (10 + random.nextInt(90));

            String domain = domains[i % domains.length];
            String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + (i + 1) + "@" + domain;

            Contact c = new Contact(
                    firstName,
                    null, // middle name
                    lastName,
                    null, // nickname
                    phone,
                    null, // sec phone
                    email,
                    "linkedin.com/in/" + firstName.toLowerCase() + lastName.toLowerCase(),
                    null, // birth date
                    random.nextBoolean() ? "E" : "K" // gender
            );

            dao.addContact(c);
        }

        System.out.println("Seeding Completed. Added 50 contacts.");
    }
}
