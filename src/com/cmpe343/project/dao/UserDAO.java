package com.cmpe343.project.dao;

import com.cmpe343.project.db.DatabaseConnection;
import com.cmpe343.project.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public User getUserByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("user_id");
                String user = rs.getString("username");
                String first = rs.getString("name");
                String last = rs.getString("surname");
                String passHash = rs.getString("password_hash");
                String roleStr = rs.getString("role");

                Role role = Role.fromDbValue(roleStr);

                switch (role) {
                    case TESTER:
                        return new Tester(id, user, first, last, passHash);
                    case JUNIOR_DEVELOPER:
                        return new JuniorDeveloper(id, user, first, last, passHash);
                    case SENIOR_DEVELOPER:
                        return new SeniorDeveloper(id, user, first, last, passHash);
                    case MANAGER:
                        return new Manager(id, user, first, last, passHash);
                    default:
                        return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
