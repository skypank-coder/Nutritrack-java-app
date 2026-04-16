package com.nutritrack.model;

public class Food {
    private String name;
    private double caloriesPer100g;
    private double proteinPer100g;
    private double carbsPer100g;
    private double fatPer100g;
    private String category;
    
    public Food(String name, double caloriesPer100g, double proteinPer100g, double carbsPer100g, double fatPer100g, String category) {
        this.name = name;
        this.caloriesPer100g = caloriesPer100g;
        this.proteinPer100g = proteinPer100g;
        this.carbsPer100g = carbsPer100g;
        this.fatPer100g = fatPer100g;
        this.category = category;
    }
    
    public String getName() { return name; }
    public double getCaloriesPer100g() { return caloriesPer100g; }
    public double getProteinPer100g() { return proteinPer100g; }
    public double getCarbsPer100g() { return carbsPer100g; }
    public double getFatPer100g() { return fatPer100g; }
    public String getCategory() { return category; }
    
    public NutritionInfo getNutritionForGrams(double grams) {
        double factor = grams / 100.0;
        return new NutritionInfo(
            caloriesPer100g * factor,
            proteinPer100g * factor,
            carbsPer100g * factor,
            fatPer100g * factor
        );
    }
    
    @Override
    public String toString() {
        return name;
    }
}
