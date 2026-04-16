package com.nutritrack.util;

/**
 * Utility class for input validation
 */
public class ValidationUtil {
    
    /**
     * Validate username
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        String trimmed = username.trim();
        return trimmed.length() >= 3 && trimmed.length() <= 20 && 
               trimmed.matches("^[a-zA-Z0-9_]+$");
    }
    
    /**
     * Validate password
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        return password.length() >= 6 && password.length() <= 50;
    }
    
    /**
     * Validate name
     */
    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        String trimmed = name.trim();
        return trimmed.length() >= 2 && trimmed.length() <= 50 && 
               trimmed.matches("^[a-zA-Z\\s]+$");
    }
    
    /**
     * Validate height in meters
     */
    public static boolean isValidHeight(double height) {
        return height >= 0.5 && height <= 3.0;
    }
    
    /**
     * Validate weight in kg
     */
    public static boolean isValidWeight(double weight) {
        return weight >= 20.0 && weight <= 500.0;
    }
    
    /**
     * Validate age
     */
    public static boolean isValidAge(int age) {
        return age >= 10 && age <= 120;
    }
    
    /**
     * Validate grams
     */
    public static boolean isValidGrams(double grams) {
        return grams >= 1.0 && grams <= 10000.0;
    }
    
    /**
     * Get validation error message
     */
    public static String getValidationErrorMessage(String field, String value) {
        if ("username".equals(field)) {
            if (value == null || value.trim().isEmpty()) {
                return "Username cannot be empty";
            } else if (value.trim().length() < 3) {
                return "Username must be at least 3 characters";
            } else if (value.trim().length() > 20) {
                return "Username cannot exceed 20 characters";
            } else {
                return "Username can only contain letters, numbers, and underscores";
            }
        } else if ("password".equals(field)) {
            if (value == null || value.isEmpty()) {
                return "Password cannot be empty";
            } else if (value.length() < 6) {
                return "Password must be at least 6 characters";
            } else if (value.length() > 50) {
                return "Password cannot exceed 50 characters";
            } else {
                return "Invalid password";
            }
        } else if ("name".equals(field)) {
            if (value == null || value.trim().isEmpty()) {
                return "Name cannot be empty";
            } else if (value.trim().length() < 2) {
                return "Name must be at least 2 characters";
            } else if (value.trim().length() > 50) {
                return "Name cannot exceed 50 characters";
            } else {
                return "Name can only contain letters and spaces";
            }
        } else {
            return "Invalid " + field;
        }
    }
}
