package dao;

import db.DatabaseConnection;

import model.ActivityLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ActivityLogDAO {

    public void logAction(int userId, String actionType, String details) {
        String query = "INSERT INTO activity_logs (user_id, action_type, details) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            stmt.setString(2, actionType);
            stmt.setString(3, details);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to log activity: " + actionType);
        }
    }

    // Overload for system actions or when user ID is not available/relevant
    public void logAction(String actionType, String details) {
        String query = "INSERT INTO activity_logs (action_type, details) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, actionType);
            stmt.setString(2, details);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to log activity: " + actionType);
        }
    }

    public List<ActivityLog> getAllLogs() {
        List<ActivityLog> logs = new ArrayList<>();
        String query = "SELECT l.log_id, l.user_id, l.action_type, l.details, l.timestamp, u.username " +
                "FROM activity_logs l " +
                "LEFT JOIN users u ON l.user_id = u.user_id " +
                "ORDER BY l.timestamp DESC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String username = rs.getString("username");
                if (username == null) {
                    username = "Unknown/System";
                }

                logs.add(new ActivityLog(
                        rs.getInt("log_id"),
                        rs.getInt("user_id"),
                        username,
                        rs.getString("action_type"),
                        rs.getString("details"),
                        rs.getTimestamp("timestamp")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }
}
