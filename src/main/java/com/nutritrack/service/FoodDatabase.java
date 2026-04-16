package com.nutritrack.service;

import com.nutritrack.model.Food;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Food database service for NutriTrack web application
 * Manages food items and their nutritional information
 * Not currently wired to any controller - kept for future use
 */
// @Service
public class FoodDatabase {
    private Map<String, Food> foods;
    
    public FoodDatabase() {
        foods = new HashMap<>();
        initializeFoods();
    }
    
    private void initializeFoods() {
        // Proteins
        addFood("chicken", "Chicken Breast", 165, 31, 0, 3.6, "Protein");
        addFood("fish", "Salmon", 208, 25, 0, 13, "Protein");
        addFood("egg", "Egg", 155, 13, 1.1, 11, "Protein");
        addFood("paneer", "Paneer", 265, 26, 3.6, 17, "Protein");
        addFood("tofu", "Tofu", 144, 17, 3, 9, "Protein");
        addFood("dal", "Dal", 116, 8, 20, 1, "Protein");
        
        // Carbohydrates
        addFood("rice", "White Rice", 130, 2.7, 28, 0.3, "Carbs");
        addFood("bread", "Whole Wheat Bread", 247, 13, 41, 3.4, "Carbs");
        addFood("pasta", "Pasta", 131, 5, 25, 1.1, "Carbs");
        addFood("oats", "Oats", 389, 16.9, 66.3, 6.9, "Carbs");
        addFood("roti", "Roti", 264, 9, 53, 4, "Carbs");
        
        // Vegetables
        addFood("broccoli", "Broccoli", 34, 2.8, 7, 0.4, "Vegetables");
        addFood("spinach", "Spinach", 23, 2.9, 3.6, 0.4, "Vegetables");
        addFood("carrot", "Carrot", 41, 0.9, 10, 0.2, "Vegetables");
        addFood("tomato", "Tomato", 18, 0.9, 3.9, 0.2, "Vegetables");
        
        // Fruits
        addFood("apple", "Apple", 52, 0.3, 14, 0.2, "Fruits");
        addFood("banana", "Banana", 89, 1.1, 23, 0.3, "Fruits");
        addFood("orange", "Orange", 47, 0.9, 12, 0.3, "Fruits");
        
        // Healthy Fats
        addFood("almonds", "Almonds", 579, 21, 22, 50, "Fats");
        addFood("olive_oil", "Olive Oil", 884, 0, 0, 100, "Fats");
        addFood("avocado", "Avocado", 160, 2, 9, 15, "Fats");
        
        // Dairy
        addFood("milk", "Milk", 42, 3.4, 5, 1, "Dairy");
        addFood("yogurt", "Yogurt", 59, 10, 3.3, 0.4, "Dairy");
        
        // Indian Foods
        addFood("dal_tadka", "Dal Tadka", 119, 9, 19, 2, "Indian");
        addFood("samosa", "Samosa", 251, 5, 32, 12, "Indian");
        addFood("dosa", "Dosa", 168, 3.5, 32, 2.5, "Indian");
        addFood("biryani", "Biryani (1 serving)", 487, 24, 52, 18, "Indian");
    }
    
    private void addFood(String key, String name, double calories, double protein, double carbs, double fat, String category) {
        foods.put(key, new Food(name, calories, protein, carbs, fat, category));
    }
    
    public Optional<Food> getFood(String key) {
        return Optional.ofNullable(foods.get(key.toLowerCase()));
    }
    
    public List<Food> getFoodsByCategory(String category) {
        List<Food> result = new ArrayList<>();
        for (Food food : foods.values()) {
            if (food.getCategory().equalsIgnoreCase(category)) {
                result.add(food);
            }
        }
        return result;
    }
    
    public List<String> getAllFoodNames() {
        return new ArrayList<>(foods.keySet());
    }
    
    public List<Food> getAllFoods() {
        return new ArrayList<>(foods.values());
    }
    
    public List<String> getCategories() {
        return List.of("Protein", "Carbs", "Vegetables", "Fruits", "Fats", "Dairy", "Indian");
    }
}
