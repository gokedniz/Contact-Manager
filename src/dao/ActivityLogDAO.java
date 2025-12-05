package dao;

import db.DatabaseConnection;

import model.ActivityLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for managing ActivityLog records in the database.
 * 
 * <p>This class handles logging of user actions and system events. It provides methods to
 * record both user-specific actions and system-level actions, retrieve activity logs with
 * optional filtering and sorting, and delete activity logs for specific users.</p>
 * 
 * @author Project2-Group10
 * @version 1.0
 * @since 1.0
 */
public class ActivityLogDAO {

    /**
     * Logs a user action to the activity_logs table.
     * 
     * <p>Records an action performed by a specific user, including the action type and
     * relevant details. The timestamp is automatically set by the database.</p>
     * 
     * @param userId The ID of the user performing the action.
     * @param actionType The type of action performed (e.g., "LOGIN", "CREATE_CONTACT", "DELETE_USER").
     * @param details Additional details about the action.
     * 
     * @throws SQLException is caught internally; errors are logged via printStackTrace and System.err.
     */
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

    /**
     * Logs a system action to the activity_logs table without a specific user.
     * 
     * <p>Records system-level actions or actions when a user ID is not available.
     * The user_id field will be NULL in the database.</p>
     * 
     * @param actionType The type of action (e.g., "SYSTEM_STARTUP", "DATABASE_ERROR").
     * @param details Additional details about the action.
     * 
     * @throws SQLException is caught internally; errors are logged via printStackTrace and System.err.
     */
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


    /**
     * Retrieves activity logs with optional filtering and sorting.
     * 
     * <p>Builds a dynamic query to fetch logs with optional filters for username and action type.
     * Results are joined with the users table to include usernames; system logs show "Unknown/System"
     * for NULL user_ids.</p>
     * 
     * @param usernameFilter Optional filter for username (LIKE pattern matching). Pass null or empty string to skip.
     * @param actionFilter Optional filter for action type (LIKE pattern matching). Pass null or empty string to skip.
     * @param sortOrder Sort direction for log_id: "ASC" for ascending, defaults to "DESC" for descending.
     * 
     * @return A list of ActivityLog objects matching the filters, ordered as specified.
     *         Returns an empty list if no logs match or if an error occurs.
     * 
     * @throws SQLException is caught internally; errors are logged via printStackTrace.
     */
    public List<ActivityLog> getAllLogs(String usernameFilter, String actionFilter, String sortOrder) {
        List<ActivityLog> logs = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT l.log_id, l.user_id, l.action_type, l.details, l.timestamp, u.username " +
                        "FROM activity_logs l " +
                        "LEFT JOIN users u ON l.user_id = u.user_id " +
                        "WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        if (usernameFilter != null && !usernameFilter.trim().isEmpty()) {
            queryBuilder.append("AND u.username LIKE ? ");
            params.add("%" + usernameFilter.trim() + "%");
        }

        if (actionFilter != null && !actionFilter.trim().isEmpty()) {
            queryBuilder.append("AND l.action_type LIKE ? ");
            params.add("%" + actionFilter.trim() + "%");
        }

        String sort = (sortOrder != null && sortOrder.equalsIgnoreCase("ASC")) ? "ASC" : "DESC";
        queryBuilder.append("ORDER BY l.log_id ").append(sort);

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(queryBuilder.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }

    

    /**
     * Deletes all activity logs for a specific user.
     * 
     * <p>Removes all records in activity_logs where user_id matches the provided ID.
     * This is typically called when a user account is deleted.</p>
     * 
     * @param userId The ID of the user whose logs should be deleted.
     * 
     * @throws SQLException is caught internally; errors are logged via printStackTrace and System.err.
     */
    public void deleteLogsByUserId(int userId) {
        String query = "DELETE FROM activity_logs WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to delete activity logs for user ID: " + userId);
        }
    }
}
