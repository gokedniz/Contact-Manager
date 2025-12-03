package dao;

import db.DatabaseConnection;
import model.Contact;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactDAO {

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

    public List<Contact> searchContacts(String queryStr) {
        List<Contact> contacts = new ArrayList<>();
        String sql = "SELECT * FROM contacts WHERE first_name LIKE ? OR last_name LIKE ? OR phone_primary LIKE ? OR email LIKE ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            String likeQuery = "%" + queryStr + "%";
            stmt.setString(1, likeQuery);
            stmt.setString(2, likeQuery);
            stmt.setString(3, likeQuery);
            stmt.setString(4, likeQuery);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                contacts.add(mapResultSetToContact(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contacts;
    }

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
