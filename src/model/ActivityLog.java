package model;

import java.sql.Timestamp;

public class ActivityLog {
    private int logId;
    private int userId;
    private String username;
    private String actionType;
    private String details;
    private Timestamp timestamp;

    public ActivityLog(int logId, int userId, String username, String actionType, String details, Timestamp timestamp) {
        this.logId = logId;
        this.userId = userId;
        this.username = username;
        this.actionType = actionType;
        this.details = details;
        this.timestamp = timestamp;
    }

    public int getLogId() {
        return logId;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getActionType() {
        return actionType;
    }

    public String getDetails() {
        return details;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("[%s] User %s (%d) - %s: %s", timestamp, username, userId, actionType, details);
    }
}
