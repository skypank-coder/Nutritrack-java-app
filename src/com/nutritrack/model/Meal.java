package com.nutritrack.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Meal {
    private String name;
    private List<FoodItem> foodItems;
    private LocalDate date;
    
    public Meal(String name, LocalDate date) {
        this.name = name;
        this.date = date;
        this.foodItems = new ArrayList<>();
    }
    
    public String getName() { return name; }
    public LocalDate getDate() { return date; }
    public List<FoodItem> getFoodItems() { return new ArrayList<>(foodItems); }
    
    public void addFoodItem(FoodItem foodItem) {
        foodItems.add(foodItem);
    }
    
    public void removeFoodItem(int index) {
        if (index >= 0 && index < foodItems.size()) {
            foodItems.remove(index);
        }
    }
    
    public NutritionInfo getTotalNutrition() {
        NutritionInfo total = new NutritionInfo(0, 0, 0, 0);
        for (FoodItem item : foodItems) {
            total.add(item.getNutritionInfo());
        }
        return total;
    }
    
    public boolean isEmpty() {
        return foodItems.isEmpty();
    }
    
    @Override
    public String toString() {
        return String.format("%s: %s", name, getTotalNutrition());
    }
}
