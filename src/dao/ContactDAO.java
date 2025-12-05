package dao;

import db.DatabaseConnection;
import model.Contact;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for managing Contact entities in the database.
 * 
 * <p>This class provides CRUD (Create, Read, Update, Delete) operations for contacts.
 * It handles all database interactions related to the contacts table, including inserting
 * new contacts, retrieving contacts by various criteria, updating existing contacts,
 * deleting contacts, and searching for contacts based on query strings.</p>
 * 
 * <p>The class uses the Singleton pattern for database connection management through
 * {@link DatabaseConnection#getInstance()}. All database operations use try-with-resources
 * statements to ensure proper resource management and automatic closure of database connections.</p>
 * 
 * <p><strong>Database Table Structure:</strong><br>
 * The contacts table contains the following columns:
 * <ul>
 *   <li>contact_id (PRIMARY KEY, AUTO_INCREMENT)</li>
 *   <li>first_name</li>
 *   <li>middle_name</li>
 *   <li>last_name</li>
 *   <li>nickname</li>
 *   <li>phone_primary</li>
 *   <li>phone_secondary</li>
 *   <li>email</li>
 *   <li>linkedin_url</li>
 *   <li>birth_date</li>
 * </ul>
 * </p>
 * 
 * @author Project2-Group10
 */
public class ContactDAO {

    /**
     * Adds a new contact to the database and returns its generated ID.
     * 
     * <p>This method inserts a new contact record into the contacts table with all
     * provided information. The method automatically retrieves the generated contact ID
     * from the database and prints a success message to the console.</p>
     * 
     * <p><strong>Note:</strong> If the operation fails due to a SQLException, the error
     * is printed to the standard error stream and the method returns -1 as a failure indicator.</p>
     * 
     * @param contact The {@link Contact} object containing the contact information to be added.
     *                Cannot be null. The contact object should have all required fields set
     *                (firstName, lastName, email, etc.)
     * 
     * @return The automatically generated contact ID (primary key) if the insertion is successful,
     *         or -1 if the operation fails or no generated keys were retrieved.
     * 
     * @throws SQLException is caught internally; errors are logged via printStackTrace.
     * 
     * @see Contact
     * @see DatabaseConnection
     */
    public int addContact(Contact contact) {
        String query = "INSERT INTO contacts (first_name, middle_name, last_name, nickname, phone_primary, phone_secondary, email, linkedin_url, birth_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            setContactStatement(stmt, contact);
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    System.out.println("Contact added successfully with ID: " + id);
                    return id;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Retrieves all contacts from the database.
     * 
     * <p>This method executes a SELECT query to fetch all contact records from the contacts
     * table and returns them as a list of Contact objects. The results are mapped from the
     * database ResultSet to Contact objects using the {@link #mapResultSetToContact(ResultSet)}
     * helper method.</p>
     * 
     * <p>If no contacts exist in the database, an empty list is returned. If a SQLException
     * occurs during the operation, the error is logged and an empty list is returned.</p>
     * 
     * @return A {@link List} of {@link Contact} objects representing all contacts in the database.
     *         Returns an empty list if no contacts exist or if an error occurs.
     * 
     * @throws SQLException is caught internally; errors are logged via printStackTrace.
     * 
     * @see Contact
     * @see #mapResultSetToContact(ResultSet)
     */
    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        String query = "SELECT * FROM contacts";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                contacts.add(mapResultSetToContact(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contacts;
    }

    /**
     * Retrieves a contact from the database by its contact ID.
     * 
     * <p>This method executes a parameterized SELECT query to fetch a single contact
     * record from the contacts table where the contact_id matches the provided ID.
     * The result is mapped from the database ResultSet to a Contact object.</p>
     * 
     * <p>If no contact with the specified ID exists in the database, null is returned.
     * If a SQLException occurs during the operation, the error is logged and null is returned.</p>
     * 
     * @param id The unique identifier (contact_id) of the contact to retrieve.
     *           Must be a valid positive integer representing an existing contact.
     * 
     * @return A {@link Contact} object if a contact with the specified ID is found,
     *         or null if no such contact exists or if an error occurs.
     * 
     * @throws SQLException is caught internally; errors are logged via printStackTrace.
     * 
     * @see Contact
     * @see #mapResultSetToContact(ResultSet)
     */
    public Contact getContactById(int id) {
        String query = "SELECT * FROM contacts WHERE contact_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToContact(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Updates an existing contact in the database.
     * 
     * <p>This method updates all contact information for a contact identified by its ID.
     * All fields including first_name, middle_name, last_name, nickname, phone numbers,
     * email, LinkedIn URL, and birth date are updated based on the provided Contact object.</p>
     * 
     * <p>The contact to be updated is identified by the ID in the Contact object.
     * If no contact with the specified ID exists, the update operation silently completes
     * without affecting any records. A success message is printed to the console upon
     * successful execution.</p>
     * 
     * <p>If a SQLException occurs during the operation, the error is logged via printStackTrace.</p>
     * 
     * @param contact The {@link Contact} object containing the updated information.
     *                Cannot be null. The contact object must have a valid ID set, and all
     *                fields should contain the values to be updated in the database.
     * 
     * @throws SQLException is caught internally; errors are logged via printStackTrace.
     * 
     * @see Contact
     * @see #setContactStatement(PreparedStatement, Contact)
     */
    public void updateContact(Contact contact) {
        String query = "UPDATE contacts SET first_name=?, middle_name=?, last_name=?, nickname=?, phone_primary=?, phone_secondary=?, email=?, linkedin_url=?, birth_date=? WHERE contact_id=?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            setContactStatement(stmt, contact);
            stmt.setInt(10, contact.getId());
            stmt.executeUpdate();
            System.out.println("Contact updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Searches for contacts using a query string with default search fields.
     * 
     * <p>This is a convenience method that delegates to
     * {@link #searchContacts(String, List)} with a null fields parameter,
     * causing the search to use default fields: first_name, last_name,
     * phone_primary, and email.</p>
     * 
     * @param queryStr The search query string. This string will be wrapped with '%' characters
     *                 for SQL LIKE pattern matching (e.g., "%queryStr%").
     * 
     * @return A {@link List} of {@link Contact} objects matching the search criteria,
     *         ordered by first_name then last_name in ascending order.
     *         Returns an empty list if no matches are found or if an error occurs.
     * 
     * @see #searchContacts(String, List)
     */
    public List<Contact> searchContacts(String queryStr) {
        return searchContacts(queryStr, null);
    }

    /**
     * Searches for contacts using a query string and custom search fields.
     * 
     * <p>This method performs a flexible search across contact records by creating a dynamic
     * SQL query with multiple OR conditions for the specified fields. Each field is searched
     * using SQL LIKE pattern matching with wildcard characters ('%') to find partial matches.</p>
     * 
     * <p>If no fields are specified or the fields list is empty, default fields are used:
     * first_name, last_name, phone_primary, and email.</p>
     * 
     * <p>Results are automatically sorted by first_name and last_name in ascending order
     * to provide consistent, user-friendly output.</p>
     * 
     * <p>If a SQLException occurs during the search, the error is logged via printStackTrace
     * and an empty list is returned.</p>
     * 
     * @param queryStr The search query string. This string will be wrapped with '%' characters
     *                 for SQL LIKE pattern matching (e.g., "%queryStr%").
     *                 Cannot be null.
     * 
     * @param fields A {@link List} of field names to search in the contacts table.
     *               Valid field names include: first_name, middle_name, last_name, nickname,
     *               phone_primary, phone_secondary, email, linkedin_url, birth_date.
     *               If null or empty, default fields are used: first_name, last_name,
     *               phone_primary, and email.
     * 
     * @return A {@link List} of {@link Contact} objects matching the search criteria in at least
     *         one of the specified fields. Results are ordered by first_name and last_name.
     *         Returns an empty list if no matches are found or if an error occurs.
     * 
     * @throws SQLException is caught internally; errors are logged via printStackTrace.
     * 
     * @see Contact
     * @see #mapResultSetToContact(ResultSet)
     */
    public List<Contact> searchContacts(String queryStr, List<String> fields) {
        List<Contact> contacts = new ArrayList<>();
        if (fields == null || fields.isEmpty()) {
            fields = new ArrayList<>();
            fields.add("first_name");
            fields.add("last_name");
            fields.add("phone_primary");
            fields.add("email");
        }

        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM contacts WHERE ");
        for (int i = 0; i < fields.size(); i++) {
            if (i > 0)
                sqlBuilder.append(" OR ");
            sqlBuilder.append(fields.get(i)).append(" LIKE ?");
        }

        // Add sorting
        sqlBuilder.append(" ORDER BY first_name ASC, last_name ASC");

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {

            String likeQuery = "%" + queryStr + "%";
            for (int i = 0; i < fields.size(); i++) {
                stmt.setString(i + 1, likeQuery);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                contacts.add(mapResultSetToContact(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contacts;
    }

    /**
     * Deletes a contact from the database by its contact ID.
     * 
     * <p>This method executes a parameterized DELETE query to remove a contact record
     * from the contacts table where the contact_id matches the provided ID. If the contact
     * is successfully deleted, a success message is printed to the console.</p>
     * 
     * <p>If no contact with the specified ID exists, the delete operation silently completes
     * without affecting any records. If a SQLException occurs during the operation,
     * the error is logged via printStackTrace.</p>
     * 
     * @param id The unique identifier (contact_id) of the contact to delete.
     *           Must be a valid positive integer.
     * 
     * @throws SQLException is caught internally; errors are logged via printStackTrace.
     * 
     * @see #getContactById(int)
     */
    public void deleteContact(int id) {
        String query = "DELETE FROM contacts WHERE contact_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Contact deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method to set all contact fields in a PreparedStatement.
     * 
     * <p>This private utility method populates a PreparedStatement with contact field values
     * in the correct order. It is used by both {@link #addContact(Contact)} and
     * {@link #updateContact(Contact)} methods to avoid code duplication.</p>
     * 
     * <p>The method sets parameters in the following order:
     * <ol>
     *   <li>first_name (String)</li>
     *   <li>middle_name (String)</li>
     *   <li>last_name (String)</li>
     *   <li>nickname (String)</li>
     *   <li>phone_primary (String)</li>
     *   <li>phone_secondary (String)</li>
     *   <li>email (String)</li>
     *   <li>linkedin_url (String)</li>
     *   <li>birth_date (java.sql.Date)</li>
     * </ol>
     * </p>
     * 
     * @param stmt The {@link PreparedStatement} to populate with contact values.
     *             Cannot be null.
     * 
     * @param contact The {@link Contact} object containing the values to set.
     *                Cannot be null.
     * 
     * @throws SQLException if there is an error setting values in the PreparedStatement.
     * 
     * @see Contact
     */
    private void setContactStatement(PreparedStatement stmt, Contact contact) throws SQLException {
        stmt.setString(1, contact.getFirstName());
        stmt.setString(2, contact.getMiddleName());
        stmt.setString(3, contact.getLastName());
        stmt.setString(4, contact.getNickname());
        stmt.setString(5, contact.getPhonePrimary());
        stmt.setString(6, contact.getPhoneSecondary());
        stmt.setString(7, contact.getEmail());
        stmt.setString(8, contact.getLinkedinUrl());
        stmt.setDate(9, contact.getBirthDate());
    }

    /**
     * Helper method to map a database ResultSet row to a Contact object.
     * 
     * <p>This private utility method converts a single row from a database ResultSet
     * into a fully constructed Contact object. It extracts all fields from the ResultSet
     * and passes them to the Contact constructor in the correct order.</p>
     * 
     * <p>This method is used by {@link #getAllContacts()}, {@link #getContactById(int)},
     * and {@link #searchContacts(String, List)} to avoid code duplication.</p>
     * 
     * @param rs The {@link ResultSet} positioned at the current row to map.
     *           Cannot be null. The ResultSet should have been obtained from a query
     *           selecting from the contacts table.
     * 
     * @return A new {@link Contact} object populated with values from the current
     *         ResultSet row.
     * 
     * @throws SQLException if there is an error retrieving values from the ResultSet
     *                      or if the ResultSet is not properly initialized.
     * 
     * @see Contact
     */
    private Contact mapResultSetToContact(ResultSet rs) throws SQLException {
        return new Contact(
                rs.getInt("contact_id"),
                rs.getString("first_name"),
                rs.getString("middle_name"),
                rs.getString("last_name"),
                rs.getString("nickname"),
                rs.getString("phone_primary"),
                rs.getString("phone_secondary"),
                rs.getString("email"),
                rs.getString("linkedin_url"),
                rs.getDate("birth_date"));
    }
}
