package com.nutritrack.util;

/**
 * Utility class for BMI calculations
 * Follows Single Responsibility Principle
 */
public class BMICalculator {
    
    /**
     * Calculate BMI using standard formula
     * BMI = weight (kg) / height² (m²)
     */
    public static double calculateBMI(double weight, double height) {
        if (weight <= 0 || height <= 0) {
            throw new IllegalArgumentException("Weight and height must be positive values");
        }
        return weight / (height * height);
    }
    
    /**
     * Get BMI category based on BMI value
     */
    public static BMICategory getBMICategory(double bmi) {
        if (bmi < 18.5) {
            return BMICategory.UNDERWEIGHT;
        } else if (bmi < 25) {
            return BMICategory.NORMAL_WEIGHT;
        } else if (bmi < 30) {
            return BMICategory.OVERWEIGHT;
        } else {
            return BMICategory.OBESE;
        }
    }
    
    /**
     * Get BMI category as string
     */
    public static String getBMICategoryString(double bmi) {
        return getBMICategory(bmi).getDisplayName();
    }
    
    /**
     * BMI categories enum
     */
    public enum BMICategory {
        UNDERWEIGHT("Underweight"),
        NORMAL_WEIGHT("Normal weight"),
        OVERWEIGHT("Overweight"),
        OBESE("Obese");
        
        private final String displayName;
        
        BMICategory(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}
