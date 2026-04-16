package com.nutritrack.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Utility class for precise calculations and rounding
 */
public class CalculationUtils {
    
    /**
     * Round a double value to 2 decimal places
     */
    public static double roundToTwoDecimals(double value) {
        if (value == 0) return 0;
        
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    /**
     * Round a double value to 1 decimal place
     */
    public static double roundToOneDecimal(double value) {
        if (value == 0) return 0;
        
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    /**
     * Calculate average protein over days that have meals
     */
    public static double calculateAverageProtein(double totalProtein, int daysWithMeals) {
        if (daysWithMeals == 0) return 0;
        return roundToTwoDecimals(totalProtein / daysWithMeals);
    }
    
    /**
     * Calculate average calories over given days
     */
    public static double calculateAverageCalories(double totalCalories, int days) {
        if (days == 0) return 0;
        return roundToTwoDecimals(totalCalories / days);
    }
    
    /**
     * Calculate average carbs over given days
     */
    public static double calculateAverageCarbs(double totalCarbs, int days) {
        if (days == 0) return 0;
        return roundToTwoDecimals(totalCarbs / days);
    }
    
    /**
     * Calculate average fat over given days
     */
    public static double calculateAverageFat(double totalFat, int days) {
        if (days == 0) return 0;
        return roundToTwoDecimals(totalFat / days);
    }
    
    /**
     * Calculate progress percentage
     */
    public static double calculateProgressPercentage(double current, double target) {
        if (target == 0) return 0;
        return roundToOneDecimal((current / target) * 100);
    }
    
    /**
     * Calculate remaining calories
     */
    public static int calculateRemainingCalories(int targetCalories, double currentCalories) {
        return Math.max(0, targetCalories - (int) currentCalories);
    }
    
    /**
     * Calculate remaining protein
     */
    public static double calculateRemainingProtein(double targetProtein, double currentProtein) {
        return Math.max(0, roundToTwoDecimals(targetProtein - currentProtein));
    }
    
    /**
     * Calculate recommended daily calories for weight goals
     */
    public static int calculateRecommendedCalories(double currentWeight, double goalWeight, int weeks, String goalType, double bmr) {
        if (goalType == null || goalType.equals("maintain")) {
            return (int) bmr;
        }
        
        double weightDifference = goalWeight - currentWeight;
        double weeklyWeightChange = weightDifference / weeks;
        
        // 1 kg of body weight = 7700 calories
        double dailyCalorieAdjustment = (weeklyWeightChange * 7700) / 7;
        
        double recommendedCalories = bmr + dailyCalorieAdjustment;
        
        // Ensure minimum safe calorie levels
        if (recommendedCalories < 1200) {
            return 1200;
        }
        
        return (int) recommendedCalories;
    }
    
    /**
     * Validate that a value is positive
     */
    public static boolean isPositive(double value) {
        return value > 0;
    }
    
    /**
     * Validate that a value is within range
     */
    public static boolean isInRange(double value, double min, double max) {
        return value >= min && value <= max;
    }
}
