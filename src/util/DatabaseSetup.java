package util;

import db.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseSetup {

    public static void updatePasswordsToHash() {
        System.out.println("Updating plain text passwords to hashes...");
        updateUserPassword("tt", "tt");
        updateUserPassword("jd", "jd");
        updateUserPassword("sd", "sd");
        updateUserPassword("man", "man");
        System.out.println("Passwords updated.");
    }

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
