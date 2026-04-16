package com.nutritrack.service;

import com.nutritrack.model.Meal;
import com.nutritrack.model.User;
import com.nutritrack.repository.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Service
public class NutritionService {
    
    @Autowired
    private MealRepository mealRepository;
    
    /**
     * Add a new meal
     */
    public Meal addMeal(Meal meal) {
        if (meal.getDate() == null) {
            meal.setDate(LocalDate.now());
        }
        return mealRepository.save(meal);
    }
    
    /**
     * Get all meals for a user
     */
    public List<Meal> getMealsByUser(User user) {
        return mealRepository.findByUserOrderByDateDesc(user);
    }
    
    /**
     * Get meals for a user on a specific date
     */
    public List<Meal> getMealsByUserAndDate(User user, LocalDate date) {
        return mealRepository.findMealsByUserAndDate(user, date);
    }
    
    /**
     * Get today's meals for a user
     */
    public List<Meal> getTodayMeals(User user) {
        return mealRepository.findTodayMealsByUser(user);
    }
    
    /**
     * Calculate daily nutrition summary
     */
    public Map<String, Object> calculateDailySummary(User user, LocalDate date) {
        Map<String, Object> summary = new HashMap<>();
        
        // Get totals for the day
        Double calories = mealRepository.getTotalCaloriesByUserAndDate(user, date);
        Double protein = mealRepository.getTotalProteinByUserAndDate(user, date);
        Double carbs = mealRepository.getTotalCarbsByUserAndDate(user, date);
        Double fat = mealRepository.getTotalFatByUserAndDate(user, date);
        
        // Handle null values
        calories = calories != null ? calories : 0.0;
        protein = protein != null ? protein : 0.0;
        carbs = carbs != null ? carbs : 0.0;
        fat = fat != null ? fat : 0.0;
        
        // Calculate daily target based on user's BMR and activity level
        double dailyTarget = calculateDailyCalorieTarget(user);
        double remaining = dailyTarget - calories;
        
        summary.put("date", date);
        summary.put("calories", calories);
        summary.put("protein", protein);
        summary.put("carbs", carbs);
        summary.put("fat", fat);
        summary.put("targetCalories", dailyTarget);
        summary.put("remainingCalories", remaining);
        summary.put("percentage", (calories / dailyTarget) * 100);
        
        return summary;
    }
    
    /**
     * Calculate daily calorie target based on BMR and activity level
     */
    private double calculateDailyCalorieTarget(User user) {
        // Calculate BMR using Mifflin-St Jeor equation
        double bmr;
        if ("male".equals(user.getGender())) {
            bmr = 88.362 + (13.397 * user.getWeight()) + (4.799 * user.getHeight() * 100) - (5.677 * user.getAge());
        } else {
            bmr = 447.593 + (9.247 * user.getWeight()) + (3.098 * user.getHeight() * 100) - (4.330 * user.getAge());
        }
        
        // Apply activity multiplier
        double activityMultiplier = getActivityMultiplier(user.getActivityLevel());
        return bmr * activityMultiplier;
    }
    
    /**
     * Get activity multiplier based on activity level
     */
    private double getActivityMultiplier(String activityLevel) {
        switch (activityLevel.toUpperCase()) {
            case "SEDENTARY":
                return 1.2;
            case "LIGHT":
                return 1.375;
            case "MODERATE":
                return 1.55;
            case "VERY_ACTIVE":
                return 1.725;
            case "EXTRA_ACTIVE":
                return 1.9;
            default:
                return 1.55;
        }
    }
    
    /**
     * Get nutrition summary for multiple days
     */
    public Map<String, Object> getWeeklySummary(User user) {
        Map<String, Object> summary = new HashMap<>();
        
        LocalDate today = LocalDate.now();
        LocalDate weekAgo = today.minusDays(7);
        
        // Get meals for the past week
        List<Meal> weeklyMeals = mealRepository.findByUserAndDateBetween(user, weekAgo, today);
        
        // Calculate totals
        double totalCalories = weeklyMeals.stream().mapToDouble(Meal::getCalories).sum();
        double totalProtein = weeklyMeals.stream().mapToDouble(Meal::getProtein).sum();
        double totalCarbs = weeklyMeals.stream().mapToDouble(Meal::getCarbs).sum();
        double totalFat = weeklyMeals.stream().mapToDouble(Meal::getFat).sum();
        
        // Calculate averages
        int daysWithMeals = (int) weeklyMeals.stream()
                .map(meal -> meal.getDate())
                .distinct()
                .count();
        
        double avgCalories = daysWithMeals > 0 ? totalCalories / daysWithMeals : 0;
        double avgProtein = daysWithMeals > 0 ? totalProtein / daysWithMeals : 0;
        double avgCarbs = daysWithMeals > 0 ? totalCarbs / daysWithMeals : 0;
        double avgFat = daysWithMeals > 0 ? totalFat / daysWithMeals : 0;
        
        summary.put("weekly", Map.of(
                "calories", totalCalories,
                "protein", totalProtein,
                "carbs", totalCarbs,
                "fat", totalFat
        ));
        
        summary.put("average", Map.of(
                "calories", avgCalories,
                "protein", avgProtein,
                "carbs", avgCarbs,
                "fat", avgFat
        ));
        
        summary.put("daysWithMeals", daysWithMeals);
        
        return summary;
    }
    
