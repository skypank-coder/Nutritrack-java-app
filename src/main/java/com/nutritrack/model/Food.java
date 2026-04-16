package com.nutritrack.model;

/**
 * Food model for NutriTrack web application
 * Represents a food item with nutritional information
 */
public class Food {
    private String name;
    private double caloriesPer100g;
    private double proteinPer100g;
    private double carbsPer100g;
    private double fatPer100g;
    private String category;
    
    public Food() {}
    
    public Food(String name, double caloriesPer100g, double proteinPer100g, double carbsPer100g, double fatPer100g, String category) {
        this.name = name;
        this.caloriesPer100g = caloriesPer100g;
        this.proteinPer100g = proteinPer100g;
        this.carbsPer100g = carbsPer100g;
        this.fatPer100g = fatPer100g;
        this.category = category;
    }
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public double getCaloriesPer100g() { return caloriesPer100g; }
    public void setCaloriesPer100g(double caloriesPer100g) { this.caloriesPer100g = caloriesPer100g; }
    
    public double getProteinPer100g() { return proteinPer100g; }
    public void setProteinPer100g(double proteinPer100g) { this.proteinPer100g = proteinPer100g; }
    
    public double getCarbsPer100g() { return carbsPer100g; }
    public void setCarbsPer100g(double carbsPer100g) { this.carbsPer100g = carbsPer100g; }
    
    public double getFatPer100g() { return fatPer100g; }
    public void setFatPer100g(double fatPer100g) { this.fatPer100g = fatPer100g; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    @Override
    public String toString() {
        return name;
    }
}
