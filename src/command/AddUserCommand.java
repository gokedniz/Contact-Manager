package command;

import dao.ActivityLogDAO;
import dao.UserDAO;
import model.User;

public class AddUserCommand implements Command {
    private UserDAO userDAO;
    private ActivityLogDAO activityLogDAO;
    private User user;
    private String passwordHash;
    private int adminId;
    private boolean success;

    public AddUserCommand(UserDAO userDAO, ActivityLogDAO activityLogDAO, User user, String passwordHash, int adminId) {
        this.userDAO = userDAO;
        this.activityLogDAO = activityLogDAO;
        this.user = user;
        this.passwordHash = passwordHash;
        this.adminId = adminId;
    }

    @Override
    public void execute() {
        success = userDAO.addUser(user, passwordHash);
        if (success) {
            // We need to fetch the user to get the ID if it was auto-generated,
            // but UserDAO.addUser doesn't return ID.
            // For undo to work, we need the ID.
            // Let's assume username is unique and fetch by username.
            User addedUser = userDAO.getUserByUsername(user.getUsername());
            if (addedUser != null) {
                user.setId(addedUser.getId());
                activityLogDAO.logAction(adminId, "ADD_USER", "Added user: " + user.getUsername());
            }
        }
    }

    @Override
    public void undo() {
        if (success && user.getId() > 0) {
            activityLogDAO.deleteLogsByUserId(user.getId());
            userDAO.deleteUser(user.getId());
            // Log undo? Maybe not to keep history clean or log as UNDO
        }
    }
}
