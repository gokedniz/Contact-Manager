package dao;

import db.DatabaseConnection;
import model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

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

    public boolean updateUser(User user) {
        String query = "UPDATE users SET name = ?, surname = ?, role = ? WHERE user_id = ?";
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
            stmt.setInt(4, user.getId());

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

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
}
