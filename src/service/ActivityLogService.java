package service;

import dao.ActivityLogDAO;
import model.ActivityLog;

import java.util.List;

/**
 * Service layer for managing activity logs.
 * 
 * <p>Provides access to system-wide activity logs with filtering and sorting capabilities.
 * Activity logs track user actions and system events, useful for auditing and monitoring.</p>
 * 
 * @author Group 10
 * @version 1.0
 */
public class ActivityLogService {
    private ActivityLogDAO activityLogDAO;

    public ActivityLogService() {
        this.activityLogDAO = new ActivityLogDAO();
    }

    /**
     * Retrieves activity logs with optional filtering and sorting.
     * 
     * <p>Supports filtering by username and/or action type using LIKE pattern matching.
     * Results can be sorted in ascending or descending order by log ID.</p>
     * 
     * @param usernameFilter Optional username filter (null or empty to skip). Uses LIKE matching.
     * @param actionFilter Optional action type filter (null or empty to skip). Uses LIKE matching.
     * @param sortOrder Sort direction: "ASC" for ascending, defaults to "DESC".
     * 
     * @return A list of ActivityLog objects matching the filters, sorted as specified.
     *         Returns an empty list if no logs match or if an error occurs.
     * 
     * @see ActivityLogDAO#getAllLogs(String, String, String)
     */
    public List<ActivityLog> getAllLogs(String usernameFilter, String actionFilter, String sortOrder) {
        return activityLogDAO.getAllLogs(usernameFilter, actionFilter, sortOrder);
    }
}
