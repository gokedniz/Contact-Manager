package model;

import java.sql.Timestamp;

/**
 * Represents an activity log entry in the contact management system.
 * Contains details about user actions such as login, logout, and contact modifications.
 * 
 * <p>Each log entry includes the user ID, username, action type, details, and timestamp.</p>
 * 
 * @author Group 10
 * @version 1.0
 */
public class ActivityLog {
    private int logId;
    private int userId;
    private String username;
    private String actionType;
    private String details;
    private Timestamp timestamp;
    /**
     * Constructs a new ActivityLog entry with the specified details.
     * 
     * @param logId      The unique identifier for the log entry.
     * @param userId     The ID of the user who performed the action.
     * @param username   The username of the user who performed the action.
     * @param actionType The type of action performed (e.g., "LOGIN", "LOGOUT", "CREATE_CONTACT").
     * @param details    Additional details about the action.
     * @param timestamp  The timestamp when the action was performed.
     */
    public ActivityLog(int logId, int userId, String username, String actionType, String details, Timestamp timestamp) {
        this.logId = logId;
        this.userId = userId;
        this.username = username;
        this.actionType = actionType;
        this.details = details;
        this.timestamp = timestamp;
    }
    /**
     * Gets the unique identifier of the log entry.
     * 
     * @return The log ID.
     */
    public int getLogId() {
        return logId;
    }
    /**
     * Gets the ID of the user who performed the action.
     * 
     * @return The user ID.
     */
    public int getUserId() {
        return userId;
    }
    /**
     * Gets the username of the user who performed the action.
     * 
     * @return The username.
     */
    public String getUsername() {
        return username;
    }
    /**
     * Gets the type of action performed.
     * 
     * @return The action type.
     */
    public String getActionType() {
        return actionType;
    }
    /**
     * Gets additional details about the action.
     * 
     * @return The action details.
     */
    public String getDetails() {
        return details;
    }
    /**
     * Gets the timestamp when the action was performed.
     * 
     * @return The timestamp.
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }
    /**
     * Returns a string representation of the ActivityLog entry.
     * 
     * @return A formatted string containing log details.
     */
    @Override
    public String toString() {
        return String.format("[%s] User %s (%d) - %s: %s", timestamp, username, userId, actionType, details);
    }
}
