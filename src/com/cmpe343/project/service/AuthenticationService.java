package com.cmpe343.project.service;

import com.cmpe343.project.dao.UserDAO;
import com.cmpe343.project.model.User;
import com.cmpe343.project.util.PasswordUtil;

public class AuthenticationService {

    private UserDAO userDAO;

    public AuthenticationService() {
        this.userDAO = new UserDAO();
    }

    public User login(String username, String password) {
        User user = userDAO.getUserByUsername(username);

        if (user != null) {
            String inputHash = PasswordUtil.hashPassword(password);
            if (inputHash.equals(user.getPasswordHash())) {
                return user;
            }
        }
        return null;
    }
}
