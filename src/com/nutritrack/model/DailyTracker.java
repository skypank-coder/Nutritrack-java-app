package com.nutritrack.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class DailyTracker {
    private LocalDate date;
    private Map<String, Meal> meals;
    
    public DailyTracker(LocalDate date) {
        this.date = date;
        this.meals = new HashMap<>();
        initializeMeals();
    }
    
    private void initializeMeals() {
        meals.put("breakfast", new Meal("Breakfast", date));
        meals.put("lunch", new Meal("Lunch", date));
        meals.put("dinner", new Meal("Dinner", date));
        meals.put("snacks", new Meal("Snacks", date));
    }
    
    public LocalDate getDate() { return date; }
    public Map<String, Meal> getMeals() { return meals; }
    
    public void addFoodToMeal(String mealType, FoodItem foodItem) {
        Meal meal = meals.get(mealType.toLowerCase());
        if (meal != null) {
            meal.addFoodItem(foodItem);
        }
    }
    
    public NutritionInfo getDailyTotal() {
        NutritionInfo total = new NutritionInfo(0, 0, 0, 0);
        for (Meal meal : meals.values()) {
            total.add(meal.getTotalNutrition());
        }
        return total;
    }
    
    public Meal getMeal(String mealType) {
        return meals.get(mealType.toLowerCase());
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Daily Report for ").append(date).append("\n");
        sb.append("=====================================\n");
        
        for (Map.Entry<String, Meal> entry : meals.entrySet()) {
            Meal meal = entry.getValue();
            if (!meal.isEmpty()) {
                sb.append(meal.toString()).append("\n");
                for (FoodItem item : meal.getFoodItems()) {
                    sb.append("  - ").append(item.toString()).append("\n");
                }
            }
        }
        
        sb.append("\nDaily Total: ").append(getDailyTotal().toString()).append("\n");
        return sb.toString();
    }
}
