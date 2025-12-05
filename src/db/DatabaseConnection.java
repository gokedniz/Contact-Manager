package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton class for managing database connections.
 * 
 * <p>
 * Provides a single, reusable connection to the MySQL database using the
 * Singleton pattern.
 * Automatically reconnects if the connection is closed. Connection details are
 * hardcoded
 * and should be externalized in production environments.
 * </p>
 * 
 * <p>
 * <strong>Connection Details:</strong>
 * <ul>
 * <li>Database: cmpe343_project</li>
 * <li>Host: localhost:3306</li>
 * <li>Driver: MySQL Connector/J (com.mysql.cj.jdbc.Driver)</li>
 * </ul>
 * </p>
 * 
 * @author Group 10
 * @version 1.0
 */
public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    private static final String URL = "jdbc:mysql://localhost:3306/cmpe343_project";
    private static final String USER = "myuser";
    private static final String PASSWORD = "1234";

    private DatabaseConnection() {
        try {
            // Load MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gets the singleton instance of DatabaseConnection.
     * 
     * <p>
     * Lazily initializes the connection on first call. If the connection is closed,
     * automatically re-establishes it with synchronized access for thread-safety.
     * </p>
     * 
     * @return The singleton DatabaseConnection instance.
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        } else {
            try {
                if (instance.getConnection().isClosed()) {
                    instance = new DatabaseConnection();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    /**
     * Gets the underlying SQL Connection object.
     * 
     * @return The active database Connection, or null if connection failed.
     */
    public Connection getConnection() {
        return connection;
    }
}
