package util;

import db.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Utility class for database initialization and maintenance tasks.
 * 
 * <p>Provides methods for setting up and configuring the database schema and data.
 * Currently supports migrating plain text passwords to hashed format using SHA-256.</p>
 * 
 * @author Group 10
 * @version 1.0
 */
public class DatabaseSetup {

    /**
     * Updates all default user passwords from plain text to SHA-256 hashes.
     * 
     * <p>Converts the following test user passwords:
     * <ul>
     *   <li>tt (Tester)</li>
     *   <li>jd (Junior Developer)</li>
     *   <li>sd (Senior Developer)</li>
     *   <li>man (Manager)</li>
     * </ul>
     */
    public static void updatePasswordsToHash() {
        System.out.println("Updating plain text passwords to hashes...");
        updateUserPassword("tt", "tt");
        updateUserPassword("jd", "jd");
        updateUserPassword("sd", "sd");
        updateUserPassword("man", "man");
        System.out.println("Passwords updated.");
    }

    /**
     * Updates a single user's password in the database.
     * 
     * <p>Hashes the plain text password using SHA-256 and updates the users table.
     * Errors are logged via printStackTrace().</p>
     * 
     * @param username The username of the user whose password to update.
     * @param plainPassword The plain text password to hash and store.
     * 
     * @see PasswordUtil#hashPassword(String)
     */
    private static void updateUserPassword(String username, String plainPassword) {
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);
        String query = "UPDATE users SET password_hash = ? WHERE username = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, hashedPassword);
            stmt.setString(2, username);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
