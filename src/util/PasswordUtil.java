package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

/**
 * Utility class for password hashing using SHA-256 algorithm.
 * 
 * <p>Provides secure password hashing using SHA-256 cryptographic hash function.
 * Passwords are hashed using UTF-8 encoding for consistent representation across systems.</p>
 * 
 * @author Group 10
 * @version 1.0
 */
public class PasswordUtil {

    /**
     * Hashes a password using the SHA-256 algorithm.
     * 
     * <p>Converts the plaintext password to a hexadecimal SHA-256 hash for secure storage.
     * The same password always produces the same hash for verification during authentication.</p>
     * 
     * @param password The plaintext password to hash. Cannot be null.
     * @return A hexadecimal string representation of the SHA-256 hash.
     * @throws RuntimeException if SHA-256 algorithm is not available (should not occur).
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * Converts a byte array to its hexadecimal string representation.
     * 
     * <p>Each byte is converted to two hexadecimal characters, with leading zeros
     * added for single-digit values. Used internally for hash formatting.</p>
     * 
     * @param hash The byte array to convert (typically from MessageDigest).
     * @return A hexadecimal string representation of the byte array.
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
