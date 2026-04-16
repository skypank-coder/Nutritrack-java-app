package com.nutritrack.model;

import java.time.LocalDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/**
 * Meal entity for NutriTrack web application
 */
@Entity
@Table(name = "meals")
public class Meal {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    @NotBlank
    private String mealType;
    
    @Column(nullable = false)
    @NotBlank
    private String foodName;
    
    @Column(nullable = false)
    private double grams;
    
    @Column(nullable = false)
    private LocalDate date;
    
    @Column(nullable = false)
    private double calories;
    
    @Column(nullable = false)
    private double protein;
    
    @Column(nullable = false)
    private double carbs;
    
    @Column(nullable = false)
    private double fat;
    
    // Default constructor for JPA
    public Meal() {}
    
    public Meal(User user, String mealType, String foodName, double grams, LocalDate date, 
                double calories, double protein, double carbs, double fat) {
        this.user = user;
        this.mealType = mealType;
        this.foodName = foodName;
        this.grams = grams;
        this.date = date;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }
    
    public String getFoodName() { return foodName; }
    public void setFoodName(String foodName) { this.foodName = foodName; }
    
    public double getGrams() { return grams; }
    public void setGrams(double grams) { this.grams = grams; }
    
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    
    public double getCalories() { return calories; }
    public void setCalories(double calories) { this.calories = calories; }
    
    public double getProtein() { return protein; }
    public void setProtein(double protein) { this.protein = protein; }
    
    public double getCarbs() { return carbs; }
    public void setCarbs(double carbs) { this.carbs = carbs; }
    
    public double getFat() { return fat; }
    public void setFat(double fat) { this.fat = fat; }
    
    @Override
    public String toString() {
        return String.format("Meal{id=%d, user='%s', type='%s', food='%s', grams=%.0f, date=%s}",
                id, user.getUsername(), mealType, foodName, grams, date);
    }
}
