package service;

import dao.UserDAO;
import model.User;
import util.PasswordUtil;

public class AuthenticationService {

    private UserDAO userDAO;
    private dao.ActivityLogDAO activityLogDAO;

    public AuthenticationService() {
        this.userDAO = new UserDAO();
        this.activityLogDAO = new dao.ActivityLogDAO();
    }

    public User login(String username, String password) {
        User user = userDAO.getUserByUsername(username);

        if (user != null) {
            String inputHash = PasswordUtil.hashPassword(password);
            if (inputHash.equals(user.getPasswordHash())) {
                activityLogDAO.logAction(user.getId(), "LOGIN", "User logged in successfully.");
                return user;
            }
        }
        return null;
    }

    public int changePassword(User user, String oldPassword, String newPassword) {
        String oldHash = PasswordUtil.hashPassword(oldPassword);
        if (!oldHash.equals(user.getPasswordHash())) {
            return 1; // Wrong old password
        }

        if (oldPassword.equals(newPassword)) {
            return 2; // Same as old password
        }

        String newHash = PasswordUtil.hashPassword(newPassword);
        boolean success = userDAO.updatePassword(user.getId(), newHash);
        return success ? 0 : 3; // 0=Success, 3=DB Error
    }
}
