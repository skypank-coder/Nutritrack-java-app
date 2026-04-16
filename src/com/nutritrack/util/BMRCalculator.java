package com.nutritrack.util;

/**
 * Utility class for BMR calculations
 * Follows Single Responsibility Principle
 */
public class BMRCalculator {
    
    /**
     * Calculate BMR using Harris-Benedict equation
     * 
     * Male: BMR = 10W + 6.25H - 5A + 5
     * Female: BMR = 10W + 6.25H - 5A - 161
     * 
     * Where: W = weight (kg), H = height (cm), A = age (years)
     */
    public static double calculateBMR(double weight, double height, int age, String gender) {
        if (weight <= 0 || height <= 0 || age <= 0) {
            throw new IllegalArgumentException("Weight, height, and age must be positive values");
        }
        
        if (gender == null || gender.trim().isEmpty()) {
            throw new IllegalArgumentException("Gender cannot be null or empty");
        }
        
        double bmr;
        if (gender.equalsIgnoreCase("male")) {
            bmr = 10 * weight + 6.25 * height - 5 * age + 5;
        } else if (gender.equalsIgnoreCase("female")) {
            bmr = 10 * weight + 6.25 * height - 5 * age - 161;
        } else {
            throw new IllegalArgumentException("Gender must be 'male' or 'female'");
        }
        
        return bmr;
    }
    
    /**
     * Calculate daily calorie requirement based on activity level
     */
    public static double calculateDailyCalorieRequirement(double bmr, String activityLevel) {
        if (bmr <= 0) {
            throw new IllegalArgumentException("BMR must be positive");
        }
        
        if (activityLevel == null || activityLevel.trim().isEmpty()) {
            throw new IllegalArgumentException("Activity level cannot be null or empty");
        }
        
        double multiplier = getActivityMultiplier(activityLevel);
        return bmr * multiplier;
    }
    
    /**
     * Get activity multiplier for different activity levels
     */
    public static double getActivityMultiplier(String activityLevel) {
        return switch (activityLevel.toLowerCase()) {
            case "sedentary" -> 1.2;
            case "light" -> 1.375;
            case "moderate" -> 1.55;
            case "very_active" -> 1.725;
            case "extra_active" -> 1.9;
            default -> 1.55; // Default to moderate
        };
    }
    
    /**
     * Calculate complete daily calorie requirement
     */
    public static double calculateDailyCalorieRequirement(double weight, double height, int age, 
                                                        String gender, String activityLevel) {
        double bmr = calculateBMR(weight, height, age, gender);
        return calculateDailyCalorieRequirement(bmr, activityLevel);
    }
}
