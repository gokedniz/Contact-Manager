package command;

import dao.ActivityLogDAO;
import dao.UserDAO;
import model.User;

/**
 * Command for registering a new user in the system.
 * 
 * <p>Implements the Command pattern to support undo. When executed, adds a user
 * to the database with the provided password hash and logs the action.
 * When undone, deletes the user and all associated activity logs.</p>
 * 
 * @author Group 10
 * @version 1.0
 */
public class AddUserCommand implements Command {
    private UserDAO userDAO;
    private ActivityLogDAO activityLogDAO;
    private User user;
    private String passwordHash;
    private int adminId;
    private boolean success;

    /**
     * Constructs an AddUserCommand.
     * 
     * @param userDAO The DAO for user database operations.
     * @param activityLogDAO The DAO for logging activities.
     * @param user The user object to add.
     * @param passwordHash The hashed password for the user.
     * @param adminId The ID of the admin performing this action (for logging).
     */
    public AddUserCommand(UserDAO userDAO, ActivityLogDAO activityLogDAO, User user, String passwordHash, int adminId) {
        this.userDAO = userDAO;
        this.activityLogDAO = activityLogDAO;
        this.user = user;
        this.passwordHash = passwordHash;
        this.adminId = adminId;
    }

    /**
     * Executes the add user command.
     * 
     * <p>Inserts the user into the database, retrieves the user by username to get
     * the generated ID, and logs the registration action.</p>
     */
    @Override
    public void execute() {
        success = userDAO.addUser(user, passwordHash);
        if (success) {
            User addedUser = userDAO.getUserByUsername(user.getUsername());
            if (addedUser != null) {
                user.setId(addedUser.getId());
                activityLogDAO.logAction(adminId, "ADD_USER", "Added user: " + user.getUsername());
            }
        }
    }

    /**
     * Undoes the add user command by deleting the user and associated logs.
     * 
     * <p>Removes all activity logs for the user and then deletes the user account.</p>
     */
    @Override
    public void undo() {
        if (success && user.getId() > 0) {
            activityLogDAO.deleteLogsByUserId(user.getId());
            userDAO.deleteUser(user.getId());
            // Log undo? Maybe not to keep history clean or log as UNDO
        }
    }
}
