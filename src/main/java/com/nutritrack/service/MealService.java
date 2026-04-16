package com.nutritrack.service;

import com.nutritrack.model.Food;
import com.nutritrack.model.Meal;
import com.nutritrack.model.User;
import com.nutritrack.repository.MealRepository;
import com.nutritrack.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Meal service for NutriTrack web application
 * Handles meal tracking and nutrition calculations
 */
// Disabled - uses model.Meal which conflicts with entity.Meal in MealRepository
// @Service
public class MealService {
    
    @Autowired
    private MealRepository mealRepository;
    
    @Autowired
    private FoodDatabase foodDatabase;
    
    /**
     * Add a meal for a user
     */
    public Meal addMeal(Long userId, String mealType, String foodKey, double grams) {
        // Validation
        if (!ValidationUtil.isValidGrams(grams)) {
            throw new IllegalArgumentException("Grams must be between 1 and 10000");
        }
        
        // Get food from database
        Optional<Food> foodOpt = foodDatabase.getFood(foodKey);
        if (foodOpt.isEmpty()) {
            throw new IllegalArgumentException("Food not found: " + foodKey);
        }
        
        Food food = foodOpt.get();
        
        // Calculate nutrition for the given grams
        double factor = grams / 100.0;
        double calories = food.getCaloriesPer100g() * factor;
        double protein = food.getProteinPer100g() * factor;
        double carbs = food.getCarbsPer100g() * factor;
        double fat = food.getFatPer100g() * factor;
        
        // Create meal (user will be set by controller)
        Meal meal = new Meal(null, mealType, food.getName(), grams, LocalDate.now(), 
                           calories, protein, carbs, fat);
        
        return mealRepository.save(meal);
    }
    
    /**
     * Get all meals for a user on a specific date
     */
    public List<Meal> getMealsByUserAndDate(User user, LocalDate date) {
        return mealRepository.findByUserAndDate(user, date);
    }
    
    /**
     * Get daily nutrition summary for a user
     */
    public Map<String, Double> getDailyNutritionSummary(User user, LocalDate date) {
        Map<String, Double> summary = new HashMap<>();
        
        Double calories = mealRepository.getTotalCaloriesByUserAndDate(user, date);
        Double protein = mealRepository.getTotalProteinByUserAndDate(user, date);
        Double carbs = mealRepository.getTotalCarbsByUserAndDate(user, date);
        Double fat = mealRepository.getTotalFatByUserAndDate(user, date);
        
        summary.put("calories", calories != null ? calories : 0.0);
        summary.put("protein", protein != null ? protein : 0.0);
        summary.put("carbs", carbs != null ? carbs : 0.0);
        summary.put("fat", fat != null ? fat : 0.0);
        
        return summary;
    }
    
    /**
     * Get weekly nutrition summary for a user
     */
    public Map<String, Double> getWeeklyNutritionSummary(User user) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6);
        
        List<Meal> weeklyMeals = mealRepository.findByUserAndDateBetween(user, startDate, endDate);
        
        Map<String, Double> summary = new HashMap<>();
        double totalCalories = 0;
        double totalProtein = 0;
        double totalCarbs = 0;
        double totalFat = 0;
        
        for (Meal meal : weeklyMeals) {
            totalCalories += meal.getCalories();
            totalProtein += meal.getProtein();
            totalCarbs += meal.getCarbs();
            totalFat += meal.getFat();
        }
        
        summary.put("calories", totalCalories / 7.0); // Average per day
        summary.put("protein", totalProtein / 7.0);
        summary.put("carbs", totalCarbs / 7.0);
        summary.put("fat", totalFat / 7.0);
        
        return summary;
    }
    
    /**
     * Get all available food names
     */
    public List<String> getAllFoodNames() {
        return foodDatabase.getAllFoodNames();
    }
    
    /**
     * Get food information
     */
    public Optional<Food> getFood(String foodKey) {
        return foodDatabase.getFood(foodKey);
    }
}