    /**
     * Calculate nutrition values for a food item
     */
    public Map<String, Double> calculateNutrition(String foodName, double grams) {
        Map<String, Double> nutrition = new HashMap<>();
        
        // Nutrition database (values per 100g)
        Map<String, Map<String, Double>> nutritionDB = new HashMap<>();
        
        nutritionDB.put("oatmeal", Map.of("calories", 389.0, "protein", 16.9, "carbs", 66.3, "fat", 6.9));
        nutritionDB.put("chicken", Map.of("calories", 165.0, "protein", 31.0, "carbs", 0.0, "fat", 3.6));
        nutritionDB.put("rice", Map.of("calories", 130.0, "protein", 2.7, "carbs", 28.0, "fat", 0.3));
        nutritionDB.put("bread", Map.of("calories", 265.0, "protein", 9.0, "carbs", 49.0, "fat", 3.2));
        nutritionDB.put("egg", Map.of("calories", 155.0, "protein", 13.0, "carbs", 1.1, "fat", 11.0));
        nutritionDB.put("milk", Map.of("calories", 42.0, "protein", 3.4, "carbs", 5.0, "fat", 1.0));
        nutritionDB.put("apple", Map.of("calories", 52.0, "protein", 0.3, "carbs", 14.0, "fat", 0.2));
        nutritionDB.put("banana", Map.of("calories", 89.0, "protein", 1.1, "carbs", 23.0, "fat", 0.3));
        nutritionDB.put("spinach", Map.of("calories", 23.0, "protein", 2.9, "carbs", 3.6, "fat", 0.4));
        nutritionDB.put("broccoli", Map.of("calories", 34.0, "protein", 2.8, "carbs", 7.0, "fat", 0.4));
        nutritionDB.put("dal", Map.of("calories", 113.0, "protein", 6.0, "carbs", 20.0, "fat", 0.8));
        nutritionDB.put("biryani", Map.of("calories", 299.0, "protein", 16.0, "carbs", 42.0, "fat", 12.0));
        nutritionDB.put("samosa", Map.of("calories", 308.0, "protein", 8.0, "carbs", 40.0, "fat", 17.0));
        nutritionDB.put("dosa", Map.of("calories", 133.0, "protein", 3.0, "carbs", 25.0, "fat", 4.0));
        nutritionDB.put("yogurt", Map.of("calories", 59.0, "protein", 10.0, "carbs", 3.6, "fat", 3.3));
        nutritionDB.put("paneer", Map.of("calories", 265.0, "protein", 18.0, "carbs", 4.9, "fat", 21.0));
        nutritionDB.put("tofu", Map.of("calories", 76.0, "protein", 8.0, "carbs", 1.9, "fat", 4.8));
        nutritionDB.put("fish", Map.of("calories", 206.0, "protein", 22.0, "carbs", 0.0, "fat", 12.0));
        nutritionDB.put("almonds", Map.of("calories", 579.0, "protein", 21.0, "carbs", 22.0, "fat", 50.0));
        nutritionDB.put("olive_oil", Map.of("calories", 884.0, "protein", 0.0, "carbs", 0.0, "fat", 100.0));
        nutritionDB.put("avocado", Map.of("calories", 160.0, "protein", 2.0, "carbs", 9.0, "fat", 15.0));
        nutritionDB.put("orange", Map.of("calories", 47.0, "protein", 0.9, "carbs", 12.0, "fat", 0.1));
        nutritionDB.put("carrot", Map.of("calories", 41.0, "protein", 0.9, "carbs", 10.0, "fat", 0.2));
        nutritionDB.put("tomato", Map.of("calories", 18.0, "protein", 0.9, "carbs", 3.9, "fat", 0.2));
        nutritionDB.put("oats", Map.of("calories", 389.0, "protein", 16.9, "carbs", 66.3, "fat", 6.9));
        nutritionDB.put("roti", Map.of("calories", 104.0, "protein", 3.0, "carbs", 21.0, "fat", 0.3));
        nutritionDB.put("dal_tadka", Map.of("calories", 197.0, "protein", 8.0, "carbs", 26.0, "fat", 8.0));
        
        // Get base nutrition values
        Map<String, Double> baseNutrition = nutritionDB.getOrDefault(foodName.toLowerCase(), 
            Map.of("calories", 100.0, "protein", 5.0, "carbs", 20.0, "fat", 2.0));
        
        // Scale by grams (base values are per 100g)
        double factor = grams / 100.0;
        
        nutrition.put("calories", baseNutrition.get("calories") * factor);
        nutrition.put("protein", baseNutrition.get("protein") * factor);
        nutrition.put("carbs", baseNutrition.get("carbs") * factor);
        nutrition.put("fat", baseNutrition.get("fat") * factor);
        
        return nutrition;
    }
}
