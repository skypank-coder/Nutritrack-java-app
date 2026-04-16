package com.nutritrack.model;

public class NutritionInfo {
    private double calories;
    private double protein;
    private double carbs;
    private double fat;
    
    public NutritionInfo(double calories, double protein, double carbs, double fat) {
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
    }
    
    public double getCalories() { return calories; }
    public double getProtein() { return protein; }
    public double getCarbs() { return carbs; }
    public double getFat() { return fat; }
    
    public void setCalories(double calories) { this.calories = calories; }
    public void setProtein(double protein) { this.protein = protein; }
    public void setCarbs(double carbs) { this.carbs = carbs; }
    public void setFat(double fat) { this.fat = fat; }
    
    public double getProteinPercentage() { 
        return calories > 0 ? (protein * 4) / calories * 100 : 0; 
    }
    
    public double getCarbsPercentage() { 
        return calories > 0 ? (carbs * 4) / calories * 100 : 0; 
    }
    
    public double getFatPercentage() { 
        return calories > 0 ? (fat * 9) / calories * 100 : 0; 
    }
    
    public void add(NutritionInfo other) {
        this.calories += other.calories;
        this.protein += other.protein;
        this.carbs += other.carbs;
        this.fat += other.fat;
    }
    
    @Override
    public String toString() {
        return String.format("%.0f cal | %.1fg protein | %.1fg carbs | %.1fg fat", 
            calories, protein, carbs, fat);
    }
}
