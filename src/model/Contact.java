package model;

import java.sql.Date;

/**
 * Represents a contact in the contact management system.
 * Includes personal details and contact information such as names, phone
 * numbers, and linked accounts.
 * 
 * <p>
 * It provides constructors, getters, setters, and a toString method for easy
 * representation.
 * </p>
 * 
 * @author Group 10
 */
public class Contact {
    private int id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String nickname;
    private String phonePrimary;
    private String phoneSecondary;
    private String email;
    private String linkedinUrl;
    private Date birthDate;

    /**
     * Constructor to initialize all fields of the Contact class.
     * 
     * @param id             The unique identifier for the contact.
     * @param firstName      The first name of the contact.
     * @param middleName     The middle name of the contact.
     * @param lastName       The last name of the contact.
     * @param nickname       The nickname of the contact.
     * @param phonePrimary   The primary phone number of the contact.
     * @param phoneSecondary The secondary phone number of the contact.
     * @param email          The email address of the contact.
     * @param linkedinUrl    The LinkedIn URL of the contact.
     * @param birthDate      The birth date of the contact.
     */
    public Contact(int id, String firstName, String middleName, String lastName, String nickname,
            String phonePrimary, String phoneSecondary, String email, String linkedinUrl, Date birthDate) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.nickname = nickname;
        this.phonePrimary = phonePrimary;
        this.phoneSecondary = phoneSecondary;
        this.email = email;
        this.linkedinUrl = linkedinUrl;
        this.birthDate = birthDate;
    }

    /**
     * Constructor to initialize a Contact without an ID (for new contacts).
     * 
     * @param firstName      The first name of the contact.
     * @param middleName     The middle name of the contact.
     * @param lastName       The last name of the contact.
     * @param nickname       The nickname of the contact.
     * @param phonePrimary   The primary phone number of the contact.
     * @param phoneSecondary The secondary phone number of the contact.
     * @param email          The email address of the contact.
     * @param linkedinUrl    The LinkedIn URL of the contact.
     * @param birthDate      The birth date of the contact.
     */
    public Contact(String firstName, String middleName, String lastName, String nickname,
            String phonePrimary, String phoneSecondary, String email, String linkedinUrl, Date birthDate) {
        this(0, firstName, middleName, lastName, nickname, phonePrimary, phoneSecondary, email, linkedinUrl, birthDate);
    }

    // Getters and Setters
    /**
     * Gets the ID of the contact.
     * 
     * @return The ID of the contact.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the contact.
     * 
     * @param id The ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the first name of the contact.
     * 
     * @return The first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the contact.
     * 
     * @param firstName The first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the middle name of the contact.
     * 
     * @return The middle name.
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * Sets the middle name of the contact.
     * 
     * @param middleName The middle name.
     */
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    /**
     * Gets the last name of the contact.
     * 
     * @return The last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the contact.
     * 
     * @param lastName The last name.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the nickname of the contact.
     * 
     * @return The nickname.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Sets the nickname of the contact.
     * 
     * @param nickname The nickname.
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Gets the primary phone number of the contact.
     * 
     * @return The primary phone number.
     */
    public String getPhonePrimary() {
        return phonePrimary;
    }

    /**
     * Sets the primary phone number of the contact.
     * 
     * @param phonePrimary The primary phone number.
     */
    public void setPhonePrimary(String phonePrimary) {
        this.phonePrimary = phonePrimary;
    }

    /**
     * Gets the secondary phone number of the contact.
     * 
     * @return The secondary phone number.
     */
    public String getPhoneSecondary() {
        return phoneSecondary;
    }

    /**
     * Sets the secondary phone number of the contact.
     * 
     * @param phoneSecondary The secondary phone number.
     */
    public void setPhoneSecondary(String phoneSecondary) {
        this.phoneSecondary = phoneSecondary;
    }

    /**
     * Gets the email address of the contact.
     * 
     * @return The email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the contact.
     * 
     * @param email The email address.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the LinkedIn URL of the contact.
     * 
     * @return The LinkedIn URL.
     */
    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    /**
     * Sets the LinkedIn URL of the contact.
     * 
     * @param linkedinUrl The LinkedIn URL.
     */
    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    /**
     * Gets the birth date of the contact.
     * 
     * @return The birth date.
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the birth date of the contact.
     * 
     * @param birthDate The birth date.
     */
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Returns a string representation of the contact.
     * This method is mainly used for debugging and logging purposes, providing a
     * concise summary of the contact's key details (ID, Name, and Phone).
     * 
     * @return A string containing the contact's ID, name, and primary phone number.
     */
    @Override
    public String toString() {
        return String.format("Contact[id=%d, name=%s %s, phone=%s]", id, firstName, lastName, phonePrimary);
    }
}
