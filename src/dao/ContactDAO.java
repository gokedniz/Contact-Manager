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
 * @author Group 10
 * @version 1.0
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
     */
    public int addContact(Contact contact) {
        String query = "INSERT INTO contacts (first_name, middle_name, last_name, nickname, phone_primary, phone_secondary, email, linkedin_url, birth_date, gender) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
     * <p>Executes a SELECT query to fetch all contact records and returns them as a list.
     * If no contacts exist, an empty list is returned.</p>
     * 
    * @return A list of all Contact objects. Returns an empty list if no contacts exist or error occurs.
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
     * <p>Executes a parameterized SELECT query to fetch a single contact record.
     * Returns null if no contact with the specified ID exists.</p>
     * 
     * @param id The unique identifier (contact_id) of the contact to retrieve.
     * 
    * @return A Contact object if found, or null if not found or error occurs.
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
     * <p>Updates all contact information for the contact identified by its ID.
     * Prints a success message upon completion.</p>
     * 
    * @param contact The Contact object with updated information. Must have a valid ID.
     */
    public void updateContact(Contact contact) {
        String query = "UPDATE contacts SET first_name=?, middle_name=?, last_name=?, nickname=?, phone_primary=?, phone_secondary=?, email=?, linkedin_url=?, birth_date=?, gender=? WHERE contact_id=?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            setContactStatement(stmt, contact);
            stmt.setInt(11, contact.getId());
            stmt.executeUpdate();
            System.out.println("Contact updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Searches for contacts using default search fields.
     * 
     * <p>Convenience method that delegates to searchContacts(String, List) with null fields.</p>
     * 
     * @param queryStr The search query string (LIKE pattern matching).
     * 
     * @return A list of matching contacts, ordered by first name then last name.
     * 
     * @see #searchContacts(String, List)
     */
    public List<Contact> searchContacts(String queryStr) {
        return searchContacts(queryStr, null);
    }

    /**
     * Searches for contacts in specified fields.
     * 
     * <p>Performs flexible search with multiple OR conditions using LIKE pattern matching.
     * If no fields specified, uses defaults: first_name, last_name, phone_primary, email.
     * Results are automatically sorted by first_name and last_name.</p>
     * 
     * @param queryStr The search query string (wrapped with '%' for LIKE matching).
     * @param fields List of field names to search. If null/empty, defaults are used.
     * 
    * @return A list of matching contacts, ordered by first and last name.
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
     * <p>Removes the contact record and prints a success message.</p>
     * 
    * @param id The unique identifier (contact_id) of the contact to delete.
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
     * <p>Populates a PreparedStatement with contact field values in the correct order.
     * Used by both addContact and updateContact to avoid code duplication.</p>
     * 
     * @param stmt The PreparedStatement to populate. Cannot be null.
     * @param contact The Contact object containing the values. Cannot be null.
     * 
     * @throws SQLException if there is an error setting values in the PreparedStatement.
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
        stmt.setString(10, contact.getGender());
    }

    /**
     * Helper method to map a database ResultSet row to a Contact object.
     * 
     * <p>Converts a single row from a database ResultSet into a fully constructed Contact object.
     * Used by getAllContacts, getContactById, and searchContacts methods.</p>
     * 
     * @param rs The ResultSet positioned at the current row. Cannot be null.
     * 
     * @return A new Contact object populated with values from the ResultSet row.
     * 
     * @throws SQLException if there is an error retrieving values from the ResultSet.
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
                rs.getDate("birth_date"),
                rs.getString("gender"));
    }
}
