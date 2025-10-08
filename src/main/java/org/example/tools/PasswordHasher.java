package org.example.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordHasher {
    
    private static final String ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 16;
    
    /**
     * Hash a password with a random salt
     * @param plainPassword The plain text password
     * @return A string containing the salt and hash separated by ":"
     */
    public static String hashPassword(String plainPassword) {
        try {
            // Generate random salt
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);
            
            // Hash the password with salt
            String hashedPassword = hashWithSalt(plainPassword, salt);
            
            // Return salt:hash
            return Base64.getEncoder().encodeToString(salt) + ":" + hashedPassword;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
    
    /**
     * Check if a plain password matches a stored hash
     * @param plainPassword The plain text password to check
     * @param storedHash The stored hash in format "salt:hash"
     * @return true if password matches, false otherwise
     */
    public static boolean checkPassword(String plainPassword, String storedHash) {
        try {
            // Split salt and hash
            String[] parts = storedHash.split(":");
            if (parts.length != 2) {
                return false;
            }
            
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            String storedPasswordHash = parts[1];
            
            // Hash the provided password with the stored salt
            String hashedPassword = hashWithSalt(plainPassword, salt);
            
            // Compare hashes
            return hashedPassword.equals(storedPasswordHash);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Hash a password with a given salt
     * @param password The plain text password
     * @param salt The salt to use
     * @return The hashed password
     */
    private static String hashWithSalt(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(ALGORITHM);
        md.update(salt);
        byte[] hashedBytes = md.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedBytes);
    }
}
