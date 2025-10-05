package org.example.utils;

import java.util.regex.Pattern;

/**
 * Utility class for input validation
 */
public class Validators {
    
    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    
    // Phone validation pattern (basic)
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^[+]?[0-9\\s\\-\\(\\)]{10,}$");

    /**
     * Validate email format
     * @param email email to validate
     * @return true if valid email format
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Validate phone number format
     * @param phone phone number to validate
     * @return true if valid phone format
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    /**
     * Validate password strength
     * @param password password to validate
     * @return true if password meets minimum requirements
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }
        // At least 6 characters, contains at least one letter and one number
        return password.matches(".*[A-Za-z].*") && password.matches(".*[0-9].*");
    }

    /**
     * Validate name (first name, last name)
     * @param name name to validate
     * @return true if valid name
     */
    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        // Only letters, spaces, hyphens, and apostrophes allowed
        return name.trim().matches("^[A-Za-z\\s\\-'']+$");
    }

    /**
     * Validate product name
     * @param name product name to validate
     * @return true if valid product name
     */
    public static boolean isValidProductName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return name.trim().length() >= 2 && name.trim().length() <= 100;
    }

    /**
     * Validate price
     * @param price price to validate
     * @return true if valid price
     */
    public static boolean isValidPrice(String price) {
        if (price == null || price.trim().isEmpty()) {
            return false;
        }
        try {
            double priceValue = Double.parseDouble(price.trim());
            return priceValue > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validate stock quantity
     * @param stock stock quantity to validate
     * @return true if valid stock quantity
     */
    public static boolean isValidStock(String stock) {
        if (stock == null || stock.trim().isEmpty()) {
            return false;
        }
        try {
            int stockValue = Integer.parseInt(stock.trim());
            return stockValue >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Sanitize string input
     * @param input input string to sanitize
     * @return sanitized string
     */
    public static String sanitizeString(String input) {
        if (input == null) {
            return "";
        }
        return input.trim().replaceAll("[<>\"'&]", "");
    }

    /**
     * Check if string is empty or null
     * @param input input string to check
     * @return true if empty or null
     */
    public static boolean isEmpty(String input) {
        return input == null || input.trim().isEmpty();
    }
}
