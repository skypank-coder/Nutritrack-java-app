package com.nutritrack.service;

import com.nutritrack.model.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class NutritionService {
    private static NutritionService instance;
    private Map<LocalDate, DailyTracker> dailyTrackers;
    private FoodDatabase foodDatabase;
    
    private NutritionService() {
        dailyTrackers = new HashMap<>();
        foodDatabase = FoodDatabase.getInstance();
    }
    
    public static NutritionService getInstance() {
        if (instance == null) {
            instance = new NutritionService();
        }
        return instance;
    }
    
    public DailyTracker getDailyTracker(LocalDate date) {
        return dailyTrackers.computeIfAbsent(date, DailyTracker::new);
    }
    
    public boolean addMealItem(String username, LocalDate date, String mealType, String foodKey, double grams) {
        Food food = foodDatabase.getFood(foodKey);
        if (food == null) {
            return false;
        }
        
        DailyTracker tracker = getDailyTracker(date);
        FoodItem foodItem = new FoodItem(food, grams);
        tracker.addFoodToMeal(mealType, foodItem);
        return true;
    }
    
    public NutritionInfo getDailyNutrition(LocalDate date) {
        DailyTracker tracker = dailyTrackers.get(date);
        return tracker != null ? tracker.getDailyTotal() : new NutritionInfo(0, 0, 0, 0);
    }
    
    public Map<LocalDate, NutritionInfo> getWeeklyNutrition(LocalDate startDate) {
        Map<LocalDate, NutritionInfo> weeklyData = new HashMap<>();
        for (int i = 0; i < 7; i++) {
            LocalDate date = startDate.plusDays(i);
            weeklyData.put(date, getDailyNutrition(date));
        }
        return weeklyData;
    }
    
    public NutritionInfo getWeeklyAverage(LocalDate startDate) {
        Map<LocalDate, NutritionInfo> weeklyData = getWeeklyNutrition(startDate);
        double totalCalories = 0, totalProtein = 0, totalCarbs = 0, totalFat = 0;
        int daysWithData = 0;
        
        for (NutritionInfo info : weeklyData.values()) {
            if (info.getCalories() > 0) {
                totalCalories += info.getCalories();
                totalProtein += info.getProtein();
                totalCarbs += info.getCarbs();
                totalFat += info.getFat();
                daysWithData++;
            }
        }
        
        if (daysWithData == 0) {
            return new NutritionInfo(0, 0, 0, 0);
        }
        
        return new NutritionInfo(
            totalCalories / daysWithData,
            totalProtein / daysWithData,
            totalCarbs / daysWithData,
            totalFat / daysWithData
        );
    }
    
    public FoodDatabase getFoodDatabase() {
        return foodDatabase;
    }
    
    public String generateNutritionAdvice(User user, NutritionInfo dailyNutrition) {
        StringBuilder advice = new StringBuilder();
        double targetCalories = user.getDailyCalorieRequirement();
        double caloriePercentage = (dailyNutrition.getCalories() / targetCalories) * 100;
        
        advice.append("Nutrition Analysis:\n");
        advice.append(String.format("Calories: %.0f / %.0f (%.1f%% of target)\n", 
            dailyNutrition.getCalories(), targetCalories, caloriePercentage));
        
        if (caloriePercentage < 80) {
            advice.append("⚠ You're consuming too few calories. Consider adding more nutritious meals.\n");
        } else if (caloriePercentage > 120) {
            advice.append("⚠ You're exceeding your calorie target. Consider portion control.\n");
        } else {
            advice.append("✅ Your calorie intake is on target!\n");
        }
        
        advice.append(String.format("Macro Distribution: Protein %.1f%%, Carbs %.1f%%, Fat %.1f%%\n",
            dailyNutrition.getProteinPercentage(), dailyNutrition.getCarbsPercentage(), dailyNutrition.getFatPercentage()));
        
        return advice.toString();
    }
}
