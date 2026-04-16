package com.nutritrack.model;

public class FoodItem {
    private Food food;
    private double grams;
    
    public FoodItem(Food food, double grams) {
        this.food = food;
        this.grams = grams;
    }
    
    public Food getFood() { return food; }
    public double getGrams() { return grams; }
    
    public NutritionInfo getNutritionInfo() {
        return food.getNutritionForGrams(grams);
    }
    
    @Override
    public String toString() {
        return String.format("%s (%.0fg) - %s", food.getName(), grams, getNutritionInfo());
    }
}
