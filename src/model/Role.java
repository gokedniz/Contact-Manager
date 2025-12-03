package model;

/**
 * Enum representing different user roles in the contact management system.
 * Each role defines a specific level of access and permissions within the system.
 * 
 * <p>The roles include TESTER, JUNIOR_DEVELOPER, SENIOR_DEVELOPER, and MANAGER.</p>
 * 
 * @author Group 10
 */
public enum Role {
    TESTER,
    JUNIOR_DEVELOPER,
    SENIOR_DEVELOPER,
    MANAGER;

    /**
     * Converts a string representation of a role from the database to the corresponding Role enum.
     * 
     * @param value The string representation of the role (e.g., "TESTER", "JUNIOR", "SENIOR", "MANAGER").
     * @return The corresponding Role enum.
     * @throws IllegalArgumentException if the provided value does not match any known role.
     */
    public static Role fromDbValue(String value) {
        switch (value.toUpperCase()) {
            case "TESTER":
                return TESTER;
            case "JUNIOR":
                return JUNIOR_DEVELOPER;
            case "SENIOR":
                return SENIOR_DEVELOPER;
            case "MANAGER":
                return MANAGER;
            default:
                throw new IllegalArgumentException("Unknown role: " + value);
        }
    }
}
