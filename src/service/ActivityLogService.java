package service;

import dao.ActivityLogDAO;
import model.ActivityLog;

import java.util.List;

public class ActivityLogService {
    private ActivityLogDAO activityLogDAO;

    public ActivityLogService() {
        this.activityLogDAO = new ActivityLogDAO();
    }

    public List<ActivityLog> getAllLogs(String usernameFilter, String actionFilter, String sortOrder) {
        return activityLogDAO.getAllLogs(usernameFilter, actionFilter, sortOrder);
    }
}
