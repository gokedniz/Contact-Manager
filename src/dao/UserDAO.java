package dao;

import db.DatabaseConnection;
import model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object (DAO) for managing User entities in the database.
 * 
 * <p>
 * This class handles all database operations related to users, including authentication,
 * user creation, updates, deletion, and user role management. It supports different user
 * types (Tester, JuniorDeveloper, SeniorDeveloper, Manager) and automatically instantiates
 * the correct user subclass based on the role stored in the database.
 * </p>
 * 
 * @author Group 10
 * @version 1.0
 */
public class UserDAO {

    /**
     * Retrieves a user from the database by username.
     * 
     * <p>Queries the users table for a user with the specified username and returns
     * the appropriate User subclass instance based on the stored role.</p>
     * 
     * @param username The username to search for. Cannot be null.
     * 
    * @return A User object of the appropriate type (Tester, JuniorDeveloper, SeniorDeveloper, or Manager),
    *         or null if no user is found.
     */
    public User getUserByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("user_id");
                String user = rs.getString("username");
                String first = rs.getString("name");
                String last = rs.getString("surname");
                String passHash = rs.getString("password_hash");
                String roleStr = rs.getString("role");

                Role role = Role.fromDbValue(roleStr);

                switch (role) {
                    case TESTER:
                        return new Tester(id, user, first, last, passHash);
                    case JUNIOR_DEVELOPER:
                        return new JuniorDeveloper(id, user, first, last, passHash);
                    case SENIOR_DEVELOPER:
                        return new SeniorDeveloper(id, user, first, last, passHash);
                    case MANAGER:
                        return new Manager(id, user, first, last, passHash);
                    default:
                        return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Updates a user's password hash in the database.
     * 
     * @param userId The ID of the user whose password should be updated.
     * @param newPasswordHash The new hashed password to store.
     * 
    * @return true if the update was successful (at least one row affected), false otherwise.
     */
    public boolean updatePassword(int userId, String newPasswordHash) {
        String query = "UPDATE users SET password_hash = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newPasswordHash);
            stmt.setInt(2, userId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Adds a new user to the database.
     * 
     * <p>Inserts a new user record with the provided information. The user's role is
     * determined from the user object's type (Tester, JuniorDeveloper, etc.).</p>
     * 
     * @param user The User object containing the user information. Cannot be null.
     * @param passwordHash The hashed password for the user.
     * 
    * @return true if the user was successfully added, false otherwise.
     */
    public boolean addUser(User user, String passwordHash) {
        String query = "INSERT INTO users (username, password_hash, name, surname, role) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, passwordHash);
            stmt.setString(3, user.getFirstName());
            stmt.setString(4, user.getLastName());

            String roleStr = "JUNIOR"; // Default fallback
            if (user instanceof Tester)
                roleStr = "TESTER";
            else if (user instanceof JuniorDeveloper)
                roleStr = "JUNIOR";
            else if (user instanceof SeniorDeveloper)
                roleStr = "SENIOR";
            else if (user instanceof Manager)
                roleStr = "MANAGER";

            stmt.setString(5, roleStr);

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves all users from the database.
     * 
     * <p>Fetches all user records and instantiates the appropriate User subclass
     * for each based on the stored role.</p>
     * 
    * @return A list of User objects of various types. Returns an empty list if no
    *         users exist or if an error occurs.
     */
    public java.util.List<User> getAllUsers() {
        java.util.List<User> users = new java.util.ArrayList<>();
        String query = "SELECT * FROM users";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("user_id");
                String user = rs.getString("username");
                String first = rs.getString("name");
                String last = rs.getString("surname");
                String passHash = rs.getString("password_hash");
                String roleStr = rs.getString("role");

                Role role = Role.fromDbValue(roleStr);
                User u = null;
                switch (role) {
                    case TESTER:
                        u = new Tester(id, user, first, last, passHash);
                        break;
                    case JUNIOR_DEVELOPER:
                        u = new JuniorDeveloper(id, user, first, last, passHash);
                        break;
                    case SENIOR_DEVELOPER:
                        u = new SeniorDeveloper(id, user, first, last, passHash);
                        break;
                    case MANAGER:
                        u = new Manager(id, user, first, last, passHash);
                        break;
                }
                if (u != null) {
                    users.add(u);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Updates an existing user's information in the database.
     * 
     * <p>Updates the user's name, surname, role, username, and password hash based on
     * the provided User object's current state.</p>
     * 
     * @param user The User object with updated information. Must have a valid ID.
     * 
    * @return true if the update was successful (at least one row affected), false otherwise.
     */
    public boolean updateUser(User user) {
        // Update Name, Surname, Role, Username, and Password
        String query = "UPDATE users SET name = ?, surname = ?, role = ?, username = ?, password_hash = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());

            String roleStr = "JUNIOR";
            if (user instanceof Tester)
                roleStr = "TESTER";
            else if (user instanceof JuniorDeveloper)
                roleStr = "JUNIOR";
            else if (user instanceof SeniorDeveloper)
                roleStr = "SENIOR";
            else if (user instanceof Manager)
                roleStr = "MANAGER";

            stmt.setString(3, roleStr);
            stmt.setString(4, user.getUsername());
            stmt.setString(5, user.getPasswordHash());
            stmt.setInt(6, user.getId());

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a user from the database by user ID.
     * 
     * @param userId The ID of the user to delete.
     * 
    * @return true if the user was successfully deleted, false otherwise.
     */
    public boolean deleteUser(int userId) {
        String query = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if at least one manager user exists in the database.
     * 
    * @return true if at least one user with the MANAGER role exists, false otherwise.
     */
    public boolean hasManager() {
        String query = "SELECT COUNT(*) FROM users WHERE role = 'MANAGER'";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
