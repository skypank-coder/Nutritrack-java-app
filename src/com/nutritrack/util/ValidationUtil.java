package com.nutritrack.util;

public class ValidationUtil {
    
    public static boolean isValidUsername(String username) {
        return username != null && username.length() >= 3 && username.length() <= 20 && username.matches("^[a-zA-Z0-9_]+$");
    }
    
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 4;
    }
    
    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && name.length() <= 50;
    }
    
    public static boolean isValidHeight(double height) {
        return height > 0.5 && height < 3.0;
    }
    
    public static boolean isValidWeight(double weight) {
        return weight > 20 && weight < 500;
    }
    
    public static boolean isValidAge(int age) {
        return age >= 10 && age <= 120;
    }
    
    public static boolean isValidGender(String gender) {
        return gender != null && (gender.equalsIgnoreCase("male") || gender.equalsIgnoreCase("female"));
    }
    
    public static boolean isValidActivityLevel(String activityLevel) {
        return activityLevel != null && (
            activityLevel.equalsIgnoreCase("sedentary") ||
            activityLevel.equalsIgnoreCase("light") ||
            activityLevel.equalsIgnoreCase("moderate") ||
            activityLevel.equalsIgnoreCase("very_active") ||
            activityLevel.equalsIgnoreCase("extra_active")
        );
    }
    
    public static boolean isValidGrams(double grams) {
        return grams > 0 && grams <= 10000;
    }
    
    public static boolean isValidCalories(double calories) {
        return calories >= 0 && calories <= 10000;
    }
    
    public static boolean isValidMacro(double macro) {
        return macro >= 0 && macro <= 1000;
    }
    
    public static String getValidationErrorMessage(String field, String value) {
        switch (field.toLowerCase()) {
            case "username":
                if (!isValidUsername(value)) {
                    return "Username must be 3-20 characters long and contain only letters, numbers, and underscores";
                }
                break;
            case "password":
                if (!isValidPassword(value)) {
                    return "Password must be at least 4 characters long";
                }
                break;
            case "name":
                if (!isValidName(value)) {
                    return "Name must not be empty and must be less than 50 characters";
                }
                break;
            case "gender":
                if (!isValidGender(value)) {
                    return "Gender must be either 'male' or 'female'";
                }
                break;
            case "activity_level":
                if (!isValidActivityLevel(value)) {
                    return "Activity level must be: sedentary, light, moderate, very_active, or extra_active";
                }
                break;
        }
        return "";
    }
}
