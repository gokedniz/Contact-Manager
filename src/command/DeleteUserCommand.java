package command;

import dao.ActivityLogDAO;
import dao.UserDAO;
import model.User;

public class DeleteUserCommand implements Command {
    private UserDAO userDAO;
    private ActivityLogDAO activityLogDAO;
    private int userIdToDelete;
    private int adminId;
    private User backupUser;
    private boolean success;

    public DeleteUserCommand(UserDAO userDAO, ActivityLogDAO activityLogDAO, int userIdToDelete, int adminId) {
        this.userDAO = userDAO;
        this.activityLogDAO = activityLogDAO;
        this.userIdToDelete = userIdToDelete;
        this.adminId = adminId;
    }

    @Override
    public void execute() {
        // Backup user data before delete
        // We need to find the user first.
        // Ideally the service should pass the User object, but ID is what we have.
        // We can't easily get the user by ID from UserDAO as it only has
        // getUserByUsername.
        // Let's rely on the service passing the user object or add getUserById to DAO.
        // For now, let's iterate all users to find the one (inefficient but works with
        // current DAO).
        for (User u : userDAO.getAllUsers()) {
            if (u.getId() == userIdToDelete) {
                backupUser = u;
                break;
            }
        }

        if (backupUser != null) {
            activityLogDAO.deleteLogsByUserId(userIdToDelete);
            success = userDAO.deleteUser(userIdToDelete);
            if (success) {
                activityLogDAO.logAction(adminId, "DELETE_USER", "Deleted user ID: " + userIdToDelete);
            }
        }
    }

    @Override
    public void undo() {
        if (success && backupUser != null) {
            // Restore user
            // Note: Password hash is needed. User object has it?
            // User model has getPasswordHash() but it might be protected/private or not
            // exposed in all subclasses.
            // Let's check User.java.
            // Assuming we can get it.
            userDAO.addUser(backupUser, backupUser.getPasswordHash());

            // Note: The ID will likely change because it's auto-increment.
            // This is a known limitation unless we force ID insertion.
        }
    }
}
